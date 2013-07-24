package org.semanticweb.binaryowl;

import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public interface HasSignature {

    Set<OWLClass> getClassesInSignature();

    Set<OWLObjectProperty> getObjectPropertiesInSignature();

    Set<OWLDataProperty> getDataPropertiesInSignature();

    Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature();

    Set<OWLNamedIndividual> getIndividualsInSignature();

    Set<OWLDatatype> getDatatypesInSignature();
}
