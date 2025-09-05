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
 * aint with this program.  If not, see http://www.gnu.org/licenses/.
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


import org.semanticweb.binaryowl.chunk.ChunkUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/04/2012
 */
public class BinaryOWLOntologyDocumentPreamble {
    
    public static final int MAGIC_NUMBER = ChunkUtil.toInt("BO2O");

    public static final BinaryOWLVersion DEFAULT_VERSION = BinaryOWLVersion.getVersion(3);

    private BinaryOWLVersion fileFormatVersion;
    
    
    public BinaryOWLOntologyDocumentPreamble() {
        this(DEFAULT_VERSION);
    }

    public BinaryOWLOntologyDocumentPreamble(DataInputStream dataInputStream) throws IOException, BinaryOWLParseException {
        read(dataInputStream);
    }

    private BinaryOWLOntologyDocumentPreamble(BinaryOWLVersion version) {
        this.fileFormatVersion = version;
    }

    private void read(DataInputStream dataInputStream) throws IOException, BinaryOWLParseException {
        int magicNumber = dataInputStream.readInt();
        if(magicNumber != MAGIC_NUMBER) {
            throw new BinaryOWLParseException("Corrupt file or not a binary OWL format (Magic Number Not Present)");
        }
        short version = dataInputStream.readShort();
        fileFormatVersion = BinaryOWLVersion.getVersion(version);
    }


    /**
     * Gets the version number that describes the format of the file.
     * @return The version number
     */
    public BinaryOWLVersion getFileFormatVersion() {
        return fileFormatVersion;
    }

    /**
     * Writes this preamble out to the specified output stream.
     * @param dos The {@link DataOutputStream}.
     * @throws IOException If there was a problem writing to the specified stream.
     */
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(MAGIC_NUMBER);
        dos.writeShort(fileFormatVersion.getVersion());
    }
    
    
}
