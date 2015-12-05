package core.module.codec;

import java.util.Calendar;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterLastChargeTimeAck extends GetParameterAck {

    private Calendar lastChargeTime;

    public GetParameterLastChargeTimeAck(Calendar lastChargeTime) {
        this.lastChargeTime = lastChargeTime;
    }

    public Calendar getLastChargeTime() {
        return this.lastChargeTime;
    }

    public String toString() {
        return super.toString() + "[lastChargeTime=" + lastChargeTime.getTime().toString() + "]";
    }
}