package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterDebugInfoAck extends GetParameterAck {

    private boolean enableDebugInfo;

    public GetParameterDebugInfoAck(boolean enableDebugInfo) {
        this.enableDebugInfo = enableDebugInfo;
    }

    public boolean isDebugInfoEnabled() {
        return this.enableDebugInfo;
    }

    public String toString() {
        return super.toString() + "[enableDebugInfo=" + enableDebugInfo + "]";
    }
}
