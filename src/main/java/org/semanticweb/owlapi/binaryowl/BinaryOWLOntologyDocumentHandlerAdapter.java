package org.semanticweb.owlapi.binaryowl;

import org.semanticweb.owlapi.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/04/2013
 */
public class BinaryOWLOntologyDocumentHandlerAdapter<E extends Throwable> implements BinaryOWLOntologyDocumentHandler<E> {

    @Override
    public void handleBeginDocument() throws E {
    }

    @Override
    public void handleEndDocument() throws E {
    }

    @Override
    public void handleBeginInitialDocumentBlock() throws E {
    }

    @Override
    public void handleEndInitialDocumentBlock() throws E {
    }

    @Override
    public void handleBeginDocumentChangesBlock() throws E {
    }

    @Override
    public void handleEndDocumentChangesBlock() throws E {
    }

    @Override
    public void handlePreamble(BinaryOWLOntologyDocumentPreamble preamble) throws E {
    }

    @Override
    public void handleDocumentMetaData(BinaryOWLMetadata metadata) throws E {
    }

    @Override
    public void handleOntologyID(OWLOntologyID ontologyID) throws E {
    }

    @Override
    public void handleImportsDeclarations(Set<OWLImportsDeclaration> importsDeclarations) throws E {
    }

    @Override
    public void handleOntologyAnnotations(Set<OWLAnnotation> annotations) throws E {
    }

    @Override
    public void handleAxioms(Set<OWLAxiom> axioms) throws E {
    }

    @Override
    public void handleChanges(OntologyChangeDataList changesList) {
    }
}
