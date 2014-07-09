package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.BinaryOWLVersion;
import org.semanticweb.binaryowl.owlobject.serializer.OWLLiteralSerializer;
import org.semanticweb.binaryowl.owlobject.serializer.OWLObjectSerializer;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplNoCompression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/07/2014
 */
public class EncodingTestCase {

    public static final String TEST_STRING = "Protégé";

    @Test
    public void shouldBeIndependentOfPlatformEncoding() throws IOException {
        // Need to have the default encoding set to something other than UTF-8 otherwise this test will just pass
        // even if there is a problem with the platform encoding.
        OWLObjectSerializer<OWLLiteral> literalSerializer = new OWLLiteralSerializer();
        OWLLiteral literal = new OWLLiteralImplNoCompression(TEST_STRING, "en", null);
        ByteArrayOutputStream dataOutput = new ByteArrayOutputStream();
        literalSerializer.write(literal, new BinaryOWLOutputStream(dataOutput, BinaryOWLVersion.getVersion(1)));
        OWLLiteral in = literalSerializer.read(new BinaryOWLInputStream(new ByteArrayInputStream(dataOutput.toByteArray()), new OWLDataFactoryImpl(), BinaryOWLVersion.getVersion(1)));
        assertThat(in.getLiteral(), is(equalTo(TEST_STRING)));
    }


}
