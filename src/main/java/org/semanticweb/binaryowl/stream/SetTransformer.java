package org.semanticweb.binaryowl.stream;

import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 12/09/2014
 *
 * Transforms one kind of set into another kind of set.
 */
public interface SetTransformer {

    /**
     * Transforms the specified set into another set.
     * @param set The set to be transformed.  Not {@code null}.
     * @param <O> The type of elements contained in the set.
     * @return The transformed set.  Not {@code null}.
     */
    <O extends Comparable> Set<O> transform(Set<O> set);
}
