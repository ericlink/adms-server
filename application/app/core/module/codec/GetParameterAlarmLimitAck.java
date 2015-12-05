package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterAlarmLimitAck extends GetParameterAck {

    private int lowLimit;
    private int highLimit;

    public GetParameterAlarmLimitAck(int lowLimit, int highLimit) {
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
    }

    public int getLowLimit() {
        return this.lowLimit;
    }

    public int getHighLimit() {
        return this.highLimit;
    }

    public String toString() {
        return super.toString() + "[lowLimit=" + lowLimit + ",highLimit=" + highLimit + "]";
    }
}
