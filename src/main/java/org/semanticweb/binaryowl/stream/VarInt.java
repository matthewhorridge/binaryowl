package org.semanticweb.binaryowl.stream;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Utility class for encoding and decoding variable-length integers ("VarInt").
 *
 * <p>A VarInt is a compact encoding for non-negative integers that uses
 * one or more bytes depending on the size of the value. This encoding
 * is widely used in binary serialization formats such as
 * Protocol Buffers, Avro, and Minecraft's network protocol.
 *
 * <h2>Encoding scheme</h2>
 *
 * Each byte stores 7 bits of the integer. The most significant bit
 * (MSB) of each byte acts as a continuation flag:
 * <ul>
 *   <li>If the MSB is <b>1</b>, another byte follows.</li>
 *   <li>If the MSB is <b>0</b>, this is the final byte.</li>
 * </ul>
 *
 * <p>For example, the integer value 300 (binary {@code 0000 0001 0010 1100})
 * is encoded as two bytes:
 *
 * <pre>
 *   0xAC 0x02
 *
 *   Step 1: Take lower 7 bits = 0b0101100 (44 decimal).
 *           Set continuation bit → 0b10101100 (0xAC).
 *   Step 2: Remaining bits = 0b10 (2 decimal).
 *           No continuation needed → 0b00000010 (0x02).
 * </pre>
 *
 * <p>This saves space for small numbers:
 * <ul>
 *   <li>0–127 → 1 byte</li>
 *   <li>128–16,383 → 2 bytes</li>
 *   <li>16,384–2,097,151 → 3 bytes</li>
 *   <li>… and so on up to full 32-bit range.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 *
 * Writing an integer:
 * <pre>
 *   try (DataOutputStream out = new DataOutputStream(...)) {
 *       VarInt.writeVarInt(out, 300);
 *   }
 * </pre>
 *
 * Reading an integer:
 * <pre>
 *   try (DataInputStream in = new DataInputStream(...)) {
 *       int value = VarInt.readVarInt(in);
 *   }
 * </pre>
 *
 * <h2>Error handling</h2>
 * If more than 5 bytes are read without finding a terminating byte,
 * {@link #readVarInt(DataInput)} throws an {@link IOException}, since
 * a 32-bit integer cannot exceed 5 bytes in VarInt format.
 *
 * <h2>Typical use case</h2>
 * VarInts are often used to prefix a byte array with its length:
 * <pre>
 *   VarInt.writeVarInt(out, data.length);
 *   out.write(data);
 * </pre>
 * Then:
 * <pre>
 *   int len = VarInt.readVarInt(in);
 *   byte[] data = new byte[len];
 *   in.readFully(data);
 * </pre>
 */
public class VarInt {

    /**
     * Writes an integer as a VarInt to the given DataOutput.
     *
     * @param out   the output stream to write to
     * @param value the integer to encode (non-negative)
     * @throws IOException if an I/O error occurs
     */
    public static void writeVarInt(DataOutput out, int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value);
    }

    /**
     * Reads a VarInt-encoded integer from the given DataInput.
     *
     * @param in the input stream to read from
     * @return the decoded integer value
     * @throws IOException if an I/O error occurs or the encoding is invalid
     */
    public static int readVarInt(DataInput in) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (currentByte & 0x7F) << position;

            if ((currentByte & 0x80) == 0) {
                break; // last byte
            }

            position += 7;

            if (position >= 32) {
                throw new IOException("VarInt too long");
            }
        }

        return value;
    }
}
