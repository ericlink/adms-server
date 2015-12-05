package core.event;

import java.util.logging.Level;
import java.util.logging.Logger;
import core.datapoint.DataPoint;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DataPointEventManager {

    private static Logger logger = Logger.getLogger(DataPointEventManager.class.getName());
    private RealTimeAlertListener realTimeAlert = new RealTimeAlertListener();
    private BloodGlucoseNotificationListener riskAlert = new BloodGlucoseNotificationListener();

    /**
     * Respond to new data points, messaging each bean that handles onDataPoint events.
     **/
    public void onNewDataPoint(DataPoint dataPoint) {
        logger.log(Level.FINE, "New data point from {0},{1},timestamp={2},value={3}", new Object[]{
                dataPoint.getMedicalDevice().getSerialNumber(),
                dataPoint.getUser().getHandle(),
                dataPoint.getTimestamp(),
                dataPoint.getValue()
            });
        // fire the events to the beans
        realTimeAlert.onNewDataPoint(dataPoint); // TODO; dealock on risk alert after rta sent; due to delayed db statements firing?
        riskAlert.onNewDataPoint(dataPoint);
    }
}
