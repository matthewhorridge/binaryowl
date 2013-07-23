package org.semanticweb.owlapi.binaryowl.owlobject;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;

import java.io.IOException;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class SWRLBuiltInAtomSerializer extends OWLObjectSerializer<SWRLBuiltInAtom> {

    @Override
    protected void writeObject(SWRLBuiltInAtom object, BinaryOWLOutputStream outputStream) throws IOException {
        outputStream.writeIRI(object.getPredicate());
        outputStream.writeOWLObjectList(object.getArguments());
    }

    @Override
    protected SWRLBuiltInAtom readObject(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        IRI predicate = inputStream.readIRI();
        List<SWRLDArgument> arguments = inputStream.readOWLObjectList();
        return inputStream.getDataFactory().getSWRLBuiltInAtom(predicate, arguments);
    }
}
