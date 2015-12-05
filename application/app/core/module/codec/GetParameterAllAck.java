package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterAllAck extends GetParameterAck {

    java.util.Collection<GetParameterAck> parameters = new java.util.ArrayList<GetParameterAck>();

    public GetParameterAllAck(
        GetParameterOtaTimeSettingsAck ota,
        GetParameterRadioSleepAck radioSleep,
        GetParameterAlarmLimitAck alarmLimit,
        GetParameterAutoDeleteAck autoDelete,
        GetParameterBatteryStatusAck batteryStatus,
        GetParameterSoftwareVersionAck softwareVersion,
        GetParameterLedStateAck ledState,
        GetParameterAutoSendNetworkInfoAck autoSend,
        GetParameterNetworkInfoAck infoAck,
        GetParameterEnableFlexSuiteHeaderAck enableFlexSuiteHeader,
        GetParameterMessageCountAck messageCountAck,
        GetParameterEnableEncryptionAck enableEncryption,
        GetParameterDebugInfoAck debugInfo,
        GetParameterBatteryVoltageAck batteryVoltage,
        GetParameterDutyCyclesAck dutyCycles,
        GetParameterLastChargeTimeAck chargeTime,
        GetParameterBatteryLowAck batteryLowVoltage,
        GetParameterBatteryOffsetAck batteryOffsetVoltage,
        GetParameterBatteryMaxAck batteryMaxVoltage,
        GetParameterMailToAck mailTo) {
        parameters.add(ota);
        parameters.add(radioSleep);
        parameters.add(alarmLimit);
        parameters.add(autoDelete);
        parameters.add(batteryStatus);
        parameters.add(softwareVersion);
        parameters.add(ledState);
        parameters.add(autoSend);
        parameters.add(infoAck);
        parameters.add(enableFlexSuiteHeader);
        parameters.add(messageCountAck);
        parameters.add(enableEncryption);
        parameters.add(debugInfo);
        parameters.add(batteryVoltage);
        parameters.add(dutyCycles);
        parameters.add(chargeTime);
        parameters.add(batteryLowVoltage);
        parameters.add(batteryOffsetVoltage);
        parameters.add(batteryMaxVoltage);
        parameters.add(mailTo);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (GetParameterAck ack : parameters) {
            sb.append("\n");
            sb.append(ack.toString());
        }
        return sb.toString();
    }
}
