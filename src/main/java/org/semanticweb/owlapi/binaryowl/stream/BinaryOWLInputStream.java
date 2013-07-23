package org.semanticweb.owlapi.binaryowl.stream;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.BinaryOWLVersion;
import org.semanticweb.owlapi.binaryowl.OWLObjectBinaryType;
import org.semanticweb.owlapi.binaryowl.lookup.AnonymousIndividualLookupTable;
import org.semanticweb.owlapi.binaryowl.lookup.IRILookupTable;
import org.semanticweb.owlapi.binaryowl.lookup.LiteralLookupTable;
import org.semanticweb.owlapi.binaryowl.lookup.LookupTable;
import org.semanticweb.owlapi.binaryowl.owlobject.OWLLiteralSerializer;
import org.semanticweb.owlapi.model.*;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.semanticweb.owlapi.binaryowl.stream.BinaryOWLStreamUtil.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/10/2012
 */
public class BinaryOWLInputStream extends InputStream {

    private DataInputStream dataInput;
    
    private LookupTable lookupTable;
    
    private OWLDataFactory dataFactory;

    private BinaryOWLVersion version;

    public static final OWLLiteralSerializer LITERAL_SERIALIZER = new OWLLiteralSerializer();

    private ArrayList<OWLAnonymousIndividual> anonIndividualList = new ArrayList<OWLAnonymousIndividual>();

    public BinaryOWLInputStream(InputStream inputStream, OWLDataFactory dataFactory, BinaryOWLVersion version) {
        this(inputStream, createEmptyLookupTable(dataFactory), dataFactory, version);
    }

    private static LookupTable createEmptyLookupTable(OWLDataFactory dataFactory) {
        return LookupTable.emptyLookupTable();
    }

    public BinaryOWLInputStream(InputStream inputStream, LookupTable lookupTable, OWLDataFactory dataFactory, BinaryOWLVersion version) {
        if(inputStream instanceof DataInputStream) {
            this.dataInput = (DataInputStream) inputStream;
        }
        else {
            this.dataInput = new DataInputStream(inputStream);
        }
        this.lookupTable = lookupTable;
        this.dataFactory = dataFactory;
        this.version = version;
    }

    public BinaryOWLVersion getVersion() {
        return version;
    }

    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    @SuppressWarnings("unchecked")
    public <O extends OWLObject> O readOWLObject() throws IOException, BinaryOWLParseException  {
        return OWLObjectBinaryType.read(this);
    }

    @SuppressWarnings("unchecked")
    public <O extends OWLObject> Set<O> readOWLObjects() throws IOException, BinaryOWLParseException {
        int length = readVariableLengthUnsignedInt(dataInput);
        if(length == 0) {
            return Collections.emptySet();
        }
        else {
            Set<O> result = new ListBackedSet<O>(length + 2);
            for(int i = 0; i < length; i++) {
                OWLObject object = readOWLObject();
                result.add((O) object);
            }
            return result;
        }
    }

    public <O extends OWLObject> List<O> readOWLObjectList() throws IOException, BinaryOWLParseException {
        int size = readVariableLengthUnsignedInt(dataInput);
        if(size == 0) {
            return Collections.emptyList();
        }
        else {
            List<O> result = new ArrayList<O>();
            for(int i = 0; i < size; i++) {
                O element = readOWLObject();
                result.add(element);
            }
            return result;
        }
    }

    public IRI readIRI() throws IOException {
        return lookupTable.readIRI(dataInput);
    }
    
    public OWLClass readClassIRI() throws IOException {
        return lookupTable.readClassIRI(dataInput);
    }
    
    public OWLObjectProperty readObjectPropertyIRI() throws IOException {
        return lookupTable.readObjectPropertyIRI(dataInput);
    }
    
    public OWLDataProperty readDataPropertyIRI() throws IOException {
        return lookupTable.readDataPropertyIRI(dataInput);
    }

    public OWLAnnotationProperty readAnnotationPropertyIRI() throws IOException {
        return lookupTable.readAnnotationPropertyIRI(dataInput);
    }

    public OWLNamedIndividual readIndividualIRI() throws IOException {
        return lookupTable.readIndividualIRI(dataInput);
    }

    public OWLDatatype readDatatypeIRI() throws IOException {
        return lookupTable.readDatatypeIRI(dataInput);
    }

    public OWLLiteral readLiteral() throws IOException, BinaryOWLParseException {
        if(version.getVersion() == 1) {
            return LITERAL_SERIALIZER.readLiteral(this);
        }
        else {
            // Just read as OWLObject
            return readOWLObject();
        }
    }
    
    private Map<Integer, OWLAnonymousIndividual> map = new HashMap<Integer, OWLAnonymousIndividual>();

    public OWLAnonymousIndividual readAnonymousIndividual() throws IOException {
        int id = readInt();
//        OWLAnonymousIndividual ind = map.get(id);
//        if(ind == null) {
//            ind = dataFactory.getOWLAnonymousIndividual(Integer.toString(id));
//            map.put(id, ind);
//        }
//        return ind;
//        System.out.println(id);
        if(id > 100000) {
            return dataFactory.getOWLAnonymousIndividual(Integer.toString(id));
        }
        if(id == anonIndividualList.size()) {
            OWLAnonymousIndividual ind = dataFactory.getOWLAnonymousIndividual(Integer.toString(id));
            anonIndividualList.add(ind);
            return ind;
        }
        anonIndividualList.ensureCapacity(id);
        OWLAnonymousIndividual ind;
        if (id < anonIndividualList.size()) {
            ind = anonIndividualList.get(id);
            if(ind == null) {
                ind = dataFactory.getOWLAnonymousIndividual(Integer.toString(id));
                anonIndividualList.set(id, ind);
            }
        }
        else {
            for(int i = anonIndividualList.size(); i < id + 1; i++) {
                anonIndividualList.add(null);
            }
            ind = dataFactory.getOWLAnonymousIndividual(Integer.toString(id));
            anonIndividualList.set(id, ind);
        }
        return ind;
    }

    
    /**
     * Reads one input byte and returns
     * <code>true</code> if that byte is nonzero,
     * <code>false</code> if that byte is zero.
     * This method is suitable for reading
     * the byte written by the <code>writeBoolean</code>
     * method of interface <code>DataOutput</code>.
     * @return the <code>boolean</code> value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public boolean readBoolean() throws IOException {
        return dataInput.readBoolean();
    }

    /**
     * Reads and returns one input byte.
     * The byte is treated as a signed value in
     * the range <code>-128</code> through <code>127</code>,
     * inclusive.
     * This method is suitable for
     * reading the byte written by the <code>writeByte</code>
     * method of interface <code>DataOutput</code>.
     * @return the 8-bit value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public byte readByte() throws IOException {
        return dataInput.readByte();
    }

    /**
     * Reads one input byte, zero-extends
     * it to type <code>int</code>, and returns
     * the result, which is therefore in the range
     * <code>0</code>
     * through <code>255</code>.
     * This method is suitable for reading
     * the byte written by the <code>writeByte</code>
     * method of interface <code>DataOutput</code>
     * if the argument to <code>writeByte</code>
     * was intended to be a value in the range
     * <code>0</code> through <code>255</code>.
     * @return the unsigned 8-bit value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public int readUnsignedByte() throws IOException {
        return dataInput.readUnsignedByte();
    }

    /**
     * Reads two input bytes and returns
     * a <code>short</code> value. Let <code>a</code>
     * be the first byte read and <code>b</code>
     * be the second byte. The value
     * returned
     * is:
     * <p><pre><code>(short)((a &lt;&lt; 8) | (b &amp; 0xff))
     * </code></pre>
     * This method
     * is suitable for reading the bytes written
     * by the <code>writeShort</code> method of
     * interface <code>DataOutput</code>.
     * @return the 16-bit value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public short readShort() throws IOException {
        return dataInput.readShort();
    }

    /**
     * Reads two input bytes and returns
     * an <code>int</code> value in the range <code>0</code>
     * through <code>65535</code>. Let <code>a</code>
     * be the first byte read and
     * <code>b</code>
     * be the second byte. The value returned is:
     * <p><pre><code>(((a &amp; 0xff) &lt;&lt; 8) | (b &amp; 0xff))
     * </code></pre>
     * This method is suitable for reading the bytes
     * written by the <code>writeShort</code> method
     * of interface <code>DataOutput</code>  if
     * the argument to <code>writeShort</code>
     * was intended to be a value in the range
     * <code>0</code> through <code>65535</code>.
     * @return the unsigned 16-bit value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public int readUnsignedShort() throws IOException {
        return dataInput.readUnsignedShort();
    }

    /**
     * Reads two input bytes and returns a <code>char</code> value.
     * Let <code>a</code>
     * be the first byte read and <code>b</code>
     * be the second byte. The value
     * returned is:
     * <p><pre><code>(char)((a &lt;&lt; 8) | (b &amp; 0xff))
     * </code></pre>
     * This method
     * is suitable for reading bytes written by
     * the <code>writeChar</code> method of interface
     * <code>DataOutput</code>.
     * @return the <code>char</code> value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public char readChar() throws IOException {
        return dataInput.readChar();
    }

    /**
     * Reads four input bytes and returns an
     * <code>int</code> value. Let <code>a-d</code>
     * be the first through fourth bytes read. The value returned is:
     * <p><pre>
     * <code>
     * (((a &amp; 0xff) &lt;&lt; 24) | ((b &amp; 0xff) &lt;&lt; 16) |
     * &#32;((c &amp; 0xff) &lt;&lt; 8) | (d &amp; 0xff))
     * </code></pre>
     * This method is suitable
     * for reading bytes written by the <code>writeInt</code>
     * method of interface <code>DataOutput</code>.
     * @return the <code>int</code> value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public int readInt() throws IOException {
        return dataInput.readInt();
    }

    /**
     * Reads eight input bytes and returns
     * a <code>long</code> value. Let <code>a-h</code>
     * be the first through eighth bytes read.
     * The value returned is:
     * <p><pre> <code>
     * (((long)(a &amp; 0xff) &lt;&lt; 56) |
     *  ((long)(b &amp; 0xff) &lt;&lt; 48) |
     *  ((long)(c &amp; 0xff) &lt;&lt; 40) |
     *  ((long)(d &amp; 0xff) &lt;&lt; 32) |
     *  ((long)(e &amp; 0xff) &lt;&lt; 24) |
     *  ((long)(f &amp; 0xff) &lt;&lt; 16) |
     *  ((long)(g &amp; 0xff) &lt;&lt;  8) |
     *  ((long)(h &amp; 0xff)))
     * </code></pre>
     * <p>
     * This method is suitable
     * for reading bytes written by the <code>writeLong</code>
     * method of interface <code>DataOutput</code>.
     * @return the <code>long</code> value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public long readLong() throws IOException {
        return dataInput.readLong();
    }

    /**
     * Reads four input bytes and returns
     * a <code>float</code> value. It does this
     * by first constructing an <code>int</code>
     * value in exactly the manner
     * of the <code>readInt</code>
     * method, then converting this <code>int</code>
     * value to a <code>float</code> in
     * exactly the manner of the method <code>Float.intBitsToFloat</code>.
     * This method is suitable for reading
     * bytes written by the <code>writeFloat</code>
     * method of interface <code>DataOutput</code>.
     * @return the <code>float</code> value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public float readFloat() throws IOException {
        return dataInput.readFloat();
    }

    /**
     * Reads eight input bytes and returns
     * a <code>double</code> value. It does this
     * by first constructing a <code>long</code>
     * value in exactly the manner
     * of the <code>readlong</code>
     * method, then converting this <code>long</code>
     * value to a <code>double</code> in exactly
     * the manner of the method <code>Double.longBitsToDouble</code>.
     * This method is suitable for reading
     * bytes written by the <code>writeDouble</code>
     * method of interface <code>DataOutput</code>.
     * @return the <code>double</code> value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    public double readDouble() throws IOException {
        return dataInput.readDouble();
    }

    /**
     * Reads the next line of text from the input stream.
     * It reads successive bytes, converting
     * each byte separately into a character,
     * until it encounters a line terminator or
     * end of
     * file; the characters read are then
     * returned as a <code>String</code>. Note
     * that because this
     * method processes bytes,
     * it does not support input of the full Unicode
     * character set.
     * <p>
     * If end of file is encountered
     * before even one byte can be read, then <code>null</code>
     * is returned. Otherwise, each byte that is
     * read is converted to type <code>char</code>
     * by zero-extension. If the character <code>'\n'</code>
     * is encountered, it is discarded and reading
     * ceases. If the character <code>'\r'</code>
     * is encountered, it is discarded and, if
     * the following byte converts &#32;to the
     * character <code>'\n'</code>, then that is
     * discarded also; reading then ceases. If
     * end of file is encountered before either
     * of the characters <code>'\n'</code> and
     * <code>'\r'</code> is encountered, reading
     * ceases. Once reading has ceased, a <code>String</code>
     * is returned that contains all the characters
     * read and not discarded, taken in order.
     * Note that every character in this string
     * will have a value less than <code>&#92;u0100</code>,
     * that is, <code>(char)256</code>.
     * @return the next line of text from the input stream,
     *         or <CODE>null</CODE> if the end of file is
     *         encountered before a byte can be read.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public String readLine() throws IOException {
        return dataInput.readLine();
    }

    /**
     * Reads in a string that has been encoded using a
     * <a href="#modified-utf-8">modified UTF-8</a>
     * format.
     * The general contract of <code>readUTF</code>
     * is that it reads a representation of a Unicode
     * character string encoded in modified
     * UTF-8 format; this string of characters
     * is then returned as a <code>String</code>.
     * <p>
     * First, two bytes are read and used to
     * construct an unsigned 16-bit integer in
     * exactly the manner of the <code>readUnsignedShort</code>
     * method . This integer value is called the
     * <i>UTF length</i> and specifies the number
     * of additional bytes to be read. These bytes
     * are then converted to characters by considering
     * them in groups. The length of each group
     * is computed from the value of the first
     * byte of the group. The byte following a
     * group, if any, is the first byte of the
     * next group.
     * <p>
     * If the first byte of a group
     * matches the bit pattern <code>0xxxxxxx</code>
     * (where <code>x</code> means "may be <code>0</code>
     * or <code>1</code>"), then the group consists
     * of just that byte. The byte is zero-extended
     * to form a character.
     * <p>
     * If the first byte
     * of a group matches the bit pattern <code>110xxxxx</code>,
     * then the group consists of that byte <code>a</code>
     * and a second byte <code>b</code>. If there
     * is no byte <code>b</code> (because byte
     * <code>a</code> was the last of the bytes
     * to be read), or if byte <code>b</code> does
     * not match the bit pattern <code>10xxxxxx</code>,
     * then a <code>UTFDataFormatException</code>
     * is thrown. Otherwise, the group is converted
     * to the character:<p>
     * <pre><code>(char)(((a&amp; 0x1F) &lt;&lt; 6) | (b &amp; 0x3F))
     * </code></pre>
     * If the first byte of a group
     * matches the bit pattern <code>1110xxxx</code>,
     * then the group consists of that byte <code>a</code>
     * and two more bytes <code>b</code> and <code>c</code>.
     * If there is no byte <code>c</code> (because
     * byte <code>a</code> was one of the last
     * two of the bytes to be read), or either
     * byte <code>b</code> or byte <code>c</code>
     * does not match the bit pattern <code>10xxxxxx</code>,
     * then a <code>UTFDataFormatException</code>
     * is thrown. Otherwise, the group is converted
     * to the character:<p>
     * <pre><code>
     * (char)(((a &amp; 0x0F) &lt;&lt; 12) | ((b &amp; 0x3F) &lt;&lt; 6) | (c &amp; 0x3F))
     * </code></pre>
     * If the first byte of a group matches the
     * pattern <code>1111xxxx</code> or the pattern
     * <code>10xxxxxx</code>, then a <code>UTFDataFormatException</code>
     * is thrown.
     * <p>
     * If end of file is encountered
     * at any time during this entire process,
     * then an <code>EOFException</code> is thrown.
     * <p>
     * After every group has been converted to
     * a character by this process, the characters
     * are gathered, in the same order in which
     * their corresponding groups were read from
     * the input stream, to form a <code>String</code>,
     * which is returned.
     * <p>
     * The <code>writeUTF</code>
     * method of interface <code>DataOutput</code>
     * may be used to write data that is suitable
     * for reading by this method.
     * @return a Unicode string.
     * @throws java.io.EOFException           if this stream reaches the end
     *                                        before reading all the bytes.
     * @throws java.io.IOException            if an I/O error occurs.
     * @throws java.io.UTFDataFormatException if the bytes do not represent a
     *                                        valid modified UTF-8 encoding of a string.
     */
    public String readUTF() throws IOException {
        return dataInput.readUTF();
    }

    /**
     * Reads some bytes from an input
     * stream and stores them into the buffer
     * array <code>b</code>. The number of bytes
     * read is equal
     * to the length of <code>b</code>.
     * <p>
     * This method blocks until one of the
     * following conditions occurs:<p>
     * <ul>
     * <li><code>b.length</code>
     * bytes of input data are available, in which
     * case a normal return is made.
     *
     * <li>End of
     * file is detected, in which case an <code>EOFException</code>
     * is thrown.
     *
     * <li>An I/O error occurs, in
     * which case an <code>IOException</code> other
     * than <code>EOFException</code> is thrown.
     * </ul>
     * <p>
     * If <code>b</code> is <code>null</code>,
     * a <code>NullPointerException</code> is thrown.
     * If <code>b.length</code> is zero, then
     * no bytes are read. Otherwise, the first
     * byte read is stored into element <code>b[0]</code>,
     * the next one into <code>b[1]</code>, and
     * so on.
     * If an exception is thrown from
     * this method, then it may be that some but
     * not all bytes of <code>b</code> have been
     * updated with data from the input stream.
     *
     * @param     bytes   the buffer into which the data is read.
     * @exception java.io.EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
    public void readFully(byte [] bytes) throws IOException {
        dataInput.readFully(bytes);
    }

    public void skipBytes(int n) throws IOException {
        dataInput.skipBytes(n);
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * <p> A subclass must provide an implementation of this method.
     * @return the next byte of data, or <code>-1</code> if the end of the
     *         stream is reached.
     * @throws java.io.IOException if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        return dataInput.read();
    }
}
