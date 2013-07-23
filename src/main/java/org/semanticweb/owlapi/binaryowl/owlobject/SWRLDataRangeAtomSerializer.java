package org.semanticweb.owlapi.binaryowl.owlobject;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLDataRangeAtomSerializer extends OWLObjectSerializer<SWRLDataRangeAtom> {

    @Override
    protected void writeObject(SWRLDataRangeAtom object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getPredicate());
        outputStream.writeOWLObject(object.getArgument());
    }

    @Override
    protected SWRLDataRangeAtom readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        OWLDataRange predicate = inputStream.readOWLObject();
        SWRLDArgument argument = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLDataRangeAtom(predicate, argument);
    }
}
