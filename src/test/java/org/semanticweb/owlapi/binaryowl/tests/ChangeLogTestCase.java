package org.semanticweb.owlapi.binaryowl.tests;

import junit.framework.TestCase;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.owlapi.binaryowl.BinaryOWLMetadata;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.owlapi.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/11/2012
 */
public class ChangeLogTestCase  {

    @Test
    public void testChangeLog() throws Exception {
        File file = File.createTempFile("BinaryOWLTest", "Bin");
        file.deleteOnExit();
        BinaryOWLOntologyChangeLog log = new BinaryOWLOntologyChangeLog();
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = man.createOntology(IRI.create("http://stuff.com/testont"));
        OWLAxiom ax = man.getOWLDataFactory().getOWLDeclarationAxiom(man.getOWLDataFactory().getOWLClass(IRI.create("http://stuff.com#A")));
        AddAxiom addAxiom = new AddAxiom(ont, ax);
        log.appendChanges(Arrays.<OWLOntologyChange>asList(addAxiom), System.currentTimeMillis(), BinaryOWLMetadata.emptyMetadata(), file);

        BinaryOWLOntologyChangeLog log2 = new BinaryOWLOntologyChangeLog();
        log2.readChanges(new FileInputStream(file), man.getOWLDataFactory(), new BinaryOWLChangeLogHandler() {
            @Override
            public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long filePosition) {
                System.out.println(list);
            }
        });

    }
}
