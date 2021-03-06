package org.semanticweb.binaryowl.owlobject.serializer;

import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLIndividualArgumentSerializer extends OWLObjectSerializer<SWRLIndividualArgument> {

    @Override
    protected void writeObject(SWRLIndividualArgument object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getIndividual());
    }

    @Override
    protected SWRLIndividualArgument readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        OWLIndividual individual = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLIndividualArgument(individual);
    }
}
