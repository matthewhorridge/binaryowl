package org.semanticweb.binaryowl.tests;

import com.google.common.base.Optional;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;
import static org.semanticweb.binaryowl.tests.TestUtil.roundTrip;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 */
public class ObjectRoundTripTestCase {

    private static DefaultPrefixManager pm = new DefaultPrefixManager("http://another.com/ontology#");

    private static OWLClass A = Class("A", pm);

    private static OWLClass B = Class("B", pm);

    private static OWLClass C = Class("C", pm);

    private static OWLClass D = Class("D", pm);

    private static OWLObjectProperty R = ObjectProperty("R", pm);
    
    private static OWLObjectProperty S = ObjectProperty("S", pm);

    private static OWLDataProperty T = DataProperty("T", pm);

    private static OWLDataProperty U = DataProperty("U", pm);

    private static OWLAnnotationProperty L = AnnotationProperty("L", pm);

    private static OWLAnnotationProperty M = AnnotationProperty("M", pm);

    private static OWLNamedIndividual i = NamedIndividual("i", pm);

    private static OWLNamedIndividual j = NamedIndividual("j", pm);

    private static OWLNamedIndividual k = NamedIndividual("k", pm);

    public static final OWLDatatype DT = Datatype(XSDVocabulary.STRING.getIRI());

    public static final OWLDatatype DTI = Datatype(XSDVocabulary.INTEGER.getIRI());

    public static final OWLLiteral LIT = Literal("Test");

    @Test
    public void _Class() {
        roundTrip(A);
    }

    @Test
    public void _Datatype() {
        roundTrip(DT);
    }

    @Test
    public void _ObjectProperty() {
        roundTrip(R);
    }

    @Test
    public void _DataProperty() {
        roundTrip(T);
    }

    @Test
    public void _AnnotationProperty() {
        roundTrip(L);
    }

    @Test
    public void _NamedIndividual() {
        roundTrip(i);
    }

    @Test
    public void _PlainLiteralNoLang() {
        roundTrip(LIT);
    }

    @Test
    public void _PlainLiteralWithLang() {
        OWLLiteral lit = Literal("Test", "en");
        roundTrip(lit);
    }

    @Test
    public void _TypedLiteral() {
        OWLLiteral lit = Literal(33.0);
        roundTrip(lit);
    }

    @Test
    public void _Declaration() {
        OWLAxiom ax = Declaration(A);
        roundTrip(ax);
    }




    @Test
    public void _ObjectInverseOf() {
        OWLObjectPropertyExpression prop = R.getInverseProperty();
        roundTrip(prop);

        OWLObjectPropertyExpression propInv = prop.getInverseProperty();
        roundTrip(propInv);
    }


    @Test
    public void _DataIntersectionOf() {
        OWLDataIntersectionOf obj = DataIntersectionOf(DT, DTI);
        roundTrip(obj);
    }

    @Test
    public void _DataUnionOf() {
        OWLDataUnionOf obj = DataUnionOf(DT, DTI);
        roundTrip(obj);
    }

    @Test
    public void _DataComplementOf() {
        OWLDataComplementOf obj = DataComplementOf(DT);
        roundTrip(obj);
    }

    @Test
    public void _DataOneOf() {
        OWLDataOneOf obj = DataOneOf(LIT);
        roundTrip(obj);
    }

    @Test
    public void _DatatypeRestriction() {
        OWLFacetRestriction f0 = FacetRestriction(OWLFacet.MIN_EXCLUSIVE, LIT);
        OWLFacetRestriction f1 = FacetRestriction(OWLFacet.MAX_EXCLUSIVE, LIT);
        OWLDatatypeRestriction obj = DatatypeRestriction(DT, f0, f1);
        roundTrip(obj);
    }



    @Test
    public void _ObjectIntersectionOf() {
        OWLObject obj = ObjectIntersectionOf(A, B, C);
        roundTrip(obj);
    }

    @Test
    public void _ObjectUnionOf() {
        OWLObject obj = ObjectUnionOf(A, B, C);
        roundTrip(obj);
    }

    @Test
    public void _ObjectComplementOf() {
        OWLObject obj = ObjectComplementOf(A);
        roundTrip(obj);
    }


    @Test
    public void _ObjectOneOf() {
        OWLObject obj = ObjectOneOf(i, j, k);
        roundTrip(obj);
    }

    @Test
    public void _ObjectSomeValuesFrom() {
        OWLObjectSomeValuesFrom obj = ObjectSomeValuesFrom(R, A);
        roundTrip(obj);
    }

    @Test
    public void _ObjectAllValuesFrom() {
        OWLObjectAllValuesFrom obj = ObjectAllValuesFrom(R, A);
        roundTrip(obj);
    }

    @Test
    public void _ObjectHasValue() {
        OWLObjectHasValue obj = ObjectHasValue(R, i);
        roundTrip(obj);
    }

    @Test
    public void _ObjectHasSelf() {
        OWLObjectHasSelf obj = ObjectHasSelf(R);
        roundTrip(obj);
    }



    @Test
    public void _ObjectMinCardinality() {
        OWLObjectMinCardinality obj = ObjectMinCardinality(3, R, A);
        roundTrip(obj);
    }

    @Test
    public void _ObjectMaxCardinality() {
        OWLObjectMaxCardinality obj = ObjectMaxCardinality(3, R, A);
        roundTrip(obj);
    }

    @Test
    public void _ObjectExactCardinality() {
        OWLObjectExactCardinality obj = ObjectExactCardinality(3, R, A);
        roundTrip(obj);
    }


    @Test
    public void _DataSomeValuesFrom() {
        OWLDataSomeValuesFrom obj = DataSomeValuesFrom(T, DT);
        roundTrip(obj);
    }

    @Test
    public void _DataAllValuesFrom() {
        OWLDataAllValuesFrom obj = DataAllValuesFrom(T, DT);
        roundTrip(obj);
    }

    @Test
    public void _DataHasValue() {
        OWLDataHasValue obj = DataHasValue(T, LIT);
        roundTrip(obj);
    }

    @Test
    public void _DataMinCardinality() {
        OWLDataMinCardinality obj = DataMinCardinality(3, T, DT);
        roundTrip(obj);
    }

    @Test
    public void _DataMaxCardinality() {
        OWLDataMaxCardinality obj = DataMaxCardinality(3, T, DT);
        roundTrip(obj);
    }

    @Test
    public void _DataExactCardinality() {
        OWLDataExactCardinality obj = DataExactCardinality(3, T, DT);
        roundTrip(obj);
    }



    public static void roundTripAxiom(OWLAxiom ax) {
        roundTrip(ax);
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        Set<OWLAnnotation> annos = new HashSet<OWLAnnotation>();
        annos.add(df.getOWLAnnotation(L, A.getIRI()));
        OWLAxiom annotatedAxiom = ax.getAnnotatedAxiom(annos);
        roundTrip(annotatedAxiom);
    }



    @Test
    public void _SubClassOf() {
        OWLAxiom ax = SubClassOf(A, B);
        roundTripAxiom(ax);
    }

    @Test
    public void _EquivalentClasses() {
        OWLAxiom ax = EquivalentClasses(A, B, C);
        roundTripAxiom(ax);
    }

    @Test
    public void _DisjointClasses() {
        OWLAxiom ax = DisjointClasses(A, B);
        roundTripAxiom(ax);
    }

    @Test
    public void _DisjointUnion() {
        OWLAxiom ax = DisjointUnion(A, B, C, D);
        roundTripAxiom(ax);
    }
    
    @Test
    public void _SubObjectPropertyOf() {
        OWLAxiom ax = SubObjectPropertyOf(R, S);
        roundTripAxiom(ax);
    }
    
    @Test
    public void _EquivalentObjectProperties() {
        OWLAxiom ax = EquivalentObjectProperties(R, S);
        roundTripAxiom(ax);
    }

    @Test
    public void _DisjointObjectProperties() {
        OWLAxiom ax = DisjointObjectProperties(R, S);
        roundTripAxiom(ax);
    }

    @Test
    public void _InverseObjectProperties() {
        OWLInverseObjectPropertiesAxiom obj = InverseObjectProperties(R, S);
        roundTripAxiom(obj);
    }

    @Test
    public void _ObjectPropertyDomain() {
        OWLObjectPropertyDomainAxiom obj = ObjectPropertyDomain(R, A);
        roundTripAxiom(obj);
    }

    @Test
    public void _ObjectPropertyRange() {
        OWLObjectPropertyRangeAxiom obj = ObjectPropertyRange(R, A);
        roundTripAxiom(obj);
    }


    @Test
    public void _FunctionalObjectProperty() {
        OWLFunctionalObjectPropertyAxiom obj = FunctionalObjectProperty(R);
        roundTripAxiom(obj);
    }

    @Test
    public void _InverseFunctionalObjectProperty() {
        OWLInverseFunctionalObjectPropertyAxiom obj = InverseFunctionalObjectProperty(R);
        roundTripAxiom(obj);
    }

    @Test
    public void _ReflexiveObjectProperty() {
        OWLReflexiveObjectPropertyAxiom obj = ReflexiveObjectProperty(R);
        roundTripAxiom(obj);
    }

    @Test
    public void _IrreflexiveObjectProperty() {
        OWLIrreflexiveObjectPropertyAxiom obj = IrreflexiveObjectProperty(R);
        roundTripAxiom(obj);
    }

    @Test
    public void _SymmetricObjectProperty() {
        OWLSymmetricObjectPropertyAxiom obj = SymmetricObjectProperty(R);
        roundTripAxiom(obj);
    }

    @Test
    public void _AsymmetricObjectProperty() {
        OWLAsymmetricObjectPropertyAxiom obj = AsymmetricObjectProperty(R);
        roundTripAxiom(obj);
    }

    @Test
    public void _TransitiveObjectProperty() {
        OWLTransitiveObjectPropertyAxiom obj = TransitiveObjectProperty(R);
        roundTripAxiom(obj);
    }




    @Test
    public void _SubDataPropertyOf() {
        OWLAxiom ax = SubDataPropertyOf(T, U);
        roundTripAxiom(ax);
    }

    @Test
    public void _EquivalentDataProperties() {
        OWLAxiom ax = EquivalentDataProperties(T, U);
        roundTripAxiom(ax);
    }

    @Test
    public void _DisjointDataProperties() {
        OWLAxiom ax = DisjointDataProperties(T, U);
        roundTripAxiom(ax);
    }

    @Test
    public void _DataPropertyDomain() {
        OWLDataPropertyDomainAxiom obj = DataPropertyDomain(T, A);
        roundTripAxiom(obj);
    }

    @Test
    public void _DataPropertyRange() {
        OWLDataPropertyRangeAxiom obj = DataPropertyRange(T, DT);
        roundTripAxiom(obj);
    }


    @Test
    public void _FunctionalDataProperty() {
        OWLFunctionalDataPropertyAxiom obj = FunctionalDataProperty(T);
        roundTripAxiom(obj);
    }



    @Test
    public void _DatatypeDefinition() {
        OWLDatatypeDefinitionAxiom obj = DatatypeDefinition(DT, DTI);
        roundTripAxiom(obj);
    }




    @Test
    public void _HasKey() {
        OWLHasKeyAxiom ax = HasKey(A, R, S, T, U);
        roundTripAxiom(ax);
    }



    @Test
    public void _SameIndividual() {
        OWLSameIndividualAxiom obj = SameIndividual(i, j, k);
        roundTripAxiom(obj);
    }

    @Test
    public void _DifferentIndividuals() {
        OWLDifferentIndividualsAxiom obj = DifferentIndividuals(i, j, k);
        roundTripAxiom(obj);
    }

    @Test
    public void _ObjectPropertyAssertion() {
        OWLObjectPropertyAssertionAxiom obj = ObjectPropertyAssertion(R, i, j);
        roundTripAxiom(obj);
    }

    @Test
    public void _NegativeObjectPropertyAssertion() {
        OWLNegativeObjectPropertyAssertionAxiom obj = NegativeObjectPropertyAssertion(R, i, j);
        roundTripAxiom(obj);
    }

    @Test
    public void _DataPropertyAssertion() {
        OWLDataPropertyAssertionAxiom obj = DataPropertyAssertion(T, i, LIT);
        roundTripAxiom(obj);
    }

    @Test
    public void _NegativeDataPropertyAssertion() {
        OWLNegativeDataPropertyAssertionAxiom obj = NegativeDataPropertyAssertion(T, i, LIT);
        roundTripAxiom(obj);
    }

    @Test
    public void _AnnotationAssertion() {
        roundTripAxiom(AnnotationAssertion(L, A.getIRI(), B.getIRI()));
        roundTripAxiom(AnnotationAssertion(L, A.getIRI(), LIT));
    }

    @Test
    public void _SubAnnotationPropertyOf() {
        roundTripAxiom(SubAnnotationPropertyOf(L, M));
    }

    @Test
    public void _AnnotationPropertyDomain() {
        roundTripAxiom(AnnotationPropertyDomain(L, A.getIRI()));
    }

    @Test
    public void _AnnotationPropertyRange() {
        roundTripAxiom(AnnotationPropertyRange(L, A.getIRI()));
    }

    @Test
    public void _Annotation() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        roundTrip(df.getOWLAnnotation(L, A.getIRI()));
    }

    @Test
    public void _Annotation_Nested() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        OWLAnnotation inner1 = df.getOWLAnnotation(L, A.getIRI());
        OWLAnnotation inner2 = df.getOWLAnnotation(L, B.getIRI());
        roundTrip(df.getOWLAnnotation(L, A.getIRI(), new HashSet<OWLAnnotation>(Arrays.asList(inner1, inner2))));
    }

    @Test
    public void _SWRLVariable() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLVariable variable = df.getSWRLVariable(A.getIRI());
        roundTrip(variable);
    }

    @Test
    public void _SWRLLiteralArgument() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLLiteralArgument literalArgument = df.getSWRLLiteralArgument(LIT);
        roundTrip(literalArgument);
    }

    @Test
    public void _SWRLIndividualArgument() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLIndividualArgument individualArgument = df.getSWRLIndividualArgument(i);
        roundTrip(individualArgument);
    }

    @Test
    public void _SWRLDifferentIndividuals() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLDifferentIndividualsAtom atom = df.getSWRLDifferentIndividualsAtom(df.getSWRLIndividualArgument(i), df.getSWRLIndividualArgument(j));
        roundTrip(atom);
    }


    @Test
    public void _SWRLBuiltInAtom() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLBuiltInAtom builtInAtom = df.getSWRLBuiltInAtom(A.getIRI(), Arrays.<SWRLDArgument>asList(df.getSWRLVariable(B.getIRI()), df.getSWRLLiteralArgument(LIT)));
        roundTrip(builtInAtom);
    }

    @Test
    public void _SWRLObjectPropertyAtom() {
        OWLDataFactory df  = OWLManager.getOWLDataFactory();
        SWRLObjectPropertyAtom atom = df.getSWRLObjectPropertyAtom(R, df.getSWRLIndividualArgument(i), df.getSWRLIndividualArgument(j));
        roundTrip(atom);
    }

    @Test
    public void _SWRLDataPropertyAtom() {
        OWLDataFactory df  = OWLManager.getOWLDataFactory();
        SWRLDataPropertyAtom atom = df.getSWRLDataPropertyAtom(T, df.getSWRLIndividualArgument(i), df.getSWRLLiteralArgument(LIT));
        roundTrip(atom);
    }

    @Test
    public void _SWRLClassAtom() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLClassAtom atom = df.getSWRLClassAtom(A, df.getSWRLIndividualArgument(i));
        roundTrip(atom);
    }

    @Test
    public void _SWRLDataRangeAtom() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLDataRangeAtom atom = df.getSWRLDataRangeAtom(DT, df.getSWRLLiteralArgument(LIT));
        roundTrip(atom);
    }

    @Test
    public void _SWRLRule() {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        SWRLClassAtom atomA = df.getSWRLClassAtom(A, df.getSWRLIndividualArgument(i));
        SWRLClassAtom atomB = df.getSWRLClassAtom(B, df.getSWRLIndividualArgument(i));
        SWRLRule rule = df.getSWRLRule(Collections.singleton(atomA), Collections.singleton(atomB));
        roundTrip(rule);
    }

    @Test
    public void _Ontology() throws OWLOntologyCreationException {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = Ontology(man, Declaration(A), Declaration(B), Declaration(C));


        OWLDataFactory owlDataFactory = man.getOWLDataFactory();
        OWLAnnotation anno = owlDataFactory.getOWLAnnotation(L, A.getIRI());
        man.applyChange(new AddOntologyAnnotation(ont, anno));

        // IRI
        man.applyChange(new SetOntologyID(ont, A.getIRI()));
        roundTrip(ont);

        // IRI + Version IRI
        man.applyChange(new SetOntologyID(ont, new OWLOntologyID(Optional.of(A.getIRI()), Optional.of(B.getIRI()))));
        roundTrip(ont);
    }


}
