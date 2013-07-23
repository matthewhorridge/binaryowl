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

package org.semanticweb.owlapi.binaryowl.change;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.change.serializer.*;
import org.semanticweb.owlapi.binaryowl.lookup.LookupTable;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.change.*;

import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/04/2012
 */
public enum OntologyChangeDataType {

    SET_ONTOLOGY_ID(1, SetOntologyIDData.class, new SetOntologyIDDataSerializer()),

    ADD_IMPORT(2, AddImportData.class, new AddImportDataSerializer()),

    REMOVE_IMPORT(3, RemoveImportData.class, new RemoveImportDataSerializer()),

    ADD_ONTOLOGY_ANNOTATION(4, AddOntologyAnnotationData.class, new AddOntologyAnnotationDataSerializer()),

    REMOVE_ONTOLOGY_ANNOTATION(5, RemoveOntologyAnnotationData.class, new RemoveOntologyAnnotationDataSerializer()),

    ADD_AXIOM(6, AddAxiomData.class, new AddAxiomDataSerializer()),

    REMOVE_AXIOM(7, RemoveAxiomData.class, new RemoveAxiomDataSerializer());

    
    
    private byte marker;
    
    private Class<? extends OWLOntologyChangeData> ontologyChangeClass;

    private OntologyChangeDataSerializer<?> serializer;
    
    private <C extends OWLOntologyChangeData> OntologyChangeDataType(int marker, Class<C> ontologyChangeClass, OntologyChangeDataSerializer<C> serializer) {
        this.marker = (byte) marker;
        this.ontologyChangeClass = ontologyChangeClass;
        this.serializer = serializer;
    }
    
    public static OntologyChangeDataType get(byte marker) {
        return values()[marker - 1];
    }

    public static OntologyChangeDataType get(OWLOntologyChangeData Data) {
        OntologyChangeData2OntologyChangeInfoTypeTranslator translator = new OntologyChangeData2OntologyChangeInfoTypeTranslator();
        return Data.accept(translator);
    }

    public byte getMarker() {
        return marker;
    }

    public Class<? extends OWLOntologyChangeData> getOntologyChangeClass() {
        return ontologyChangeClass;
    }

    public static void write(OWLOntologyChangeData info, BinaryOWLOutputStream outputStream) throws IOException {
        OntologyChangeDataType type = get(info);
        outputStream.writeByte(type.getMarker());
        @SuppressWarnings("unchecked")
        OntologyChangeDataSerializer<OWLOntologyChangeData> serializer = (OntologyChangeDataSerializer<OWLOntologyChangeData>) type.serializer;
        serializer.write(info, outputStream);
    }
    
    public static OWLOntologyChangeData read(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        byte marker =  inputStream.readByte();
        OntologyChangeDataType type = get(marker);
        @SuppressWarnings("unchecked")
        OntologyChangeDataSerializer<OWLOntologyChangeData> serializer = (OntologyChangeDataSerializer<OWLOntologyChangeData>) type.serializer;
        return serializer.read(inputStream);

    }
    
}
