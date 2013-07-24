package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.owlobject.serializer.OWLFacetRestrictionSerializer;
import org.semanticweb.owlapi.vocab.OWLFacet;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class FacetRestrictionTypeMarkerTestCase {

    @Test
    public void ensureFacetRestrictionTypeMarkersMatchSpec() {
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.LENGTH), 0);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MIN_LENGTH), 1);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MAX_LENGTH), 2);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.PATTERN), 3);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MIN_INCLUSIVE), 4);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MIN_EXCLUSIVE), 5);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MAX_INCLUSIVE), 6);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MAX_EXCLUSIVE), 7);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.TOTAL_DIGITS), 8);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.FRACTION_DIGITS), 9);
        assertEquals(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.LANG_RANGE), 10);

        assertEquals(OWLFacetRestrictionSerializer.getFacet(0), OWLFacet.LENGTH);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(1), OWLFacet.MIN_LENGTH);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(2), OWLFacet.MAX_LENGTH);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(3), OWLFacet.PATTERN);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(4), OWLFacet.MIN_INCLUSIVE);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(5), OWLFacet.MIN_EXCLUSIVE);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(6), OWLFacet.MAX_INCLUSIVE);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(7), OWLFacet.MAX_EXCLUSIVE);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(8), OWLFacet.TOTAL_DIGITS);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(9), OWLFacet.FRACTION_DIGITS);
        assertEquals(OWLFacetRestrictionSerializer.getFacet(10), OWLFacet.LANG_RANGE);

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void ensureThrowsIndexOutOfBoundsWhenLessThan0() {
        OWLFacetRestrictionSerializer.getFacet(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void ensureThrowsIndexOfOutBoundsWhenGreaterThan10() {
        OWLFacetRestrictionSerializer.getFacet(11);
    }

    @Test(expected = NullPointerException.class)
    public void ensureNullPointerExceptionWhenFacetIsNull() {
        OWLFacetRestrictionSerializer.getFacetMarker(null);
    }
}
