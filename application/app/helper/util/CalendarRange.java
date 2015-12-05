package helper.util;

import java.util.Calendar;

/**   
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class CalendarRange {

    private Calendar start;
    private Calendar end;

    public CalendarRange(Calendar start, Calendar end) {
        this.setStart(start);
        this.setEnd(end);
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }
}