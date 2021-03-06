package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterBatteryVoltageAck extends GetParameterAck {

    private int batteryVoltage;

    public GetParameterBatteryVoltageAck(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public int getBatteryVoltage() {
        return this.batteryVoltage;
    }

    public String toString() {
        return super.toString() + "[batteryVoltage=" + batteryVoltage + "]";
    }
}