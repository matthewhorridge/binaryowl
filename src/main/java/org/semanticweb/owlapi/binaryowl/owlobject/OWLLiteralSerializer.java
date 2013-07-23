/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, The University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.binaryowl.owlobject;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.lookup.IRILookupTable;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplNoCompression;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
public class OWLLiteralSerializer extends OWLObjectSerializer<OWLLiteral> {

    public static final String UTF_8 = "UTF-8";

//    public static final byte NOT_INDEXED_MARKER = -2;

    private static final byte RDF_PLAIN_LITERAL_MARKER = 0;

    private static final byte XSD_STRING_MARKER = 1;

    private static final byte XSD_BOOLEAN_MARKER = 2;

    private static final byte OTHER_DATATYPE_MARKER = 3;

    private static final byte LANG_MARKER = 1;

    private static final byte NO_LANG_MARKER = 0;


    private static final OWLDatatype RDF_PLAIN_LITERAL_DATATYPE = new OWLDatatypeImpl(OWL2Datatype.RDF_PLAIN_LITERAL.getIRI());

    private static final OWLDatatype XSD_STRING_DATATYPE = new OWLDatatypeImpl(OWL2Datatype.XSD_STRING.getIRI());

    private static final OWLDatatype XSD_BOOLEAN_DATATYPE = new OWLDatatypeImpl(OWL2Datatype.XSD_BOOLEAN.getIRI());



    private static final OWLLiteral BOOLEAN_TRUE = new OWLLiteralImplNoCompression("true", null, XSD_BOOLEAN_DATATYPE);

    private static final OWLLiteral BOOLEAN_FALSE = new OWLLiteralImplNoCompression("false", null, XSD_BOOLEAN_DATATYPE);

    @Override
    protected void writeObject(OWLLiteral object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeLiteral(object);
    }

    @Override
    protected OWLLiteral readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        return inputStream.readLiteral();
    }

    public OWLLiteral readLiteral(BinaryOWLInputStream is) throws IOException {

            return readRawLiteral(is);

    }

    private OWLLiteral readRawLiteral(BinaryOWLInputStream is) throws IOException {
        int typeMarker = is.readByte();
        if (typeMarker == RDF_PLAIN_LITERAL_MARKER) {
            int langMarker = is.readByte();
            if (langMarker == LANG_MARKER) {
                String lang = is.readUTF();
                byte[] literalBytes = readBytes(is);
                return new OWLLiteralImplNoCompression(literalBytes, lang, RDF_PLAIN_LITERAL_DATATYPE);
            }
            else if (langMarker == NO_LANG_MARKER) {
                byte[] literalBytes = readBytes(is);
                return new OWLLiteralImplNoCompression(literalBytes, null, RDF_PLAIN_LITERAL_DATATYPE);
            }
            else {
                throw new IOException("Unknown lang marker: " + langMarker);
            }
        }
        else if(typeMarker == XSD_STRING_MARKER) {
            byte[] literalBytes = readBytes(is);
            return new OWLLiteralImplNoCompression(literalBytes, null, XSD_STRING_DATATYPE);
        }
        else if(typeMarker == XSD_BOOLEAN_MARKER) {
            if(is.readBoolean()) {
                return BOOLEAN_TRUE;
            }
            else {
                return BOOLEAN_FALSE;
            }
        }
        else if (typeMarker == OTHER_DATATYPE_MARKER) {

            OWLDatatype datatype = is.readDatatypeIRI();
            byte[] literalBytes = readBytes(is);
            return new OWLLiteralImplNoCompression(literalBytes, null, datatype);
        }
        else {
            throw new RuntimeException("Unknown type marker: " + typeMarker);
        }


    }


    public void writeLiteral(BinaryOWLOutputStream outputStream, OWLLiteral literal) throws IOException {
            writeRawLiteral(outputStream, literal);


    }

    private void writeRawLiteral(BinaryOWLOutputStream outputStream, OWLLiteral literal) throws IOException {
        if(literal.getDatatype().equals(XSD_BOOLEAN_DATATYPE)) {
            outputStream.writeByte(XSD_BOOLEAN_MARKER);
            outputStream.writeBoolean(literal.parseBoolean());
            return;
        }
        else if (literal.isRDFPlainLiteral()) {
            outputStream.writeByte(RDF_PLAIN_LITERAL_MARKER);
            if (literal.hasLang()) {
                outputStream.write(LANG_MARKER);
                writeString(literal.getLang(), outputStream);
            }
            else {
                outputStream.writeByte(NO_LANG_MARKER);
            }
        }
        else if(literal.getDatatype().equals(XSD_STRING_DATATYPE)) {
            outputStream.writeByte(XSD_STRING_MARKER);
        }
        else {
            outputStream.writeByte(OTHER_DATATYPE_MARKER);
            outputStream.writeIRI(literal.getDatatype().getIRI());
        }

        byte[] literalBytes;
        if (literal instanceof OWLLiteralImplNoCompression) {
            literalBytes = ((OWLLiteralImplNoCompression) literal).getLiteral().getBytes();
        }
        else {
            literalBytes = literal.getLiteral().getBytes(UTF_8);
        }
        writeBytes(literalBytes, outputStream);



    }

    private void writeString(String s, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeUTF(s);
    }


    private void writeBytes(byte[] bytes, BinaryOWLOutputStream os) throws IOException {
        os.writeShort(bytes.length);
        os.write(bytes);
    }


    private byte[] readBytes(BinaryOWLInputStream is) throws IOException {
        int length = is.readShort();
        byte[] bytes = new byte[length];
        is.readFully(bytes);
        return bytes;
    }
}
