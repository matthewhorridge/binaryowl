package org.semanticweb.owlapi.binaryowl.owlobject;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLObjectPropertyAtomSerializer extends OWLObjectSerializer<SWRLObjectPropertyAtom> {

    @Override
    protected void writeObject(SWRLObjectPropertyAtom object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getPredicate());
        outputStream.writeOWLObject(object.getFirstArgument());
        outputStream.writeOWLObject(object.getSecondArgument());
    }

    @Override
    protected SWRLObjectPropertyAtom readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        OWLObjectPropertyExpression predicate = inputStream.readOWLObject();
        SWRLIArgument firstArg = inputStream.readOWLObject();
        SWRLIArgument secondArg = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLObjectPropertyAtom(predicate, firstArg, secondArg);
    }
}
