package org.semanticweb.binaryowl;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public interface HasAxioms {

    <T extends OWLAxiom> Set<T> getAxioms(AxiomType<T> axiomType);
}
