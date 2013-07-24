package org.semanticweb.binaryowl.stream;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/10/2012
 */
public final class BinaryOWLStreamUtil {


    /**
     * Every collection in the file is stored as a set.  Therefore, when we read it in, we don't have to check for
     * duplicates.  Further more, most consumers of read in sets just iterate over them - they don't call contains()
     * or add().  Therefore, we can optimise this and have a set which is really a list.
     * @param <O>
     */
    protected static class ListBackedSet<O> extends ArrayList<O> implements Set<O> {

        protected ListBackedSet(int initialCapacity) {
            super(initialCapacity);
        }
    }


    protected static int readCollectionSize(DataInput dataInput) throws IOException {
        return readVariableLengthUnsignedInt(dataInput);
    }

    protected static void writeCollectionSize(int size, DataOutput dataOutput) throws IOException {
        writeVariableLengthUnsignedInt(size, dataOutput);
    }

    public static void writeVariableLengthUnsignedInt(int i, DataOutput dataOutput) throws IOException {
        if(i < 0) {
            throw new RuntimeException("Cannot write int < 0");
        }
        else if(i == 0) {
            dataOutput.writeByte(0);
        }
        else if(i < Byte.MAX_VALUE) {
            dataOutput.writeByte(i);
        }
        else if(i < Short.MAX_VALUE) {
            dataOutput.writeByte(-2);
            dataOutput.writeShort(i);
        }
        else {
            dataOutput.writeByte(-4);
            dataOutput.writeInt(i);
        }
    }

    public static int readVariableLengthUnsignedInt(DataInput dataInput) throws IOException {
        byte size = dataInput.readByte();
        if(size == 0) {
            return 0;
        }
        else if(size == -2) {
            return dataInput.readShort();
        }
        else if(size == -4) {
            return dataInput.readInt();
        }
        else if(size < Byte.MAX_VALUE) {
            return size;
        }
        else {
            throw new RuntimeException();
        }
    }


    public static DataInputStream getDataInputStream(InputStream inputStream) {
        DataInputStream dis;
        if(inputStream instanceof DataInputStream) {
            dis = (DataInputStream) inputStream;
        }
        else {
            dis = new DataInputStream(inputStream);
        }
        return dis;
    }

}



