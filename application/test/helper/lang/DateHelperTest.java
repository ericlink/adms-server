package helper.lang;

import play.test.*;
import org.junit.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import helper.util.CalendarRange;
import helper.util.DateHelper;
import org.apache.commons.lang.time.DateFormatUtils;

/**   
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DateHelperTest extends UnitTest {

    @Test
    public void testAsUtcDayCalendar() {
        Calendar localTime = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        Calendar cal = DateHelper.asUtcDay(localTime);
        assertEquals(0, cal.get(Calendar.MILLISECOND));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.HOUR));
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(DateHelper.UTC_TIME_ZONE, cal.getTimeZone());
        assertEquals(TimeZone.getTimeZone("CST"), localTime.getTimeZone());
        assertEquals(localTime.get(Calendar.YEAR), cal.get(Calendar.YEAR));
        assertEquals(localTime.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        assertEquals(localTime.get(Calendar.DATE), cal.get(Calendar.DATE));
    }

    @Test
    public void testAsUtcDayDate() {
        Calendar localTime = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        Calendar cal = DateHelper.asUtcDay(localTime.getTime());
        assertEquals(0, cal.get(Calendar.MILLISECOND));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.HOUR));
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(DateHelper.UTC_TIME_ZONE, cal.getTimeZone());
        //assertEquals(TimeZone.getTimeZone("CST"), localTime.getTimeZone());
        //assertEquals(localTime.get(Calendar.YEAR), cal.get(Calendar.YEAR));
        //assertEquals(localTime.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        //assertEquals(localTime.get(Calendar.DATE), cal.get(Calendar.DATE));
    }

    @Test
    public void testNowUtc() {
        long nowBeforeCall = System.currentTimeMillis();
        Calendar cal = DateHelper.nowUtc();
        long nowAfterCall = System.currentTimeMillis();
        assertTrue(cal.getTimeInMillis() >= nowBeforeCall);
        assertTrue(cal.getTimeInMillis() <= nowAfterCall);
        assertEquals(DateHelper.UTC_TIME_ZONE, cal.getTimeZone());
    }

    @Test
    public void testGetRangeDaysBeforeTodayUtc() {
        int days = 1;
        Calendar nowUtc = DateHelper.nowUtc();

        CalendarRange range = DateHelper.getRangeFullDaysBefore(nowUtc, days);
        Calendar start = range.getStart();
        Calendar end = range.getEnd();
        System.out.println(DateFormatUtils.SMTP_DATETIME_FORMAT.format(start));
        System.out.println(DateFormatUtils.SMTP_DATETIME_FORMAT.format(end));

        // end date is midnight of date passed in
        assertEquals(nowUtc.get(Calendar.YEAR), end.get(Calendar.YEAR));
        assertEquals(nowUtc.get(Calendar.MONTH), end.get(Calendar.MONTH));
        assertEquals(nowUtc.get(Calendar.DATE), end.get(Calendar.DATE));
        assertEquals(0, end.get(Calendar.HOUR));
        assertEquals(0, end.get(Calendar.MINUTE));
        assertEquals(0, end.get(Calendar.SECOND));
        assertEquals(0, end.get(Calendar.MILLISECOND));
        assertEquals(0, end.get(Calendar.HOUR_OF_DAY));
        assertEquals(DateHelper.UTC_TIME_ZONE, end.getTimeZone());

        // start date is 11:59:59 or 'days' time
        nowUtc.add(Calendar.DATE, -(days + 2));
        assertEquals(nowUtc.get(Calendar.YEAR), start.get(Calendar.YEAR));
        assertEquals(nowUtc.get(Calendar.MONTH), start.get(Calendar.MONTH));
        assertEquals(nowUtc.get(Calendar.DATE), start.get(Calendar.DATE));
        assertEquals(11, start.get(Calendar.HOUR));
        assertEquals(23, start.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, start.get(Calendar.MINUTE));
        assertEquals(59, start.get(Calendar.SECOND));
        assertEquals(999, start.get(Calendar.MILLISECOND));
        assertEquals(DateHelper.UTC_TIME_ZONE, start.getTimeZone());

    }

    @Test
    public void testGetRangeDaysBeforeOneDay() {
        int days = 1;
        Calendar localtime = Calendar.getInstance();
        CalendarRange range = DateHelper.getRangeFullDaysBefore(localtime, days);
        Calendar start = range.getStart();
        Calendar end = range.getEnd();
        System.out.println(DateFormatUtils.SMTP_DATETIME_FORMAT.format(start));
        System.out.println(DateFormatUtils.SMTP_DATETIME_FORMAT.format(end));

        // end date is midnight of date passed in
        assertEquals(localtime.get(Calendar.YEAR), end.get(Calendar.YEAR));
        assertEquals(localtime.get(Calendar.MONTH), end.get(Calendar.MONTH));
        assertEquals(localtime.get(Calendar.DATE), end.get(Calendar.DATE));
        assertEquals(0, end.get(Calendar.HOUR));
        assertEquals(0, end.get(Calendar.MINUTE));
        assertEquals(0, end.get(Calendar.SECOND));
        assertEquals(0, end.get(Calendar.MILLISECOND));
        assertEquals(0, end.get(Calendar.HOUR_OF_DAY));
        assertEquals(DateHelper.UTC_TIME_ZONE, end.getTimeZone());

        // start date is 11:59:59 or 'days' time
        localtime.add(Calendar.DATE, -(days + 2)); //-1 is day before, -1 for day before day before, to include day before (> x)
        assertEquals(localtime.get(Calendar.YEAR), start.get(Calendar.YEAR));
        assertEquals(localtime.get(Calendar.MONTH), start.get(Calendar.MONTH));
        assertEquals(localtime.get(Calendar.DATE), start.get(Calendar.DATE));
        assertEquals(11, start.get(Calendar.HOUR));
        assertEquals(23, start.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, start.get(Calendar.MINUTE));
        assertEquals(59, start.get(Calendar.SECOND));
        assertEquals(999, start.get(Calendar.MILLISECOND));
        assertEquals(DateHelper.UTC_TIME_ZONE, start.getTimeZone());
    }

    @Test
    public void testGetRangeDaysBeforeTwoDays() {
        int days = 2;
        Calendar localtime = Calendar.getInstance();
        CalendarRange range = DateHelper.getRangeFullDaysBefore(localtime, days);
        Calendar start = range.getStart();
        Calendar end = range.getEnd();
        System.out.println(DateFormatUtils.SMTP_DATETIME_FORMAT.format(start));
        System.out.println(DateFormatUtils.SMTP_DATETIME_FORMAT.format(end));

        // end date is midnight of date passed in
        assertEquals(localtime.get(Calendar.YEAR), end.get(Calendar.YEAR));
        assertEquals(localtime.get(Calendar.MONTH), end.get(Calendar.MONTH));
        assertEquals(localtime.get(Calendar.DATE), end.get(Calendar.DATE));
        assertEquals(0, end.get(Calendar.HOUR));
        assertEquals(0, end.get(Calendar.MINUTE));
        assertEquals(0, end.get(Calendar.SECOND));
        assertEquals(0, end.get(Calendar.MILLISECOND));
        assertEquals(0, end.get(Calendar.HOUR_OF_DAY));
        assertEquals(DateHelper.UTC_TIME_ZONE, end.getTimeZone());

        // start date is 11:59:59 or 'days' time
        localtime.add(Calendar.DATE, -(days + 2));
        assertEquals(localtime.get(Calendar.YEAR), start.get(Calendar.YEAR));
        assertEquals(localtime.get(Calendar.MONTH), start.get(Calendar.MONTH));
        assertEquals(localtime.get(Calendar.DATE), start.get(Calendar.DATE));
        assertEquals(11, start.get(Calendar.HOUR));
        assertEquals(23, start.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, start.get(Calendar.MINUTE));
        assertEquals(59, start.get(Calendar.SECOND));
        assertEquals(999, start.get(Calendar.MILLISECOND));
        assertEquals(DateHelper.UTC_TIME_ZONE, start.getTimeZone());
    }

    @Test
    public void testTodayUtc() {
        Calendar localTime = Calendar.getInstance(DateHelper.UTC_TIME_ZONE);
        Calendar cal = DateHelper.todayUtc();
        assertEquals(localTime.get(Calendar.YEAR), cal.get(Calendar.YEAR));
        assertEquals(localTime.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        assertEquals(localTime.get(Calendar.DATE), cal.get(Calendar.DATE));

        assertEquals(DateHelper.UTC_TIME_ZONE, cal.getTimeZone());
        assertEquals(0, cal.get(Calendar.MILLISECOND));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.HOUR));
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testTallyItemsPerDayOnePerDay() {
        Map<Calendar, Integer> itemsPerDay = new HashMap<Calendar, Integer>();

        Calendar timestamp = Calendar.getInstance();
        for (int i = 0; i < 10; i++) {
            timestamp.add(Calendar.DATE, i);
            DateHelper.tallyItemsPerDay(itemsPerDay, timestamp.getTime());
        }

        assertEquals(10, itemsPerDay.size()); // ten days
        for (int i : itemsPerDay.values()) {
            assertEquals(1, i); //one item per day
        }
    }

    @Test
    public void testTallyItemsPerDayTwoPerDay() {
        Map<Calendar, Integer> itemsPerDay = new HashMap<Calendar, Integer>();

        Calendar timestamp = Calendar.getInstance();
        for (int i = 0; i < 10; i++) {
            timestamp.add(Calendar.DATE, i);
            DateHelper.tallyItemsPerDay(itemsPerDay, timestamp.getTime());
            DateHelper.tallyItemsPerDay(itemsPerDay, timestamp.getTime());
        }

        assertEquals(10, itemsPerDay.size()); // ten days
        for (int i : itemsPerDay.values()) {
            assertEquals(2, i); //two items per day
        }
    }

    @Test
    public void testMillisToNextHour() {
        long now = System.currentTimeMillis();
        long millis = DateHelper.millisToNextHour();
        Calendar cal = Calendar.getInstance();
        assertEquals(now, System.currentTimeMillis());// only valid test if time calls all happened w/in same millisecond
        cal.setTimeInMillis(now + millis);
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(0, cal.get(Calendar.MILLISECOND));
    }

    @Test
    public void testDiffSameTime() {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = cal1;
        assertEquals(0, DateHelper.diffInMillis(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
    }

    @Test
    public void testDiffAndConversionsSameDate() {
        Calendar cal1 = new GregorianCalendar(1998, Calendar.DECEMBER, 25, 2, 2);
        Calendar cal2 = new GregorianCalendar(1998, Calendar.DECEMBER, 25, 3, 3);
        long diffMillis = DateHelper.diffInMillis(cal1.getTimeInMillis(), cal2.getTimeInMillis());

        assertEquals(3660000, diffMillis);
        assertEquals(3660, DateHelper.millisToSeconds(diffMillis));
        assertEquals(61, DateHelper.millisToMinutes(diffMillis));
        assertEquals(1, DateHelper.millisToHours(diffMillis));
        assertEquals(0, DateHelper.millisToDays(diffMillis));
    }

    @Test
    public void testDiffAndConversionsDifferentDate() {
        Calendar cal1 = new GregorianCalendar(1998, Calendar.DECEMBER, 25);
        Calendar cal2 = new GregorianCalendar(1999, Calendar.JANUARY, 1);
        long diffMillis = DateHelper.diffInMillis(cal1.getTimeInMillis(), cal2.getTimeInMillis());

        assertEquals(604800000L, diffMillis);
        assertEquals(604800L, DateHelper.millisToSeconds(diffMillis));
        assertEquals(10080, DateHelper.millisToMinutes(diffMillis));
        assertEquals(168, DateHelper.millisToHours(diffMillis));
        assertEquals(7, DateHelper.millisToDays(diffMillis));
    }
}
