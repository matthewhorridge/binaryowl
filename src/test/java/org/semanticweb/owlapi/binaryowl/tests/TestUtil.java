package org.semanticweb.owlapi.binaryowl.tests;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.BinaryOWLVersion;
import org.semanticweb.owlapi.binaryowl.OWLObjectBinaryTypeSelector;
import org.semanticweb.owlapi.binaryowl.owlobject.OWLObjectSerializer;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 */
public class TestUtil {


    public static final BinaryOWLVersion VERSION = BinaryOWLVersion.getVersion(1);


    public static void roundTrip(OWLObject obj) {
        roundTripObject(obj);
    }

    @SuppressWarnings("unchecked")
    private static void roundTripObject(OWLObject obj) {
        try {
            OWLObjectBinaryTypeSelector selector = new OWLObjectBinaryTypeSelector();
            OWLObjectSerializer<OWLObject> serializer = (OWLObjectSerializer<OWLObject>) obj.accept(selector).getSerializer();
            ByteArrayOutputStream dataOutput = new ByteArrayOutputStream();
            BinaryOWLOutputStream out = new BinaryOWLOutputStream(dataOutput, VERSION);
            serializer.write(obj, out);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(dataOutput.toByteArray());
            OWLObject readObject = serializer.read(new BinaryOWLInputStream(inputStream, OWLManager.getOWLDataFactory(), VERSION));
            assertEquals(obj, readObject);
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
        catch (BinaryOWLParseException e) {
            fail(e.getMessage());
        }
    }
}
