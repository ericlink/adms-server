package core.schedulable;

//package net.diabetech.glucodynamix.schedulable;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TimeZone;
//import junit.framework.*;
//import net.diabetech.entity.DataPoint;
//import net.diabetech.entity.FrequencyAssessmentProfile;
//import net.diabetech.entity.MedicalDevice;
//import net.diabetech.entity.Module;
//import net.diabetech.entity.Monitoring;
//import net.diabetech.entity.QuickTip;
//import net.diabetech.entity.QuickTipType;
//import net.diabetech.entity.User;
//import net.diabetech.entity.UserAppointmentReminderProfile;
//import net.diabetech.entity.UserDayOverDayReportProfile;
//import net.diabetech.entity.UserFrequencyAssessmentProfile;
//import net.diabetech.entity.UserMedicalDeviceRegistrationProfile;
//import net.diabetech.entity.UserParticipationRequestProfile;
//import net.diabetech.entity.UserRealTimeAlertProfile;
//import net.diabetech.entity.UserRiskAlertProfile;
//import net.diabetech.entity.dao.EntityDAO;
//import net.diabetech.util.CalendarRange;
//import net.diabetech.util.DateHelper;
//
///**
// * Confidential Information.
// * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
// * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
// **/
//public class FrequencyAssessmentBeanTest extends TestCase {
//    FrequencyAssessmentBean frequencyAssessmentBean;
//
//    public FrequencyAssessmentBeanTest(String testName) {
//        super(testName);
//    }
//
//    protected void setUp() throws Exception {
//        frequencyAssessmentBean = new FrequencyAssessmentBean();
//        frequencyAssessmentBean.entityDao = new EntityDAOMock();
//    }
//
//    protected void tearDown() throws Exception {
//    }
//
////    TODO add higher level testing as well public void testRun() {
////        frequencyAssessmentBean.run();
////    }
//
//    private void setTypeI(UserFrequencyAssessmentProfile profile) {
//        FrequencyAssessmentProfile freqProfile = (FrequencyAssessmentProfile)profile.getFeatureProfile();
//        freqProfile.setDaysToCheck(2);
//        freqProfile.setRequiredDays(2);
//        freqProfile.setMinimumDataPointsPerRequiredDay(4);
//    }
//    private void setTypeII(UserFrequencyAssessmentProfile profile) {
//        FrequencyAssessmentProfile freqProfile = (FrequencyAssessmentProfile)profile.getFeatureProfile();
//        freqProfile.setDaysToCheck(7);
//        freqProfile.setRequiredDays(5);
//        freqProfile.setMinimumDataPointsPerRequiredDay(1);
//    }
//
//    private UserFrequencyAssessmentProfile getProfile(final int dataPointsPerDay, final int numDays) {
//        User user = getUserWithDataPointsPerDayForDays(dataPointsPerDay,numDays);
//        frequencyAssessmentBean.entityDao.create(user);
//        FrequencyAssessmentProfile freqProfile = new FrequencyAssessmentProfile();
//        UserFrequencyAssessmentProfile profile = new UserFrequencyAssessmentProfile();
//        profile.setFeatureProfile(freqProfile);
//        profile.setUser(user);
//        return profile;
//    }
//    public void testUser_4_2_PST() {
//        UserFrequencyAssessmentProfile profile = getProfile(4,2);
//        profile.getUser().setTimeZone(TimeZone.getTimeZone("PST"));
//        setTypeI(profile);
//        assertTrue( frequencyAssessmentBean.isFrequentChecker( profile ) );
//        setTypeII(profile);
//        assertFalse( frequencyAssessmentBean.isFrequentChecker( profile ) );
//    }
//
//    public void testUser_4_8_Plus() {
//        for( int i = 0; i < 20; i++ ) {
//            UserFrequencyAssessmentProfile profile = getProfile(4,8+i);
//            profile.getUser().setTimeZone(TimeZone.getTimeZone("PST"));
//            setTypeI(profile);
//            assertTrue( frequencyAssessmentBean.isFrequentChecker( profile ) );
//            setTypeII(profile);
//            assertTrue( frequencyAssessmentBean.isFrequentChecker( profile ) );
//        }
//    }
//
//    private void assertFalseFalse(final int dataPointsPerDay, final int numDays) {
//        UserFrequencyAssessmentProfile profile = getProfile(dataPointsPerDay,numDays);
//        setTypeI(profile);
//        assertFalse( frequencyAssessmentBean.isFrequentChecker( profile ) );
//        setTypeII(profile);
//        assertFalse( frequencyAssessmentBean.isFrequentChecker( profile ) );
//    }
//    private void assertFalseTrue(final int dataPointsPerDay, final int numDays) {
//        UserFrequencyAssessmentProfile profile = getProfile(dataPointsPerDay,numDays);
//        setTypeI(profile);
//        assertFalse( frequencyAssessmentBean.isFrequentChecker( profile ) );
//        setTypeII(profile);
//        assertTrue( frequencyAssessmentBean.isFrequentChecker( profile ) );
//    }
//    private void assertTrueFalse(final int dataPointsPerDay, final int numDays) {
//        UserFrequencyAssessmentProfile profile = getProfile(dataPointsPerDay,numDays);
//        setTypeI(profile);
//        assertTrue( frequencyAssessmentBean.isFrequentChecker( profile ) );
//        setTypeII(profile);
//        assertFalse( frequencyAssessmentBean.isFrequentChecker( profile ) );
//    }
//    private void assertTrueTrue(final int dataPointsPerDay, final int numDays) {
//        UserFrequencyAssessmentProfile profile = getProfile(dataPointsPerDay,numDays);
//        setTypeI(profile);
//        assertTrue( frequencyAssessmentBean.isFrequentChecker( profile ) );
//        setTypeII(profile);
//        assertTrue( frequencyAssessmentBean.isFrequentChecker( profile ) );
//    }
//
//    public void testFalseFalse() {
//        assertFalseFalse(0,0);
//        assertFalseFalse(1,1);
//        assertFalseFalse(1,2);
//        assertFalseFalse(1,3);
//        assertFalseFalse(1,4);
//        assertFalseFalse(3,2);
//        assertFalseFalse(4,1);
//    }
//    public void testFalseTrue() {
//        assertFalseTrue(1,5);
//        assertFalseTrue(1,6);
//        assertFalseTrue(1,7);
//        assertFalseTrue(1,8);
//    }
//    public void testTrueFalse() {
//        assertTrueFalse(4,2);
//        assertTrueFalse(4,3);
//        assertTrueFalse(4,4);
//        assertTrueFalse(5,2);
//    }
//    public void testTrueTrue() {
//        assertTrueTrue(4,5);
//        assertTrueTrue(4,6);
//        assertTrueTrue(4,7);
//    }
//    private User getUserWithDataPointsPerDayForDays(final int dataPointsPerDay, final int numDays) {
//        User user = new User();
//        user.setLogin("Login");
//        user.setHandle("Handle");
//        user.setTimeZone(DateHelper.UTC_TIME_ZONE);
//        addDataPointsForDays(dataPointsPerDay,numDays,user);
//        return user;
//    }
//
//    private void addDataPointsForDays(final int dataPointsPerDay, final int numDays, final User user) {
//        Calendar timestamp = Calendar.getInstance(DateHelper.UTC_TIME_ZONE);
//        for ( int days = 0; days < numDays; days++ ) {
//            // create a chunk of days before today, for numDays
//            timestamp.add(Calendar.DATE,-1);
//            for ( int numDataPoints = 0; numDataPoints < dataPointsPerDay; numDataPoints++ ) {
//                addDataPointToUser( timestamp, user );
//            }
//        }
//    }
//
//    private DataPoint addDataPointToUser(Calendar timestamp, User user ) {
//        DataPoint dp = new DataPoint();
//        user.addDataPoint(dp);
//        dp.setTimestamp( timestamp.getTime() );
//        dp.setValue(100);
//        return dp;
//    }
//
//    class EntityDAOMock implements EntityDAO {
//        Map<User,Collection<DataPoint>> users = new HashMap<User,Collection<DataPoint>>();
//
//        public EntityDAOMock() {
//            int dayRange = 10;
//            int dataPointsPerDay = 10;
//            for ( int numDataPoints = 0; numDataPoints < dataPointsPerDay; numDataPoints++ ) {
//                for ( int days = 0; days < dayRange; days++ ) {
//                    User user = getUserWithDataPointsPerDayForDays(days,numDataPoints);
//                    users.put(user,user.getDataPoints());
//                }
//            }
//            for ( int numDataPoints = 0; numDataPoints < dataPointsPerDay; numDataPoints++ ) {
//                for ( int days = 0; days < dayRange; days++ ) {
//                    User user = getUserWithDataPointsPerDayForDays(days,numDataPoints);
//                    user.setTimeZone(TimeZone.getTimeZone("PST"));
//                    users.put(user,user.getDataPoints());
//                }
//            }
//
//        }
//        public void create(User user) {
//            users.put(user,user.getDataPoints());
//        }
//
//        public void create(Module module) {
//        }
//
//        public MedicalDevice findMedicalDeviceBySerialNumber(String serialNumber) {
//            return null;
//        }
//
//        public Module findModuleByPin(String pin) {
//            return null;
//        }
//
//        public User findUserById(int id) {
//            return null;
//        }
//
//        public boolean isDataPointPersisted(User user, Date timestamp, int value) {
//            return true;
//        }
//
//        public Collection<DataPoint> findDataPoints(CalendarRange calendarRange, User user) {
//
//            if (true) return user.getDataPoints(); //TODO hashCode issue?
//
//            // mock assumes control readings are filtered out by DAO, tested w/ DAO tests
//            Collection<DataPoint> filteredDataPoints = new ArrayList<DataPoint>();
//            Collection<DataPoint> dataPoints = users.get(user);
//            for ( DataPoint dp : dataPoints )  {
//                if (dp.getTimestamp().after(calendarRange.getStart().getTime())
//                && dp.getTimestamp().before(calendarRange.getEnd().getTime())
//                ) {
//                    filteredDataPoints.add(dp);
//                }
//
//            }
//            return filteredDataPoints;
//        }
//
//        public void create(QuickTip quicktip) {
//        }
//
//        public QuickTip findQuickTipById(int id) {
//            return null;
//        }
//
//        public QuickTip findNextQuickTip(QuickTipType type, int previousOrdinal) {
//            return null;
//        }
//
//        public Collection<UserRealTimeAlertProfile> findUserRealTimeAlertProfiles(User user) {
//            return null;
//        }
//
//        public Collection<UserRiskAlertProfile> findUserRiskAlertProfiles(User user) {
//            return null;
//        }
//
//        public Collection<UserMedicalDeviceRegistrationProfile> findUserMedicalDeviceRegistrationProfiles(User user) {
//            return null;
//        }
//
//        public Collection<UserDayOverDayReportProfile> findUserDayOverDayReportProfiles() {
//            return null;
//        }
//
//        public Collection<UserParticipationRequestProfile> findUserParticipationRequestProfiles() {
//            return null;
//        }
//
//        public Collection<UserFrequencyAssessmentProfile> findUserFrequencyAssessmentProfiles() {
//            return null;//return users.keySet();
//        }
//
//        public Collection<Module> findActiveModules() {
//            return null;
//        }
//
//        public Collection<UserAppointmentReminderProfile> findUserAppointmentReminderProfiles() {
//            return null;
//        }
//
//        public Monitoring setLastPingTime() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//    }
//}
