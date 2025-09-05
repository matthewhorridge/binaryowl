package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.binaryowl.doc.OWLOntologyDocument;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyBuildingHandler;
import org.semanticweb.binaryowl.owlapi.OWLOntologyWrapper;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

public class LargeLiteralSerializationTestCase {

    @Test
    public void shouldSerializeLargeLiteralInOntologyDocument() throws IOException, OWLOntologyCreationException {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = Ontology(man,
                AnnotationAssertion(
                        AnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()),
                        IRI.create("https://example.org/A"),
                        Literal(largeValue())
                )
        );
        BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
        OWLOntologyDocument doc = new OWLOntologyWrapper(ont);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializer.write(doc, outputStream);

        OWLOntologyManager man2 = OWLManager.createOWLOntologyManager();
        OWLOntology ont2 = man2.createOntology();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        serializer.read(inputStream, new BinaryOWLOntologyBuildingHandler(ont2), man2.getOWLDataFactory());

        assertEquals(ont.getAxioms(), ont2.getAxioms());
    }@Test

    public void shouldSerializeLargeLiteralInChangeLog() throws IOException, OWLOntologyCreationException {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = man.createOntology();
        OWLAnnotationAssertionAxiom ax = AnnotationAssertion(
                AnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()),
                IRI.create("https://example.org/A"),
                Literal(largeValue())
        );
        BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<OWLOntologyChange> changes = new ArrayList<>();
        changes.add(new AddAxiom(ont, ax));
        changeLog.appendChanges(changes, 33, new BinaryOWLMetadata(), os);

        BinaryOWLOntologyChangeLog log2 = new BinaryOWLOntologyChangeLog();

        List<OWLAxiom> axioms = new ArrayList<>();
        log2.readChanges(new ByteArrayInputStream(os.toByteArray()), man.getOWLDataFactory(), new BinaryOWLChangeLogHandler() {
            @Override
            public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long filePosition) {
                list.getChangeRecords()
                        .stream()
                        .map(OWLOntologyChangeRecord::getData)
                        .map(OWLOntologyChangeData::getItem)
                        .map(i -> (OWLAxiom) i)
                        .forEach(axioms::add);
            }
        });
        assertTrue(axioms.contains(ax));
    }

    private static String largeValue() {
        StringBuilder sb = new StringBuilder(Short.MAX_VALUE + 1);
        for (int i = 0; i <= Short.MAX_VALUE; i++) {
            sb.append('a');
        }
        return sb.toString();
    }
}
