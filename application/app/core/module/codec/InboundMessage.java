package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public abstract class InboundMessage {

    public InboundMessage() {
    }

    public String toString() {
        return this.getClass().getName();
    }
}
