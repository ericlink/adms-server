package core.module;

import core.module.codec.InboundMessage;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public interface ModuleInbound {

    void onMessage(Module module, InboundMessage message) throws ModuleInboundException;
}
