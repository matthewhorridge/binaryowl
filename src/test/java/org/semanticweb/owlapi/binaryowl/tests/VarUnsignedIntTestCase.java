package org.semanticweb.owlapi.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLStreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class VarUnsignedIntTestCase {

    @Test
    public void testZero() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(0, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertEquals(bytes.length, 1);
        assertEquals(bytes[0], 0);
    }

    @Test
    public void testOne() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(1, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertEquals(bytes.length, 1);
        assertEquals(bytes[0], 1);
    }

    @Test
    public void test126() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(126, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertEquals(bytes.length, 1);
        assertEquals(bytes[0], 126);
    }

    @Test
    public void test127() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(127, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertEquals(bytes.length, 3);
        assertEquals(bytes[0], -2);
        assertEquals(bytes[1], 0);
        assertEquals(bytes[2], 127);
    }

    @Test
    public void testTwoBytesMax() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(Short.MAX_VALUE - 1, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertEquals(bytes.length, 3);
        assertEquals(bytes[1], 127);
        assertEquals(bytes[2], -2);
    }

    @Test
    public void testFiveBytes() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(Short.MAX_VALUE + 1, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertEquals(bytes.length, 5);
        assertEquals(bytes[1], 0);
        assertEquals(bytes[2], 0);
        assertEquals(bytes[3], -128);
        assertEquals(bytes[4], 0);
    }


}
