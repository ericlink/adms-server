package core.event;

import java.util.logging.Logger;
import java.util.logging.Level;
import core.medicaldevice.MedicalDevice;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class MedicalDeviceEventManager {

    private static Logger logger = Logger.getLogger(MedicalDeviceEventManager.class.getName());
    private NewMedicalDeviceAlertListener newMedicalDeviceAlert = new NewMedicalDeviceAlertListener();

    public void onNewMedicalDevice(MedicalDevice medicalDevice) {
        logger.log(Level.FINE, "New medical device: serialNumber={0}", medicalDevice.getSerialNumber());
        newMedicalDeviceAlert.onNewMedicalDevice(medicalDevice);
    }
}
