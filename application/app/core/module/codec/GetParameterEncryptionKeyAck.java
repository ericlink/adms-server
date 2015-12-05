package core.module.codec;

import org.apache.commons.codec.binary.Base64;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterEncryptionKeyAck extends GetParameterAck {

    private byte[] secretKey;

    public GetParameterEncryptionKeyAck(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public String getSecretKeyBase64() {
        return new String(Base64.encodeBase64(secretKey));
    }

    public String toString() {
        return super.toString() + "[secretKeyBase64=" + getSecretKeyBase64() + "]";
    }
}
