package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterMessageCountAck extends GetParameterAck {

    private int stored;
    private int sent;
    private int failed;

    public GetParameterMessageCountAck(int stored, int sent, int failed) {
        this.stored = stored;
        this.sent = sent;
        this.failed = failed;
    }

    public int getStoredCount() {
        return this.stored;
    }

    public int getSentCount() {
        return this.sent;
    }

    public int getFailedCount() {
        return this.failed;
    }

    public String toString() {
        return super.toString() + "[stored=" + stored + ",sent=" + sent + ",failed=" + failed + "]";
    }
}