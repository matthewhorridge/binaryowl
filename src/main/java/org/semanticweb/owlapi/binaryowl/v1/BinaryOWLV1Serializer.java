package org.semanticweb.owlapi.binaryowl.v1;

import org.semanticweb.owlapi.binaryowl.*;
import org.semanticweb.owlapi.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.owlapi.binaryowl.chunk.BinaryOWLMetadataChunk;
import org.semanticweb.owlapi.binaryowl.lookup.IRILookupTable;
import org.semanticweb.owlapi.binaryowl.lookup.LookupTable;
import org.semanticweb.owlapi.binaryowl.owlobject.BinaryOWLImportsDeclarationSet;
import org.semanticweb.owlapi.binaryowl.owlobject.BinaryOWLOntologyID;
import org.semanticweb.owlapi.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.owlapi.model.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 * <p>
 *     A serializer for legacy v1 format
 * </p>
 */
public class BinaryOWLV1Serializer {

    private static final BinaryOWLVersion VERSION = BinaryOWLVersion.getVersion(1);

    public <E extends Throwable> BinaryOWLOntologyDocumentFormat read(DataInputStream dis, BinaryOWLOntologyDocumentHandler<E> handler, OWLDataFactory df) throws IOException, BinaryOWLParseException, UnloadableImportException, E {

        BinaryOWLInputStream inputStream = new BinaryOWLInputStream(dis, df, VERSION);

        // Metadata
        BinaryOWLMetadataChunk chunk = new BinaryOWLMetadataChunk(inputStream);
        BinaryOWLMetadata metadata = chunk.getMetadata();
        handler.handleDocumentMetaData(metadata);

        handler.handleBeginInitialDocumentBlock();

        // Ontology ID
        BinaryOWLOntologyID serializer = new BinaryOWLOntologyID(inputStream);
        OWLOntologyID ontologyID = serializer.getOntologyID();
        handler.handleOntologyID(ontologyID);

        // Imported ontologies
        BinaryOWLImportsDeclarationSet importsDeclarationSet = new BinaryOWLImportsDeclarationSet(inputStream);
        Set<OWLImportsDeclaration> importsDeclarations = importsDeclarationSet.getImportsDeclarations();
        handler.handleImportsDeclarations(importsDeclarations);

        // IRI Table
        IRILookupTable iriLookupTable = new IRILookupTable(dis);

        // Used to be literal table
        // Skip 1 byte - the interning marker
        inputStream.skip(1);


        LookupTable lookupTable = new LookupTable(iriLookupTable);


        BinaryOWLInputStream lookupTableStream = new BinaryOWLInputStream(dis, lookupTable, df, VERSION);

        // Ontology Annotations
        Set<OWLAnnotation> annotations = lookupTableStream.readOWLObjects();
        handler.handleOntologyAnnotations(annotations);

        // Axiom tables - axioms by type
        for (int i = 0; i < AxiomType.AXIOM_TYPES.size(); i++) {
            Set<OWLAxiom> axiomsOfType = lookupTableStream.readOWLObjects();
            handler.handleAxioms(axiomsOfType);
        }

        handler.handleEndInitialDocumentBlock();
        handler.handleBeginDocumentChangesBlock();
        BinaryOWLInputStream changesInputStream = new BinaryOWLInputStream(dis, df, VERSION);
        // Read any changes that have been appended to the end of the file - no look up table for this
        readOntologyChanges(changesInputStream, handler);
        handler.handleEndDocumentChangesBlock();
        handler.handleEndDocument();
        return new BinaryOWLOntologyDocumentFormat(metadata);
    }

    public void readOntologyChanges(BinaryOWLInputStream inputStream, BinaryOWLOntologyDocumentAppendedChangeHandler changeHandler) throws IOException, BinaryOWLParseException {
        byte chunkFollowsMarker = (byte) inputStream.read();
        while (chunkFollowsMarker != -1) {
            OntologyChangeDataList list = new OntologyChangeDataList(inputStream);
            changeHandler.handleChanges(list);
            chunkFollowsMarker = (byte) inputStream.read();
        }
    }

}
