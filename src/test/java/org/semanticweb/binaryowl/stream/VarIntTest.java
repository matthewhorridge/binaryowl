package org.semanticweb.binaryowl.stream;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class VarIntTest {

    private static byte[] encode(int value) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        VarInt.writeVarInt(dos, value);
        dos.flush();
        return baos.toByteArray();
    }

    private static int decode(byte[] bytes) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        return VarInt.readVarInt(dis);
    }

    @Test
    public void encodesZero() throws IOException {
        assertArrayEquals(new byte[]{0x00}, encode(0));
    }

    @Test
    public void encodes127() throws IOException {
        assertArrayEquals(new byte[]{0x7F}, encode(127));
    }

    @Test
    public void encodes128() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0x80, 0x01}, encode(128));
    }

    @Test
    public void encodes16383() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0xFF, 0x7F}, encode(16_383));
    }

    @Test
    public void encodes16384() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0x80, (byte) 0x80, 0x01}, encode(16_384));
    }

    @Test
    public void encodesMaxInt() throws IOException {
        // 0x7FFFFFFF => 0xFF 0xFF 0xFF 0xFF 0x07
        assertArrayEquals(
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x07},
                encode(Integer.MAX_VALUE)
        );
    }

    // --- Round-trip decoding ---

    @Test
    public void roundTripsKeyBoundaries() throws IOException {
        int[] values = new int[]{
                0, 1, 2,
                0x7F, 0x80,
                0x3FFF, 0x4000,
                1 << 20, (1 << 21) - 1, 1 << 21,
                Integer.MAX_VALUE
        };
        for (int v : values) {
            byte[] enc = encode(v);
            int dec = decode(enc);
            assertEquals("Failed round-trip for value: " + v, v, dec);
        }
    }

    @Test
    public void roundTripsSequentialValuesInAStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        int[] values = {0, 1, 127, 128, 300, 16_383, 16_384, 1_234_567};
        for (int v : values) {
            VarInt.writeVarInt(out, v);
        }
        out.flush();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        for (int expected : values) {
            int actual = VarInt.readVarInt(in);
            assertEquals(expected, actual);
        }
        assertEquals("Stream should be fully consumed" , -1, in.read());
    }

    // --- Error handling ---

    @Test
    public void throwsIfMoreThanFiveBytesWithContinuation() throws IOException {
        // Five bytes with MSB set => position progresses to 35 (>=32) => IOException
        byte[] tooLong = new byte[]{
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80
        };
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(tooLong));
        try {
            VarInt.readVarInt(in);
            fail("Expected IOException for VarInt too long" );
        } catch (IOException ex) {
            assertTrue("Exception message should mention VarInt" ,
                    ex.getMessage() != null && ex.getMessage().toLowerCase().contains("varint" ));
        }
    }

    @Test(expected = EOFException.class)
    public void throwsOnUnexpectedEof() throws IOException {
        // Continuation set but stream ends before a terminating byte
        byte[] truncated = new byte[]{(byte) 0x80};
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(truncated));
        VarInt.readVarInt(in);
    }

    @Test
    public void decodesMaxIntEncoding() throws IOException {
        assertEquals(Integer.MAX_VALUE,
                decode(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x07}));
    }
}