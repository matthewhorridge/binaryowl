package org.semanticweb.binaryowl.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.binaryowl.stream.TreeSetTransformer;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 13/09/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeSetTransformerTestCase<O extends Comparable<O>> {

    @Mock
    private O element;

    private HashSet<O> hashSet;

    private TreeSet<O> treeSet;

    private TreeSetTransformer transformer;

    @Before
    public void setUp() throws Exception {
        when(element.compareTo(element)).thenReturn(0);

        hashSet = new HashSet<O>();
        hashSet.add(element);

        treeSet = new TreeSet<O>();
        transformer = new TreeSetTransformer();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfSuppliedSetIsNull() {
        transformer.transform(null);
    }

    @Test
    public void shouldReturnSuppliedSet() {
        Set<O> transformed = transformer.transform(treeSet);
        assertThat(transformed, is(equalTo((Set<O>) treeSet)));
    }

    @Test
    public void shouldReturnFreshTreeSet() {
        Set<O> transformed = transformer.transform(hashSet);
        assertThat(transformed, is(instanceOf(TreeSet.class)));
    }

    @Test
    public void shouldReturnElements() {
        Set<O> transformed = transformer.transform(hashSet);
        assertThat(transformed.contains(element), is(true));
    }
}
