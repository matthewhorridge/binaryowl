package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentHandlerAdapter;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.UnloadableImportException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

public class Version2DocumentParseTestCase {

    @Test
    public void testVersion2DocumentParses() {
        try {
            URL url = Version1DocumentParseTestCase.class.getResource("/binary-owl-v1-ontology.binaryowl");
            BinaryOWLOntologyDocumentSerializer parser = new BinaryOWLOntologyDocumentSerializer();
            final BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            OWLDataFactory df = OWLManager.getOWLDataFactory();
            parser.read(inputStream, new BinaryOWLOntologyDocumentHandlerAdapter<RuntimeException>(), df);
            inputStream.close();
            assertTrue("Parsed successfully", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
