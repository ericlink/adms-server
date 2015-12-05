package core.report.dayoverday;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import core.datapoint.DataPoint;
import core.type.UnitOfMeasureType;
import models.User;
import helper.util.DateHelper;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006, 2010, 2011 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DayOverDayDataSource implements JRDataSource {

    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String NONE = "none";
    public static final String DEFAULT = "";
    private static final Logger logger = Logger.getLogger(DayOverDayDataSource.class.getName());

    public DayOverDayDataSource(User user, Calendar reportDate, Collection<DataPoint> dataPoints) {
        if (dataPoints == null) {
            dataPoints = new ArrayList<DataPoint>();
        }
        this.user = user;
        this.reportDate = reportDate;
        logger.log(Level.FINE, "Report date with formatter: [{0}]", getField("reportDate"));
        setTimeStampLabels();
        fillDataPointMatrix(dataPoints);
    }

    private void fillDataPointMatrix(Collection<DataPoint> dataPoints) {
        for (DataPoint dataPoint : dataPoints) {
            Calendar timeStamp = DateHelper.nowUtc();
            timeStamp.setTime(dataPoint.getTimestamp());
            logger.log(
                Level.INFO,
                "copied utc timeStamp={0}",
                new Object[]{
                    timeStamp.getTime()
                });
            setBg(dataPoint);
        }
    }

    private void setDataRangeIndicatorForSingleValue(
        final DataPoint dataPoint,
        final int dayOffset,
        final int timeStampHourOffset) throws RuntimeException {
        //todo this could all move into dataPoint if range is by user; if by feature then still need more 
        final int HIGH_THRESHOLD_MGDL = 180;
        final int LOW_THRESHOLD_MGDL = 80;
        final double HIGH_THRESHOLD_MMOL = (double) HIGH_THRESHOLD_MGDL / (double) 18;
        final double LOW_THRESHOLD_MMOL = (double) LOW_THRESHOLD_MGDL / (double) 18;
        final DecimalFormat formatter = new DecimalFormat("#0.0");
        if (user.getPreferredUnitOfMeasure().
            equals(UnitOfMeasureType.MMOL_L)) {
            // UOM   
            // must consider rounding in UOM conversions and apply concept to dislay
            // value, e.g. displayed unit of meausre
            // display UOM value, UOM value to compare to, include rounding (formatter)
            // be sure to promote all calculations to floating point also
            double mmolValue = dataPoint.getValueMmol();
            if (mmolValue > HIGH_THRESHOLD_MMOL) {
                dataRangeIndicator[dayOffset][timeStampHourOffset] = HIGH;
            } else if (mmolValue < LOW_THRESHOLD_MMOL) {
                dataRangeIndicator[dayOffset][timeStampHourOffset] = LOW;
            }
        } else if (user.getPreferredUnitOfMeasure().
            equals(UnitOfMeasureType.MG_DL)) {
            if (dataPoint.getValue() > HIGH_THRESHOLD_MGDL) {
                dataRangeIndicator[dayOffset][timeStampHourOffset] = HIGH;
            } else if (dataPoint.getValue() < LOW_THRESHOLD_MGDL) {
                dataRangeIndicator[dayOffset][timeStampHourOffset] = LOW;
            }
        } else {
            throw new RuntimeException("Unknown unit of measure");
        }
    }

    private void setTimeStampLabels() {
        Calendar cal = (Calendar) reportDate.clone();
        DateFormat dateFormat = DateHelper.getDateFormatDayOfWeekMmDdYyyy(cal.getTimeZone());
        for (int i = 0; i < data.length; i++) {
            data[i][0] = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
    }

    /**
     * @param timeStamp the date for the reading
     * @param value the bg mg/dl value for the reading
     **/
    private void setBg(DataPoint dataPoint) {
        logger.log(
            Level.INFO,
            "dataPoint timeStamp={0}",
            new Object[]{
                dataPoint.getTimestamp()
            });
        // figure out the timestamp slot based on startDate offset
        // compare utc to utc, to create offsets to fill the matrix.
        // offset calculation is all utc based, since data points are in utc
        // initialize utc report date w/ report date calendar dates

        // report date is just used to calculate the day bucket offsets.
        // so get a utc version of it, at a day boundary, but as utc for comparison
        // to utc data points
        Calendar reportDateUtcCal = DateHelper.asUtcDay(reportDate);
        int reportDateDayOfYear = reportDateUtcCal.get(Calendar.DAY_OF_YEAR);

        // get the timestamp variables for the calculations
        Calendar timeStampUtcCal = DateHelper.nowUtc();
        timeStampUtcCal.setTime(dataPoint.getTimestamp());

        // figure out the day offset, considering the end of year boundary
        int timeStampDayOfYear = timeStampUtcCal.get(Calendar.DAY_OF_YEAR);
        int dayOffset = reportDateDayOfYear - timeStampDayOfYear;
        if (dayOffset < 0) {
            // day was from prior year
            // last years days plus this years days is offset in days
            // use getActualMaximum day of year this accounts for leap year  
            int maxDaysInLastYear = timeStampUtcCal.getActualMaximum(Calendar.DAY_OF_YEAR);
            int timeStampDaysInLastYear = maxDaysInLastYear - timeStampDayOfYear;
            dayOffset = timeStampDaysInLastYear + reportDateDayOfYear;
        }

        // figure out the hour offset
        // add one to the offset skip timestamp column in the data matrix
        int timeStampHourOfDay = timeStampUtcCal.get(Calendar.HOUR_OF_DAY);
        int timeStampHourOffset = timeStampHourOfDay + 1;

        // check array bounds for safety in case date shift is a problem
        // offset < 0 trims all future timestamps
        if (dayOffset < 0 || dayOffset > data.length - 1 || timeStampHourOffset > data[0].length - 1) {
            return;
        }

        logger.log(
            Level.INFO,
            "Calling populateMatrixCell: timeStampHourOfDay={0},timeStampDayOfYear={1},reportDateDayOfYear={2},offset={3},timeStampOffset={4},cell=({3},{4}),timeStamp={5}",
            new Object[]{
                timeStampHourOfDay,
                timeStampDayOfYear,
                reportDateDayOfYear,
                dayOffset,
                timeStampHourOffset,
                dataPoint.getTimestamp()
            });
        populateMatrixCell(dayOffset, timeStampHourOffset, dataPoint);
    }

    private void populateMatrixCell(final int dayOffset, final int timeStampHourOffset, final DataPoint dataPoint) {
        // put the value in the correct hour for this timestamp row
        String currentCellValue = (String) data[dayOffset][timeStampHourOffset];
        if (currentCellValue.length() > 0) {
            // data exists, so append
            data[dayOffset][timeStampHourOffset] = currentCellValue + "\n" + dataPoint.getFormattedValue();
            dataRangeIndicator[dayOffset][timeStampHourOffset] = NONE;
        } else {
            data[dayOffset][timeStampHourOffset] = dataPoint.getFormattedValue();
            setDataRangeIndicatorForSingleValue(dataPoint, dayOffset, timeStampHourOffset);
        }
    }

    public boolean next() throws JRException {
        return (++index < data.length);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        return getField(field.getName());
    }

    public Object getField(String fieldName) {
        if ("timeStamp".equals(fieldName)) {
            return data[index][0];
        } else if ("reportDate".equals(fieldName)) {
            //return reportDate;
            DateFormat dateFormat = DateHelper.getDateFormatDayOfWeekDayOfMonthYearAmTz(reportDate.getTimeZone());
            String formattedReportDate = dateFormat.format(reportDate.getTime());
            return formattedReportDate;
        } else if ("handle".equals(fieldName)) {
            return user.getHandle();
        } else if (fieldName.startsWith("bg")) {
            int bgIndex = Integer.parseInt(fieldName.substring(2));
            // increment to skip timestamp column
            return data[index][++bgIndex];
        } else if (fieldName.startsWith("rangeIndicator")) {
            int bgIndex = Integer.parseInt(fieldName.substring(14));
            // increment to skip timestamp column
            return dataRangeIndicator[index][++bgIndex];
        }

        return null;
    }
    /**
     * The timestamp (col 0) is set in the constructor.  Each value field is intialized to empty string here.
     **/
    private Object[][] data = {
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
    };
    /**
     * The timestamp (col 0) is set in the constructor.
     * If value is evaluated as 'normal', value will be ""
     * If value is evaluated as 'high', value will be "high"
     * If value is evaluated as 'low', value will be "low"
     *
     **/
    private Object[][] dataRangeIndicator = {
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
        {null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
    };
    private int index = -1;
    private Calendar reportDate;
    private User user;
}
