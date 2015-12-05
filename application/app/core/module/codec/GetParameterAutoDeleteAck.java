package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterAutoDeleteAck extends GetParameterAck {

    private boolean autoDeleteGlucoseMeter;

    public GetParameterAutoDeleteAck(boolean autoDeleteGlucoseMeter) {
        this.autoDeleteGlucoseMeter = autoDeleteGlucoseMeter;
    }

    public boolean isAutoDeleteGlucoseMeter() {
        return this.autoDeleteGlucoseMeter;
    }

    public String toString() {
        return super.toString() + "[autoDeleteGlucoseMeter=" + autoDeleteGlucoseMeter + "]";
    }
}
