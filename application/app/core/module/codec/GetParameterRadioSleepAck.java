package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterRadioSleepAck extends GetParameterAck {

    private int startTimeMinutes;
    private int endTimeMinutes;

    public GetParameterRadioSleepAck(int startTimeMinutes, int endTimeMinutes) {
        this.startTimeMinutes = startTimeMinutes;
        this.endTimeMinutes = endTimeMinutes;
    }

    public int getEndTimeMinutes() {
        return this.endTimeMinutes;
    }

    public int getStartTimeMinutes() {
        return this.startTimeMinutes;
    }

    public String toString() {
        return super.toString() + "[startTimeMinutes=" + startTimeMinutes + ",endTimeMinutes=" + endTimeMinutes + "]";
    }
}