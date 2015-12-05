/**

// TODO split out DAO and DatabaseMessageEventHandler testing

 * TESTING CHECKLIST FOR JPA methods
 * Create helper methods to create types of objects, so they are created w/ correct assumptions
 * Tests for unmanaged (id == 0) and persisted ( id > 0 )
 * Check relationships between the classes (did app code + persisting them set them correctly?)
 * Test with existing managed entities (update) and new (create) cases.
 * em.flush() to ensure not-null checks are run (otherwise queries are delayed and may not happen before test rollback)
 * Make sure entities equals, compare to and hash code methods are implemented.
 * Test in container and view data in database to confirm all data populated as expected (fks correct, callbacks fired, etc.)
 * Make same pre-test assertions after test to make sure no side effects.
 **/
package core.module;

import org.junit.*;
import play.test.*;
import java.util.Date;
import java.util.TimeZone;
import core.datapoint.DataPoint;
import core.type.DiagnosisType;
import core.medicaldevice.MedicalDevice;
import models.User;
import models.UserType;
import core.module.codec.GlucoMonDatabase;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006, 2011 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DatabaseMessageEventHandlerBeanTest extends UnitTest {

    DatabaseMessageEventHandlerBean databaseMessageEventHandlerBean = new DatabaseMessageEventHandlerBean();
//    DataPointEventListenerMock dataPointEventListener;
//    MedicalDeviceEventListenerMock medicalDeviceEventListener;
//    private void clearDb() throws Exception {
//        txn = em.getTransaction();
//        txn.begin();
//        em.createQuery( "delete from AlertOutboundMessage" ).executeUpdate();
//        em.createQuery( "delete from ModuleOutboundMessage" ).executeUpdate();
//
//        em.createQuery( "delete from Destination" ).executeUpdate();
//        em.createQuery( "delete from Threshold" ).executeUpdate();
//
//        em.createQuery( "delete from DataMark" ).executeUpdate();
//        em.createQuery( "delete from DataPoint" ).executeUpdate();
//
//        em.createQuery( "delete from MedicalDevice" ).executeUpdate();
//        em.createQuery( "delete from Module" ).executeUpdate();
//        em.createQuery( "delete from User" ).executeUpdate();
//
//        em.createQuery( "delete from QuickTip" ).executeUpdate();
//
//        em.createQuery( "delete from IntensiveManagementProtocol" ).executeUpdate();
//        em.createQuery( "delete from FeatureProfile" ).executeUpdate();
//        em.createQuery( "delete from UserFeatureProfile" ).executeUpdate();
//        em.createQuery( "delete from UserFeatureProfileDestinationProfile" ).executeUpdate();
//
//        em.createQuery( "delete from RiskAlertProfile" ).executeUpdate();
//        em.createQuery( "delete from UserRiskAlertProfile" ).executeUpdate();
//
//        txn.commit();
//    }
//
//    public void setUp() throws Exception {
////        dataPointEventListener = new DataPointEventListenerMock();
// //       medicalDeviceEventListener = new MedicalDeviceEventListenerMock();
//   //     databaseMessageEventHandlerBean.dataPointEventListener = dataPointEventListener;
//     //   databaseMessageEventHandlerBean.medicalDeviceEventListener = medicalDeviceEventListener;
//        // assert db is clear of any entities at beginning of test
//        assertNumberOfModulesPersisted(0);
//        assertNumberOfUsersPersisted(0);
//        assertNumberOfMedicalDevicesPersisted(0);
//        assertNumberOfDataPointsPersisted(0);
//    }

    private User createUser(String handle) {
        User u = new User();
        u.setHandle(handle);
        u.setTimeZone(TimeZone.getTimeZone("PST"));
        u.setType(UserType.PARTICIPANT);
        u.setDiagnosisType(DiagnosisType.UNKNOWN);
        return u;
    }

    private Module createModule(String pin) {
        Module m = new Module();
        m.setPin(pin);
        m.setDisplayKey("displayKey");
        m.setNetworkType(NetworkType.USA_MOBILITY);
        m.setTimeZone(TimeZone.getTimeZone("CST"));
        return m;
    }

    private GlucoMonDatabase createGlucoMonDatabase() {
        return new GlucoMonDatabase();
    }

    private MedicalDevice createMedicalDevice(String serialNumber) {
        return new MedicalDevice(serialNumber);
    }

    private DataPoint createDataPoint(Date timestamp, int value) {
        DataPoint dp = new DataPoint();
        dp.setTimestamp(timestamp);
        dp.setOriginated(timestamp);
        dp.setValue(value);
        return dp;
    }

    private void assertNumberOfUsersPersisted(long expected, String handle) {
        int size = User.find("select u from User u where handle = :handle").
            bind("handle", handle).
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertNumberOfUsersPersisted(long expected) {
        int size = User.find("select u from User u").
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertNumberOfModulesPersisted(long expected, String pin) {
        int size = Module.find("select m from Module m where pin = :pin").
            bind("pin", pin).
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertNumberOfModulesPersisted(long expected) {
        int size = Module.find("select m from Module m").
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertNumberOfMedicalDevicesPersisted(long expected) {
        int size = MedicalDevice.find("select md from MedicalDevice md").
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertNumberOfMedicalDevicesPersisted(long expected, String serialNumber) {
        int size = MedicalDevice.find("select md from MedicalDevice md where serialNumber = :serialNumber").
            bind("serialNumber", serialNumber).
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertNumberOfDataPointsPersisted(long expected) {
        int size = DataPoint.find("select dp from DataPoint dp").
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertNumberOfDataPointsPersisted(int expected, User user, Date timestamp, int value) {
//        int size = MedicalDevice.find("select dp from DataPoint dp where user = :user and timestamp = :timestamp and value = :value").
        int size = MedicalDevice.find("select dp from DataPoint dp where user = :user and timestamp = :timestamp ").
            bind("user", user).
            bind("timestamp", timestamp).
            //bind("value", value).
            fetch().
            size();
        assertEquals(expected, size);
    }

    private void assertPersisted(Long id) {
        assertTrue(id > 0);
    }

    private void assertNotPersisted(Long id) {
        assertNull(id);
    }

//    private Collection<MedicalDevice> getNewMedicalDevices() {
//        return medicalDeviceEventListener.newMedicalDevices;
//    }
//
//    private Map<User, DataPoint> getNewDataPoints() {
//        return dataPointEventListener.newDataPoints;
//    }
    // module w/ 1 md, 1 md multiple users, etc... inactive/active md, etc.
    // ... matrix of combinations of module and gmon db
    @Test
    public void testPersist() {
        Fixtures.deleteAll();
        createUser("test1").save();
        createUser("test2").save();
        createUser("test3").save();
        assertNumberOfUsersPersisted(3);
        createUser("test4").save();
        createUser("test5").save();
        createUser("test6").save();
        assertNumberOfUsersPersisted(6);
    }

    @Test
    public void testDifferentUserThanDefault() throws Exception {
        Fixtures.deleteAll();
        // setup managed entities
        Module module = createModule("pin");
        User defaultUser = createUser("default user");
        User mdUser = createUser("12345's user");
        module.setUser(defaultUser); //default user for module, will be assigned to med device
        MedicalDevice mdManaged = createMedicalDevice("12345"); //unmanaged, same serial number
        for (int i = 0; i < 4; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(mdUser);
            module.addDataPoint(dpExisting);
            mdManaged.addDataPoint(dpExisting);
        }
        mdUser.addMedicalDevice(mdManaged);
        mdUser = mdUser.save();
        module = module.save();
        assertPersisted(mdUser.getId());
        assertPersisted(defaultUser.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged.getId());
        assertNotSame(mdUser, module.getUser());
        Long originalMdId = mdManaged.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        Date dpDate = new Date();
        DataPoint dp = createDataPoint(dpDate, 44);
        mdUnmanaged.addDataPoint(dp);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged);
        // default user setup correct?
        assertNotSame(defaultUser, mdUnmanaged.getUser());
        assertNull(mdUnmanaged.getUser()); // no user just parsed with sn and data points
        assertNull(dp.getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //em.flush(); //flush to force persistence checks
        // new md event?

        //TODO assertNotNull(getNewMedicalDevices());
        //TODO assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //TODO assertNotNull(getNewDataPoints());
        //TODO assertEquals(1, getNewDataPoints().                size());
        //TODO assertEquals(dp, getNewDataPoints().                get(mdUser));
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfUsersPersisted(1, "12345's user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(2);
        assertNumberOfMedicalDevicesPersisted(1);
        assertNumberOfDataPointsPersisted(5);
        //todoassertNumberOfDataPointsPersisted(1, mdUser, dpDate, 44);
        assertPersisted(dp.getId());
        assertPersisted(dp.getUser().
            getId());
        assertNotPersisted(mdUnmanaged.getId());
        // same md, nothing changed
        assertSame(originalMdId, mdManaged.getId());
        assertNotSame(defaultUser, mdUnmanaged.getUser());
        // default user still ok?
        assertEquals(defaultUser.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        assertEquals(mdUser.getId(), dp.getUser().
            getId());
        assertEquals(module.getId(), dp.getModule().
            getId());
        // md has it's original user and new data point
        assertEquals(mdUser.getId(), mdManaged.getUser().
            getId());
        //todo assertTrue(mdManaged.getDataPoints().                contains(dp));
        DataPoint dpFound = DataPoint.findById(dp.getId());
        assertEquals(dp.getId(), dpFound.getId());
        assertEquals(mdUser.getId(), dpFound.getUser().
            getId());
        assertEquals(module.getId(), dpFound.getModule().
            getId());
    }

    @Test
    public void testOnMessageTwoMedicalDevicesForTwoUsers() throws Exception {
        Fixtures.deleteAll();
        // setup managed entities
        Module module = createModule("pin");
        User defaultUser = createUser("default user");
        User mdUser0 = createUser("12345's user");
        User mdUser1 = createUser("54321's user");
        module.setUser(defaultUser); //default user for module, will be assigned to med device
        MedicalDevice mdManaged0 = createMedicalDevice("12345"); //unmanaged, same serial number
        MedicalDevice mdManaged1 = createMedicalDevice("54321"); //unmanaged, same serial number
        for (int i = 0; i < 3; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(mdUser0);
            module.addDataPoint(dpExisting);
            mdManaged0.addDataPoint(dpExisting);
        }
        for (int i = 0; i < 3; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(mdUser1);
            module.addDataPoint(dpExisting);
            mdManaged1.addDataPoint(dpExisting);
        }
        mdUser0.addMedicalDevice(mdManaged0);
        mdUser1.addMedicalDevice(mdManaged1);
        mdUser0 = mdUser0.save();
        mdUser1 = mdUser1.save();
        module = module.save();
        assertPersisted(mdUser0.getId());
        assertPersisted(mdUser1.getId());
        assertPersisted(defaultUser.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged0.getId());
        assertPersisted(mdManaged1.getId());
        assertNotSame(mdUser0, module.getUser());
        assertNotSame(mdUser1, module.getUser());
        Long originalMdId0 = mdManaged0.getId();
        Long originalMdId1 = mdManaged1.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged0 = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        MedicalDevice mdUnmanaged1 = createMedicalDevice("54321"); //unmanaged, same serial number as existing managed
        Date dpDate = new Date();
        DataPoint dp0 = createDataPoint(dpDate, 44);
        mdUnmanaged0.addDataPoint(dp0);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged0);
        DataPoint dp1 = createDataPoint(dpDate, 44);
        mdUnmanaged1.addDataPoint(dp1);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged1);
        // default user setup correct?
        assertNotSame(defaultUser, mdUnmanaged0.getUser());
        assertNotSame(defaultUser, mdUnmanaged1.getUser());
        assertNull(mdUnmanaged0.getUser()); // no user just parsed with sn and data points
        assertNull(mdUnmanaged1.getUser()); // no user just parsed with sn and data points
        assertNull(dp0.getUser());
        assertNull(dp1.getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //em.flush(); //flush to force persistence checks
        // new md event?
        //todo assertNotNull(getNewMedicalDevices());
        //todo assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //todo assertNotNull(getNewDataPoints());
        //todo assertEquals(2, getNewDataPoints().                size());
        //todo assertEquals(dp0, getNewDataPoints().                get(mdUser0));
        //todo assertEquals(dp1, getNewDataPoints().                get(mdUser1));
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfUsersPersisted(1, "12345's user");
        assertNumberOfUsersPersisted(1, "54321's user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfMedicalDevicesPersisted(1, "54321");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(3);
        assertNumberOfMedicalDevicesPersisted(2);
        assertNumberOfDataPointsPersisted(8);
        assertNumberOfDataPointsPersisted(1, mdUser0, dpDate, 44);
        assertNumberOfDataPointsPersisted(1, mdUser1, dpDate, 44);
        assertPersisted(dp0.getId());
        assertPersisted(dp1.getId());
        assertPersisted(dp0.getUser().
            getId());
        assertPersisted(dp1.getUser().
            getId());
        assertNotPersisted(mdUnmanaged0.getId());
        assertNotPersisted(mdUnmanaged1.getId());
        // same md, nothing changed
        assertSame(originalMdId0, mdManaged0.getId());
        assertSame(originalMdId1, mdManaged1.getId());
        assertNotSame(defaultUser, mdUnmanaged0.getUser());
        assertNotSame(defaultUser, mdUnmanaged1.getUser());
        // default user still ok?
        assertEquals(defaultUser.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        assertEquals(mdUser0.getId(), dp0.getUser().
            getId());
        assertEquals(mdUser1.getId(), dp1.getUser().
            getId());
        assertEquals(module.getId(), dp0.getModule().
            getId());
        assertEquals(module.getId(), dp1.getModule().
            getId());
        // md has it's original user and new data point
        assertEquals(mdUser0.getId(), mdManaged0.getUser().
            getId());
        assertEquals(mdUser1.getId(), mdManaged1.getUser().
            getId());
        //todo//assertTrue(mdManaged0.getDataPoints().                contains(dp0));
        //todo assertTrue(mdManaged1.getDataPoints().                contains(dp1));
        DataPoint dpFound = DataPoint.findById(dp0.getId());
        assertEquals(dp0.getId(), dpFound.getId());
        assertEquals(mdUser0.getId(), dpFound.getUser().
            getId());
        assertEquals(module.getId(), dpFound.getModule().
            getId());
        DataPoint dpFound1 = DataPoint.findById(dp1.getId());
        assertEquals(dp1.getId(), dpFound1.getId());
        assertEquals(mdUser1.getId(), dpFound1.getUser().
            getId());
        assertEquals(module.getId(), dpFound1.getModule().
            getId());
    }

    @Test
    public void testOnMessageNoNewDataPoints() throws Exception {
        Fixtures.deleteAll();
        // setup managed entities
        Module module = createModule("pin");
        User user = createUser("default user");
        module.setUser(user); //default user for module, will be assigned to med device
        MedicalDevice mdManaged = createMedicalDevice("12345"); //unmanaged, same serial number
        Date dpDateBase = new Date();
        for (int i = 0; i < 3; i++) {
            DataPoint dpExisting = createDataPoint(new Date(dpDateBase.getTime() + 1000 * 60 * i), i);
            dpExisting.setUser(user);
            module.addDataPoint(dpExisting);
            mdManaged.addDataPoint(dpExisting);
            //mdManaged = mdManaged.save();
        }
        user.addMedicalDevice(mdManaged);
        user = user.save();
        module = module.save();
        assertPersisted(user.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged.getId());
        assertNumberOfDataPointsPersisted(3);

        Long originalMdId = mdManaged.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        Date dpDate0 = dpDateBase;
        Date dpDate1 = new Date(dpDate0.getTime() + 1000 * 60);     //+1min
        Date dpDate2 = new Date(dpDate0.getTime() + 1000 * 60 * 2);   //+2min
        DataPoint[] dp = {createDataPoint(dpDate0, 0), createDataPoint(dpDate1, 1), createDataPoint(dpDate2, 2)};
        mdUnmanaged.addDataPoint(dp[0]);
        mdUnmanaged.addDataPoint(dp[1]);
        mdUnmanaged.addDataPoint(dp[2]);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged);
        // default user setup correct?
        assertNotSame(user, mdUnmanaged.getUser());
        assertNull(mdUnmanaged.getUser()); // no user just parsed with sn and data points
        assertNull(dp[0].getUser());
        assertNull(dp[1].getUser());
        assertNull(dp[2].getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //todo em.flush(); //flush to force persistence checks
        // new md event?
        //todo assertNotNull(getNewMedicalDevices());
        //todo assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //todo assertNotNull(getNewDataPoints());
        //todo assertEquals(0, getNewDataPoints().                size());
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(1);
        assertNumberOfMedicalDevicesPersisted(1);
        //todo assertNumberOfDataPointsPersisted(3);
        //todo assertNumberOfDataPointsPersisted(1, user, dpDate0, 0);
        //todo assertNumberOfDataPointsPersisted(1, user, dpDate1, 1);
        //todo assertNumberOfDataPointsPersisted(1, user, dpDate2, 2);
        assertSame(3, mdManaged.getDataPoints().
            size());

        for (int i = 0; i < 3; i++) {
            //todo assertNotPersisted(dp[i].getId());
            assertTrue(mdManaged.getDataPoints().
                contains(dp[i]));
            //assertTrue( user.getDataPoints().contains(dp[i]));
        }
        assertNotPersisted(mdUnmanaged.getId());
        // same md, nothing changed
        assertSame(originalMdId, mdManaged.getId());
        // default user still ok?
        assertEquals(user.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        // md has default user from module and new data point
        assertEquals(user.getId(), mdManaged.getUser().
            getId());
    }

    public void testOnMessageOneNewDataPoint() throws Exception {
        // setup managed entities
        Module module = createModule("pin");
        User user = createUser("default user");
        module.setUser(user); //default user for module, will be assigned to med device
        MedicalDevice mdManaged = createMedicalDevice("12345"); //unmanaged, same serial number
        for (int i = 0; i < 4; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(user);
            module.addDataPoint(dpExisting);
            mdManaged.addDataPoint(dpExisting);
        }
        user.addMedicalDevice(mdManaged);
        user = user.save();
        module = module.save();
        assertPersisted(user.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged.getId());
        Long originalMdId = mdManaged.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        Date dpDate = new Date();
        DataPoint dp = createDataPoint(dpDate, 44);
        mdUnmanaged.addDataPoint(dp);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged);
        // default user setup correct?
        assertNotSame(user, mdUnmanaged.getUser());
        assertNull(mdUnmanaged.getUser()); // no user just parsed with sn and data points
        assertNull(dp.getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //todo em.flush(); //flush to force persistence checks
        // new md event?
        //todo assertNotNull(getNewMedicalDevices());
        //todo assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //todo assertNotNull(getNewDataPoints());
        //todo assertEquals(1, getNewDataPoints().                size());
        //todo assertEquals(dp, getNewDataPoints().                get(user));
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(1);
        assertNumberOfMedicalDevicesPersisted(1);
        assertNumberOfDataPointsPersisted(5);
        assertNumberOfDataPointsPersisted(1, user, dpDate, 44);
        assertPersisted(dp.getId());
        assertPersisted(dp.getUser().
            getId());
        assertNotPersisted(mdUnmanaged.getId());
        // same md, nothing changed
        assertSame(originalMdId, mdManaged.getId());
        // default user still ok?
        assertEquals(user.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        assertEquals(user.getId(), dp.getUser().
            getId());
        assertEquals(module.getId(), dp.getModule().
            getId());
        // md has default user from module and new data point
        assertEquals(user.getId(), mdManaged.getUser().
            getId());
        assertTrue(mdManaged.getDataPoints().
            contains(dp));
        DataPoint dpFound = DataPoint.findById(dp.getId());
        assertEquals(dp.getId(), dpFound.getId());
        assertEquals(user.getId(), dpFound.getUser().
            getId());
        assertEquals(module.getId(), dpFound.getModule().
            getId());
    }

    public void testOnMessageNewDataPointEventNotOrderDependent() throws Exception {
        // setup managed entities
        Module module = createModule("pin");
        User user = createUser("default user");
        module.setUser(user); //default user for module, will be assigned to med device
        MedicalDevice mdManaged = createMedicalDevice("12345"); //unmanaged, same serial number
        for (int i = 0; i < 4; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(user);
            module.addDataPoint(dpExisting);
            mdManaged.addDataPoint(dpExisting);
        }
        user.addMedicalDevice(mdManaged);
        user = user.save();
        module = module.save();
        assertPersisted(user.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged.getId());
        Long originalMdId = mdManaged.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        Date dpDate0 = new Date();
        Date dpDate1 = new Date(dpDate0.getTime() + 1000 * 60);     //+1min
        Date dpDate2 = new Date(dpDate0.getTime() + 1000 * 60 * 2);   //+2min
        DataPoint[] dp = {createDataPoint(dpDate0, 0), createDataPoint(dpDate1, 1), createDataPoint(dpDate2, 2)};
        mdUnmanaged.addDataPoint(dp[0]);
        mdUnmanaged.addDataPoint(dp[2]); /// THIS IS THE ONE THAT WILL BE IN THE EVENT
        mdUnmanaged.addDataPoint(dp[1]);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged);
        // default user setup correct?
        assertNotSame(user, mdUnmanaged.getUser());
        assertNull(mdUnmanaged.getUser()); // no user just parsed with sn and data points
        assertNull(dp[0].getUser());
        assertNull(dp[1].getUser());
        assertNull(dp[2].getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //todo em.flush(); //flush to force persistence checks
        // new md event?
        //todo assertNotNull(getNewMedicalDevices());
        //todo assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //todo assertNotNull(getNewDataPoints());
        //todo assertEquals(1, getNewDataPoints().                size());
        //todo //todo assertEquals(dp[2], getNewDataPoints().                get(user)); //newest datapoint is last one
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(1);
        assertNumberOfMedicalDevicesPersisted(1);
        assertNumberOfDataPointsPersisted(7);
        assertNumberOfDataPointsPersisted(1, user, dpDate0, 0);
        assertNumberOfDataPointsPersisted(1, user, dpDate1, 1);
        assertNumberOfDataPointsPersisted(1, user, dpDate2, 2);
        for (int i = 0; i < 3; i++) {
            assertPersisted(dp[i].getId());
            assertPersisted(dp[i].getUser().
                getId());
            assertEquals(user.getId(), dp[i].getUser().
                getId());
            assertEquals(module.getId(), dp[i].getModule().
                getId());
            DataPoint dpFound = DataPoint.findById(dp[i].getId());
            assertEquals(dp[i].getId(), dpFound.getId());
            assertEquals(user.getId(), dpFound.getUser().
                getId());
            assertEquals(module.getId(), dpFound.getModule().
                getId());
            assertTrue(mdManaged.getDataPoints().
                contains(dp[i]));
        }
        assertNotPersisted(mdUnmanaged.getId());
        // same md, nothing changed
        assertSame(originalMdId, mdManaged.getId());
        // default user still ok?
        assertEquals(user.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        // md has default user from module and new data point
        assertEquals(user.getId(), mdManaged.getUser().
            getId());
    }

    public void testOnMessageThreeNewDataPoints() throws Exception {
        // setup managed entities
        Module module = createModule("pin");
        User user = createUser("default user");
        module.setUser(user); //default user for module, will be assigned to med device
        MedicalDevice mdManaged = createMedicalDevice("12345"); //unmanaged, same serial number
        for (int i = 0; i < 4; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(user);
            module.addDataPoint(dpExisting);
            mdManaged.addDataPoint(dpExisting);
        }
        user.addMedicalDevice(mdManaged);
        user = user.save();
        module = module.save();
        assertPersisted(user.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged.getId());
        Long originalMdId = mdManaged.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        Date dpDate0 = new Date();
        Date dpDate1 = new Date(dpDate0.getTime() + 1000 * 60);     //+1min
        Date dpDate2 = new Date(dpDate0.getTime() + 1000 * 60 * 2);   //+2min
        DataPoint[] dp = {createDataPoint(dpDate0, 0), createDataPoint(dpDate1, 1), createDataPoint(dpDate2, 2)};
        mdUnmanaged.addDataPoint(dp[0]);
        mdUnmanaged.addDataPoint(dp[1]);
        mdUnmanaged.addDataPoint(dp[2]);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged);
        // default user setup correct?
        assertNotSame(user, mdUnmanaged.getUser());
        assertNull(mdUnmanaged.getUser()); // no user just parsed with sn and data points
        assertNull(dp[0].getUser());
        assertNull(dp[1].getUser());
        assertNull(dp[2].getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //todo em.flush(); //flush to force persistence checks
        // new md event?
        //todo assertNotNull(getNewMedicalDevices());
        //todo assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //todo assertNotNull(getNewDataPoints());
        //todo assertEquals(1, getNewDataPoints().                size());
        //todo assertEquals(dp[2], getNewDataPoints().                get(user)); //newest datapoint is last one
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(1);
        assertNumberOfMedicalDevicesPersisted(1);
        assertNumberOfDataPointsPersisted(7);
        assertNumberOfDataPointsPersisted(1, user, dpDate0, 0);
        assertNumberOfDataPointsPersisted(1, user, dpDate1, 1);
        assertNumberOfDataPointsPersisted(1, user, dpDate2, 2);
        for (int i = 0; i < 3; i++) {
            assertPersisted(dp[i].getId());
            assertPersisted(dp[i].getUser().
                getId());
            assertEquals(user.getId(), dp[i].getUser().
                getId());
            assertEquals(module.getId(), dp[i].getModule().
                getId());
            DataPoint dpFound = DataPoint.findById(dp[i].getId());
            assertEquals(dp[i].getId(), dpFound.getId());
            assertEquals(user.getId(), dpFound.getUser().
                getId());
            assertEquals(module.getId(), dpFound.getModule().
                getId());
            assertTrue(mdManaged.getDataPoints().
                contains(dp[i]));
        }
        assertNotPersisted(mdUnmanaged.getId());
        // same md, nothing changed
        assertSame(originalMdId, mdManaged.getId());
        // default user still ok?
        assertEquals(user.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        // md has default user from module and new data point
        assertEquals(user.getId(), mdManaged.getUser().
            getId());
    }

    public void testOnMessageStaleDataPoints() throws Exception {
        // setup managed entities
        Module module = createModule("pin");
        User user = createUser("default user");
        module.setUser(user); //default user for module, will be assigned to med device
        MedicalDevice mdManaged = createMedicalDevice("12345"); //unmanaged, same serial number
        for (int i = 0; i < 4; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(user);
            module.addDataPoint(dpExisting);
            mdManaged.addDataPoint(dpExisting);
        }
        user.addMedicalDevice(mdManaged);
        user = user.save();
        module = module.save();
        assertPersisted(user.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged.getId());
        Long originalMdId = mdManaged.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        Date dpDate0 = new Date(0);
        Date dpDate1 = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 10 - 1000 * 60));       //10 days old - 1 minute
        Date dpDate2 = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 10));     //10 days old
        DataPoint[] dp = {createDataPoint(dpDate0, 0), createDataPoint(dpDate1, 1), createDataPoint(dpDate2, 2)};
        mdUnmanaged.addDataPoint(dp[0]);
        mdUnmanaged.addDataPoint(dp[1]);
        mdUnmanaged.addDataPoint(dp[2]);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged);
        // default user setup correct?
        assertNotSame(user, mdUnmanaged.getUser());
        assertNull(mdUnmanaged.getUser()); // no user just parsed with sn and data points
        assertNull(dp[0].getUser());
        assertNull(dp[1].getUser());
        assertNull(dp[2].getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //todo em.flush(); //flush to force persistence checks
        // new md event?
        //todo assertNotNull(getNewMedicalDevices());
        //todo assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //todo assertNotNull(getNewDataPoints());
        //todo assertEquals(1, getNewDataPoints().                size());
        //todo assertEquals(dp[1], getNewDataPoints().                get(user)); //newest datapoint that is not stale
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(1);
        assertNumberOfMedicalDevicesPersisted(1);
        assertNumberOfDataPointsPersisted(7);
        assertNumberOfDataPointsPersisted(1, user, dpDate0, 0);
        assertNumberOfDataPointsPersisted(1, user, dpDate1, 1);
        assertNumberOfDataPointsPersisted(1, user, dpDate2, 2);
        for (int i = 0; i < 3; i++) {
            assertPersisted(dp[i].getId());
            assertPersisted(dp[i].getUser().
                getId());
            assertEquals(user.getId(), dp[i].getUser().
                getId());
            assertEquals(module.getId(), dp[i].getModule().
                getId());
            DataPoint dpFound = DataPoint.findById(dp[i].getId());
            assertEquals(dp[i].getId(), dpFound.getId());
            assertEquals(user.getId(), dpFound.getUser().
                getId());
            assertEquals(module.getId(), dpFound.getModule().
                getId());
            assertTrue(mdManaged.getDataPoints().
                contains(dp[i]));
        }
        assertNotPersisted(mdUnmanaged.getId());
        // same md, nothing changed
        assertSame(originalMdId, mdManaged.getId());
        // default user still ok?
        assertEquals(user.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        // md has default user from module and new data point
        assertEquals(user.getId(), mdManaged.getUser().
            getId());
    }

    public void testOnMessageStaleDataPointsNone() throws Exception {
        // setup managed entities
        Module module = createModule("pin");
        User user = createUser("default user");
        module.setUser(user); //default user for module, will be assigned to med device
        MedicalDevice mdManaged = createMedicalDevice("12345"); //unmanaged, same serial number
        for (int i = 0; i < 4; i++) {
            DataPoint dpExisting = createDataPoint(new Date(), 0);
            dpExisting.setUser(user);
            module.addDataPoint(dpExisting);
            mdManaged.addDataPoint(dpExisting);
        }
        user.addMedicalDevice(mdManaged);
        user = user.save();
        module = module.save();
        assertPersisted(user.getId());
        assertPersisted(module.getId());
        assertPersisted(mdManaged.getId());
        Long originalMdId = mdManaged.getId();

        // unmanaged glucomon db and datapoint
        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
        MedicalDevice mdUnmanaged = createMedicalDevice("12345"); //unmanaged, same serial number as existing managed
        Date dpDate0 = new Date(0);
        Date dpDate1 = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 10);                 //10 days old
        Date dpDate2 = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 10 + 1000 * 60));     //10 days and 1minute
        DataPoint[] dp = {createDataPoint(dpDate0, 0), createDataPoint(dpDate1, 1), createDataPoint(dpDate2, 2)};
        mdUnmanaged.addDataPoint(dp[0]);
        mdUnmanaged.addDataPoint(dp[1]);
        mdUnmanaged.addDataPoint(dp[2]);
        glucoMonDatabase.addGlucoseMeter(mdUnmanaged);
        // default user setup correct?
        assertNotSame(user, mdUnmanaged.getUser());
        assertNull(mdUnmanaged.getUser()); // no user just parsed with sn and data points
        assertNull(dp[0].getUser());
        assertNull(dp[1].getUser());
        assertNull(dp[2].getUser());
        // run test
        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
        //todo em.flush(); //flush to force persistence checks
        // new md event?
        //todo assertNotNull(getNewMedicalDevices());
        //todo assertEquals(0, getNewMedicalDevices().                size());
        // new data point event?
        //todo assertNotNull(getNewDataPoints());
        //todo assertEquals(0, getNewDataPoints().                size());
        //todo assertEquals(null, getNewDataPoints().                get(user)); //newest datapoint is last one
        // persisted data ok?
        assertNumberOfModulesPersisted(1, "pin");
        assertNumberOfUsersPersisted(1, "default user");
        assertNumberOfMedicalDevicesPersisted(1, "12345");
        assertNumberOfModulesPersisted(1);
        assertNumberOfUsersPersisted(1);
        assertNumberOfMedicalDevicesPersisted(1);
        assertNumberOfDataPointsPersisted(7);
        assertNumberOfDataPointsPersisted(1, user, dpDate0, 0);
        assertNumberOfDataPointsPersisted(1, user, dpDate1, 1);
        assertNumberOfDataPointsPersisted(1, user, dpDate2, 2);
        for (int i = 0; i < 3; i++) {
            assertPersisted(dp[i].getId());
            assertPersisted(dp[i].getUser().
                getId());
            assertEquals(user.getId(), dp[i].getUser().
                getId());
            assertEquals(module.getId(), dp[i].getModule().
                getId());
            DataPoint dpFound = DataPoint.findById(dp[i].getId());
            assertEquals(dp[i].getId(), dpFound.getId());
            assertEquals(user.getId(), dpFound.getUser().
                getId());
            assertEquals(module.getId(), dpFound.getModule().
                getId());
            assertTrue(mdManaged.getDataPoints().
                contains(dp[i]));
        }
        assertNotPersisted(mdUnmanaged.getId());
        // same md, nothing changed
        assertSame(originalMdId, mdManaged.getId());
        // default user still ok?
        assertEquals(user.getId(), module.getUser().
            getId());
        // dp has default user and correct module
        // md has default user from module and new data point
        assertEquals(user.getId(), mdManaged.getUser().
            getId());
    }
    /**
     * Module (managed entity) has no medical devices.
     * GlucoMON database
     *  - has one medical device
     *  - with one data point with current timestamp.
     * New medical device event fired, user is default user from module.
     * New data point event fired, user is default user from module.
     **/
//    public void testOnMessageOneNewMedicalDevice() throws Exception {
//        // setup managed entities
//        Module module = createModule("pin");
//        User user = createUser("default user");
//        module.setUser(user); //default user for module, will be assigned to med device
//        getDao().
//                create(module);
//        assertPersisted(user.getId());
//        assertPersisted(module.getId());
//        // unmanaged glucomon db and datapoint
//        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
//        MedicalDevice md = createMedicalDevice("NOT_FOUND");
//        Date dpDate = new Date();
//        DataPoint dp = createDataPoint(dpDate, 44);
//        md.addDataPoint(dp);
//        glucoMonDatabase.addGlucoseMeter(md);
//        // default user setup correct?
//        assertNotSame(user, md.getUser());
//        assertNull(md.getUser()); // no user just parsed with sn and data points
//        // run test
//        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
//        em.flush(); //flush to force persistence checks
//        // new md event?
//        assertNotNull(getNewMedicalDevices());
//        assertEquals(1, getNewMedicalDevices().
//                size());
//        MedicalDevice newMd = (MedicalDevice) getNewMedicalDevices().
//                toArray()[0];
//        assertEquals("NOT_FOUND", newMd.getSerialNumber()); //not found, md is from gmon db
//        assertEquals(user, newMd.getUser()); //user is the default user from the module, this is a new md
//        // new data point event?
//        assertNotNull(getNewDataPoints());
//        assertEquals(1, getNewDataPoints().
//                size());
//        assertEquals(dp, getNewDataPoints().
//                get(user));
//        // persisted data ok?
//        assertNumberOfModulesPersisted(1, "pin");
//        assertNumberOfUsersPersisted(1, "default user");
//        assertNumberOfMedicalDevicesPersisted(1, "NOT_FOUND");
//        assertNumberOfModulesPersisted(1);
//        assertNumberOfUsersPersisted(1);
//        assertNumberOfMedicalDevicesPersisted(1);
//        assertNumberOfDataPointsPersisted(1);
//        assertNumberOfDataPointsPersisted(1, user, dpDate, 44);
//        assertPersisted(md.getId());
//        assertPersisted(dp.getId());
//        assertPersisted(dp.getUser().
//                getId());
//        // default user still ok?
//        assertEquals(user.getId(), module.getUser().
//                getId());
//        // dp has default user and correct module
//        assertEquals(user.getId(), dp.getUser().
//                getId());
//        assertEquals(module.getId(), dp.getModule().
//                getId());
//        // md has default user from module
//        assertEquals(user.getId(), md.getUser().
//                getId());
//
//        DataPoint dpFound = (DataPoint) em.find(DataPoint.class, dp.getId());
//        assertEquals(dp.getId(), dpFound.getId());
//        assertEquals(user.getId(), dpFound.getUser().
//                getId());
//        assertEquals(module.getId(), dpFound.getModule().
//                getId());
//    }
//    public void testOnMessageThreeNewMedicalDevices() throws Exception {
//        // setup managed entities
//        Module module = createModule("pin");
//        User user = createUser("default user");
//        module.setUser(user); //default user for module, will be assigned to med device
//        getDao().
//                create(module);
//        em.flush();
//        assertPersisted(user.getId());
//        assertPersisted(module.getId());
//        // unmanaged glucomon db and datapoint
//        GlucoMonDatabase glucoMonDatabase = createGlucoMonDatabase();
//        MedicalDevice[] md = {createMedicalDevice("NOT_FOUND_0"), createMedicalDevice("NOT_FOUND_1"), createMedicalDevice("NOT_FOUND_2")};
//        Date dpDate0 = new Date();
//        Date dpDate1 = new Date(dpDate0.getTime() + 1000 * 60);
//        Date dpDate2 = new Date(dpDate0.getTime() + 1000 * 60 * 2);
//        DataPoint[] dp = {createDataPoint(dpDate0, 0), createDataPoint(dpDate1, 1), createDataPoint(dpDate2, 2)};
//        for (int i = 0; i < 3; i++) {
//            md[i].addDataPoint(dp[i]);
//            glucoMonDatabase.addGlucoseMeter(md[i]);
//            // default user setup correct?
//            assertNotSame(user, md[i].getUser());
//            assertNull(md[i].getUser()); // no user just parsed with sn and data points
//        }
//        // run test
//        databaseMessageEventHandlerBean.onMessage(module, glucoMonDatabase);
//        em.flush(); //flush to force persistence checks
//        // new md event?
//        assertNotNull(getNewMedicalDevices());
//        assertEquals(3, getNewMedicalDevices().
//                size());
//        for (int i = 0; i < 3; i++) {
//            MedicalDevice newMd = (MedicalDevice) getNewMedicalDevices().
//                    toArray()[i];
//            assertEquals("NOT_FOUND_" + i, newMd.getSerialNumber()); //not found, md is from gmon db
//            assertEquals(user, newMd.getUser()); //user is the default user from the module, this is a new md
//        }
//        // new data point event?
//        assertNotNull(getNewDataPoints());
//        assertEquals(1, getNewDataPoints().
//                size()); //only one for this user
//        assertEquals(dp[2], getNewDataPoints().
//                get(user)); //todo which is correct?
//        // persisted data ok?
//        assertNumberOfModulesPersisted(1, "pin");
//        assertNumberOfUsersPersisted(1, "default user");
//        for (int i = 0; i < 3; i++) {
//            assertNumberOfMedicalDevicesPersisted(1, "NOT_FOUND_" + i);
//            assertPersisted(md[i].getId());
//            assertPersisted(dp[i].getId());
//            assertPersisted(dp[i].getUser().
//                    getId());
//        }
//        assertNumberOfDataPointsPersisted(1, user, dpDate0, 0);
//        assertNumberOfDataPointsPersisted(1, user, dpDate1, 1);
//        assertNumberOfDataPointsPersisted(1, user, dpDate2, 2);
//        assertNumberOfModulesPersisted(1);
//        assertNumberOfUsersPersisted(1);
//        assertNumberOfMedicalDevicesPersisted(3);
//        assertNumberOfDataPointsPersisted(3);
//        // default user still ok?
//        assertEquals(user.getId(), module.getUser().
//                getId());
//        for (int i = 0; i < 3; i++) {
//            // dp has default user and correct module
//            assertEquals(user.getId(), dp[i].getUser().
//                    getId());
//            assertEquals(module.getId(), dp[i].getModule().
//                    getId());
//            // md has default user from module
//            assertEquals(user.getId(), md[i].getUser().
//                    getId());
//
//            DataPoint dpFound = (DataPoint) em.find(DataPoint.class, dp[i].getId());
//            assertEquals(dp[i].getId(), dpFound.getId());
//            assertEquals(user.getId(), dpFound.getUser().
//                    getId());
//            assertEquals(module.getId(), dpFound.getModule().
//                    getId());
//        }
//    }
//    /**
//     * Store the results so tests can be self checking
//     **/
//    class DataPointEventListenerMock implements DataPointEventListener {
//
//        public Map<User, DataPoint> newDataPoints = new HashMap<User, DataPoint>();
//
//        public void onNewDataPoint(DataPoint dataPoint) {
//            this.newDataPoints.put(dataPoint.getUser(), dataPoint);
//        }
//    }
//
//    /**
//     * Store the results so tests can be self checking
//     **/
//    class MedicalDeviceEventListenerMock implements MedicalDeviceEventListener {
//
//        public Collection<MedicalDevice> newMedicalDevices = new ArrayList<MedicalDevice>();
//
//        public void onNewMedicalDevice(MedicalDevice medicalDevice) {
//            this.newMedicalDevices.add(medicalDevice);
//        }
//    }
}
