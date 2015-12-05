package core.report.dayoverday;

import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;
import core.datapoint.DataPoint;
import helper.util.DateHelper;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006, 2011 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DayOverDayDataSourceTest extends UnitTest {

    @Test
    public void testFromDb() throws Exception { // DAO test plus date range test, move to dao tests, remove test dependency on this stuff.
        Calendar reportDate = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        reportDate.setTime(new Date(DateHelper.MILLIS_PER_DAY * 21));

        User u = new User();
        u.setHandle("HANDLE");

        Collection<DataPoint> dataPoints = getDataPoints();
        // 3 wks, 1 per hour, 504 data points (dupe check is working)
        assertEquals(21 * 24, dataPoints.size());
        for (DataPoint dp : dataPoints) {
            //System.out.println("dp ts="+dp.getTimestamp());
            dp.setUser(u);
        }

        DayOverDayDataSource ds = new DayOverDayDataSource(u, reportDate, dataPoints);
        while (ds.next()) {
            // TODO check each report cell for expected values/formatting
//"timeStamp"
//"reportDate"
//"handle"
//"bg0"
//"bg1"
            assertEquals("HANDLE", ds.getField("handle"));
            assertNotNull(ds.getField("reportDate"));
        }

    }

    private Collection<DataPoint> getDataPoints() {
        Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();

        long zeroPlusThreeWeeks = DateHelper.MILLIS_PER_DAY * 21;
        zeroPlusThreeWeeks -= (DateHelper.MILLIS_PER_MINUTE * 30); //on the 1/2 hour
        int threeWeeksHours = 24 * 7 * 3;

        for (int i = 0; i < threeWeeksHours; i++) {
            DataPoint dp = new DataPoint();
            dp.setTimestamp(new Date(zeroPlusThreeWeeks));
            dp.setValue(i);
            dataPoints.add(dp);
            zeroPlusThreeWeeks -= DateHelper.MILLIS_PER_HOUR;
            //System.out.println("TS:"+dp.getTimestamp());
        }

        return dataPoints;
    }
}
