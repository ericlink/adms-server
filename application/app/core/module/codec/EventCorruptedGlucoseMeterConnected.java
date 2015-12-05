package core.module.codec;

import java.util.Date;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class EventCorruptedGlucoseMeterConnected extends Event {

    public EventCorruptedGlucoseMeterConnected(Date messageSubmitDate) {
        this.messageSubmitDate = messageSubmitDate;
    }
}
