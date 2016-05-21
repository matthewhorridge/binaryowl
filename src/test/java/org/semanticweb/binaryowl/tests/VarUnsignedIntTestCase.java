package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.stream.BinaryOWLStreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
        assertThat(bytes.length, is(1));
        assertThat(bytes[0], is((byte)0));
    }

    @Test
    public void testOne() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(1, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertThat(bytes.length, is(1));
        assertThat(bytes[0], is((byte)1));
    }

    @Test
    public void test126() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(126, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertThat(bytes.length, is(1));
        assertThat(bytes[0], is((byte)126));
    }

    @Test
    public void test127() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(127, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertThat(bytes.length, is(3));
        assertThat(bytes[0], is((byte)-2));
        assertThat(bytes[1], is((byte)0));
        assertThat(bytes[2], is((byte)127));
    }

    @Test
    public void testTwoBytesMax() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(Short.MAX_VALUE - 1, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertThat(bytes.length, is(3));
        assertThat(bytes[1], is((byte)127));
        assertThat(bytes[2], is((byte)-2));
    }

    @Test
    public void testFiveBytes() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(os);
        BinaryOWLStreamUtil.writeVariableLengthUnsignedInt(Short.MAX_VALUE + 1, dataOutput);
        final byte[] bytes = os.toByteArray();
        assertThat(bytes.length, is(5));
        assertThat(bytes[1], is((byte)0));
        assertThat(bytes[2], is((byte)0));
        assertThat(bytes[3], is((byte)-128));
        assertThat(bytes[4], is((byte)0));
    }


}
