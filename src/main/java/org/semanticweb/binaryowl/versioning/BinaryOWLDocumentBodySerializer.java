package org.semanticweb.binaryowl.versioning;

import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentHandler;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.UnloadableImportException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 * <p>
 *     An interface to objects that can read and write the document "part" of binary OWL documents which is everything
 *     after the document preamble.
 * </p>
 */
public interface BinaryOWLDocumentBodySerializer {

    <T extends Throwable> void read(DataInputStream dis, BinaryOWLOntologyDocumentHandler<T> handler, OWLDataFactory dataFactory) throws IOException, BinaryOWLParseException, UnloadableImportException, T;

    void write(OWLOntology ontology, DataOutputStream dos, BinaryOWLMetadata documentMetadata) throws IOException;

}
