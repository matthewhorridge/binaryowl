package org.semanticweb.owlapi.binaryowl.versioning;

import org.semanticweb.owlapi.binaryowl.BinaryOWLDocumentBodySerializer;
import org.semanticweb.owlapi.binaryowl.BinaryOWLVersion;
import org.semanticweb.owlapi.binaryowl.versioning.v1.BinaryOWLV1DocumentBodySerializer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class BinaryOWLDocumentBodySerializerSelector {

    public BinaryOWLDocumentBodySerializer getSerializerForVersion(BinaryOWLVersion version) {
        if(version.getVersion() == 1) {
            return new BinaryOWLV1DocumentBodySerializer();
        }
        else {
            throw new RuntimeException("Unsupported Binary OWL Version: " + version);
        }
    }
}
