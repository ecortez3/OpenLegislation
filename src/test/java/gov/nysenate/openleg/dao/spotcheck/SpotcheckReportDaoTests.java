package gov.nysenate.openleg.dao.spotcheck;

import com.google.common.collect.Sets;
import gov.nysenate.openleg.BaseTests;
import gov.nysenate.openleg.dao.base.LimitOffset;
import gov.nysenate.openleg.model.bill.BaseBillId;
import gov.nysenate.openleg.model.spotcheck.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

@Transactional
public class SpotcheckReportDaoTests extends BaseTests {

    @Autowired
    private BaseBillIdSpotCheckReportDao reportDao;
    private BaseBillId billId = new BaseBillId("S999999", 2017); // A bill that only exists this test world.
    private LocalDateTime start;

    @Before
    public void setup() {
       start = LocalDateTime.now();
    }

    @Test
    public void simpleSummaryTest() {
        reportDao.getMismatchStatusSummary(SpotCheckDataSource.LBDC, LocalDateTime.now());
    }

    /**
     * Test status updates when saving new mismatches
     */

    @Test
    public void testSaveNewMismatch() {
        reportDao.saveReport(createMismatchReport(start));

        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getKey(), is(billId));
        assertThat(actual.getStatus(), is(SpotCheckMismatchStatus.NEW));
    }

    @Test
    public void testSaveExistingMismatch() {
        // Save new mismatch
        reportDao.saveReport(createMismatchReport(start));
        // Save same mismatch again
        reportDao.saveReport(createMismatchReport(start.plusMinutes(1)));

        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getKey(), is(billId));
        assertThat(actual.getStatus(), is(SpotCheckMismatchStatus.EXISTING));
    }

    @Test
    public void testSaveResolvedMismatch() {
        reportDao.saveReport(createMismatchReport(start));
        // Then save report without a mismatch
        reportDao.saveReport(createEmptyReport(start.plusMinutes(1)));

        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getKey(), is(billId));
        assertThat(actual.getStatus(), is(SpotCheckMismatchStatus.RESOLVED));
    }

    @Test
    public void testSaveRegressionMismatch() throws InterruptedException {
        // Save new mismatch
        reportDao.saveReport(createMismatchReport(start));
        // Resolve the mismatch
        reportDao.saveReport(createEmptyReport(start.plusMinutes(1)));
        // Encounter it again
        reportDao.saveReport(createMismatchReport(start.plusMinutes(2)));

        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getKey(), is(billId));
        assertThat(actual.getStatus(), is(SpotCheckMismatchStatus.REGRESSION));
    }

    /**
     * Test ignore status updates when saving new mismatches
     */

    @Test
    public void givenIgnoreOnce_setToNotIgnoredOnNextOccurance() {
        reportDao.saveReport(createMismatchReport(start, SpotCheckMismatchIgnore.IGNORE_ONCE));
        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getIgnoreStatus(), is(SpotCheckMismatchIgnore.IGNORE_ONCE));
        reportDao.saveReport(createMismatchReport(start.plusMinutes(1)));
        actual = queryMostRecentMismatch();
        assertThat(actual.getIgnoreStatus(), is(SpotCheckMismatchIgnore.NOT_IGNORED));
    }

    @Test
    public void givenIgnoreUntilResolved_keepIgnoredUntilResolved() {
        // Create mismatch with ignore until resolved status.
        reportDao.saveReport(createMismatchReport(start, SpotCheckMismatchIgnore.IGNORE_UNTIL_RESOLVED));
        // Repeat the same mismatch
        reportDao.saveReport(createMismatchReport(start.plusMinutes(1)));
        // Resolve it
        reportDao.saveReport(createEmptyReport(start.plusMinutes(2)));
        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getIgnoreStatus(), is(SpotCheckMismatchIgnore.NOT_IGNORED));
    }

    @Test
    public void givenIgnorePermanently_neverUnIgnore() {
        // Create mismatch with ignored permanently status.
        reportDao.saveReport(createMismatchReport(start, SpotCheckMismatchIgnore.IGNORE_PERMANENTLY));
        // Repeat the same mismatch
        reportDao.saveReport(createMismatchReport(start.plusMinutes(1)));
        // Resolve it
        reportDao.saveReport(createEmptyReport(start.plusMinutes(2)));
        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getIgnoreStatus(), is(SpotCheckMismatchIgnore.IGNORE_PERMANENTLY));
    }

    /**
     * Test setMismatchIgnoreStatus()
     */

    @Test(expected = IllegalArgumentException.class)
    public void givenNullIgnoreStatus_throwException() {
        reportDao.setMismatchIgnoreStatus(1, null);
    }

    @Test
    public void canUpdateIgnoreStatus() {
        reportDao.saveReport(createMismatchReport(start));
        DeNormSpotCheckMismatch mismatch = queryMostRecentMismatch();
        reportDao.setMismatchIgnoreStatus(mismatch.getMismatchId(), SpotCheckMismatchIgnore.IGNORE_PERMANENTLY);
        DeNormSpotCheckMismatch actual = queryMostRecentMismatch();
        assertThat(actual.getIgnoreStatus(), is(SpotCheckMismatchIgnore.IGNORE_PERMANENTLY));
    }

    /**
     * Test add/delete issue id's
     */

    @Test
    public void canAddIssueId() {
        reportDao.saveReport(createMismatchReport(start));
        DeNormSpotCheckMismatch mismatch = queryMostRecentMismatch();
        reportDao.addIssueId(mismatch.getMismatchId(), "10800");
        Set<String> actual = queryMostRecentMismatch().getIssueIds();
        assertThat(actual, contains("10800"));
    }

    @Test
    public void canAddMultipleIssueIds() {
        reportDao.saveReport(createMismatchReport(start));
        DeNormSpotCheckMismatch mismatch = queryMostRecentMismatch();
        reportDao.addIssueId(mismatch.getMismatchId(), "10800");
        reportDao.addIssueId(mismatch.getMismatchId(), "10899");
        Set<String> actual = queryMostRecentMismatch().getIssueIds();
        assertThat(actual, containsInAnyOrder("10800", "10899"));
    }

    @Test
    public void duplicateIssuesNotSaved() {
        reportDao.saveReport(createMismatchReport(start));
        DeNormSpotCheckMismatch mismatch = queryMostRecentMismatch();
        reportDao.addIssueId(mismatch.getMismatchId(), "10800");
        reportDao.addIssueId(mismatch.getMismatchId(), "10800");
        Set<String> actual = queryMostRecentMismatch().getIssueIds();
        assertThat(actual, is(Sets.newHashSet("10800")));
    }

    @Test
    public void canDeleteIssueIds() {
        reportDao.saveReport(createMismatchReport(start));
        DeNormSpotCheckMismatch mismatch = queryMostRecentMismatch();
        reportDao.addIssueId(mismatch.getMismatchId(), "10800");
        reportDao.deleteIssueId(mismatch.getMismatchId(), "10800");
        Set<String> actual = queryMostRecentMismatch().getIssueIds();
        assertThat(actual, is(empty()));
    }

    private DeNormSpotCheckMismatch queryMostRecentMismatch() {
        MismatchQuery query = new MismatchQuery(SpotCheckDataSource.LBDC, Collections.singleton(SpotCheckContentType.BILL))
                .withFromDate(start)
                .withToDate(start.plusHours(1))
                .withMismatchStatuses(EnumSet.allOf(SpotCheckMismatchStatus.class))
                .withIgnoredStatuses(EnumSet.allOf(SpotCheckMismatchIgnore.class));
        return reportDao.getMismatches(query, LimitOffset.ALL).getResults().get(0);
    }

    private SpotCheckReport createMismatchReport(LocalDateTime refDateTime) {
        return createMismatchReport(refDateTime, SpotCheckMismatchIgnore.NOT_IGNORED);
    }

    private SpotCheckReport createMismatchReport(LocalDateTime refDateTime, SpotCheckMismatchIgnore ignoreStatus) {
        SpotCheckReportId reportId = new SpotCheckReportId(SpotCheckRefType.LBDC_DAYBREAK, refDateTime, LocalDateTime.now());
        SpotCheckReport report = new SpotCheckReport();
        report.setReportId(reportId);
        SpotCheckObservation ob = new SpotCheckObservation(new SpotCheckReferenceId(SpotCheckRefType.LBDC_DAYBREAK, refDateTime), billId);
        SpotCheckMismatch mm = new SpotCheckMismatch(SpotCheckMismatchType.BILL_COSPONSOR, "ObservedSponsor", "ReferenceSponsor");
        mm.setIgnoreStatus(ignoreStatus);
        ob.addMismatch(mm);
        report.addObservation(ob);
        return report;
    }

    private SpotCheckReport createEmptyReport(LocalDateTime refDateTime)  {
        SpotCheckReportId reportId = new SpotCheckReportId(SpotCheckRefType.LBDC_DAYBREAK, refDateTime, LocalDateTime.now());
        SpotCheckReport report = new SpotCheckReport();
        report.setReportId(reportId);
        SpotCheckObservation ob = new SpotCheckObservation(new SpotCheckReferenceId(SpotCheckRefType.LBDC_DAYBREAK, refDateTime), billId);
        report.addObservation(ob);
        return report;
    }
}
