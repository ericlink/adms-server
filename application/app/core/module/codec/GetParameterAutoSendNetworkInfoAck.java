package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterAutoSendNetworkInfoAck extends GetParameterAck {

    private boolean autoSendNetworkInfo;

    public GetParameterAutoSendNetworkInfoAck(boolean autoSendNetworkInfo) {
        this.autoSendNetworkInfo = autoSendNetworkInfo;
    }

    public boolean isAutoSendNetworkInfo() {
        return this.autoSendNetworkInfo;
    }

    public String toString() {
        return super.toString() + "[autoSendNetworkInfo=" + autoSendNetworkInfo + "]";
    }
}
