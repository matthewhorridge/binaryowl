package org.semanticweb.binaryowl.versioning.v1;

import org.semanticweb.binaryowl.*;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.chunk.BinaryOWLMetadataChunk;
import org.semanticweb.binaryowl.lookup.IRILookupTable;
import org.semanticweb.binaryowl.lookup.LiteralLookupTable;
import org.semanticweb.binaryowl.lookup.LookupTable;
import org.semanticweb.binaryowl.owlobject.BinaryOWLImportsDeclarationSet;
import org.semanticweb.binaryowl.owlobject.BinaryOWLOntologyID;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.binaryowl.versioning.BinaryOWLDocumentBodySerializer;
import org.semanticweb.owlapi.model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
public class BinaryOWLV1DocumentBodySerializer implements BinaryOWLDocumentBodySerializer {

    /**
     * The version written by this serializer - always 1.
     */
    private static final BinaryOWLVersion VERSION = BinaryOWLVersion.getVersion(1);

    public <E extends Throwable> void read(DataInputStream dis, BinaryOWLOntologyDocumentHandler<E> handler, OWLDataFactory df) throws IOException, BinaryOWLParseException, UnloadableImportException, E {

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
    }

    private void readOntologyChanges(BinaryOWLInputStream inputStream, BinaryOWLOntologyDocumentAppendedChangeHandler changeHandler) throws IOException, BinaryOWLParseException {
        byte chunkFollowsMarker = (byte) inputStream.read();
        while (chunkFollowsMarker != -1) {
            OntologyChangeDataList list = new OntologyChangeDataList(inputStream);
            changeHandler.handleChanges(list);
            chunkFollowsMarker = (byte) inputStream.read();
        }
    }




    public void write(OWLOntologyDocument doc, DataOutputStream dos, BinaryOWLMetadata documentMetadata) throws IOException {

        BinaryOWLOutputStream nonLookupTableOutputStream = new BinaryOWLOutputStream(dos, VERSION);

        // Metadata
        BinaryOWLMetadataChunk metadataChunk = new BinaryOWLMetadataChunk(documentMetadata);
        metadataChunk.write(nonLookupTableOutputStream);

        // Ontology ID
        BinaryOWLOntologyID serializer = new BinaryOWLOntologyID(doc.getOntologyID());
        serializer.write(nonLookupTableOutputStream);

        // Imports
        BinaryOWLImportsDeclarationSet importsDeclarationSet = new BinaryOWLImportsDeclarationSet(doc.getImportsDeclarations());
        importsDeclarationSet.write(nonLookupTableOutputStream);

        // IRI Table
        IRILookupTable iriLookupTable = new IRILookupTable(doc);
        iriLookupTable.write(dos);

        // Literal Table
        LiteralLookupTable literalLookupTable = new LiteralLookupTable(doc, iriLookupTable);
        literalLookupTable.write(dos);

        LookupTable lookupTable = new LookupTable(iriLookupTable);

        BinaryOWLOutputStream lookupTableOutputStream = new BinaryOWLOutputStream(dos, lookupTable);

        // Ontology Annotations
        lookupTableOutputStream.writeOWLObjects(doc.getAnnotations());

        // Axiom tables - axioms by type
        for (AxiomType<?> axiomType : AxiomType.AXIOM_TYPES) {
            Set<? extends OWLAxiom> axioms = doc.getAxioms(axiomType);
            lookupTableOutputStream.writeOWLObjects(axioms);
        }

        dos.flush();
    }

}
