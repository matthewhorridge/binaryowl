package org.semanticweb.binaryowl.serializer;

import org.semanticweb.binaryowl.BinaryOWLVersion;
import org.semanticweb.binaryowl.serializer.v1.BinaryOWLV1DocumentBodySerializer;
import org.semanticweb.binaryowl.serializer.v2.BinaryOWLV2DocumentBodySerializer;
import org.semanticweb.binaryowl.serializer.v3.BinaryOWLV3DocumentBodySerializer;

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
        else if(version.getVersion() == 2) {
            return new BinaryOWLV2DocumentBodySerializer();
        }
        else if(version.getVersion() == 3) {
            return new BinaryOWLV3DocumentBodySerializer();
        }
        else {
            throw new RuntimeException("Unsupported Binary OWL Version: " + version);
        }
    }
}
