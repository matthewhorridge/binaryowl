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

package org.semanticweb.binaryowl.owlobject;

import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
public class OWLFacetRestrictionSerializer extends OWLObjectSerializer<OWLFacetRestriction> {

    @Override
    protected void writeObject(OWLFacetRestriction object, BinaryOWLOutputStream outputStream) throws IOException {
        int facetMarker = getFacetMarker(object.getFacet());
        outputStream.writeInt(facetMarker);
        outputStream.writeOWLObject(object.getFacetValue());
    }

    @Override
    protected OWLFacetRestriction readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        int facetIndex = inputStream.readInt();
        OWLFacet facet = getFacet(facetIndex);
        OWLLiteral literal = inputStream.readOWLObject();
        return inputStream.getDataFactory().getOWLFacetRestriction(facet, literal);
    }


    /**
     * Gets the facet marker for a given facet.
     * @param facet The facet.  Not {@code null}.
     * @return The marker for the facet.
     */
    public static int getFacetMarker(OWLFacet facet) {
        switch (facet) {
            case LENGTH:
                return 0;
            case MIN_LENGTH:
                return 1;
            case MAX_LENGTH:
                return 2;
            case PATTERN:
                return 3;
            case MIN_INCLUSIVE:
                return 4;
            case MIN_EXCLUSIVE:
                return 5;
            case MAX_INCLUSIVE:
                return 6;
            case MAX_EXCLUSIVE:
                return 7;
            case TOTAL_DIGITS:
                return 8;
            case FRACTION_DIGITS:
                return 9;
            case LANG_RANGE:
                return 10;
            default:
                throw new RuntimeException("Illegal state.  Case not covered");
        }
    }

    /**
     * Gets the {@link OWLFacet} for the specified facet marker index.
     * @param facetMarker The facet marker index (>= 0, <= 10).
     * @return The corresponding {@link OWLFacet}.
     * @throws IndexOutOfBoundsException if the facet marker is out of the specified range.
     */
    public static OWLFacet getFacet(int facetMarker) {
        if(facetMarker < 0 || facetMarker > 10) {
            throw new IndexOutOfBoundsException("Facet marker out of range");
        }
        switch (facetMarker) {
            case 0:
                return OWLFacet.LENGTH;
            case 1:
                return OWLFacet.MIN_LENGTH;
            case 2:
                return OWLFacet.MAX_LENGTH;
            case 3:
                return OWLFacet.PATTERN;
            case 4:
                return OWLFacet.MIN_INCLUSIVE;
            case 5:
                return OWLFacet.MIN_EXCLUSIVE;
            case 6:
                return OWLFacet.MAX_INCLUSIVE;
            case 7:
                return OWLFacet.MAX_EXCLUSIVE;
            case 8:
                return OWLFacet.TOTAL_DIGITS;
            case 9:
                return OWLFacet.FRACTION_DIGITS;
            case 10:
                return OWLFacet.LANG_RANGE;
            default:
                throw new IllegalArgumentException("Facet marker out of range");
        }
    }
}
