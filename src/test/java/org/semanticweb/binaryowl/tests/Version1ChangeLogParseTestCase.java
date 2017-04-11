package org.semanticweb.binaryowl.tests;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.io.BufferedInputStream;
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

    private static final int EXPECTED_CHANGE_SETS_COUNT = 83;

    @Before
    public void setUp() throws Exception {
        changeDataURL = Version1ChangeLogParseTestCase.class.getResource("/change-data-v1.binary");
        df = OWLManager.getOWLDataFactory();
    }

    @Test
    public void shouldParseVersion1ChangeLog() throws Exception {
        try {
            final BufferedInputStream inputStream = new BufferedInputStream(changeDataURL.openStream());
            final BinaryOWLOntologyChangeLog log = new BinaryOWLOntologyChangeLog();
            log.readChanges(inputStream, df,
                            (list, skipSetting, filePosition) -> counter += list.getChangeRecords().size());
            inputStream.close();
            assertThat(counter, is(EXPECTED_CHANGE_COUNT));
        } catch (Exception e) {
            fail("Parse failed with exception: " + e);
            e.printStackTrace();
        }
    }

    @Test
    public void shouldParseVersion1ChangeLogWithSkipSetting() throws Exception {
        try {
            final BufferedInputStream inputStream = new BufferedInputStream(changeDataURL.openStream());
            final BinaryOWLOntologyChangeLog log = new BinaryOWLOntologyChangeLog();
            log.readChanges(inputStream, df,
                            (list, skipSetting, filePosition) -> counter ++,
                            SkipSetting.SKIP_DATA);
            inputStream.close();
            assertThat(counter, is(EXPECTED_CHANGE_SETS_COUNT));
        } catch (Exception e) {
            fail("Parse failed with exception: " + e);
            e.printStackTrace();
        }
    }


}
