package org.semanticweb.binaryowl.owlapi;

import org.semanticweb.binaryowl.OWLOntologyDocument;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class OWLOntologyWrapper implements OWLOntologyDocument {

    private OWLOntology delegate;

    public OWLOntologyWrapper(OWLOntology delegate) {
        this.delegate = delegate;
    }

    @Override
    public OWLOntologyID getOntologyID() {
        return delegate.getOntologyID();
    }

    @Override
    public Set<OWLAnnotation> getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public Set<OWLImportsDeclaration> getImportsDeclarations() {
        return delegate.getImportsDeclarations();
    }

    @Override
    public <T extends OWLAxiom> Set<T> getAxioms(AxiomType<T> axiomType) {
        return delegate.getAxioms(axiomType);
    }

    @Override
    public Set<OWLClass> getClassesInSignature() {
        return delegate.getClassesInSignature();
    }

    @Override
    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        return delegate.getObjectPropertiesInSignature();
    }

    @Override
    public Set<OWLDataProperty> getDataPropertiesInSignature() {
        return delegate.getDataPropertiesInSignature();
    }

    @Override
    public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
        return delegate.getAnnotationPropertiesInSignature();
    }

    @Override
    public Set<OWLNamedIndividual> getIndividualsInSignature() {
        return delegate.getIndividualsInSignature();
    }

    @Override
    public Set<OWLDatatype> getDatatypesInSignature() {
        return delegate.getDatatypesInSignature();
    }
}
