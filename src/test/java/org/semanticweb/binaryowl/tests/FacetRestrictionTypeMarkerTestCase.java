package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.owlobject.serializer.OWLFacetRestrictionSerializer;
import org.semanticweb.owlapi.vocab.OWLFacet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class FacetRestrictionTypeMarkerTestCase {

    @Test
    public void ensureFacetRestrictionTypeMarkersMatchSpec() {
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.LENGTH), is(0));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MIN_LENGTH), is(1));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MAX_LENGTH), is(2));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.PATTERN), is(3));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MIN_INCLUSIVE), is(4));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MIN_EXCLUSIVE), is(5));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MAX_INCLUSIVE), is(6));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.MAX_EXCLUSIVE), is(7));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.TOTAL_DIGITS), is(8));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.FRACTION_DIGITS), is(9));
        assertThat(OWLFacetRestrictionSerializer.getFacetMarker(OWLFacet.LANG_RANGE), is(10));

        assertThat(OWLFacetRestrictionSerializer.getFacet(0), is(OWLFacet.LENGTH));
        assertThat(OWLFacetRestrictionSerializer.getFacet(1), is(OWLFacet.MIN_LENGTH));
        assertThat(OWLFacetRestrictionSerializer.getFacet(2), is(OWLFacet.MAX_LENGTH));
        assertThat(OWLFacetRestrictionSerializer.getFacet(3), is(OWLFacet.PATTERN));
        assertThat(OWLFacetRestrictionSerializer.getFacet(4), is(OWLFacet.MIN_INCLUSIVE));
        assertThat(OWLFacetRestrictionSerializer.getFacet(5), is(OWLFacet.MIN_EXCLUSIVE));
        assertThat(OWLFacetRestrictionSerializer.getFacet(6), is(OWLFacet.MAX_INCLUSIVE));
        assertThat(OWLFacetRestrictionSerializer.getFacet(7), is(OWLFacet.MAX_EXCLUSIVE));
        assertThat(OWLFacetRestrictionSerializer.getFacet(8), is(OWLFacet.TOTAL_DIGITS));
        assertThat(OWLFacetRestrictionSerializer.getFacet(9), is(OWLFacet.FRACTION_DIGITS));
        assertThat(OWLFacetRestrictionSerializer.getFacet(10), is(OWLFacet.LANG_RANGE));

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
