package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public interface SendOtaMessage {

    /**** SET / GET PARAMETERS ****/
    public void setParameterTimeSetting(GlucoMon glucoMon, boolean updateGlucoseMeterWithNetworkTime, int profileTimeZoneUtcOffsetMinutes);

    public void getParameterTimeSetting(GlucoMon glucoMon);

    public void setParameterEnableFlexSuiteHeader(GlucoMon glucoMon, boolean enableFlexSuiteHeader);

    public void getParameterEnableFlexSuiteHeader(GlucoMon glucoMon);

    public void setParameterEnableEncryption(GlucoMon glucoMon, boolean enableEncryption);

    public void getParameterEnableEncryption(GlucoMon glucoMon);

    public void setParameterMessageCount(GlucoMon glucoMon, byte stored, byte sent, byte failed);

    public void getParameterMessageCount(GlucoMon glucoMon);

    public void setParameterEncryptionKey(GlucoMon glucoMon, byte[] secretKey);

    public void getParameterEncryptionKey(GlucoMon glucoMon);

    public void setParameterResetEncryptionKey(GlucoMon glucoMon);

    public void setParameterRadioSleep(GlucoMon glucoMon, int startTimeMinutes, int endTimeMinutes);

    public void getParameterRadioSleep(GlucoMon glucoMon);

    public void setParameterAlarmLimit(GlucoMon glucoMon, int lowLimit, int highLimit);

    public void getParameterAlarmLimit(GlucoMon glucoMon);

    public void setParameterAutoDeleteGlucoseMeter(GlucoMon glucoMon, boolean autoDeleteGlucoseMeter);

    public void getParameterAutoDeleteGlucoseMeter(GlucoMon glucoMon);

    public void setParameterMailToAddress(GlucoMon glucoMon, String newMailToAddress);

    public void getParameterMailToAddress(GlucoMon glucoMon);

    public void getParameterBatteryStatus(GlucoMon glucoMon);

    public void getParameterBatteryVoltage(GlucoMon glucoMon);

    public void getParameterDutyCycles(GlucoMon glucoMon);

    public void getParameterLastChargeTime(GlucoMon glucoMon);

    public void setParameterBatteryOffset(GlucoMon glucoMon, short batteryOffset);

    public void getParameterSoftwareVersion(GlucoMon glucoMon);

    public void setParameterAutoSendNetworkInfo(GlucoMon glucoMon, boolean autoSendNetoworkInfo);

    public void getParameterAutoSendNetworkInfo(GlucoMon glucoMon);

    public void getParameterNetworkInfo(GlucoMon glucoMon);

    public void setParameterDebugInfo(GlucoMon glucoMon, boolean isDebugOn);

    public void getParameterDebugInfo(GlucoMon glucoMon);

    /****  ERASE DMD COMMANDS ****/
    public void eraseGlucoMon(GlucoMon glucoMon, boolean onlyEraseUndeliveredReadings);

    public void eraseGlucoMon(GlucoMon glucoMon, boolean onlyEraseUndeliveredReadings, String serialNumber);

    public void eraseGlucoMonAllSerialNumbersAndReadings(GlucoMon glucoMon);

    public void eraseGlucoMonSerialNumberAndReadings(GlucoMon glucoMon, String serialNumber);

    /**** GET DATABASE COMMANDS ****/
    public void getReadings(GlucoMon glucoMon, boolean onlySendUndeliveredReadings);

    public void getReadings(GlucoMon glucoMon, boolean onlySendUndeliveredReadings, String serialNumber);

    public void getGlucoseMeterSerialNumbers(GlucoMon glucoMon);

    public void getDiagnosticInfo(GlucoMon glucoMon, byte diagnosticIndex);

    public void getAllParameters(GlucoMon glucoMon);

    /**** SEND EVENTS ****/
    public void sendEventReadGlucoseMeter(GlucoMon glucoMon);

    public void sendEventDeletePendingCommands(GlucoMon glucoMon);

    public void sendAcknowledgement(GlucoMon glucoMon);
}
