package org.semanticweb.binaryowl.tests;

import org.junit.Test;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentPreamble;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.BinaryOWLVersion;

import java.io.*;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2013
 */
public class PreambleTestCase {


    /**
     * The preamble contains a magic number which is 4 bytes + a version number which is two bytes.
     */
    @Test
    public void testParsePreamble() throws IOException, BinaryOWLParseException {
        byte [] preambleBytes = new byte[] {'B', 'O', '2', 'O', 0, 1};
        BinaryOWLOntologyDocumentPreamble preamble = new BinaryOWLOntologyDocumentPreamble(new DataInputStream(new ByteArrayInputStream(preambleBytes)));
        BinaryOWLVersion version = preamble.getFileFormatVersion();
        assertEquals(1, version.getVersion());
    }

    /**
     * If the magic number isn't found then a parse exception should be generated.
     */
    @Test(expected = BinaryOWLParseException.class)
    public void testMagicNumberNotPresent() throws IOException, BinaryOWLParseException {
        byte [] preambleBytes = new byte[] {0, 0, 0, 0};
        BinaryOWLOntologyDocumentPreamble preamble = new BinaryOWLOntologyDocumentPreamble(new DataInputStream(new ByteArrayInputStream(preambleBytes)));
        BinaryOWLVersion version = preamble.getFileFormatVersion();
    }
}
