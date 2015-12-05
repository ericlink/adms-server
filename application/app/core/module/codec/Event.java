package core.module.codec;

import java.util.Date;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public abstract class Event extends InboundMessage {

    Date messageSubmitDate;

    public Event() {
    }

    public Date getTimeStamp() {
        return messageSubmitDate;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append("[messageSubmitDate=");
        sb.append(messageSubmitDate);
        sb.append("]");
        return sb.toString();
    }
}
