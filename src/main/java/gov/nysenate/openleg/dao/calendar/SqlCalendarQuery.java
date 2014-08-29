package gov.nysenate.openleg.dao.calendar;

import gov.nysenate.openleg.dao.base.BasicSqlQuery;
import gov.nysenate.openleg.dao.base.SqlTable;

/**
 * Queries related to the retrieval and persistence of senate calendars.
 */
public enum SqlCalendarQuery implements BasicSqlQuery
{
    /** --- Calendar Base --- */

    SELECT_CALENDAR(
        "SELECT * FROM ${schema}." + SqlTable.CALENDAR + "\n" +
        "WHERE calendar_no = :calendarNo AND year = :year"
    ),
    SELECT_CALENDARS(
        "SELECT * FROM ${schema}." + SqlTable.CALENDAR + "\n" +
        "WHERE year = :year"
    ),
    UPDATE_CALENDAR(
        "UPDATE ${schema}." + SqlTable.CALENDAR + "\n" +
        "SET modified_date_time = :modifiedDateTime, published_date_time = :publishedDateTime, " +
        "    last_fragment_id = :lastFragmentId\n" +
        "WHERE calendar_no = :calendarNo AND year = :year"
    ),
    INSERT_CALENDAR(
        "INSERT INTO ${schema}." + SqlTable.CALENDAR + "\n" +
        "(calendar_no, year, modified_date_time, published_date_time, last_fragment_id) \n" +
        "VALUES (:calendarNo, :year, :modifiedDateTime, :publishedDateTime, :lastFragmentId)"
    ),
    DELETE_CALENDAR(
        "DELETE FROM ${schema}." + SqlTable.CALENDAR + "\n" +
        "WHERE calendar_no = :calendarNo AND year = :year"
    ),

    /** --- Calendar Supplemental --- */

    SELECT_CALENDAR_SUPS(
        "SELECT * FROM ${schema}." + SqlTable.CALENDAR_SUPPLEMENTAL + "\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year"
    ),
    SELECT_CALENDAR_SUP(
        SELECT_CALENDAR_SUPS.sql + " AND sup_version = :supVersion"
    ),
    SELECT_CALENDAR_SUP_ID(
        "SELECT id FROM ${schema}." + SqlTable.CALENDAR_SUPPLEMENTAL + "\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year AND sup_version = :supVersion"
    ),
    INSERT_CALENDAR_SUP(
        "INSERT INTO ${schema}." + SqlTable.CALENDAR_SUPPLEMENTAL + "\n" +
        "(calendar_no, calendar_year, sup_version, calendar_date, release_date_time, " +
        " modified_date_time, published_date_time, last_fragment_id) \n" +
        "VALUES (:calendarNo, :year, :supVersion, :calendarDate, :releaseDateTime, " +
        "        :modifiedDateTime, :publishedDateTime, :lastFragmentId)"
    ),
    DELETE_CALENDAR_SUP(
        "DELETE FROM ${schema}." + SqlTable.CALENDAR_SUPPLEMENTAL + "\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year AND sup_version = :supVersion"
    ),

    /** --- Calendar Supplemental Entries --- */

    SELECT_CALENDAR_SUP_ENTRIES(
        "SELECT * FROM ${schema}." + SqlTable.CALENDAR_SUP_ENTRY + "\n" +
        "WHERE calendar_sup_id IN (" + SELECT_CALENDAR_SUP_ID.sql + ")"
    ),
    SELECT_CALENDAR_SUP_ENTRIES_BY_SECTION(
        SELECT_CALENDAR_SUP_ENTRIES.sql + " AND section_code = :sectionCode"
    ),
    INSERT_CALENDAR_SUP_ENTRY(
        "INSERT INTO ${schema}." + SqlTable.CALENDAR_SUP_ENTRY + "\n" +
        "(calendar_sup_id, section_code, bill_calendar_no, bill_print_no, bill_amend_version, bill_session_year, \n" +
        " sub_bill_print_no, sub_bill_amend_version, sub_bill_session_year, high, last_fragment_id)\n" +
        "SELECT id, :sectionCode, :billCalNo, :printNo, :amendVersion, :session, :subPrintNo, :subAmendVersion, " +
        "       :subSession, :high, :lastFragmentId\n" +
        "FROM ${schema}." + SqlTable.CALENDAR_SUPPLEMENTAL + "\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year AND sup_version = :supVersion"
    ),
    DELETE_CALENDAR_SUP_ENTRIES(
        "DELETE FROM ${schema}." + SqlTable.CALENDAR_SUP_ENTRY + "\n" +
        "WHERE calendar_sup_id IN (" + SELECT_CALENDAR_SUP_ID.sql + ")"
    ),

    /** --- Calendar Active List --- */

    SELECT_CALENDAR_ACTIVE_LISTS(
        "SELECT * FROM ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST + "\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year"
    ),
    SELECT_CALENDAR_ACTIVE_LIST(
        SELECT_CALENDAR_ACTIVE_LISTS + " AND sequence_no = :sequenceNo"
    ),
    SELECT_CALENDAR_ACTIVE_LIST_ID(
        "SELECT id FROM ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST + "\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year AND sequence_no = :sequenceNo"
    ),
    INSERT_CALENDAR_ACTIVE_LIST(
        "INSERT INTO ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST + "\n" +
        "(sequence_no, calendar_no, calendar_year, calendar_date, notes, release_date_time, last_fragment_id, " +
        " modified_date_time, published_date_time)\n" +
        "VALUES (:sequenceNo, :calendarNo, :year, :calendarDate, :notes, :releaseDateTime, :lastFragmentId, " +
        "        :modifiedDateTime, :publishedDateTime)"
    ),
    UPDATE_CALENDAR_ACTIVE_LIST(
        "UPDATE ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST + "\n" +
        "SET calendar_date = :calendarDate, notes = :notes, release_date_time = :releaseDateTime, " +
        "    last_fragment_id = :lastFragmentId, modified_date_time = :modifiedDateTime, " +
        "    published_date_time = :publishedDateTime\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year AND sequence_no = :sequenceNo"
    ),
    DELETE_CALENDAR_ACTIVE_LIST(
        "DELETE FROM ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST + "\n" +
        "WHERE calendar_no = :calendarNo AND calendar_year = :year AND sequence_no = :sequenceNo"
    ),

    /** --- Calendar Active List --- */

    SELECT_CALENDAR_ACTIVE_LIST_ENTRIES(
        "SELECT * FROM ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST_ENTRY + "\n" +
        "WHERE calendar_active_list_id IN (" + SELECT_CALENDAR_ACTIVE_LIST_ID.sql + ")"
    ),
    INSERT_CALENDAR_ACTIVE_LIST_ENTRY(
        "INSERT INTO ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST_ENTRY + "\n" +
        "(calendar_active_list_id, bill_calendar_no, bill_print_no, bill_amend_version, bill_session_year, last_fragment_id)\n" +
        "SELECT id, :billCalendarNo, :printNo, :amendVersion, :session, :lastFragmentId\n" +
        "FROM (" + SELECT_CALENDAR_ACTIVE_LIST_ID.sql + ") cal_act_list_id"
    ),
    DELETE_CALENDAR_ACTIVE_LIST_ENTRY(
        "DELETE FROM ${schema}." + SqlTable.CALENDAR_ACTIVE_LIST_ENTRY + "\n" +
        "WHERE calendar_active_list_id IN (" + SELECT_CALENDAR_ACTIVE_LIST_ID.sql + ")"
    );

    private String sql;

    SqlCalendarQuery(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return this.sql;
    }
}