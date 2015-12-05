package core.module;

import core.module.codec.GlucoMonDatabase;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public interface DatabaseMessageEventHandler {

    /**
     * Checks each glucose meter in the message.
     * If it is a known meter, then associate readings with the user that is already hooked to that meter.
     * If it is NOT a known meter, crate a new meter in the db and make the user the module user.
     * Hold onto the last reading in for each different user, and then send alerts based on that user's settings.
     */
    void onMessage(Module module, GlucoMonDatabase glucoMonDatabase) throws ModuleInboundException;
}
