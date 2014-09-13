package org.semanticweb.binaryowl.stream;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 12/09/2014
 */
public class TreeSetTransformer implements SetTransformer {

    @Override
    public <O extends Comparable> Set<O> transform(Set<O> set) {
        if(set instanceof TreeSet) {
            return (TreeSet<O>) set;
        }
        else {
            return new TreeSet<O>(set);
        }
    }
}
