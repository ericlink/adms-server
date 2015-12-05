package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterOtaTimeSettingsAck extends GetParameterAck {

    private boolean updateGlucoseMeterWithNetworkTime;
    private int profileTimeZoneUtcOffsetMinutes;

    public GetParameterOtaTimeSettingsAck(boolean updateGlucoseMeterWithNetworkTime, int profileTimeZoneUtcOffsetMinutes) {
        this.updateGlucoseMeterWithNetworkTime = updateGlucoseMeterWithNetworkTime;
        this.profileTimeZoneUtcOffsetMinutes = profileTimeZoneUtcOffsetMinutes;
    }

    public static void main(String[] args) {
    }

    public boolean isUpdateGlucoseMeterWithNetworkTime() {
        return this.updateGlucoseMeterWithNetworkTime;
    }

    public void setUpdateGlucoseMeterWithNetworkTime(boolean updateGlucoseMeterWithNetworkTime) {
    }

    public int getProfileTimeZoneUtcOffsetMinutes() {
        return this.profileTimeZoneUtcOffsetMinutes;
    }

    public String toString() {
        return super.toString() + "[updateGlucoseMeterWithNetworkTime=" + updateGlucoseMeterWithNetworkTime + ",profileTimeZoneUtcOffsetMinutes=" + profileTimeZoneUtcOffsetMinutes + "]";
    }
}
