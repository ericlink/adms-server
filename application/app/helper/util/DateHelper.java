package helper.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang.time.DateUtils;
import play.Play;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DateHelper {

    private DateHelper() {
    }
    public static long MILLIS_PER_SECOND = DateUtils.MILLIS_PER_SECOND;
    public static long MILLIS_PER_MINUTE = DateUtils.MILLIS_PER_MINUTE;
    public static long MILLIS_PER_HOUR = DateUtils.MILLIS_PER_HOUR;
    public static long MILLIS_PER_DAY = DateUtils.MILLIS_PER_DAY;
    public static TimeZone UTC_TIME_ZONE = DateUtils.UTC_TIME_ZONE;
    final private static String DATE_FORMAT_FULL_24_HOUR_MASK = Play.configuration.getProperty("DATE_FORMAT_FULL_24_HOUR_MASK");
    final private static String DATE_FORMAT_DOW_MON_YY_AM_PM_TZ_MASK = Play.configuration.getProperty("DATE_FORMAT_DOW_MON_YY_AM_PM_TZ_MASK");
    final private static String DATE_FORMAT_DOW_MM_DD_YYYY_MASK = Play.configuration.getProperty("DATE_FORMAT_DOW_MM_DD_YYYY_MASK");
    final private static String DATE_FORMAT_SHORT_MASK = Play.configuration.getProperty("DATE_FORMAT_SHORT_MASK");
    //fixed regardless of Locale, used for file names etc. on server
    final private static String DATE_FORMAT_YYYY_MM_DD_HH_MM_MASK = "yyyy-MM-dd-HH-mm"; //file name leave for support reasons

    public static DateFormat getDateFormatYyyyMmDdHhMm(TimeZone timeZone) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_MASK);
        dateFormat.setTimeZone(timeZone);
        return dateFormat;
    }

    public static DateFormat getDateFormatShort(TimeZone timeZone) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SHORT_MASK);
        dateFormat.setTimeZone(timeZone);
        return dateFormat;
    }

    public static DateFormat getDateFormatFull24Hour(TimeZone timeZone) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FULL_24_HOUR_MASK);
        dateFormat.setTimeZone(timeZone);
        return dateFormat;
    }

    public static DateFormat getDateFormatDayOfWeekDayOfMonthYearAmTz(TimeZone timeZone) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DOW_MON_YY_AM_PM_TZ_MASK);
        dateFormat.setTimeZone(timeZone);
        return dateFormat;
    }

    public static DateFormat getDateFormatDayOfWeekMmDdYyyy(TimeZone timeZone) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DOW_MM_DD_YYYY_MASK);
        dateFormat.setTimeZone(timeZone);
        return dateFormat;
    }

    /**
     * Set the default time zone when usint libraries that peform
     * new Date() calls or Calendar.getInstance() calls,
     * and the TimeZone is relevant for user local time calenar based calculartions,
     * or for date formatting
     **/
    public static void setDefaultTimeZone() {
        TimeZone.setDefault(UTC_TIME_ZONE);
    }

    /**
     * Take a local time calendar and represent it as a day
     * (truncate to day, retain yy/mm/dd from original date)
     **/
    public static Calendar asDay(Calendar localTimeCalendar) {
        Calendar asDay = Calendar.getInstance(localTimeCalendar.getTimeZone());
        asDay.clear();
        asDay.set(
            localTimeCalendar.get(Calendar.YEAR),
            localTimeCalendar.get(Calendar.MONTH),
            localTimeCalendar.get(Calendar.DATE),
            0,
            0,
            0);
        asDay.set(Calendar.AM_PM, Calendar.AM);
        asDay.set(Calendar.MILLISECOND, 0);
        asDay.set(Calendar.HOUR, 0);
        return asDay;
    }

    /**
     * Take a local time calendar and represent it as a UTC day
     * (truncate to day, retain yy/mm/dd from original date)
     **/
    public static Calendar asUtcDay(Calendar localTimeCalendar) {
        Calendar utcDate = Calendar.getInstance(DateUtils.UTC_TIME_ZONE);
        utcDate.clear();
        utcDate.set(
            localTimeCalendar.get(Calendar.YEAR),
            localTimeCalendar.get(Calendar.MONTH),
            localTimeCalendar.get(Calendar.DATE),
            0,
            0,
            0);
        utcDate.set(Calendar.AM_PM, Calendar.AM);
        utcDate.set(Calendar.MILLISECOND, 0);
        utcDate.set(Calendar.HOUR, 0);
        return utcDate;
    }

    public static Calendar asUtcDay(Date localTimeDate) {
        Calendar cal = Calendar.getInstance(DateUtils.UTC_TIME_ZONE);
        cal.setTime(localTimeDate);
        return asUtcDay(cal);
    }

    public static Calendar nowUtc() {
        return Calendar.getInstance(DateUtils.UTC_TIME_ZONE);
    }

    public static CalendarRange getRangeHoursBeforeNow(TimeZone tz, int hours) {
        Calendar start = Calendar.getInstance(tz);
        Calendar end = Calendar.getInstance(tz);
        start.setTimeInMillis(end.getTimeInMillis());
        start.add(Calendar.HOUR_OF_DAY, -hours);
        //fix for BST range issue on check (first dock issue). 
        // BST is not observed so jst roll forward here)
        // also allows for normal variations in timestamp (will still include last one if +n minutes)
        end.add(Calendar.HOUR_OF_DAY, 24);
        return new CalendarRange(start, end);
    }

    /**
     * @return a range starting at 23:59:59.999 PM to 12:00:00 AM today designed for
     * timestamp > start && timestamp < end
     * predicates.
     **/
    public static CalendarRange getRangeDaysBeforeTodayUtc(int days) {
        return calculateRangeBeforeEnd(todayUtc(), todayUtc(), days);
    }

    /**
     * @return a range starting at 23:59:59.999 PM to 12:00:00 AM today designed for
     * timestamp > start && timestamp < end
     * predicates.
     **/
    public static CalendarRange getRangeFullDaysBefore(Calendar localTime, int days) {
        return calculateRangeBeforeEnd(DateHelper.asUtcDay(localTime), DateHelper.asUtcDay(localTime), days);
    }

    /**
     * Returnes UTC calendar date range for queries against data points
     **/
    public static CalendarRange getRangeFullDaysBefore(Calendar localTime, int days, boolean includeToday) {
        CalendarRange calendarRange = calculateRangeBeforeEnd(DateHelper.asUtcDay(localTime), DateHelper.asUtcDay(localTime), days);
        if (includeToday) {
            // bump the range over by a day, to include today.
            // the total number of days in the range remains the same,
            // so the start day is moved as well
            calendarRange.getStart().add(Calendar.DAY_OF_MONTH, +1);
            calendarRange.getEnd().add(Calendar.DAY_OF_MONTH, +1);
        }
        return calendarRange;
    }

    /**
     * @param start date already set to full day boundary (midnight)
     * @param end   date already set to full day boundary (midnight)
     **/
    private static CalendarRange calculateRangeBeforeEnd(Calendar start, Calendar end, int days) {
        start.add(Calendar.DATE, -(days + 1));
        start.add(Calendar.MINUTE, -1);
        start.add(Calendar.SECOND, 59);
        start.add(Calendar.MILLISECOND, 999);
        return new CalendarRange(start, end);
    }

    /**
     * @return the days between two calendars, including dealing with year boundary
     **/
    public static int calculateIntervalInDays(CalendarRange range) {
        Calendar start = range.getStart();
        Calendar end = range.getEnd();
        int startYear = start.get(Calendar.YEAR);
        int endYear = end.get(Calendar.YEAR);
        if (startYear == endYear) {
            if (start.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR)) {
                return 0;
            }
            return end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        } else if (start.getTimeInMillis() < end.getTimeInMillis()) {
            // let the calendar do the thinking, just increment date until we hit our end date
            Calendar startCopy = (Calendar) start.clone();
            int days = 0;
            while (true) {
                startCopy.add(Calendar.DATE, 1);
                days++;
                if (startCopy.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR)) {
                    return days;
                }
            }
        } else {
            // let the calendar do the thinking, just increment date until we hit our end date
            Calendar startCopy = (Calendar) start.clone();
            int days = 0;
            while (true) {
                startCopy.add(Calendar.DATE, -1);
                days--;
                if (startCopy.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR)) {
                    return days;
                }
            }
        }
    }

    public static Calendar todayUtc() {
        return asUtcDay(nowUtc());
    }

    /**
     * Count items in day buckets
     **/
    public static void tallyItemsPerDay(Map<Calendar, Integer> itemsPerDay, Date date) {
        Calendar day = asUtcDay(date);
        if (itemsPerDay.containsKey(day)) {
            itemsPerDay.put(day, itemsPerDay.get(day) + 1);
        } else {
            itemsPerDay.put(day, 1);
        }
    }

    public static long millisToNextHour() {
        return DateUtils.MILLIS_PER_HOUR - (System.currentTimeMillis() % DateUtils.MILLIS_PER_HOUR);
    }

    /**
     * Diff is for instant, does not consider time zone
     **/
    public static long diffInMillis(long instantOne, long instantTwo) {
        return instantTwo - instantOne;
    }

    public static long millisToSeconds(long millis) {
        return millis / MILLIS_PER_SECOND;
    }

    public static long millisToMinutes(long millis) {
        return millis / MILLIS_PER_MINUTE;
    }

    public static long millisToHours(long millis) {
        return millis / MILLIS_PER_HOUR;
    }

    public static long millisToDays(long millis) {
        return millis / MILLIS_PER_DAY;
    }
}
