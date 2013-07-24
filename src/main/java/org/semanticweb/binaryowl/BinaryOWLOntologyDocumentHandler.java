package org.semanticweb.binaryowl;

import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/04/2013
 */
public interface BinaryOWLOntologyDocumentHandler<E extends Throwable> extends BinaryOWLOntologyDocumentAppendedChangeHandler {

    void handleBeginDocument() throws E;

    void handleEndDocument() throws E;

    void handleBeginInitialDocumentBlock() throws E;

    void handleEndInitialDocumentBlock() throws E;

    void handleBeginDocumentChangesBlock() throws E;

    void handleEndDocumentChangesBlock() throws E;


    void handlePreamble(BinaryOWLOntologyDocumentPreamble preamble) throws E;

    void handleDocumentMetaData(BinaryOWLMetadata metadata) throws E;


    void handleOntologyID(OWLOntologyID ontologyID) throws E;

    void handleImportsDeclarations(Set<OWLImportsDeclaration> importsDeclarations) throws E;

    void handleOntologyAnnotations(Set<OWLAnnotation> annotations) throws E;

    void handleAxioms(Set<OWLAxiom> axioms) throws E;




}
