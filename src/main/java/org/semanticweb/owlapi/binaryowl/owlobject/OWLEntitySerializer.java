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
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
public class OWLEntitySerializer<E extends OWLEntity> extends OWLObjectSerializer<E> {

    private EntityType<E> entityType;

    public OWLEntitySerializer(EntityType<E> entityType) {
        this.entityType = entityType;
    }

    @Override
    protected void writeObject(E object, BinaryOWLOutputStream outputStream) throws IOException {
        IRI iri = object.getIRI();
        outputStream.writeIRI(iri);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected E readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        if(entityType == EntityType.CLASS) {
            return (E) inputStream.readClassIRI();
        }
        else if(entityType == EntityType.OBJECT_PROPERTY) {
            return (E) inputStream.readObjectPropertyIRI();
        }
        else if(entityType == EntityType.ANNOTATION_PROPERTY) {
            return (E) inputStream.readAnnotationPropertyIRI();
        }
        else if(entityType == EntityType.DATA_PROPERTY) {
            return (E) inputStream.readDataPropertyIRI();
        }
        else if(entityType == EntityType.DATATYPE) {
            return (E) inputStream.readDatatypeIRI();
        }
        else if(entityType == EntityType.NAMED_INDIVIDUAL) {
            return (E) inputStream.readIndividualIRI();
        }
        IRI iri = inputStream.readIRI();
        return inputStream.getDataFactory().getOWLEntity(entityType, iri);
    }
}
