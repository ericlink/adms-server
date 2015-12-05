package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterMailToAck extends GetParameterAck {

    private String emailAddress;

    public GetParameterMailToAck(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String toString() {
        return super.toString() + "[emailAddress=" + emailAddress + "]";
    }
}
