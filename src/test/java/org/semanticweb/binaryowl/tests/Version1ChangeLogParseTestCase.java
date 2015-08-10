package org.semanticweb.binaryowl.tests;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/06/15
 */
public class Version1ChangeLogParseTestCase {

    private URL changeDataURL;

    private OWLDataFactory df;

    private int counter = 0;

    private static final int EXPECTED_CHANGE_COUNT = 323;

    @Before
    public void setUp() throws Exception {
        changeDataURL = Version1ChangeLogParseTestCase.class.getResource("/change-data-v1.binary");
        df = OWLManager.getOWLDataFactory();
    }

    @Test
    public void shouldParseVersion1ChangeLog() throws Exception {
        try {
            final BinaryOWLOntologyChangeLog outLog = new BinaryOWLOntologyChangeLog();
            final BufferedInputStream inputStream = new BufferedInputStream(changeDataURL.openStream());
            final BinaryOWLOntologyChangeLog log = new BinaryOWLOntologyChangeLog();
            log.readChanges(inputStream, df, new BinaryOWLChangeLogHandler() {
                @Override
                public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long filePosition) {
                    counter += list.getChangeRecords().size();
                }
            });
            inputStream.close();
            assertThat(counter, is(EXPECTED_CHANGE_COUNT));
        } catch (Exception e) {
            fail("Parse failed with exception: " + e);
            e.printStackTrace();
        }

    }
}
