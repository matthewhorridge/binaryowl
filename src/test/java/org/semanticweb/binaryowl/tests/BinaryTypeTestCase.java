package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.owlobject.OWLObjectBinaryType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.semanticweb.binaryowl.owlobject.OWLObjectBinaryType.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class BinaryTypeTestCase {

    /**
     * Tests that they types markers for different types of object are as per spec.
     */
    @Test
    public void testExpectedBinaryType() {
        assertThat(OWL_ANNOTATION.getMarker(), is((byte)73));
        assertThat(OWL_LITERAL.getMarker(), is((byte)11));
        assertThat(OWL_ONTOLOGY.getMarker(), is((byte)1));

        assertThat(OWL_CLASS.getMarker(), is((byte)4));
        assertThat(OWL_OBJECT_PROPERTY.getMarker(), is((byte)5));
        assertThat(OWL_DATA_PROPERTY.getMarker(), is((byte)6));
        assertThat(OWL_ANNOTATION_PROPERTY.getMarker(), is((byte)7));
        assertThat(OWL_DATATYPE.getMarker(), is((byte)8));
        assertThat(OWL_NAMED_INDIVIDUAL.getMarker(), is((byte)9));
        assertThat(OWL_ANONYMOUS_INDIVIDUAL.getMarker(), is((byte)10));
        assertThat(OWL_OBJECT_INVERSE_OF.getMarker(), is((byte)12));
        assertThat(OWL_DATA_INTERSECTION_OF.getMarker(), is((byte)13));
        assertThat(OWL_DATA_UNION_OF.getMarker(), is((byte)14));
        assertThat(OWL_DATA_COMPLEMENT_OF.getMarker(), is((byte)15));
        assertThat(OWL_DATA_ONE_OF.getMarker(), is((byte)16));
        assertThat(OWL_DATATYPE_RESTRICTION.getMarker(), is((byte)17));
        assertThat(OWL_FACET_RESTRICTION.getMarker(), is((byte)18));

        assertThat(OWL_OBJECT_INTERSECTION_OF.getMarker(), is((byte)19));
        assertThat(OWL_OBJECT_UNION_OF.getMarker(), is((byte)20));
        assertThat(OWL_OBJECT_COMPLEMENT_OF.getMarker(), is((byte)21));
        assertThat(OWL_OBJECT_ONE_OF.getMarker(), is((byte)22));
        assertThat(OWL_OBJECT_SOME_VALUES_FROM.getMarker(), is((byte)23));
        assertThat(OWL_OBJECT_ALL_VALUES_FROM.getMarker(), is((byte)24));
        assertThat(OWL_OBJECT_HAS_VALUE.getMarker(), is((byte)25));
        assertThat(OWL_OBJECT_HAS_SELF.getMarker(), is((byte)26));
        assertThat(OWL_OBJECT_MIN_CARDINALITY.getMarker(), is((byte)27));
        assertThat(OWL_OBJECT_MAX_CARDINALITY.getMarker(), is((byte)28));
        assertThat(OWL_OBJECT_EXACT_CARDINALITY.getMarker(), is((byte)29));
        assertThat(OWL_DATA_SOME_VALUES_FROM.getMarker(), is((byte)30));
        assertThat(OWL_DATA_ALL_VALUES_FROM.getMarker(), is((byte)31));
        assertThat(OWL_DATA_HAS_VALUE.getMarker(), is((byte)32));
        assertThat(OWL_DATA_MIN_CARDINALITY.getMarker(), is((byte)33));
        assertThat(OWL_DATA_MAX_CARDINALITY.getMarker(), is((byte)34));
        assertThat(OWL_DATA_EXACT_CARDINALITY.getMarker(), is((byte)35));

        assertThat(OWL_SUBCLASS_OF.getMarker(), is((byte)36));
        assertThat(OWL_EQUIVALENT_CLASSES.getMarker(), is((byte)37));
        assertThat(OWL_DISJOINT_CLASSES.getMarker(), is((byte)38));
        assertThat(OWL_DISJOINT_UNION.getMarker(), is((byte)39));
        assertThat(OWL_SUB_OBJECT_PROPERTY_OF.getMarker(), is((byte)40));
        assertThat(OWL_SUB_OBJECT_PROPERTY_CHAIN_OF.getMarker(), is((byte)41));
        assertThat(OWL_EQUIVALENT_OBJECT_PROPERTIES.getMarker(), is((byte)42));
        assertThat(OWL_DISJOINT_OBJECT_PROPERTIES.getMarker(), is((byte)43));
        assertThat(OWL_OBJECT_PROPERTY_DOMAIN.getMarker(), is((byte)44));
        assertThat(OWL_OBJECT_PROPERTY_RANGE.getMarker(), is((byte)45));
        assertThat(OWL_INVERSE_OBJECT_PROPERTIES.getMarker(), is((byte)46));
        assertThat(OWL_FUNCTIONAL_OBJECT_PROPERTY.getMarker(), is((byte)47));
        assertThat(OWL_INVERSE_FUNCTIONAL_OBJECT_PROPERTY.getMarker(), is((byte)48));
        assertThat(OWL_REFLEXIVE_OBJECT_PROPERTY.getMarker(), is((byte)49));
        assertThat(OWL_IRREFLEXIVE_OBJECT_PROPERTY.getMarker(), is((byte)50));
        assertThat(OWL_SYMMETRIC_OBJECT_PROPERTY.getMarker(), is((byte)51));
        assertThat(OWL_ASYMMETRIC_OBJECT_PROPERTY.getMarker(), is((byte)52));
        assertThat(OWL_TRANSITIVE_OBJECT_PROPERTY.getMarker(), is((byte)53));
        assertThat(OWL_SUB_DATA_PROPERTY_OF.getMarker(), is((byte)54));
        assertThat(OWL_EQUIVALENT_DATA_PROPERTIES.getMarker(), is((byte)55));
        assertThat(OWL_DISJOINT_DATA_PROPERTIES.getMarker(), is((byte)56));
        assertThat(OWL_DATA_PROPERTY_DOMAIN.getMarker(), is((byte)57));
        assertThat(OWL_DATA_PROPERTY_RANGE.getMarker(), is((byte)58));
        assertThat(OWL_FUNCTIONAL_DATA_PROPERTY.getMarker(), is((byte)59));
        assertThat(OWL_DATATYPE_DEFINITION.getMarker(), is((byte)60));
        assertThat(OWL_HAS_KEY.getMarker(), is((byte)61));
        assertThat(OWL_SAME_INDIVIDUAL.getMarker(), is((byte)62));
        assertThat(OWL_DIFFERENT_INDIVIDUALS.getMarker(), is((byte)63));
        assertThat(OWL_CLASS_ASSERTION.getMarker(), is((byte)64));
        assertThat(OWL_OBJECT_PROPERTY_ASSERTION.getMarker(), is((byte)65));
        assertThat(OWL_NEGATIVE_OBJECT_PROPERTY_ASSERTION.getMarker(), is((byte)66));
        assertThat(OWL_DATA_PROPERTY_ASSERTION.getMarker(), is((byte)67));
        assertThat(OWL_NEGATIVE_DATA_PROPERTY_ASSERTION.getMarker(), is((byte)68));
        assertThat(OWL_ANNOTATION_ASSERTION.getMarker(), is((byte)69));
        assertThat(OWL_SUB_ANNOTATION_PROPERTY_OF.getMarker(), is((byte)70));
        assertThat(OWL_ANNOTATION_PROPERTY_DOMAIN.getMarker(), is((byte)71));
        assertThat(OWL_ANNOTATION_PROPERTY_RANGE.getMarker(), is((byte)72));

        assertThat(SWRL_RULE.getMarker(), is((byte)74));
        assertThat(SWRL_DIFFERENT_INDIVIDUALS_ATOM.getMarker(), is((byte)75));
        assertThat(SWRL_SAME_INDIVIDUAL_ATOM.getMarker(), is((byte)76));
        assertThat(SWRL_CLASS_ATOM.getMarker(), is((byte)77));
        assertThat(SWRL_DATA_RANGE_ATOM.getMarker(), is((byte)78));
        assertThat(SWRL_OBJECT_PROPERTY_ATOM.getMarker(), is((byte)79));
        assertThat(SWRL_DATA_PROPERTY_ATOM.getMarker(), is((byte)80));
        assertThat(SWRL_BUILT_IN_ATOM.getMarker(), is((byte)81));
        assertThat(SWRL_VARIABLE.getMarker(), is((byte)82));
        assertThat(SWRL_INDIVIDUAL_ARGUMENT.getMarker(), is((byte)83));
        assertThat(SWRL_LITERAL_ARGUMENT.getMarker(), is((byte)84));

    }
}
