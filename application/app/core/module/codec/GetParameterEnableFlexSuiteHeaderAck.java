package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterEnableFlexSuiteHeaderAck extends GetParameterAck {

    private boolean enableFlexSuiteHeader;

    public GetParameterEnableFlexSuiteHeaderAck(boolean enableFlexSuiteHeader) {
        this.enableFlexSuiteHeader = enableFlexSuiteHeader;
    }

    public boolean isEnableFlexSuiteHeader() {
        return this.enableFlexSuiteHeader;
    }

    public String toString() {
        return super.toString() + "[enableFlexSuiteHeader=" + enableFlexSuiteHeader + "]";
    }
}
