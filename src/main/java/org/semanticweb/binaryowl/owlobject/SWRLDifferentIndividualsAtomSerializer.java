package org.semanticweb.binaryowl.owlobject;

import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLDifferentIndividualsAtomSerializer extends OWLObjectSerializer<SWRLDifferentIndividualsAtom> {

    @Override
    protected void writeObject(SWRLDifferentIndividualsAtom object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getFirstArgument());
        outputStream.writeOWLObject(object.getSecondArgument());
    }

    @Override
    protected SWRLDifferentIndividualsAtom readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        SWRLIArgument first = inputStream.readOWLObject();
        SWRLIArgument second = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLDifferentIndividualsAtom(first, second);
    }
}
