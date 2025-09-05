package org.semanticweb.binaryowl.serializer.v2;

import org.semanticweb.binaryowl.*;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.chunk.BinaryOWLMetadataChunk;
import org.semanticweb.binaryowl.doc.OWLOntologyDocument;
import org.semanticweb.binaryowl.lookup.IRILookupTable;
import org.semanticweb.binaryowl.lookup.LiteralLookupTable;
import org.semanticweb.binaryowl.lookup.LookupTable;
import org.semanticweb.binaryowl.owlobject.serializer.BinaryOWLImportsDeclarationSet;
import org.semanticweb.binaryowl.owlobject.serializer.BinaryOWLOntologyID;
import org.semanticweb.binaryowl.serializer.BinaryOWLDocumentBodySerializer;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class BinaryOWLV2DocumentBodySerializer implements BinaryOWLDocumentBodySerializer {

    /**
     * The version written by this serializer - always 2.
     */
    private static final BinaryOWLVersion VERSION = BinaryOWLVersion.getVersion(2);

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
            try {
                Set<OWLAxiom> axiomsOfType = lookupTableStream.readOWLObjects();
                handler.handleAxioms(axiomsOfType);
            } catch (Throwable e) {
                System.err.println("Error reading axioms of type " + AxiomType.AXIOM_TYPES.toArray()[i]);
                throw new RuntimeException(e);
            }
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

        BinaryOWLOutputStream lookupTableOutputStream = new BinaryOWLOutputStream(dos, lookupTable, VERSION);

        // Ontology Annotations
        lookupTableOutputStream.writeOWLObjects(doc.getAnnotations());

        // Axiom tables - axioms by type
        for (AxiomType<?> axiomType : AxiomType.AXIOM_TYPES) {
            Set<? extends OWLAxiom> axioms = doc.getAxioms(axiomType);
            ArrayList<? extends OWLAxiom> orderedAxioms = new ArrayList<>(axioms);
            Collections.sort(orderedAxioms);
            LinkedHashSet<? extends OWLAxiom> tmp = new LinkedHashSet<>(orderedAxioms);
            lookupTableOutputStream.writeOWLObjects(tmp);
        }

        dos.flush();
    }

}
