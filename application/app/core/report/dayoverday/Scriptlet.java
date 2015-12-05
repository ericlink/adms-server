package core.report.dayoverday;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006, 2011 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class Scriptlet extends JRDefaultScriptlet {

    public Boolean isHigh(String rangeIndicator) throws JRScriptletException {
        return rangeIndicator.equals(DayOverDayDataSource.HIGH);
    }

    public Boolean isLow(String rangeIndicator) throws JRScriptletException {
        return rangeIndicator.equals(DayOverDayDataSource.LOW);
    }

    public Boolean isInRange(String rangeIndicator) throws JRScriptletException {
        return rangeIndicator.equals(DayOverDayDataSource.DEFAULT);
    }

    public Boolean isMultiple(String value) throws JRScriptletException {
        return value.indexOf("\n") > -1 ? Boolean.TRUE : Boolean.FALSE;
    }
}
