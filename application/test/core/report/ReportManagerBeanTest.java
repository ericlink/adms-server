package core.report;

import play.test.*;
import org.junit.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import java.util.Calendar;
import java.util.Collection;
import core.datapoint.DataPoint;
import models.User;
import models.UserType;
import helper.util.DateHelper;
import core.report.dayoverday.DayOverDayDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import play.Play;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006, 2011 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class ReportManagerBeanTest extends UnitTest {

    private static String BASE_PATH = "../../application";
    private static String REPORT_SOURCE_DIR = BASE_PATH + "/app/core/report";
    private static String REPORT_JASPER_OUTPUT_PATH = BASE_PATH + "/app/core/report";
    private static String BUILD_TEST_RESULTS_DIR = BASE_PATH + "/test-result";

    public void addDataPoint(Collection<DataPoint> dataPoints, User user, int value, Calendar timeStampDate, String time) {
        DataPoint dp = new DataPoint();
        StringTokenizer st = new StringTokenizer(time, ":");
        dp.setValue(value);
        dp.setUser(user);
        timeStampDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st.nextToken()));
        timeStampDate.set(Calendar.MINUTE, Integer.parseInt(st.nextToken()));
        timeStampDate.set(Calendar.SECOND, Integer.parseInt(st.nextToken()));
        dp.setTimestamp(timeStampDate.getTime());
        dataPoints.add(dp);
    }

    public void runDayOverDay(String name, Calendar reportDate, Calendar firstDataPointDate) throws Exception {
        Calendar now = firstDataPointDate;
        Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
        User user = new User();
        user.setHandle("Test Handle For User First DP " + now.getTime().toString());
        ///
        for (int i = 0; i < 3; i++) {
            addDataPoint(dataPoints, user, 161, now, "09:10:48");
            addDataPoint(dataPoints, user, 79, now, "18:10:48");
            addDataPoint(dataPoints, user, 80, now, "19:10:48");
            addDataPoint(dataPoints, user, 180, now, "20:10:48");
            addDataPoint(dataPoints, user, 181, now, "21:10:48");
            now.add(Calendar.DATE, -1);
            addDataPoint(dataPoints, user, 196, now, "19:16:48");
            now.add(Calendar.DATE, -1);
            addDataPoint(dataPoints, user, 261, now, "10:10:48");
            addDataPoint(dataPoints, user, 179, now, "19:10:48");
            addDataPoint(dataPoints, user, 180, now, "20:10:48");
            addDataPoint(dataPoints, user, 280, now, "21:10:48");
            addDataPoint(dataPoints, user, 281, now, "22:10:48");
            now.add(Calendar.DATE, -1);
            addDataPoint(dataPoints, user, 189, now, "09:12:08");
            addDataPoint(dataPoints, user, 140, now, "21:07:16");
            now.add(Calendar.DATE, -1);
            addDataPoint(dataPoints, user, 156, now, "09:11:16");
            addDataPoint(dataPoints, user, 231, now, "12:47:08");
            now.add(Calendar.DATE, -1);
            addDataPoint(dataPoints, user, 191, now, "09:06:08");
            addDataPoint(dataPoints, user, 229, now, "13:29:40");
            addDataPoint(dataPoints, user, 217, now, "18:07:40");
            now.add(Calendar.DATE, -1);
            addDataPoint(dataPoints, user, 242, now, "08:46:32");
            addDataPoint(dataPoints, user, 241, now, "13:01:16");
            addDataPoint(dataPoints, user, 298, now, "18:16:40");
            now.add(Calendar.DATE, -1);
            addDataPoint(dataPoints, user, 12, now, "08:51:32");
            addDataPoint(dataPoints, user, 123, now, "08:57:08");
            addDataPoint(dataPoints, user, 227, now, "09:01:40");
            addDataPoint(dataPoints, user, 277, now, "12:22:00");
            addDataPoint(dataPoints, user, 97, now, "18:43:00");
            now.add(Calendar.DATE, -1);
        }

        JRDataSource dataSource = new DayOverDayDataSource(user, reportDate, dataPoints);
        assertNotNull(dataSource);
        byte[] pdf =
            JasperRunManager.runReportToPdf(
            Play.configuration.getProperty("jasper.dayoverday"),
            null,
            dataSource);

        writeTestResults("testGetDayOverDayReport." + name + ".pdf", pdf);

    }

    @Test
    public void testDayOverDayWithLeapYearAfter() throws Exception {
        Calendar reportDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        reportDate.set(Calendar.YEAR, 2012);
        reportDate.set(Calendar.MONTH, Calendar.JANUARY);
        reportDate.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(2012, reportDate.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, reportDate.get(Calendar.MONTH));
        assertEquals(1, reportDate.get(Calendar.DAY_OF_MONTH));

        assertEquals(366, reportDate.getActualMaximum(Calendar.DAY_OF_YEAR));


        Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
        Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
        now.set(Calendar.YEAR, 2011);
        now.set(Calendar.MONTH, Calendar.DECEMBER);
        now.set(Calendar.DAY_OF_MONTH, 31);
        User user = new User();
        user.setHandle("Test Handle For User DP 1 is Dec 31 2011");
        ///
        runDayOverDay("LeapYearAfter", reportDate, now);
    }

    @Test
    public void testDayOverDayWithLeapYearBefore() throws Exception {
        Calendar reportDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        reportDate.set(Calendar.YEAR, 2013);
        reportDate.set(Calendar.MONTH, Calendar.JANUARY);
        reportDate.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(2013, reportDate.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, reportDate.get(Calendar.MONTH));
        assertEquals(1, reportDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(365, reportDate.getActualMaximum(Calendar.DAY_OF_YEAR));


        Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
        Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
        now.set(Calendar.YEAR, 2012);
        now.set(Calendar.MONTH, Calendar.DECEMBER);
        now.set(Calendar.DAY_OF_MONTH, 31);
        assertEquals(366, now.get(Calendar.DAY_OF_YEAR));
        User user = new User();
        user.setHandle("Test Handle For User DP 1 is Dec 31 2012");
        runDayOverDay("LeapYearBefore", reportDate, now);
    }

    @Test
    public void testDayOverDayWithLeapYearAfter2() throws Exception {
        Calendar reportDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        reportDate.set(Calendar.YEAR, 2012);
        reportDate.set(Calendar.MONTH, Calendar.JANUARY);
        reportDate.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(2012, reportDate.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, reportDate.get(Calendar.MONTH));
        assertEquals(1, reportDate.get(Calendar.DAY_OF_MONTH));

        assertEquals(366, reportDate.getActualMaximum(Calendar.DAY_OF_YEAR));


        Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
        Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
        now.set(Calendar.YEAR, 2012);
        now.set(Calendar.MONTH, Calendar.JANUARY);
        now.set(Calendar.DAY_OF_MONTH, 1);
        User user = new User();
        user.setHandle("Test Handle For User DP 1 is Jan 1 2012");
        runDayOverDay("LeapYearAfter2", reportDate, now);

    }

    @Test
    public void testDayOverDayWithLeapYearBefore2() throws Exception {
        Calendar reportDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        reportDate.set(Calendar.YEAR, 2013);
        reportDate.set(Calendar.MONTH, Calendar.JANUARY);
        reportDate.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(2013, reportDate.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, reportDate.get(Calendar.MONTH));
        assertEquals(1, reportDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(365, reportDate.getActualMaximum(Calendar.DAY_OF_YEAR));


        Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
        Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
        now.set(Calendar.YEAR, 2013);
        now.set(Calendar.MONTH, Calendar.JANUARY);
        now.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(1, now.get(Calendar.DAY_OF_YEAR));
        User user = new User();
        user.setHandle("Test Handle For User DP 1 is Jan 1 2013");
        runDayOverDay("LeapYearBefore2", reportDate, now);
    }

    @Test
    public void testDayOverDayReport() throws Exception {
        Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
        Calendar reportDate = DateHelper.asUtcDay(DateHelper.nowUtc());
        Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
        User user = new User();
        user.setHandle("Test Handle For User");
        runDayOverDay("Basic", reportDate, now);
    }

    @Test
    public void testGetDayOverDayReport() throws Exception {
        // TODO create NULL implementation for subclassing by unit tests. keep in dao test packages
//        EntityDAO entityDao = new EntityDAO() {
//            public void create(User user) {
//            }
//
//            public void create(Module module) {
//            }
//
//            public MedicalDevice findMedicalDeviceBySerialNumber(String serialNumber) {
//                return null;
//            }
//
//            public Module findModuleByPin(String pin) {
//                return null;
//            }
//
//            public User findUserById(int id) {
//                User user = new User();
//                user.setId(999);
//                user.setHandle("testGetDayOverDayReport handle");
//                user.setTimeZone(TimeZone.getTimeZone("PST"));
//                return user;
//            }
//
//            public Collection<DataPoint> findDataPointsXXX(CalendarRange calendarRange, User user) {
//                Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
//                Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
//                now.add(Calendar.HOUR_OF_DAY, -1); //up to midnight of tomorrow (includes today)
//                now.add(Calendar.DATE, 1); //up to midnight of tomorrow (includes today)
//
//                // series of daos that return data
//                // per date range, per hard coded, etc.
//                //now.add(Calendar.DATE, -7); //one week old datapoints, for one week
//                //for( int i = 0; i < 7; i++ ) { // days //21
//
//                //now.add(Calendar.DATE, -7); //one week old datapoints, for two weeks
//                //for( int i = 0; i < 14; i++ ) { // days //21
//
//                //for( int i = 0; i < 20; i++ ) {
//
//                for( int i = 0; i < 21; i++ ) {
//                    int value = 30;
//                    for ( int h = 0; h < 24; h++ ) { //hours per day
//                        DataPoint dp = new DataPoint();
//                        dp.setTimestamp(now.getTime());
//                        dp.setValue(value + (h*10) );
//                        dataPoints.add(dp);
//                        if ( h % 3 == 0 ) {
//                            // 2x per hour every 3 hours
//                            now.add(Calendar.MINUTE,2);
//                            DataPoint dp2 = new DataPoint();
//                            dp2.setTimestamp(now.getTime());
//                            dp2.setValue(99);
//                            dataPoints.add(dp2);
//                            now.add(Calendar.MINUTE,-2);
//                        }
//                        now.add(Calendar.HOUR_OF_DAY,-1);
//                    }
//                }
//                return dataPoints;
//            }
//
//            public void addDataPoint(Collection<DataPoint>  dataPoints,int value,Calendar timeStampDate,String time) {
//                DataPoint dp = new DataPoint();
//                StringTokenizer st = new StringTokenizer(time,":");
//                dp.setValue(value);
//                timeStampDate.set(Calendar.HOUR_OF_DAY,   Integer.parseInt(st.nextToken()));
//                timeStampDate.set(Calendar.MINUTE,        Integer.parseInt(st.nextToken()));
//                timeStampDate.set(Calendar.SECOND,        Integer.parseInt(st.nextToken()));
//                dp.setTimestamp(timeStampDate.getTime());
//                dataPoints.add(dp);
//            }
//
//            public Collection<DataPoint> findDataPoints(CalendarRange calendarRange, User user) {
//                Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
//                Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
//                now.add(Calendar.HOUR_OF_DAY, -1); //up to midnight of tomorrow (includes today)
//                now.add(Calendar.DATE, 1); //up to midnight of tomorrow (includes today)
//                addDataPoint(dataPoints,161,now,"09:10:48");
//                addDataPoint(dataPoints, 79,now,"18:10:48");
//                addDataPoint(dataPoints, 80,now,"19:10:48");
//                addDataPoint(dataPoints,180,now,"20:10:48");
//                addDataPoint(dataPoints,181,now,"21:10:48");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,196,now,"19:16:48");
//                now.add(Calendar.DATE,-1);
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,189,now, "09:12:08");
//                addDataPoint(dataPoints,140,now, "21:07:16");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,156,now, "09:11:16");
//                addDataPoint(dataPoints,231,now, "12:47:08");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,191,now, "09:06:08");
//                addDataPoint(dataPoints,229,now, "13:29:40");
//                addDataPoint(dataPoints,217,now, "18:07:40");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,242,now, "08:46:32");
//                addDataPoint(dataPoints,241,now, "13:01:16");
//                addDataPoint(dataPoints,298,now, "18:16:40");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,12,now, "08:51:32");
//                addDataPoint(dataPoints,123,now, "08:57:08");
//                addDataPoint(dataPoints,217,now, "09:01:40");
//                addDataPoint(dataPoints,277,now, "12:22:00");
//                addDataPoint(dataPoints,97,now, "18:43:00");
//
//                return dataPoints;
//            }
//
//            public boolean isDataPointPersisted(User user, Date timestamp, int value) {
//                return false;
//            }
//
//            public void create(QuickTip quicktip) {
//            }
//
//            public QuickTip findQuickTipById(int id) {
//                return null;
//            }
//
//            public QuickTip findNextQuickTip(QuickTipType type, int previousOrdinal) {
//                return null;
//            }
//
//            public Collection<UserRealTimeAlertProfile> findUserRealTimeAlertProfiles(User user) {
//                return null;
//            }
//
//            public Collection<UserRiskAlertProfile> findUserRiskAlertProfiles(User user) {
//                return null;
//            }
//
//            public Collection<UserMedicalDeviceRegistrationProfile> findUserMedicalDeviceRegistrationProfiles(User user) {
//                return null;
//            }
//
//            public Collection<UserDayOverDayReportProfile> findUserDayOverDayReportProfiles() {
//                return null;
//            }
//
//            public Collection<UserParticipationRequestProfile> findUserParticipationRequestProfiles() {
//                return null;
//            }
//
//            public Collection<UserFrequencyAssessmentProfile> findUserFrequencyAssessmentProfiles() {
//                return null;
//            }
//            public Collection<UserAppointmentReminderProfile> findUserAppointmentReminderProfiles() {
//                return null;
//            }
//
//            public Collection<Module> findActiveModules() {
//                return null;
//            }
//
//            public void setLastPingTime() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void create(Object obj) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void update(Object obj) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//


        ReportManagerBean reportManager = new ReportManagerBean();
        //reportManager.entityDao = entityDao;
        User u = (User) User.findById(17L);
        if (u != null) {
            byte[] attachment = reportManager.getDayOverDayReport(ReportOutputType.PDF,
                u).
                getBytes();
            if (attachment != null) {
                writeTestResults("testGetDayOverDayReport.pdf", attachment);
            }
        }
    }

    // TODO create NULL implementation for subclassing by unit tests. keep in dao test packages
//        EntityDAO entityDao = new EntityDAO() {
//            public void create(User user) {
//            }
//
//            public void create(Module module) {
//            }
//
//            public MedicalDevice findMedicalDeviceBySerialNumber(String serialNumber) {
//                return null;
//            }
//
//            public Module findModuleByPin(String pin) {
//                return null;
//            }
//
//            public User findUserById(int id) {
//                User user = new User();
//                user.setId(999);
//                user.setHandle("Manchester BA-1394");
//                user.setTimeZone(TimeZone.getTimeZone("Europe/London"));
//                return user;
//            }
//
//            public Collection<DataPoint> findDataPointsXXX(CalendarRange calendarRange, User user) {
//                Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
//                Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
//                now.add(Calendar.HOUR_OF_DAY, -1); //up to midnight of tomorrow (includes today)
//                now.add(Calendar.DATE, 2); //up to midnight of tomorrow (includes today)
//
//                // series of daos that return data
//                // per date range, per hard coded, etc.
//                //now.add(Calendar.DATE, -7); //one week old datapoints, for one week
//                //for( int i = 0; i < 7; i++ ) { // days //21
//
//                //now.add(Calendar.DATE, -7); //one week old datapoints, for two weeks
//                //for( int i = 0; i < 14; i++ ) { // days //21
//
//                //for( int i = 0; i < 20; i++ ) {
//
//                for( int i = 0; i < 21; i++ ) {
//                    int value = 30;
//                    for ( int h = 0; h < 24; h++ ) { //hours per day
//                        DataPoint dp = new DataPoint();
//                        dp.setTimestamp(now.getTime());
//                        dp.setValue(value + (h*10) );
//                        dataPoints.add(dp);
//                        if ( h % 3 == 0 ) {
//                            // 2x per hour every 3 hours
//                            now.add(Calendar.MINUTE,2);
//                            DataPoint dp2 = new DataPoint();
//                            dp2.setTimestamp(now.getTime());
//                            dp2.setValue(99);
//                            dataPoints.add(dp2);
//                            now.add(Calendar.MINUTE,-2);
//                        }
//                        now.add(Calendar.HOUR_OF_DAY,-1);
//                    }
//                }
//                return dataPoints;
//            }
//
//            public void addDataPoint(Collection<DataPoint>  dataPoints,int value,Calendar timeStampDate,String time) {
//                DataPoint dp = new DataPoint();
//                StringTokenizer st = new StringTokenizer(time,":");
//                dp.setValue(value);
//                timeStampDate.set(Calendar.HOUR_OF_DAY,   Integer.parseInt(st.nextToken()));
//                timeStampDate.set(Calendar.MINUTE,        Integer.parseInt(st.nextToken()));
//                timeStampDate.set(Calendar.SECOND,        Integer.parseInt(st.nextToken()));
//                dp.setTimestamp(timeStampDate.getTime());
//                dataPoints.add(dp);
//            }
//
//            public Collection<DataPoint> findDataPoints(CalendarRange calendarRange, User user) {
//                Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
//                Calendar now = DateHelper.asUtcDay(DateHelper.nowUtc());
//                now.add(Calendar.HOUR_OF_DAY, -1); //up to midnight of tomorrow (includes today)
//                now.add(Calendar.DATE, 1); //up to midnight of tomorrow (includes today)
//                addDataPoint(dataPoints,119,now,"07:10:48");
//                addDataPoint(dataPoints,127,now,"09:10:48");
//                addDataPoint(dataPoints, 69,now,"14:10:48");
//                addDataPoint(dataPoints,139,now,"17:10:48");
//                addDataPoint(dataPoints,170,now,"21:10:48");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,39,now,"02:16:48");
//                addDataPoint(dataPoints,50,now, "03:16:48");
//                addDataPoint(dataPoints,147,now,"07:16:48");
//                addDataPoint(dataPoints,102,now,"09:16:48");
//                addDataPoint(dataPoints,145,now,"13:16:48");
//                addDataPoint(dataPoints,109,now,"21:16:48");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,40,now, "03:12:08");
//                addDataPoint(dataPoints,49,now, "04:07:16");
//                addDataPoint(dataPoints,103,now, "08:12:08");
//                addDataPoint(dataPoints,152,now, "10:07:16");
//                addDataPoint(dataPoints,165,now, "12:07:16");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,93,now, "07:11:16");
//                addDataPoint(dataPoints,106,now, "09:11:16");
//                addDataPoint(dataPoints,119,now, "13:47:08");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,109,now, "06:06:08");
//                addDataPoint(dataPoints,121,now, "09:29:40");
//                addDataPoint(dataPoints,195,now, "12:07:40");
//                addDataPoint(dataPoints,182,now, "12:07:40");
//                addDataPoint(dataPoints,134,now, "15:07:40");
//                addDataPoint(dataPoints,147,now, "17:07:40");
//                now.add(Calendar.DATE,-1);
//                addDataPoint(dataPoints,100,now, "07:46:32");
//                addDataPoint(dataPoints,126,now, "09:01:16");
//                addDataPoint(dataPoints,262,now, "14:16:40");
//                addDataPoint(dataPoints,69,now, "17:16:40");
//
//                return dataPoints;
//            }
//
//            public boolean isDataPointPersisted(User user, Date timestamp, int value) {
//                return false;
//            }
//
//            public void create(QuickTip quicktip) {
//            }
//
//            public QuickTip findQuickTipById(int id) {
//                return null;
//            }
//
//            public QuickTip findNextQuickTip(QuickTipType type, int previousOrdinal) {
//                return null;
//            }
//
//            public Collection<UserRealTimeAlertProfile> findUserRealTimeAlertProfiles(User user) {
//                return null;
//            }
//
//            public Collection<UserRiskAlertProfile> findUserRiskAlertProfiles(User user) {
//                return null;
//            }
//
//            public Collection<UserMedicalDeviceRegistrationProfile> findUserMedicalDeviceRegistrationProfiles(User user) {
//                return null;
//            }
//
//            public Collection<UserDayOverDayReportProfile> findUserDayOverDayReportProfiles() {
//                return null;
//            }
//
//            public Collection<UserParticipationRequestProfile> findUserParticipationRequestProfiles() {
//                return null;
//            }
//
//            public Collection<UserFrequencyAssessmentProfile> findUserFrequencyAssessmentProfiles() {
//                return null;
//            }
//            public Collection<UserAppointmentReminderProfile> findUserAppointmentReminderProfiles() {
//                return null;
//            }
//
//            public Collection<Module> findActiveModules() {
//                return null;
//            }
//
//            public void setLastPingTime() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void create(Object obj) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void update(Object obj) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
    @Test
    public void testGetDayOverDayReport2GenerateEmptyReport() throws Exception {

        ReportManagerBean reportManager = new ReportManagerBean();
        //     reportManager.entityDao = entityDao;

        User u = new User("handle", "America/Chicago", true, new Date(), new Date(), "elink");//(User) User.findById(17L);
        u.setType(UserType.OTHER);
        u.save();
        byte[] attachment = reportManager.getDayOverDayReport(
            ReportOutputType.PDF,
            u).
            getBytes();
        writeTestResults("testGetDayOverDayReport2.pdf", attachment);
    }

    private void writeTestResults(String name, byte[] data) throws Exception {
        FileOutputStream out = new FileOutputStream(BUILD_TEST_RESULTS_DIR + File.separator + name);
        out.write(data, 0, data.length - 1);
        out.close();
    }
}
