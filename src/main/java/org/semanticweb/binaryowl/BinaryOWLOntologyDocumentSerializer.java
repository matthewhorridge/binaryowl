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

package org.semanticweb.binaryowl;

import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.doc.OWLOntologyDocument;
import org.semanticweb.binaryowl.owlobject.serializer.SerializerBase;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLStreamUtil;
import org.semanticweb.binaryowl.versioning.BinaryOWLDocumentBodySerializer;
import org.semanticweb.binaryowl.versioning.BinaryOWLDocumentBodySerializerSelector;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.UnloadableImportException;

import java.io.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/04/2012
 */
public class BinaryOWLOntologyDocumentSerializer extends SerializerBase {

    public static final byte CHUNK_FOLLOWS_MARKER = 33;

    /**
     * Reads an {@link org.semanticweb.binaryowl.doc.OWLOntologyDocument} that is stored in binary OWL.
     * @param inputStream The input stream to read the document from.  Not {@code null}.
     * @param handler The handler that handels document elements as they are read.  Not {@code null}.
     * @param df An {@link OWLDataFactory} that can be used to instantiate {@link org.semanticweb.owlapi.model.OWLObject}s.  Not {@code null}.
     * @param <E> The type of exception thrown by the handler.
     * @throws IOException If there was a problem reading from the input stream.
     * @throws BinaryOWLParseException If the binary OWL format was corrupt.
     * @throws UnloadableImportException If an import could not be loaded.
     * @throws E A custom exception type.
     */
    public <E extends Throwable> void read(InputStream inputStream, BinaryOWLOntologyDocumentHandler<E> handler, OWLDataFactory df) throws IOException, BinaryOWLParseException, UnloadableImportException, E {
        checkNotNull(inputStream);
        checkNotNull(handler);
        checkNotNull(df);

        DataInputStream dis = BinaryOWLStreamUtil.getDataInputStream(inputStream);
        BinaryOWLOntologyDocumentPreamble preamble = new BinaryOWLOntologyDocumentPreamble(dis);
        BinaryOWLVersion fileFormatVersion = preamble.getFileFormatVersion();

        handler.handleBeginDocument();
        handler.handlePreamble(preamble);

        BinaryOWLDocumentBodySerializerSelector selector = new BinaryOWLDocumentBodySerializerSelector();
        BinaryOWLDocumentBodySerializer serializer = selector.getSerializerForVersion(fileFormatVersion);
        serializer.read(dis, handler, df);
    }


    /**
     * Writes out an {@link org.semanticweb.binaryowl.doc.OWLOntologyDocument} in binary OWL.
     * @param document The document to be written out. Not {@code null}.
     * @param dos The output stream to write the document to.  Not {@code null}.
     * @throws IOException If there was a problem writing to the stream.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public void write(OWLOntologyDocument document, OutputStream dos) throws IOException {
        write(document, dos, new BinaryOWLMetadata());
    }

    /**
     * Writes out an {@link OWLOntologyDocument} in binary OWL.
     * @param document The document to be written out. Not {@code null}.
     * @param os The output stream to write the document to.  Not {@code null}.
     * @param documentMetadata Document metadata.  Not {@code null}.
     * @throws IOException If there was a problem writing to the stream.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public void write(OWLOntologyDocument document, OutputStream os, BinaryOWLMetadata documentMetadata) throws IOException {
        checkNotNull(document);
        checkNotNull(os);
        checkNotNull(documentMetadata);

        DataOutputStream dos = new DataOutputStream(os);
        BinaryOWLOntologyDocumentPreamble preamble = new BinaryOWLOntologyDocumentPreamble();
        preamble.write(dos);
        BinaryOWLDocumentBodySerializerSelector selector = new BinaryOWLDocumentBodySerializerSelector();
        BinaryOWLVersion fileFormatVersion = preamble.getFileFormatVersion();
        BinaryOWLDocumentBodySerializer serializer = selector.getSerializerForVersion(fileFormatVersion);
        serializer.write(document, dos, documentMetadata);
    }


    public void appendOntologyChanges(BinaryOWLOutputStream dos, OntologyChangeDataList changeRecords) throws IOException {
        dos.writeByte(BinaryOWLOntologyDocumentSerializer.CHUNK_FOLLOWS_MARKER);
        changeRecords.write(dos);
    }

    public void appendOntologyChanges(File file, OntologyChangeDataList changeRecords) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, true);
        appendOntologyChanges(new BinaryOWLOutputStream(fos, BinaryOWLVersion.getVersion(1)), changeRecords);
        fos.close();
    }










}
