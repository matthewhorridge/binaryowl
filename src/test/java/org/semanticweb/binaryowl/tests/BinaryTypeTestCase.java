package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.owlobject.OWLObjectBinaryType;

import static junit.framework.Assert.assertEquals;

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
        assertEquals(OWLObjectBinaryType.OWL_ANNOTATION.getMarker(), 73);
        assertEquals(OWLObjectBinaryType.OWL_LITERAL.getMarker(), 11);
        assertEquals(OWLObjectBinaryType.OWL_ONTOLOGY.getMarker(), 1);

        assertEquals(OWLObjectBinaryType.OWL_CLASS.getMarker(), 4);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_PROPERTY.getMarker(), 5);
        assertEquals(OWLObjectBinaryType.OWL_DATA_PROPERTY.getMarker(), 6);
        assertEquals(OWLObjectBinaryType.OWL_ANNOTATION_PROPERTY.getMarker(), 7);
        assertEquals(OWLObjectBinaryType.OWL_DATATYPE.getMarker(), 8);
        assertEquals(OWLObjectBinaryType.OWL_NAMED_INDIVIDUAL.getMarker(), 9);
        assertEquals(OWLObjectBinaryType.OWL_ANONYMOUS_INDIVIDUAL.getMarker(), 10);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_INVERSE_OF.getMarker(), 12);
        assertEquals(OWLObjectBinaryType.OWL_DATA_INTERSECTION_OF.getMarker(), 13);
        assertEquals(OWLObjectBinaryType.OWL_DATA_UNION_OF.getMarker(), 14);
        assertEquals(OWLObjectBinaryType.OWL_DATA_COMPLEMENT_OF.getMarker(), 15);
        assertEquals(OWLObjectBinaryType.OWL_DATA_ONE_OF.getMarker(), 16);
        assertEquals(OWLObjectBinaryType.OWL_DATATYPE_RESTRICTION.getMarker(), 17);
        assertEquals(OWLObjectBinaryType.OWL_FACET_RESTRICTION.getMarker(), 18);

        assertEquals(OWLObjectBinaryType.OWL_OBJECT_INTERSECTION_OF.getMarker(), 19);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_UNION_OF.getMarker(), 20);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_COMPLEMENT_OF.getMarker(), 21);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_ONE_OF.getMarker(), 22);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_SOME_VALUES_FROM.getMarker(), 23);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_ALL_VALUES_FROM.getMarker(), 24);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_HAS_VALUE.getMarker(), 25);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_HAS_SELF.getMarker(), 26);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_MIN_CARDINALITY.getMarker(), 27);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_MAX_CARDINALITY.getMarker(), 28);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_EXACT_CARDINALITY.getMarker(), 29);
        assertEquals(OWLObjectBinaryType.OWL_DATA_SOME_VALUES_FROM.getMarker(), 30);
        assertEquals(OWLObjectBinaryType.OWL_DATA_ALL_VALUES_FROM.getMarker(), 31);
        assertEquals(OWLObjectBinaryType.OWL_DATA_HAS_VALUE.getMarker(), 32);
        assertEquals(OWLObjectBinaryType.OWL_DATA_MIN_CARDINALITY.getMarker(), 33);
        assertEquals(OWLObjectBinaryType.OWL_DATA_MAX_CARDINALITY.getMarker(), 34);
        assertEquals(OWLObjectBinaryType.OWL_DATA_EXACT_CARDINALITY.getMarker(), 35);

        assertEquals(OWLObjectBinaryType.OWL_SUBCLASS_OF.getMarker(), 36);
        assertEquals(OWLObjectBinaryType.OWL_EQUIVALENT_CLASSES.getMarker(), 37);
        assertEquals(OWLObjectBinaryType.OWL_DISJOINT_CLASSES.getMarker(), 38);
        assertEquals(OWLObjectBinaryType.OWL_DISJOINT_UNION.getMarker(), 39);
        assertEquals(OWLObjectBinaryType.OWL_SUB_OBJECT_PROPERTY_OF.getMarker(), 40);
        assertEquals(OWLObjectBinaryType.OWL_SUB_OBJECT_PROPERTY_CHAIN_OF.getMarker(), 41);
        assertEquals(OWLObjectBinaryType.OWL_EQUIVALENT_OBJECT_PROPERTIES.getMarker(), 42);
        assertEquals(OWLObjectBinaryType.OWL_DISJOINT_OBJECT_PROPERTIES.getMarker(), 43);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_PROPERTY_DOMAIN.getMarker(), 44);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_PROPERTY_RANGE.getMarker(), 45);
        assertEquals(OWLObjectBinaryType.OWL_INVERSE_OBJECT_PROPERTIES.getMarker(), 46);
        assertEquals(OWLObjectBinaryType.OWL_FUNCTIONAL_OBJECT_PROPERTY.getMarker(), 47);
        assertEquals(OWLObjectBinaryType.OWL_INVERSE_FUNCTIONAL_OBJECT_PROPERTY.getMarker(), 48);
        assertEquals(OWLObjectBinaryType.OWL_REFLEXIVE_OBJECT_PROPERTY.getMarker(), 49);
        assertEquals(OWLObjectBinaryType.OWL_IRREFLEXIVE_OBJECT_PROPERTY.getMarker(), 50);
        assertEquals(OWLObjectBinaryType.OWL_SYMMETRIC_OBJECT_PROPERTY.getMarker(), 51);
        assertEquals(OWLObjectBinaryType.OWL_ASYMMETRIC_OBJECT_PROPERTY.getMarker(), 52);
        assertEquals(OWLObjectBinaryType.OWL_TRANSITIVE_OBJECT_PROPERTY.getMarker(), 53);
        assertEquals(OWLObjectBinaryType.OWL_SUB_DATA_PROPERTY_OF.getMarker(), 54);
        assertEquals(OWLObjectBinaryType.OWL_EQUIVALENT_DATA_PROPERTIES.getMarker(), 55);
        assertEquals(OWLObjectBinaryType.OWL_DISJOINT_DATA_PROPERTIES.getMarker(), 56);
        assertEquals(OWLObjectBinaryType.OWL_DATA_PROPERTY_DOMAIN.getMarker(), 57);
        assertEquals(OWLObjectBinaryType.OWL_DATA_PROPERTY_RANGE.getMarker(), 58);
        assertEquals(OWLObjectBinaryType.OWL_FUNCTIONAL_DATA_PROPERTY.getMarker(), 59);
        assertEquals(OWLObjectBinaryType.OWL_DATATYPE_DEFINITION.getMarker(), 60);
        assertEquals(OWLObjectBinaryType.OWL_HAS_KEY.getMarker(), 61);
        assertEquals(OWLObjectBinaryType.OWL_SAME_INDIVIDUAL.getMarker(), 62);
        assertEquals(OWLObjectBinaryType.OWL_DIFFERENT_INDIVIDUALS.getMarker(), 63);
        assertEquals(OWLObjectBinaryType.OWL_CLASS_ASSERTION.getMarker(), 64);
        assertEquals(OWLObjectBinaryType.OWL_OBJECT_PROPERTY_ASSERTION.getMarker(), 65);
        assertEquals(OWLObjectBinaryType.OWL_NEGATIVE_OBJECT_PROPERTY_ASSERTION.getMarker(), 66);
        assertEquals(OWLObjectBinaryType.OWL_DATA_PROPERTY_ASSERTION.getMarker(), 67);
        assertEquals(OWLObjectBinaryType.OWL_NEGATIVE_DATA_PROPERTY_ASSERTION.getMarker(), 68);
        assertEquals(OWLObjectBinaryType.OWL_ANNOTATION_ASSERTION.getMarker(), 69);
        assertEquals(OWLObjectBinaryType.OWL_SUB_ANNOTATION_PROPERTY_OF.getMarker(), 70);
        assertEquals(OWLObjectBinaryType.OWL_ANNOTATION_PROPERTY_DOMAIN.getMarker(), 71);
        assertEquals(OWLObjectBinaryType.OWL_ANNOTATION_PROPERTY_RANGE.getMarker(), 72);

        assertEquals(OWLObjectBinaryType.SWRL_RULE.getMarker(), 74);
        assertEquals(OWLObjectBinaryType.SWRL_DIFFERENT_INDIVIDUALS_ATOM.getMarker(), 75);
        assertEquals(OWLObjectBinaryType.SWRL_SAME_INDIVIDUAL_ATOM.getMarker(), 76);
        assertEquals(OWLObjectBinaryType.SWRL_CLASS_ATOM.getMarker(), 77);
        assertEquals(OWLObjectBinaryType.SWRL_DATA_RANGE_ATOM.getMarker(), 78);
        assertEquals(OWLObjectBinaryType.SWRL_OBJECT_PROPERTY_ATOM.getMarker(), 79);
        assertEquals(OWLObjectBinaryType.SWRL_DATA_PROPERTY_ATOM.getMarker(), 80);
        assertEquals(OWLObjectBinaryType.SWRL_BUILT_IN_ATOM.getMarker(), 81);
        assertEquals(OWLObjectBinaryType.SWRL_VARIABLE.getMarker(), 82);
        assertEquals(OWLObjectBinaryType.SWRL_INDIVIDUAL_ARGUMENT.getMarker(), 83);
        assertEquals(OWLObjectBinaryType.SWRL_LITERAL_ARGUMENT.getMarker(), 84);




    }
}
