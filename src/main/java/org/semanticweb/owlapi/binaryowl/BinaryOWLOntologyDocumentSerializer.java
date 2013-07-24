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

package org.semanticweb.owlapi.binaryowl;

import org.semanticweb.owlapi.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.owlapi.binaryowl.owlobject.SerializerBase;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLStreamUtil;
import org.semanticweb.owlapi.binaryowl.versioning.BinaryOWLDocumentBodySerializerSelector;
import org.semanticweb.owlapi.binaryowl.versioning.v1.BinaryOWLV1DocumentBodySerializer;
import org.semanticweb.owlapi.model.*;

import java.io.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/04/2012
 */
public class BinaryOWLOntologyDocumentSerializer extends SerializerBase {

    public static final byte CHUNK_FOLLOWS_MARKER = 33;

    public <E extends Throwable> BinaryOWLOntologyDocumentFormat read(InputStream inputStream, BinaryOWLOntologyDocumentHandler<E> handler, OWLDataFactory df) throws IOException, BinaryOWLParseException, UnloadableImportException, E {

        DataInputStream dis = BinaryOWLStreamUtil.getDataInputStream(inputStream);
        BinaryOWLOntologyDocumentPreamble preamble = new BinaryOWLOntologyDocumentPreamble(dis);
        BinaryOWLVersion fileFormatVersion = preamble.getFileFormatVersion();

        handler.handleBeginDocument();
        handler.handlePreamble(preamble);

        BinaryOWLDocumentBodySerializerSelector selector = new BinaryOWLDocumentBodySerializerSelector();
        BinaryOWLDocumentBodySerializer serializer = selector.getSerializerForVersion(fileFormatVersion);
        return serializer.read(dis, handler, df);
    }



    public void write(OWLOntology ontology, DataOutputStream dos) throws IOException {
        write(ontology, dos, new BinaryOWLMetadata());
    }

    public void write(OWLOntology ontology, DataOutputStream dos, BinaryOWLMetadata documentMetadata) throws IOException {
        BinaryOWLOntologyDocumentPreamble preamble = new BinaryOWLOntologyDocumentPreamble();
        preamble.write(dos);

        BinaryOWLDocumentBodySerializerSelector selector = new BinaryOWLDocumentBodySerializerSelector();
        BinaryOWLVersion fileFormatVersion = preamble.getFileFormatVersion();
        BinaryOWLDocumentBodySerializer serializer = selector.getSerializerForVersion(fileFormatVersion);
        serializer.write(ontology, dos, documentMetadata);
    }




    public void readOntologyChanges(BinaryOWLInputStream inputStream, BinaryOWLOntologyDocumentAppendedChangeHandler changeHandler) throws IOException, BinaryOWLParseException {
        byte chunkFollowsMarker = (byte) inputStream.read();
        while (chunkFollowsMarker != -1) {
            OntologyChangeDataList list = new OntologyChangeDataList(inputStream);
            changeHandler.handleChanges(list);
            chunkFollowsMarker = (byte) inputStream.read();
        }
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
