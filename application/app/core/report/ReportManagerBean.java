package core.report;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import helper.activation.ByteArrayDataSource;
import core.datapoint.DataPoint;
import models.User;
import helper.util.CalendarRange;
import helper.util.DateHelper;
import core.report.dayoverday.DayOverDayDataSource;
import helper.util.RegexHelper;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import play.Play;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class ReportManagerBean implements core.report.ReportManager {

    private static final Logger logger = Logger.getLogger(ReportManagerBean.class.getName());
    private static Map<ReportOutputType, String> mimeTypes = new HashMap<ReportOutputType, String>();
    private static Map<ReportOutputType, String> fileExtensions = new HashMap<ReportOutputType, String>();

    static {
        mimeTypes.put(ReportOutputType.PDF, "application/pdf");
//        mimeTypes.put( ReportOutputType.XLS,  "application/pdf" );
//        mimeTypes.put( ReportOutputType.HTML, "application/pdf" );
//        mimeTypes.put( ReportOutputType.CSV,  "application/pdf" );
    }

    static {
        fileExtensions.put(ReportOutputType.PDF, "pdf");
//        fileExtensions.put( ReportOutputType.XLS,  "xls" );
//        fileExtensions.put( ReportOutputType.HTML, "html" );
//        fileExtensions.put( ReportOutputType.CSV,  "csv" );
    }

    public ByteArrayDataSource getDayOverDayReport(ReportOutputType outputType, User user) {
        DateFormat full24hourUtcDateFormat = DateHelper.getDateFormatFull24Hour(DateHelper.UTC_TIME_ZONE);
        try {
            logger.fine("Running dayoverday report");

            Calendar reportDate = Calendar.getInstance(user.getTimeZone());
            CalendarRange calendarRange = DateHelper.getRangeFullDaysBefore(reportDate, 21, true);
            logger.log(Level.INFO, "Running report for user {0}:[{1}], reportDate {2}, data range {3}-{4}", new Object[]{
                    user.getId(),
                    user.getHandle(),
                    DateHelper.getDateFormatFull24Hour(reportDate.getTimeZone()).
                    format(reportDate.getTime()),
                    full24hourUtcDateFormat.format(calendarRange.getStart().
                    getTime()),
                    full24hourUtcDateFormat.format(calendarRange.getEnd().
                    getTime())
                });

            logger.fine("Querying data");
            Collection<DataPoint> dataPoints = DataPoint.findDataPoints(calendarRange, user);
//            if (dataPoints.size() > 0) {
            logger.fine("Filling data source");
            long start = System.currentTimeMillis();
            JRDataSource dataSource = new DayOverDayDataSource(user, reportDate, dataPoints);
            logger.log(Level.FINE, "Filled data source ({0} millis)", System.currentTimeMillis() - start);

            logger.fine("Running report to pdf");
            start = System.currentTimeMillis();

            byte[] pdf =
                JasperRunManager.runReportToPdf(
                Play.configuration.getProperty("jasper.dayoverday"),
                null,
                dataSource);
            logger.log(Level.FINE, "Done running report to pdf ({0} millis)", System.currentTimeMillis() - start);

            return new ByteArrayDataSource(pdf, getMimeType(outputType), getName(outputType, user, reportDate));
//            } else {
//                logger.log(Level.INFO,
//                        "No data points returned, skipping report generation for user {0}:[{1}]", new Object[]{
//                            user.getId(),
//                            user.getHandle()
//                        });
//                return null;
//            }
        } catch (JRException ex) {
            throw new ReportException(ex);
        }
    }

    /**
     *@see http://www.iana.org/assignments/media-types/application/
     *@see http://www.rfc-editor.org/rfc/rfc3778.txt
     ***/
    private String getMimeType(ReportOutputType outputType) {
        return mimeTypes.get(outputType);
    }

    private String getName(ReportOutputType outputType, User user, Calendar reportDate) {
        return MessageFormat.format(
            "{0}-{1}.{2}", new Object[]{
                RegexHelper.replaceNonWordWithUnderScore(user.getHandle()),
                DateHelper.getDateFormatYyyyMmDdHhMm(reportDate.getTimeZone()).
                format(reportDate.getTime()),
                fileExtensions.get(outputType)
            });
    }
//
//    public ByteArrayDataSource getDayOverDayReport(ReportOutputType outputType, int userId) {
//        logger.log(Level.INFO, "getDayOverDayReport({0}, {1})", new Object[]{outputType, userId});
//        User user = entityDao.findUserById(userId);
//        return getDayOverDayReport(outputType, user);
//    }
//    public void doOneOff() {
//        logger.log(Level.INFO, "entering doOneOff");
//        DateFormat full24hourUtcDateFormat = DateHelper.getDateFormatFull24Hour(DateHelper.UTC_TIME_ZONE);
//
//        //BICT-WSW004  8-13-07 through 9-6-07
//        //       'BICT-WSW004', 321
//        //       User user = entityDao.findUserById(321);
//        //BICT-ARC003  8-1-07 through 8-17-07
//        //      'BICT-ARC003', 330
//        User user = entityDao.findUserById(321);
//
//        Calendar reportDate = Calendar.getInstance(user.getTimeZone());
//        reportDate.set(Calendar.YEAR, 2007);
//        reportDate.set(Calendar.MONTH, 7);
//        reportDate.set(Calendar.DAY_OF_MONTH, 23);
//
//        try {
//            logger.fine("Getting compiled report definition");
//            InputStream is = this.getClass().getResourceAsStream("DayOverDayReport.jasper");
//            if (is == null) {
//                throw new IllegalStateException("Could not load report definition");
//            }
//
//            CalendarRange calendarRange = DateHelper.getRangeFullDaysBefore(reportDate, 21, true);
//            logger.log(Level.INFO, "Running report for user {0}:[{1}], reportDate {2}, data range {3}-{4}", new Object[]{
//                        user.getId(),
//                        user.getHandle(),
//                        DateHelper.getDateFormatFull24Hour(reportDate.getTimeZone()).format(reportDate.getTime()),
//                        full24hourUtcDateFormat.format(calendarRange.getStart().getTime()),
//                        full24hourUtcDateFormat.format(calendarRange.getEnd().getTime())
//                    });
//
//            logger.fine("Querying data");
//            Collection<DataPoint> dataPoints = entityDao.findDataPoints(calendarRange, user);
//            if (dataPoints.size() > 0) {
//                logger.fine("Filling data source");
//                long start = System.currentTimeMillis();
//                JRDataSource dataSource = new DayOverDayDataSource(user.getHandle(), reportDate, dataPoints);
//                logger.log(Level.FINE, "Filled data source ({0} millis)", System.currentTimeMillis() - start);
//
//                logger.fine("Running report to pdf");
//                start = System.currentTimeMillis();
//                byte[] pdf = JasperRunManager.runReportToPdf(is, null, dataSource);
//                logger.log(Level.FINE, "Done running report to pdf ({0} millis)", System.currentTimeMillis() - start);
//                net.diabetech.io.FileLogHelper.createInstance().log("/home/elink/Desktop", user.getHandle() + ".pdf", pdf);
//                //return new ByteArrayDataSource(pdf,getMimeType(outputType),getName(outputType,user,reportDate));
//            } else {
//                logger.log(Level.INFO,
//                        "No data points returned, skipping report generation for user {0}:[{1}]", new Object[]{
//                            user.getId(),
//                            user.getHandle()
//                        });
//                //return null;
//            }
//        } catch (Exception ex) {
//            throw new ReportException(ex);
//        }
//
//    }
}
