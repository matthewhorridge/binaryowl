package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyBuildingHandler;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class AllConstructsRoundTripTestCase {

    private static final String RESOURCE_NAME = "/allconstructs.owl";

    @Test
    public void roundTripAllConstructs() throws IOException, URISyntaxException, OWLOntologyCreationException, BinaryOWLParseException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        URL url = AllConstructsRoundTripTestCase.class.getResource(RESOURCE_NAME);
        OWLOntology ont = manager.loadOntologyFromOntologyDocument(IRI.create(url));
        BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializer.write(ont, new DataOutputStream(outputStream));

        OWLOntologyManager manIn = OWLManager.createOWLOntologyManager();
        OWLOntology ontIn = manIn.createOntology();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        serializer.read(inputStream, new BinaryOWLOntologyBuildingHandler(ontIn), manager.getOWLDataFactory());

        assertEquals(ont, ontIn);
        assertEquals(ont.getAnnotations(), ontIn.getAnnotations());
        for(AxiomType<?> type : AxiomType.AXIOM_TYPES) {
            assertEquals(ont.getAxioms(type), ontIn.getAxioms(type));
        }
    }
}
