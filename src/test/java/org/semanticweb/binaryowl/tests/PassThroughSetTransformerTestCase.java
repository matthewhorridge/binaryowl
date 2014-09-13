package org.semanticweb.binaryowl.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.binaryowl.stream.PassThroughSetTransformer;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 13/09/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class PassThroughSetTransformerTestCase<O extends Comparable> {

    @Mock
    private Set<O> set;

    private PassThroughSetTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new PassThroughSetTransformer();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfSuppliedSetIsNull() {
        transformer.transform(null);
    }

    @Test
    public void shouldReturnSuppliedSet() {
        Set<O> transformed = transformer.transform(set);
        assertThat(transformed, is(set));
    }
}
