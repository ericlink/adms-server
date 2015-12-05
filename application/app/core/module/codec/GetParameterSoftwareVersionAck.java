package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterSoftwareVersionAck extends GetParameterAck {

    int softwareVersion;

    public GetParameterSoftwareVersionAck(int softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public int getSoftwareVersion() {
        return softwareVersion;
    }

    public String toString() {
        return super.toString() + "[softwareVersion=" + softwareVersion + "]";
    }
}
