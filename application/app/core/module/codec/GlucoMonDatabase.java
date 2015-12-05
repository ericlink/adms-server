package core.module.codec;

import core.medicaldevice.MedicalDevice;

/**   
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Contains medical devices with serial numbers but no user association.  
 * Produced by parsing the over the air message and representing
 * the GlucoMON database as a set of medical devices with serial numbers,
 * and a set of datapoints for each of the medical devices.
 * The user association must be determined by the business logic interacting
 * with the persistence layer after the parsing is done.
 *
 * NOTE:
 * Consider promoting to entity package as MedicalDeviceDatabase
 * (it is just a medical device collection w/ some additional state, e.g. isAlarm)
 *
 **/
public class GlucoMonDatabase extends InboundMessage {

    private boolean isAlarmSend;
    private java.util.Collection<MedicalDevice> glucoseMeters;

    public GlucoMonDatabase() {
        glucoseMeters = new java.util.ArrayList<MedicalDevice>();
    }

    public GlucoMonDatabase(boolean isAlarmSend) {
        this();
        this.isAlarmSend = isAlarmSend;
    }

    public boolean isAlarmSend() {
        return this.isAlarmSend;
    }

    public java.util.Collection<MedicalDevice> getGlucoseMeters() {
        return glucoseMeters;
    }

    public void addGlucoseMeter(MedicalDevice glucoseMeter) {
        this.glucoseMeters.add(glucoseMeter);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append("[isAlarmSend=");
        sb.append(isAlarmSend);
        sb.append(",glucoseMeters=");
        sb.append(glucoseMeters.size());
        sb.append(", ");
        java.util.Iterator i = glucoseMeters.iterator();
        while (i.hasNext()) {
            sb.append("\n");
            sb.append(i.next().toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
