package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterDutyCyclesAck extends GetParameterAck {

    private int trickleCycle;
    private int fastCycle;

    public GetParameterDutyCyclesAck(int trickleCycle, int fastCycle) {
        this.trickleCycle = trickleCycle;
        this.fastCycle = fastCycle;
    }

    public int getTrickleCycle() {
        return this.trickleCycle;
    }

    public int getFastCycle() {
        return this.fastCycle;
    }

    public String toString() {
        return super.toString() + "[trickleCycle=" + trickleCycle + ",fastCycle=" + fastCycle + "]";
    }
}