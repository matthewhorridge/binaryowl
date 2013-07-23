package org.semanticweb.owlapi.binaryowl;

import org.semanticweb.owlapi.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/04/2013
 */
public class BinaryOWLOntologyBuildingHandler implements BinaryOWLOntologyDocumentHandler<BinaryOWLParseException>, OWLOntologyChangeDataVisitor<Void, RuntimeException> {

    private OWLOntologyLoaderConfiguration loaderConfiguration;

    private OWLOntology ontology;

    private OWLOntologyID ontologyID = new OWLOntologyID();

    private Set<OWLImportsDeclaration> importsDeclarations = new HashSet<OWLImportsDeclaration>();

    private Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();

    private List<Set<OWLAxiom>> axiomSets = new ArrayList<Set<OWLAxiom>>();

    private Set<OWLAxiom> mergedAxioms = new HashSet<OWLAxiom>(1);

    private BinaryOWLOntologyDocumentFormat format = new BinaryOWLOntologyDocumentFormat();

    public BinaryOWLOntologyBuildingHandler(OWLOntologyLoaderConfiguration loaderConfiguration, OWLOntology ontology) {
        this.loaderConfiguration = loaderConfiguration;
        this.ontology = ontology;
    }

    public BinaryOWLOntologyBuildingHandler(OWLOntology ontology) {
        this(new OWLOntologyLoaderConfiguration(), ontology);
    }

    public void build() throws UnloadableImportException {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        manager.applyChange(new SetOntologyID(ontology, ontologyID));
        for (OWLImportsDeclaration decl : importsDeclarations) {
            manager.applyChange(new AddImport(ontology, decl));
        }
        for (OWLImportsDeclaration decl : importsDeclarations) {
            if (!loaderConfiguration.isIgnoredImport(decl.getIRI())) {
                manager.makeLoadImportRequest(decl, loaderConfiguration);
            }
        }
        for (OWLAnnotation annotation : annotations) {
            manager.applyChange(new AddOntologyAnnotation(ontology, annotation));
        }
        manager.addAxioms(ontology, mergedAxioms);
    }


    @Override
    public void handleBeginDocument() throws BinaryOWLParseException {
        ontologyID = new OWLOntologyID();
        importsDeclarations.clear();
        annotations.clear();
        axiomSets.clear();
        mergedAxioms.clear();
    }

    @Override
    public void handleEndDocument() throws BinaryOWLParseException {
        try {
            build();
        }
        catch (UnloadableImportException e) {
            throw new BinaryOWLParseException(e);
        }
    }

    @Override
    public void handleBeginInitialDocumentBlock() {
    }

    @Override
    public void handleEndInitialDocumentBlock() {
        int size = 0;
        for (Set<OWLAxiom> axs : axiomSets) {
            size += axs.size();
        }
        mergedAxioms = new HashSet<OWLAxiom>(size);
        for (Set<OWLAxiom> axs : axiomSets) {
            mergedAxioms.addAll(axs);
        }
        axiomSets = new ArrayList<Set<OWLAxiom>>();
    }

    @Override
    public void handleBeginDocumentChangesBlock() {
    }

    @Override
    public void handleEndDocumentChangesBlock() {
    }

    @Override
    public void handlePreamble(BinaryOWLOntologyDocumentPreamble preamble) {

    }

    @Override
    public void handleDocumentMetaData(BinaryOWLMetadata metadata) {
        this.format = new BinaryOWLOntologyDocumentFormat(metadata);
    }

    @Override
    public void handleOntologyID(OWLOntologyID ontologyID) {
        this.ontologyID = ontologyID;
    }

    @Override
    public void handleImportsDeclarations(Set<OWLImportsDeclaration> importsDeclarations) {
        this.importsDeclarations = importsDeclarations;
    }

    @Override
    public void handleOntologyAnnotations(Set<OWLAnnotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public void handleAxioms(Set<OWLAxiom> axioms) {
        this.axiomSets.add(axioms);
    }


    @Override
    public void handleChanges(OntologyChangeDataList changesList) {
        for (OWLOntologyChangeData changeData : changesList) {
            changeData.accept(this);
        }
    }


    @Override
    public Void visit(AddAxiomData data) {
        mergedAxioms.add(data.getAxiom());
        return null;
    }

    @Override
    public Void visit(RemoveAxiomData data) {
        mergedAxioms.remove(data.getAxiom());
        return null;
    }

    @Override
    public Void visit(AddOntologyAnnotationData data) {
        annotations.add(data.getAnnotation());
        return null;
    }

    @Override
    public Void visit(RemoveOntologyAnnotationData data) {
        annotations.remove(data.getAnnotation());
        return null;
    }

    @Override
    public Void visit(SetOntologyIDData data) {
        ontologyID = data.getNewId();
        return null;
    }

    @Override
    public Void visit(AddImportData data) {
        importsDeclarations.add(data.getDeclaration());
        return null;
    }

    @Override
    public Void visit(RemoveImportData data) {
        importsDeclarations.remove(data.getDeclaration());
        return null;
    }
}
