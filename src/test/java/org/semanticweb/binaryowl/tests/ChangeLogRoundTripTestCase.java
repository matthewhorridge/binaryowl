package org.semanticweb.binaryowl.tests;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/11/2012
 */
public class ChangeLogRoundTripTestCase {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File ontologyDocument;

    private OWLDataFactory dataFactory;

    private OWLOntologyChange addAxiom;

    @Before
    public void setUp() throws Exception {
        ontologyDocument = temporaryFolder.newFile("BinaryOWLTest");
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        dataFactory = man.getOWLDataFactory();
        OWLOntology ont = man.createOntology(IRI.create("http://stuff.com/testont"));
        OWLAxiom ax = man.getOWLDataFactory().getOWLDeclarationAxiom(man.getOWLDataFactory().getOWLClass(IRI.create("http://stuff.com#A")));
        addAxiom = new AddAxiom(ont, ax);
    }

    @Test
    public void testChangeLog() throws Exception {
        BinaryOWLOntologyChangeLog log = new BinaryOWLOntologyChangeLog();
        long timestamp = System.currentTimeMillis();
        log.appendChanges(
                Arrays.asList(addAxiom),
                timestamp,
                BinaryOWLMetadata.emptyMetadata(),
                ontologyDocument);

        BinaryOWLOntologyChangeLog log2 = new BinaryOWLOntologyChangeLog();
        log2.readChanges(new FileInputStream(ontologyDocument), dataFactory, new BinaryOWLChangeLogHandler() {
            @Override
            public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long filePosition) {
                assertThat(list.getMetadata().isEmpty(), is(true));
                List<OWLOntologyChangeRecord> changeRecords = list.getChangeRecords();
                assertThat(changeRecords, hasSize(1));
                assertThat(changeRecords, contains(addAxiom.getChangeRecord()));
            }
        });

    }
}
