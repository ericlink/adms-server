package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class EraseCommandFailedResponse extends FailedResponse {

    public EraseCommandFailedResponse(int parameterId, String failedReason) {
        super(parameterId, failedReason);
    }
}