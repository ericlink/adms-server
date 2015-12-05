package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "schedule")
/**
 * Cron like schedule defaulting to 'wide open, run all the time',
 * used in the context of a user time zone.
 *
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @see http://java.sun.com/j2se/1.5.0/docs/api/java/util/Calendar.html
 **/
public class Schedule extends MyModel {

    private static Logger logger = Logger.getLogger(Schedule.class.getName());
    /**
     * http://java.sun.com/j2se/1.5.0/docs/api/constant-values.html#java.util.Calendar.JANUARY
     * java.util.Calendar
     * public static final int 	AM 	0
     * public static final int 	AM_PM 	9
     * public static final int 	APRIL 	3
     * public static final int 	AUGUST 	7
     * public static final int 	DATE 	5
     * public static final int 	DAY_OF_MONTH 	5
     * public static final int 	DAY_OF_WEEK 	7
     * public static final int 	DAY_OF_WEEK_IN_MONTH 	8
     * public static final int 	DAY_OF_YEAR 	6
     * public static final int 	DECEMBER 	11
     * public static final int 	DST_OFFSET 	16
     * public static final int 	ERA 	0
     * public static final int 	FEBRUARY 	1
     * public static final int 	FIELD_COUNT 	17
     * public static final int 	FRIDAY 	6
     * public static final int 	HOUR 	10
     * public static final int 	HOUR_OF_DAY 	11
     * public static final int 	JANUARY 	0
     * public static final int 	JULY 	6
     * public static final int 	JUNE 	5
     * public static final int 	MARCH 	2
     * public static final int 	MAY 	4
     * public static final int 	MILLISECOND 	14
     * public static final int 	MINUTE 	12
     * public static final int 	MONDAY 	2
     * public static final int 	MONTH 	2
     * public static final int 	NOVEMBER 	10
     * public static final int 	OCTOBER 	9
     * public static final int 	PM 	1
     * public static final int 	SATURDAY 	7
     * public static final int 	SECOND 	13
     * public static final int 	SEPTEMBER 	8
     * public static final int 	SUNDAY 	1
     * public static final int 	THURSDAY 	5
     * public static final int 	TUESDAY 	3
     * public static final int 	UNDECIMBER 	12
     * public static final int 	WEDNESDAY 	4
     * public static final int 	WEEK_OF_MONTH 	4
     * public static final int 	WEEK_OF_YEAR 	3
     * public static final int 	YEAR 	1
     * public static final int 	ZONE_OFFSET 	15
     ***/
    @Transient
    /**
     * Used to allow default schedules when there is no persisted schedule
     **/
    final public static Schedule DEFAULT_WIDE_OPEN_SCHEDULE = new Schedule();
    //////    From the java.util.Calendar doco:
    //////    MINUTE          0-59
    @Transient
    final private static int MINUTE_START = 0;
    @Transient
    final private static int MINUTE_END = 59;
    @Column(name = "startMinute", nullable = false)
    private int startMinute = MINUTE_START;
    @Column(name = "endMinute", nullable = false)
    private int endMinute = MINUTE_END;
    //////    HOUR_OF_DAY     is used for the 24-hour clock (0-23)
    @Transient
    final private static int HOUR_OF_DAY_START = 0;
    @Transient
    final private static int HOUR_OF_DAY_END = 23;
    @Column(name = "startHourOfDay", nullable = false)
    private int startHourOfDay = HOUR_OF_DAY_START;
    @Column(name = "endHourOfDay", nullable = false)
    private int endHourOfDay = HOUR_OF_DAY_END;
    //////    DAY_OF_MONTH    The first day of the month has value 1.
    @Transient
    final private static int DAY_OF_MONTH_START = 1;
    @Transient
    final private static int DAY_OF_MONTH_END = 31;
    @Column(name = "startDayOfMonth", nullable = false)
    private int startDayOfMonth = DAY_OF_MONTH_START;
    @Column(name = "endDayOfMonth", nullable = false)
    private int endDayOfMonth = DAY_OF_MONTH_END;
    //////    MONTH           The first month of the year is JANUARY which is 0
    @Transient
    final private static int MONTH_START = Calendar.JANUARY;
    @Transient
    final private static int MONTH_END = Calendar.DECEMBER;
    @Column(name = "startMonth", nullable = false)
    private int startMonth = MONTH_START;
    @Column(name = "endMonth", nullable = false)
    private int endMonth = MONTH_END;
    //////    DAY_OF_WEEK     SUNDAY == 1, SATURDAY == 7
    @Transient
    final private static int DAY_OF_WEEK_START = Calendar.SUNDAY;
    @Transient
    final private static int DAY_OF_WEEK_END = Calendar.SATURDAY;
    @Column(name = "startDayOfWeek", nullable = false)
    private int startDayOfWeek = DAY_OF_WEEK_START;
    @Column(name = "endDayOfWeek", nullable = false)
    private int endDayOfWeek = DAY_OF_WEEK_END;
    //////    YEAR            This is a calendar-specific value; see subclass documentation.
    @Transient
    final private static int YEAR_START = 0;
    @Transient
    final private static int YEAR_END = 9999;
    @Column(name = "startYear", nullable = false)
    private int startYear = YEAR_START;
    @Column(name = "endYear", nullable = false)
    private int endYear = YEAR_END;
    @Column(name = "name", nullable = false)
    private String name;

    public Schedule() {
    }

    private boolean isDefaultRange(int start, int end, int UNIT_START, int UNIT_END) {
        // default values, wide open
        return start == UNIT_START && end == UNIT_END;
    }

    private boolean isRunnable(int start, int end, int UNIT_START, int UNIT_END, int value) {
        // default values, wide open
        if (isDefaultRange(start, end, UNIT_START, UNIT_END)) {
            return true;
        }

        // not crossing the unit boundary, do the simple test
        if (end >= start) {
            return value >= start && value <= end;
        }

        // range crosses the unit boundary
        if (start >= end) {
            return // test before the boundary
                (value >= start && value <= UNIT_END)
                || // test after the boundary
                (value >= UNIT_START && value <= end);
        }

        // should never get here
        return false;
    }

    /**
     * @return next java timestamp when this may be run,
     * or 0 if it is not runnable in the next three months
     **/
    public long calculateNextRunnableTime(Calendar calendar) {

        if (isRunnable(calendar)) {
            return System.currentTimeMillis();
        } else {
            Calendar calendarCopy = Calendar.getInstance(calendar.getTimeZone());
            calendarCopy.setTimeInMillis(calendar.getTimeInMillis());
            Calendar calMaxInFutureToCalculate = Calendar.getInstance();
            calMaxInFutureToCalculate.add(Calendar.MONTH, 3);
            long maxInFutureToCalculateMillis = calMaxInFutureToCalculate.getTimeInMillis();

            while (!isRunnable(calendarCopy)) {
                calendarCopy.add(Calendar.MINUTE, 1);
                if (calendarCopy.getTimeInMillis() > maxInFutureToCalculateMillis) {
                    return 0; //1970
                }
            }

            return calendarCopy.getTimeInMillis();
        }

    }

    public boolean isRunnable(Calendar calendar) {
        return isRunnable(startMinute, endMinute, MINUTE_START, MINUTE_END, calendar.get(Calendar.MINUTE))
            && isRunnable(startHourOfDay, endHourOfDay, HOUR_OF_DAY_START, HOUR_OF_DAY_END, calendar.get(Calendar.HOUR_OF_DAY))
            && isRunnable(startDayOfMonth, endDayOfMonth, DAY_OF_MONTH_START, DAY_OF_MONTH_END, calendar.get(Calendar.DAY_OF_MONTH))
            && isRunnable(startDayOfWeek, endDayOfWeek, DAY_OF_WEEK_START, DAY_OF_WEEK_END, calendar.get(Calendar.DAY_OF_WEEK))
            && isRunnable(startYear, endYear, YEAR_START, YEAR_END, calendar.get(Calendar.YEAR));
    }

    /**
     * if calendar is runnable, check last run. if last run is runnable,
     * there is a possible dupe.  if all non-default runnable units are ==,
     * then this is a dupe
     *
     * false if last run cal == cal
     *
     * @return true if the schedule should run (considering the last run time)
     **/
    public boolean isRunnable(Calendar lastRunCalendar, Calendar calendar) {
        if (!isActive()) {
            // don't run inactive schedules
            return false;
        }

        if (lastRunCalendar != null && !lastRunCalendar.getTimeZone().equals(calendar.getTimeZone())) {
            throw new IllegalArgumentException(
                "Calendar time zones must be the same for comparison lastRunCalendar="
                + lastRunCalendar
                + ",calendar="
                + calendar);
        }

        if (lastRunCalendar != null && lastRunCalendar.equals(calendar)) {
            //same as last run time, so don't run again
            logger.log(Level.FINE, "lastRunCalendar.equals( calendar ), returning false");
            return false;
        }

        boolean calendarIsRunnable = isRunnable(calendar);
        if (!calendarIsRunnable) {
            // no need to check lastRun time, already not elible to run
            logger.log(Level.FINE, "!calendarIsRunnable, returning false");
            return false;
        }

        if (lastRunCalendar == null) {
            logger.log(Level.FINE, "lastRunCalendar is null, returning calendarIsRunnable={0}", calendarIsRunnable);
            return calendarIsRunnable;
        }

        // calendar is runnable, so see if last calendar applies
        boolean lastRunCalendarIsRunnable = isRunnable(lastRunCalendar);
        if (!lastRunCalendarIsRunnable) {
            // lastRunCalendar does not apply, ignore it
            logger.log(Level.FINE, "!lastRunCalendarIsRunnable, returning calendarIsRunnable={0}", calendarIsRunnable);
            return calendarIsRunnable;
        }

        // both calendars are runnable, so this could be a dupe run
        // if all non-default runnable units are ==, then this is a dupe run

        // 1 identify non-default
        List<Integer> nonDefaultUnits = identifyNonDefaultUnits();
        if (nonDefaultUnits.size() == 0) {
            // schedule is wide open, and calendars are not equal, so just use calendar
            logger.log(Level.FINE, "schedule is wide open, calendars not equal, returning calendarIsRunnable={0}", calendarIsRunnable);
            return calendarIsRunnable;
        }

        // 2 if something is set, and all set units are equal, then it's a dupe
        //   if they are not all equal, it's runnable
        calendarIsRunnable = !areAllSetRangesUnitsEqual(lastRunCalendar, calendar, nonDefaultUnits);
        logger.log(Level.FINE, "checking for dupe units, returning calendarIsRunnable={0}", calendarIsRunnable);

        return calendarIsRunnable;
    }

    private boolean areAllSetRangesUnitsEqual(final Calendar lastRunCalendar, final Calendar calendar, final List<Integer> nonDefaultUnits) {
        boolean eq = true;

        for (int unitType : nonDefaultUnits) {
            switch (unitType) {
                case Calendar.MINUTE:
                    eq = (lastRunCalendar.get(Calendar.MINUTE) == calendar.get(Calendar.MINUTE)
                        && lastRunCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY)
                        && lastRunCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
                        && lastRunCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
                    break;
                case Calendar.HOUR_OF_DAY:
                    eq = (lastRunCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY)
                        && lastRunCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
                        && lastRunCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
                    break;
                case Calendar.DAY_OF_MONTH:
                    eq = (lastRunCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
                        && lastRunCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
                        && lastRunCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
                    break;
                case Calendar.DAY_OF_WEEK:
                    eq = (lastRunCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK)
                        && lastRunCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
                        && lastRunCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
                    break;
                case Calendar.YEAR:
                    eq = lastRunCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                    break;
            }

            if (!eq) {
                // any one unit check is different, so not all equal
                return eq;
            }
        }

        return eq; // they were _all_ equal
    }

    private List<Integer> identifyNonDefaultUnits() {
        List<Integer> nonDefaultUnits = new ArrayList<Integer>();

        if (!isDefaultRange(startMinute, endMinute, MINUTE_START, MINUTE_END)) {
            nonDefaultUnits.add(Calendar.MINUTE);
        }
        if (!isDefaultRange(startHourOfDay, endHourOfDay, HOUR_OF_DAY_START, HOUR_OF_DAY_END)) {
            nonDefaultUnits.add(Calendar.HOUR_OF_DAY);
        }
        if (!isDefaultRange(startDayOfMonth, endDayOfMonth, DAY_OF_MONTH_START, DAY_OF_MONTH_END)) {
            nonDefaultUnits.add(Calendar.DAY_OF_MONTH);
        }
        if (!isDefaultRange(startDayOfWeek, endDayOfWeek, DAY_OF_WEEK_START, DAY_OF_WEEK_END)) {
            nonDefaultUnits.add(Calendar.DAY_OF_WEEK);
        }
        if (!isDefaultRange(startYear, endYear, YEAR_START, YEAR_END)) {
            nonDefaultUnits.add(Calendar.YEAR);
        }
        return nonDefaultUnits;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Schedule == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Schedule rhs = (Schedule) obj;
        return new EqualsBuilder().append(startDayOfMonth, rhs.startDayOfMonth).append(startDayOfWeek, rhs.startDayOfWeek).append(startHourOfDay, rhs.startHourOfDay).append(startMinute, rhs.startMinute).append(startMonth, rhs.startMonth).append(startYear, rhs.startYear).append(endDayOfMonth, rhs.endDayOfMonth).append(endDayOfWeek, rhs.endDayOfWeek).append(endHourOfDay, rhs.endHourOfDay).append(endMinute, rhs.endMinute).append(endMonth, rhs.endMonth).append(endYear, rhs.endYear).append(name, rhs.name).isEquals();
    }

    public int compareTo(Object o) {
        Schedule rhs = (Schedule) o;
        return new CompareToBuilder().append(startDayOfMonth, rhs.startDayOfMonth).append(startDayOfWeek, rhs.startDayOfWeek).append(startHourOfDay, rhs.startHourOfDay).append(startMinute, rhs.startMinute).append(startMonth, rhs.startMonth).append(startYear, rhs.startYear).append(endDayOfMonth, rhs.endDayOfMonth).append(endDayOfWeek, rhs.endDayOfWeek).append(endHourOfDay, rhs.endHourOfDay).append(endMinute, rhs.endMinute).append(endMonth, rhs.endMonth).append(endYear, rhs.endYear).append(name, rhs.name).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(startDayOfMonth).append(startDayOfWeek).append(startHourOfDay).append(startMinute).append(startMonth).append(startYear).append(endDayOfMonth).append(endDayOfWeek).append(endHourOfDay).append(endMinute).append(endMonth).append(endYear).append(name).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("startYear", startYear).append("endYear", endYear).append("startMonth", startMonth).append("endMonth", endMonth).append("startDayOfMonth", startDayOfMonth).append("endDayOfMonth", endDayOfMonth).append("startDayOfWeek", startDayOfWeek).append("endDayOfWeek", endDayOfWeek).append("startHourOfDay", startHourOfDay).append("endHourOfDay", endHourOfDay).append("startMinute", startMinute).append("endMinute", endMinute).append("name", name).toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setMinuteRange(int startMinute, int endMinute) {
        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public int getStartHourOfDay() {
        return startHourOfDay;
    }

    public int getEndHourOfDay() {
        return endHourOfDay;
    }

    public void setHourOfDayRange(int startHourOfDay, int endHourOfDay) {
        this.startHourOfDay = startHourOfDay;
        this.endHourOfDay = endHourOfDay;
    }

    public int getStartDayOfMonth() {
        return startDayOfMonth;
    }

    public int getEndDayOfMonth() {
        return endDayOfMonth;
    }

    public void setDayOfMonthRange(int startDayOfMonth, int endDayOfMonth) {
        this.startDayOfMonth = startDayOfMonth;
        this.endDayOfMonth = endDayOfMonth;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setMonthRange(int startMonth, int endMonth) {
        this.startMonth = startMonth;
        this.endMonth = endMonth;
    }

    public int getStartDayOfWeek() {
        return startDayOfWeek;
    }

    public int getEndDayOfWeek() {
        return endDayOfWeek;
    }

    public void setDayOfWeekRange(int startDayOfWeek, int endDayOfWeek) {
        this.startDayOfWeek = startDayOfWeek;
        this.endDayOfWeek = endDayOfWeek;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setYearRange(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
