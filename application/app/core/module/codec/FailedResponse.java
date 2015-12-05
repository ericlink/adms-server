package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class FailedResponse extends InboundMessage {

    protected int parameterId;
    protected String failedReason;

    public FailedResponse(int parameterId, java.lang.String failedReason) {
        this.parameterId = parameterId;
        this.failedReason = failedReason;
    }

    public int getParameterId() {
        return this.parameterId;
    }

    public String getFailedReason() {
        return this.failedReason;
    }

    public String toString() {
        return super.toString() + "[parameterId=0x" + Integer.toHexString(parameterId) + ",failedReason=" + failedReason + "]";
    }
}
