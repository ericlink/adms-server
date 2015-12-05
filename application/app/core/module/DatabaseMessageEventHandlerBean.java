package core.module;

import core.medicaldevice.MedicalDevice;
import core.datapoint.DataPoint;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.*;
import core.event.DataPointEventManager;
import core.event.MedicalDeviceEventManager;
import helper.util.DateHelper;
import core.module.codec.GlucoMonDatabase;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DatabaseMessageEventHandlerBean implements DatabaseMessageEventHandler {

    private static final Logger logger = Logger.getLogger(DatabaseMessageEventHandlerBean.class.getName());
    DataPointEventManager dataPointEventListener = new DataPointEventManager();
    MedicalDeviceEventManager medicalDeviceEventListener = new MedicalDeviceEventManager();

    /**GlucoMonDatabase glucoMonDatabase 
     * Persist business objects.
     * Fire new reading / new data event.
     *
     * If the medical device does not exist, the umanaged entity in the glucomon database
     * is added to the module.user medical device collection (and thus persisted).
     * If the medical device exists, a managed medical device is returned from the entity dao
     * and the new data points are added to it (and thus persisted).
     *
     * @param module managed entity passed into the method
     * @param glucoMonDatabase unmanaged class containing unmanaged entities created from parsing
     *
     **/
    public void onMessage(Module module, GlucoMonDatabase glucoMonDatabase) throws ModuleInboundException {
        logger.log(Level.FINE, "module={0},glucoMonDatabase={1}", new Object[]{module.getPin(), glucoMonDatabase.getGlucoseMeters().size()});

        if (module.getId() == 0) {
            throw new IllegalArgumentException("Module must be a managed entity " + module.getId() + module.toString());
        }

        Collection<MedicalDevice> newMedicalDevices = new ArrayList<MedicalDevice>();
        Map<User, DataPoint> newDataPoints = new HashMap<User, DataPoint>();

        //identify new medical devices, new data points, and add new data to managed entities so it is persisted
        for (MedicalDevice md : glucoMonDatabase.getGlucoseMeters()) {
            logger.log(Level.FINE, "glucoMonDatabase.md={0}", md.getSerialNumber());

            MedicalDevice mdFound = MedicalDevice.findBySerialNumber(md.getSerialNumber());
            if (mdFound == null) {
                logger.log(Level.FINE, "md not found; adding new medicalDevice={0},userId={1}", new Object[]{md.getSerialNumber(), module.getUser().getId()});
                // 1 get managed entities
                module = Module.findById(module.getId());
                User u = User.findById(module.getUser().getId());
                md.setUser(u);

                // 2 set entity relationships
                logger.log(Level.FINER, "set medical device and module for each data point");
                Collection<DataPoint> medicalDeviceCurrentDataPoints = md.getDataPoints();
                for (DataPoint dp : md.getDataPoints()) {
                    dp.setUser(u);
                    dp.setMedicalDevice(md);
                    dp.setModule(module);           // came from the module passed in
                }

                // 3 persist medical device and dataPoints (via cascade)
                md = md.save();
                newMedicalDevices.add(md);            // save to fire new device event
                logger.log(Level.FINER, "add medical device to user");
                identifyDataPointsForNewDataPointEvent(newDataPoints, medicalDeviceCurrentDataPoints);
            } else {
                logger.log(Level.FINE, "md found; considering adding dataPoints to existing medicalDevice={0},userId={1}", new Object[]{mdFound.getSerialNumber(), mdFound.getUser().getId()});
                Collection<DataPoint> newDataPointsForExistingMedicalDevice = new ArrayList<DataPoint>();
                for (DataPoint dp : md.getDataPoints()) {
                    dp.setUser(mdFound.getUser());  // user is the medical device user
                    // dp is currently unmanaged, so ask the entity manager if it already has it
                    if (!DataPoint.isDataPointPersisted(dp.getUser(), dp.getTimestamp(), dp.getValue())) {
                        logger.log(Level.FINE, "New data point, adding datapoint to medical device {0}", dp);
                        dp.setModule(module);           // came from the module passed in
                        dp.setMedicalDevice(mdFound);
                        dp = dp.save();
                        newDataPointsForExistingMedicalDevice.add(dp); // these are 'new' since never persisted before
                    }
                }
                identifyDataPointsForNewDataPointEvent(newDataPoints, newDataPointsForExistingMedicalDevice);
            }
        }

        logger.log(Level.FINE, "newMedicalDevices.size={0}", newMedicalDevices.size());
        logger.log(Level.FINE, "newDataPoints.size={0}", newDataPoints.size());
        fireNewMedicalDeviceEvent(newMedicalDevices);
        fireNewDataPointEvent(newDataPoints.values());
    }

    /**
     * Identify any data points that meet all criteria to fire a new data point event.
     * @param newDataPoints Map of user/datapoint pairs that have been identified for the new data point event
     * @param candidateDataPoints collection of data points that has never been persisted before
     **/
    private void identifyDataPointsForNewDataPointEvent(final Map<User, DataPoint> newDataPoints, final Collection<DataPoint> candidateDataPoints) {
        DataPoint newestDataPoint = findNewestDataPoint(candidateDataPoints);
        logger.log(Level.FINE, "findNewestDataPoint returned: {0}, candidateDataPoints.size={1}", new Object[]{newestDataPoint, candidateDataPoints.size()});
        if (newestDataPoint != null && !isStale(newestDataPoint)) {
            DataPoint previousNewDataPoint = newDataPoints.put(newestDataPoint.getUser(), newestDataPoint);
            if (previousNewDataPoint != null && previousNewDataPoint.getTimestamp().after(newestDataPoint.getTimestamp())) {
                // put back the datapoint we had, it's newer than the one we just found
                // will happen when there are multiple meters for a user that come in one database event
                newDataPoints.put(previousNewDataPoint.getUser(), previousNewDataPoint);
            }
        }
    }

    /**
     * @returns null if there are no data points passed in or the newest datapoint in the collection
     **/
    private DataPoint findNewestDataPoint(Collection<DataPoint> dataPoints) {
        DataPoint newestDataPoint = new DataPoint();
        newestDataPoint.setTimestamp(new Date(0));
        DataPoint DEFAULT_DATAPOINT = newestDataPoint;

        for (DataPoint dp : dataPoints) {
            if (dp.getTimestamp().after(newestDataPoint.getTimestamp())) {
                newestDataPoint = dp;
            }
        }

        return newestDataPoint.equals(DEFAULT_DATAPOINT) ? null : newestDataPoint;
    }

    /**
     * A base level check for data point age for use cases:
     *  - old meter docked
     *  - store and forward (out of coverage and come back into coverage)
     *
     * A stale data point is:
     *  > 10 days old.
     *
     * @return true if the data point is not stale.
     **/
    private boolean isStale(DataPoint dataPoint) {
        final long MILLIS_AGE_LIMIT_DURATION = DateHelper.MILLIS_PER_DAY * 10; //10 days
        boolean isStale = dataPoint.getTimestamp().before(new Date(System.currentTimeMillis() - MILLIS_AGE_LIMIT_DURATION));

        if (isStale) {
            logger.log(Level.FINE, "Data point is stale ({0})", dataPoint);
        }

        return isStale;
    }

    private void fireNewDataPointEvent(final Collection<DataPoint> dataPoints) {
        logger.log(Level.INFO, "Firing new data point event for {0} data points.", dataPoints.size());
        for (DataPoint dataPoint : dataPoints) {
            logger.log(Level.FINE, "Firing new data point event for data point ({0})", dataPoint);
            // TODO try catch on each data point; we've saved it; email happens after this; so don't want to roll back if we 
            // made it to this point
            dataPointEventListener.onNewDataPoint(dataPoint);
        }
    }

    private void fireNewMedicalDeviceEvent(final Collection<MedicalDevice> medicalDevices) {
        logger.log(Level.INFO, "Firing new medical device event for {0} medical devices.", medicalDevices.size());
        for (MedicalDevice medicalDevice : medicalDevices) {
            logger.log(Level.FINE, "Firing new medical device event for medical device ({0})", medicalDevice);
            // TODO try catch on each data point; we've saved it; email happens after this; so don't want to roll back if we 
            // made it to this point
            medicalDeviceEventListener.onNewMedicalDevice(medicalDevice);
        }
    }
}