package org.semanticweb.binaryowl.owlobject;

import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLSameIndividualAtomSerializer extends OWLObjectSerializer<SWRLSameIndividualAtom> {

    @Override
    protected void writeObject(SWRLSameIndividualAtom object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getFirstArgument());
        outputStream.writeOWLObject(object.getSecondArgument());
    }

    @Override
    protected SWRLSameIndividualAtom readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        SWRLIArgument first = inputStream.readOWLObject();
        SWRLIArgument second = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLSameIndividualAtom(first, second);
    }
}
