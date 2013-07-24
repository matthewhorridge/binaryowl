package org.semanticweb.binaryowl.owlobject.serializer;

import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLDataPropertyAtomSerializer extends OWLObjectSerializer<SWRLDataPropertyAtom> {

    @Override
    protected void writeObject(SWRLDataPropertyAtom object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeOWLObject(object.getPredicate());
        outputStream.writeOWLObject(object.getFirstArgument());
        outputStream.writeOWLObject(object.getSecondArgument());
    }

    @Override
    protected SWRLDataPropertyAtom readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        OWLDataPropertyExpression predicate = inputStream.readOWLObject();
        SWRLIArgument firstArg = inputStream.readOWLObject();
        SWRLDArgument secondArg = inputStream.readOWLObject();
        return inputStream.getDataFactory().getSWRLDataPropertyAtom(predicate, firstArg, secondArg);
    }
}
