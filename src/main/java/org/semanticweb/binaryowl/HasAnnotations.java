package org.semanticweb.binaryowl;

import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public interface HasAnnotations {

    Set<OWLAnnotation> getAnnotations();
}
