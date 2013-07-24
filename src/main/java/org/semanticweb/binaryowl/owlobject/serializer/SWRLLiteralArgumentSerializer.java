package org.semanticweb.binaryowl.owlobject.serializer;

import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLLiteralArgumentSerializer extends OWLObjectSerializer<SWRLLiteralArgument> {

    @Override
    protected void writeObject(SWRLLiteralArgument object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getLiteral());
    }

    @Override
    protected SWRLLiteralArgument readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        OWLLiteral literal = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLLiteralArgument(literal);
    }
}
