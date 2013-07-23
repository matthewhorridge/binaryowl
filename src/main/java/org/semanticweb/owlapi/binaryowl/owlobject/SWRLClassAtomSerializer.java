package org.semanticweb.owlapi.binaryowl.owlobject;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLClassAtomSerializer extends OWLObjectSerializer<SWRLClassAtom> {

    @Override
    protected void writeObject(SWRLClassAtom object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getPredicate());
        outputStream.writeOWLObject(object.getArgument());
    }

    @Override
    protected SWRLClassAtom readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        OWLClassExpression predicate = inputStream.readOWLObject();
        SWRLIArgument arg = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLClassAtom(predicate, arg);
    }
}
