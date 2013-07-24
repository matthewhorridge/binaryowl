package org.semanticweb.binaryowl.owlobject.serializer;

import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLVariable;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLVariableSerializer extends OWLObjectSerializer<SWRLVariable> {

    @Override
    protected void writeObject(SWRLVariable object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeIRI(object.getIRI());
    }

    @Override
    protected SWRLVariable readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        IRI iri = inputStream.readIRI();
        return inputStream.getDataFactory().getSWRLVariable(iri);
    }
}
