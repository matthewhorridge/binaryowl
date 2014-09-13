package org.semanticweb.binaryowl.stream;

import org.semanticweb.binaryowl.BinaryOWLVersion;
import org.semanticweb.binaryowl.owlobject.OWLObjectBinaryType;
import org.semanticweb.binaryowl.lookup.LookupTable;
import org.semanticweb.binaryowl.owlobject.serializer.OWLLiteralSerializer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import static org.semanticweb.binaryowl.stream.BinaryOWLStreamUtil.writeCollectionSize;
/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/10/2012
 */
public class BinaryOWLOutputStream extends OutputStream {

    private DataOutput dataOutput;
    
    private LookupTable lookupTable;

    public static final OWLLiteralSerializer LITERAL_SERIALIZER = new OWLLiteralSerializer();

    private BinaryOWLVersion version = BinaryOWLVersion.getVersion(1);

    private final SetTransformer setTransformer;

    public BinaryOWLOutputStream(OutputStream dataOutput, BinaryOWLVersion version) {
        if(dataOutput instanceof DataOutputStream) {
            this.dataOutput = (DataOutput) dataOutput;
        }
        else {
            this.dataOutput = new DataOutputStream(dataOutput);
        }
        this.version = version;
        lookupTable = LookupTable.emptyLookupTable();
        this.setTransformer = new PassThroughSetTransformer();
    }

    public BinaryOWLOutputStream(DataOutput dataOutput, LookupTable lookupTable) {
        this.dataOutput = dataOutput;
        this.lookupTable = lookupTable;
        this.setTransformer = new PassThroughSetTransformer();
    }

    public BinaryOWLOutputStream(DataOutput dataOutput, SetTransformer setTransformer) {
        this.dataOutput = dataOutput;
        this.setTransformer = setTransformer;
        this.lookupTable = LookupTable.emptyLookupTable();
    }

    public BinaryOWLVersion getVersion() {
        return version;
    }

    public void writeOWLObject(OWLObject object) throws IOException {
        OWLObjectBinaryType.write(object, this);
    }
    
    public void writeOWLObjects(Set<? extends OWLObject> objects) throws IOException {
        final int size = objects.size();
        writeCollectionSize(size, dataOutput);
        for(OWLObject object : setTransformer.transform(objects)) {
            writeOWLObject(object);
        }
    }

    public void writeOWLObjectList(List<? extends OWLObject> list) throws IOException {
        writeCollectionSize(list.size(), dataOutput);
        for(OWLObject object : list) {
            writeOWLObject(object);
        }
    }

    public void writeIRI(IRI iri) throws IOException {
        lookupTable.writeIRI(iri, dataOutput);
    }

    public void writeLiteral(OWLLiteral literal) throws IOException {
        if (version.getVersion() == 1) {
            LITERAL_SERIALIZER.writeLiteral(this, literal);
        }
        else {
            writeOWLObject(literal);
        }
    }

    public void writeAnonymousIndividual(OWLAnonymousIndividual individual) throws IOException {
        int index = lookupTable.getAnonymousIndividualLookupTable().getIndex(individual);
        dataOutput.writeInt(index);
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     * @param b the <code>byte</code>.
     * @throws java.io.IOException if an I/O error occurs. In particular,
     *                             an <code>IOException</code> may be thrown if the
     *                             output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {
        dataOutput.write(b);
    }

    /**
     * Writes a <code>boolean</code> value to this output stream.
     * If the argument <code>v</code>
     * is <code>true</code>, the value <code>(byte)1</code>
     * is written; if <code>v</code> is <code>false</code>,
     * the  value <code>(byte)0</code> is written.
     * The byte written by this method may
     * be read by the <code>readBoolean</code>
     * method of interface <code>DataInput</code>,
     * which will then return a <code>boolean</code>
     * equal to <code>v</code>.
     * @param v the boolean to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeBoolean(boolean v) throws IOException {
        dataOutput.writeBoolean(v);
    }

    /**
     * Writes to the output stream the eight low-
     * order bits of the argument <code>v</code>.
     * The 24 high-order bits of <code>v</code>
     * are ignored. (This means  that <code>writeByte</code>
     * does exactly the same thing as <code>write</code>
     * for an integer argument.) The byte written
     * by this method may be read by the <code>readByte</code>
     * method of interface <code>DataInput</code>,
     * which will then return a <code>byte</code>
     * equal to <code>(byte)v</code>.
     * @param v the byte value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeByte(int v) throws IOException {
        dataOutput.writeByte(v);
    }

    /**
     * Writes two bytes to the output
     * stream to represent the value of the argument.
     * The byte values to be written, in the  order
     * shown, are: <p>
     * <pre><code>
     * (byte)(0xff &amp; (v &gt;&gt; 8))
     * (byte)(0xff &amp; v)
     * </code> </pre> <p>
     * The bytes written by this method may be
     * read by the <code>readShort</code> method
     * of interface <code>DataInput</code> , which
     * will then return a <code>short</code> equal
     * to <code>(short)v</code>.
     * @param v the <code>short</code> value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeShort(int v) throws IOException {
        dataOutput.writeShort(v);
    }

    /**
     * Writes a <code>char</code> value, which
     * is comprised of two bytes, to the
     * output stream.
     * The byte values to be written, in the  order
     * shown, are:
     * <p><pre><code>
     * (byte)(0xff &amp; (v &gt;&gt; 8))
     * (byte)(0xff &amp; v)
     * </code></pre><p>
     * The bytes written by this method may be
     * read by the <code>readChar</code> method
     * of interface <code>DataInput</code> , which
     * will then return a <code>char</code> equal
     * to <code>(char)v</code>.
     * @param v the <code>char</code> value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeChar(int v) throws IOException {
        dataOutput.writeChar(v);
    }

    /**
     * Writes an <code>int</code> value, which is
     * comprised of four bytes, to the output stream.
     * The byte values to be written, in the  order
     * shown, are:
     * <p><pre><code>
     * (byte)(0xff &amp; (v &gt;&gt; 24))
     * (byte)(0xff &amp; (v &gt;&gt; 16))
     * (byte)(0xff &amp; (v &gt;&gt; &#32; &#32;8))
     * (byte)(0xff &amp; v)
     * </code></pre><p>
     * The bytes written by this method may be read
     * by the <code>readInt</code> method of interface
     * <code>DataInput</code> , which will then
     * return an <code>int</code> equal to <code>v</code>.
     * @param v the <code>int</code> value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeInt(int v) throws IOException {
        dataOutput.writeInt(v);
    }

    /**
     * Writes a <code>long</code> value, which is
     * comprised of eight bytes, to the output stream.
     * The byte values to be written, in the  order
     * shown, are:
     * <p><pre><code>
     * (byte)(0xff &amp; (v &gt;&gt; 56))
     * (byte)(0xff &amp; (v &gt;&gt; 48))
     * (byte)(0xff &amp; (v &gt;&gt; 40))
     * (byte)(0xff &amp; (v &gt;&gt; 32))
     * (byte)(0xff &amp; (v &gt;&gt; 24))
     * (byte)(0xff &amp; (v &gt;&gt; 16))
     * (byte)(0xff &amp; (v &gt;&gt;  8))
     * (byte)(0xff &amp; v)
     * </code></pre><p>
     * The bytes written by this method may be
     * read by the <code>readLong</code> method
     * of interface <code>DataInput</code> , which
     * will then return a <code>long</code> equal
     * to <code>v</code>.
     * @param v the <code>long</code> value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeLong(long v) throws IOException {
        dataOutput.writeLong(v);
    }

    /**
     * Writes a <code>float</code> value,
     * which is comprised of four bytes, to the output stream.
     * It does this as if it first converts this
     * <code>float</code> value to an <code>int</code>
     * in exactly the manner of the <code>Float.floatToIntBits</code>
     * method  and then writes the <code>int</code>
     * value in exactly the manner of the  <code>writeInt</code>
     * method.  The bytes written by this method
     * may be read by the <code>readFloat</code>
     * method of interface <code>DataInput</code>,
     * which will then return a <code>float</code>
     * equal to <code>v</code>.
     * @param v the <code>float</code> value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeFloat(float v) throws IOException {
        dataOutput.writeFloat(v);
    }

    /**
     * Writes a <code>double</code> value,
     * which is comprised of eight bytes, to the output stream.
     * It does this as if it first converts this
     * <code>double</code> value to a <code>long</code>
     * in exactly the manner of the <code>Double.doubleToLongBits</code>
     * method  and then writes the <code>long</code>
     * value in exactly the manner of the  <code>writeLong</code>
     * method. The bytes written by this method
     * may be read by the <code>readDouble</code>
     * method of interface <code>DataInput</code>,
     * which will then return a <code>double</code>
     * equal to <code>v</code>.
     * @param v the <code>double</code> value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeDouble(double v) throws IOException {
        dataOutput.writeDouble(v);
    }

    /**
     * Writes a string to the output stream.
     * For every character in the string
     * <code>s</code>,  taken in order, one byte
     * is written to the output stream.  If
     * <code>s</code> is <code>null</code>, a <code>NullPointerException</code>
     * is thrown.<p>  If <code>s.length</code>
     * is zero, then no bytes are written. Otherwise,
     * the character <code>s[0]</code> is written
     * first, then <code>s[1]</code>, and so on;
     * the last character written is <code>s[s.length-1]</code>.
     * For each character, one byte is written,
     * the low-order byte, in exactly the manner
     * of the <code>writeByte</code> method . The
     * high-order eight bits of each character
     * in the string are ignored.
     * @param s the string of bytes to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeBytes(String s) throws IOException {
        dataOutput.writeBytes(s);
    }

    /**
     * Writes every character in the string <code>s</code>,
     * to the output stream, in order,
     * two bytes per character. If <code>s</code>
     * is <code>null</code>, a <code>NullPointerException</code>
     * is thrown.  If <code>s.length</code>
     * is zero, then no characters are written.
     * Otherwise, the character <code>s[0]</code>
     * is written first, then <code>s[1]</code>,
     * and so on; the last character written is
     * <code>s[s.length-1]</code>. For each character,
     * two bytes are actually written, high-order
     * byte first, in exactly the manner of the
     * <code>writeChar</code> method.
     * @param s the string value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeChars(String s) throws IOException {
        dataOutput.writeChars(s);
    }

    /**
     * Writes two bytes of length information
     * to the output stream, followed
     * by the
     * <a href="DataInput.html#modified-utf-8">modified UTF-8</a>
     * representation
     * of  every character in the string <code>s</code>.
     * If <code>s</code> is <code>null</code>,
     * a <code>NullPointerException</code> is thrown.
     * Each character in the string <code>s</code>
     * is converted to a group of one, two, or
     * three bytes, depending on the value of the
     * character.<p>
     * If a character <code>c</code>
     * is in the range <code>&#92;u0001</code> through
     * <code>&#92;u007f</code>, it is represented
     * by one byte:<p>
     * <pre>(byte)c </pre>  <p>
     * If a character <code>c</code> is <code>&#92;u0000</code>
     * or is in the range <code>&#92;u0080</code>
     * through <code>&#92;u07ff</code>, then it is
     * represented by two bytes, to be written
     * in the order shown:<p> <pre><code>
     * (byte)(0xc0 | (0x1f &amp; (c &gt;&gt; 6)))
     * (byte)(0x80 | (0x3f &amp; c))
     *  </code></pre>  <p> If a character
     * <code>c</code> is in the range <code>&#92;u0800</code>
     * through <code>uffff</code>, then it is
     * represented by three bytes, to be written
     * in the order shown:<p> <pre><code>
     * (byte)(0xe0 | (0x0f &amp; (c &gt;&gt; 12)))
     * (byte)(0x80 | (0x3f &amp; (c &gt;&gt;  6)))
     * (byte)(0x80 | (0x3f &amp; c))
     *  </code></pre>  <p> First,
     * the total number of bytes needed to represent
     * all the characters of <code>s</code> is
     * calculated. If this number is larger than
     * <code>65535</code>, then a <code>UTFDataFormatException</code>
     * is thrown. Otherwise, this length is written
     * to the output stream in exactly the manner
     * of the <code>writeShort</code> method;
     * after this, the one-, two-, or three-byte
     * representation of each character in the
     * string <code>s</code> is written.<p>  The
     * bytes written by this method may be read
     * by the <code>readUTF</code> method of interface
     * <code>DataInput</code> , which will then
     * return a <code>String</code> equal to <code>s</code>.
     * @param s the string value to be written.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeUTF(String s) throws IOException {
        dataOutput.writeUTF(s);
    }
    
    public void write(byte [] bytes) throws IOException {
        dataOutput.write(bytes);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
