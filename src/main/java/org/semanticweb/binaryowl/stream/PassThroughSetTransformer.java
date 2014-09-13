package org.semanticweb.binaryowl.stream;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 12/09/2014
 */
public class PassThroughSetTransformer implements SetTransformer {

    /**
     * A set transformer that returns the supplied set.
     * @param set The set to be transformed.  Not {@code null}.
     * @param <O> The type of elements contained within the set.
     * @return The same set that was supplied.
     */
    @Override
    public <O extends Comparable> Set<O> transform(Set<O> set) {
        return checkNotNull(set);
    }
}
