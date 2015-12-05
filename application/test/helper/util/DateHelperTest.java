package helper.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Test;
import play.test.UnitTest;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DateHelperTest extends UnitTest {

    @Test 
    public void bstTest() {
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        Calendar london = Calendar.getInstance(tz);
        assertTrue(london.getTimeZone().equals(tz));
        System.out.println(london);
        System.out.println(london.getTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        dateFormat.setTimeZone(tz);
        System.out.println(dateFormat.format(london.getTime()));

    }

    public void testGetDateFormatYyyyMmDdHhMm() {
    }

    public void testGetDateFormatShort() {
    }

    public void testGetDateFormatFull24Hour() {
    }

    public void testGetDateFormatDayOfWeekDayOfMonthYearAmTz() {
    }

    public void testGetDateFormatDayOfWeekMmDdYyyy() {
    }

    public void testSetDefaultTimeZone() {
    }

    @Test
    public void testYesterdayOrBetterComparision() {
        // now user local time
        Calendar quietAllowedForFeature = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        quietAllowedForFeature.setTimeInMillis(DateHelper.MILLIS_PER_DAY * 2);
        // midnight 'now' user local time
        quietAllowedForFeature = DateHelper.asDay(quietAllowedForFeature);
        // midnight day before user local time
        quietAllowedForFeature.add(Calendar.DATE, -1); //1 days quiet ok; add this as per feature setting?
        // last rec'd as user local time
        Date userLastMessageReceived = new Date(DateHelper.MILLIS_PER_DAY * 3);
        // if any messages from module after midnight yesterday (any messages yesterday or newer)
        //System.out.println(quietAllowedForFeature);
        //System.out.println(userLastMessageReceived);
        assertTrue(userLastMessageReceived.after(quietAllowedForFeature.getTime()));
    }

    @Test
    public void testAsDay() {
        Calendar localTimeCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Las_Angeles"));
        Calendar localTimeCalendarAsDay = DateHelper.asDay(localTimeCalendar);
        System.out.println(localTimeCalendar.getTimeInMillis());
        System.out.println(localTimeCalendarAsDay.getTimeInMillis());
        System.out.println(new Date().getTime());
        assertEquals(0, localTimeCalendarAsDay.get(Calendar.MILLISECOND));
        assertEquals(0, localTimeCalendarAsDay.get(Calendar.MINUTE));
        assertEquals(0, localTimeCalendarAsDay.get(Calendar.SECOND));
        assertEquals(0, localTimeCalendarAsDay.get(Calendar.HOUR_OF_DAY));
        assertEquals(localTimeCalendar.get(Calendar.DATE), localTimeCalendarAsDay.get(Calendar.DATE));
        assertEquals(localTimeCalendar.get(Calendar.DAY_OF_MONTH), localTimeCalendarAsDay.get(Calendar.DAY_OF_MONTH));
        assertEquals(localTimeCalendar.get(Calendar.DAY_OF_YEAR), localTimeCalendarAsDay.get(Calendar.DAY_OF_YEAR));
        assertEquals(localTimeCalendar.get(Calendar.YEAR), localTimeCalendarAsDay.get(Calendar.YEAR));
        assertEquals(localTimeCalendar.get(Calendar.MONTH), localTimeCalendarAsDay.get(Calendar.MONTH));
    }

    public void testAsUtcDay() {
    }

    public void testNowUtc() {
    }

    public void testGetRangeHoursBeforeNow() {
    }

    public void testGetRangeDaysBeforeTodayUtc() {
    }

    public void testGetRangeFullDaysBefore() {
    }

    public void testTodayUtc() {
    }

    public void testTallyItemsPerDay() {
    }

    public void testMillisToNextHour() {
    }

    public void testDiffInMillis() {
    }

    public void testMillisToSeconds() {
    }

    public void testMillisToMinutes() {
    }

    public void testMillisToHours() {
    }

    public void testMillisToDays() {
    }

    @Test
    public void testCalculateIntervalInDays() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        CalendarRange range = new CalendarRange(start, end);
        assertEquals(0, DateHelper.calculateIntervalInDays(range));
        end.add(Calendar.DATE, 1);
        assertEquals(1, DateHelper.calculateIntervalInDays(range));
        end.add(Calendar.DATE, 1);
        assertEquals(2, DateHelper.calculateIntervalInDays(range));

        // cross year boundary
        start = Calendar.getInstance();
        start.setTimeInMillis(0);
        end = Calendar.getInstance();
        end.setTimeInMillis(0);
        range = new CalendarRange(start, end);
        assertEquals(1970, start.get(Calendar.YEAR));
        assertEquals(1970, end.get(Calendar.YEAR));
        assertEquals(0, DateHelper.calculateIntervalInDays(range));
        end.add(Calendar.DATE, 1);
        assertEquals(1970, end.get(Calendar.YEAR));
        assertEquals(1, DateHelper.calculateIntervalInDays(range));
        end.add(Calendar.DATE, 1);
        assertEquals(2, DateHelper.calculateIntervalInDays(range));
        // cross year boundary
        start = Calendar.getInstance();
        start.setTimeInMillis(0);
        end = Calendar.getInstance();
        end.setTimeInMillis(0);
        range = new CalendarRange(start, end);
        assertEquals(1970, start.get(Calendar.YEAR));
        assertEquals(1970, end.get(Calendar.YEAR));
        assertEquals(0, DateHelper.calculateIntervalInDays(range));
        start.add(Calendar.DATE, 1);
        assertEquals(1970, start.get(Calendar.YEAR));
        assertEquals(-1, DateHelper.calculateIntervalInDays(range));
        start.add(Calendar.DATE, 1);
        assertEquals(-2, DateHelper.calculateIntervalInDays(range));
    }
}
