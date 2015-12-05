/*
 * OneTouchUltraParserTest.java
 * JUnit based test
 *
 * Created on November 26, 2007, 3:54 PM
 */
package core.medicaldevice.lifescan;

import org.junit.*;
import play.test.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Date;
import core.datapoint.DataPoint;
import models.User;

/**
 *
 * @author elink
 */
public class OneTouchUltraParserTest extends UnitTest {

    /**
     * Test of getMedicalDeviceSerialNumber method,
     * of class net.diabetech.medicaldevice.lifescan.OneTouchUltraParser.
     */
    @Test
    public void testGetMedicalDeviceSerialNumber() throws Exception {
        System.out.println("getMedicalDeviceSerialNumber");
        String rawData = readEntireFile("testDataFullMeter");
        OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        String expResult = "QSW421ECT";
        String result = instance.getMedicalDevice().
            getSerialNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMedicalDeviceDataRecords method, of class net.diabetech.medicaldevice.lifescan.OneTouchUltraParser.
     */
    @Test
    public void testControlReading() throws Exception {
        System.out.println("testControlReading");
        String rawData = readEntireFile("testDataControlReading");
        OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        List<DataPoint> result = (List<DataPoint>) instance.getMedicalDevice().
            getDataPoints();
        User user = new User();

        // check against strings also to cross-validate generated data
        DateFormat dateFormat = SimpleDateFormat.getInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        result.get(0).
            setUser(user);
        assertEquals("3/3/05 2:00 AM", dateFormat.format(result.get(0).
            getTimestamp()));
        assertEquals(40, result.get(0).
            getTimestamp().
            getSeconds());
        assertEquals(209, result.get(0).
            getValue());
        assertEquals("209", result.get(0).
            getFormattedValue());

        // check control reading parsing
        result.get(1).
            setUser(user);

        assertEquals("3/3/05 12:06 AM", dateFormat.format(result.get(1).
            getTimestamp()));
        assertEquals(24, result.get(1).
            getTimestamp().
            getSeconds());
        assertEquals(96, result.get(1).
            getValue());
        assertTrue(result.get(1).
            isControl());
        assertEquals("C96", result.get(1).
            getFormattedValue());

        // check control reading parsing
        result.get(2).
            setUser(user);

        assertEquals("3/2/05 11:12 PM", dateFormat.format(result.get(2).
            getTimestamp()));
        assertEquals(8, result.get(2).
            getTimestamp().
            getSeconds());
        assertEquals(601, result.get(2).
            getValue());
        assertTrue(!result.get(2).
            isControl());
        assertEquals("HIGH", result.get(2).
            getFormattedValue());

        // check control reading parsing
        result.get(3).
            setUser(user);

        assertEquals("3/2/05 10:23 PM", dateFormat.format(result.get(3).
            getTimestamp()));
        assertEquals(00, result.get(3).
            getTimestamp().
            getSeconds());
        assertEquals(602, result.get(3).
            getValue());
        assertTrue(!result.get(3).
            isControl());
        assertEquals("REXC", result.get(3).
            getFormattedValue());

        // edge test last record
        result.get(4).
            setUser(user);

        assertEquals("3/17/05 7:07 AM", dateFormat.format(result.get(149).
            getTimestamp()));
        assertEquals(8, result.get(149).
            getTimestamp().
            getSeconds());
        assertEquals(131, result.get(149).
            getValue());
    }

    /**
     * Test of getMedicalDeviceDataRecords method, of class net.diabetech.medicaldevice.lifescan.OneTouchUltraParser.
     */
    @Test
    public void testGetMedicalDeviceDataRecords() throws Exception {
        System.out.println("getMedicalDeviceDataRecords");
        String rawData = readEntireFile("testDataFullMeter");
        OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        List<DataPoint> expResult = getFullMeterExpectedResult();
        List<DataPoint> result = (List<DataPoint>) instance.getMedicalDevice().
            getDataPoints();
        for (int i = 0; i < getFullMeterExpectedResult().
            size(); i++) {
            assertEquals(expResult.get(i), result.get(i));
        }

        // check against strings also to cross-validate generated data
        DateFormat dateFormat = SimpleDateFormat.getInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals("3/3/05 2:00 AM", dateFormat.format(result.get(0).
            getTimestamp()));
        assertEquals(40, result.get(0).
            getTimestamp().
            getSeconds());
        assertEquals(209, result.get(0).
            getValue());

        // edge test last record
        assertEquals("3/17/05 7:07 AM", dateFormat.format(result.get(149).
            getTimestamp()));
        assertEquals(8, result.get(149).
            getTimestamp().
            getSeconds());
        assertEquals(131, result.get(149).
            getValue());
    }

    @Test
    public void testFullMeterSerialNoise() throws Exception {
        System.out.println("testFullMeterSerialNoise");
        String rawData = readEntireFile("testDataFullMeterSerialNoise");
        OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        List<DataPoint> expResult = getFullMeterExpectedResult();
        List<DataPoint> result = (List<DataPoint>) instance.getMedicalDevice().
            getDataPoints();
        for (int i = 0; i < getFullMeterExpectedResult().
            size(); i++) {
            assertEquals(expResult.get(i), result.get(i));
        }

        // check against strings also to cross-validate generated data
        DateFormat dateFormat = SimpleDateFormat.getInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals("3/3/05 2:00 AM", dateFormat.format(result.get(0).
            getTimestamp()));
        assertEquals(40, result.get(0).
            getTimestamp().
            getSeconds());
        assertEquals(209, result.get(0).
            getValue());

        // edge test last record
        assertEquals("3/17/05 7:07 AM", dateFormat.format(result.get(149).
            getTimestamp()));
        assertEquals(8, result.get(149).
            getTimestamp().
            getSeconds());
        assertEquals(131, result.get(149).
            getValue());
    }

    @Test
    public void testBadChecksumHeader() throws Exception {
        try {
            System.out.println("testBadChecksumHeader");
            String rawData = readEntireFile("testBadChecksumHeader");
            OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        } catch (ParseException pe) {
            assertEquals(
                "Line [P 150,\"QSW421ECT\",\"MG/DL \" 05CD] incorrect checksum (05C7) expected 05CD",
                pe.getCause().
                getMessage());
        }
    }

    @Test
    public void testBadChecksumData() throws Exception {
        try {
            System.out.println("testBadChecksumData");
            String rawData = readEntireFile("testBadChecksumData");
            OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        } catch (ParseException pe) {
            assertEquals(
                "Line [P \"THU\",\"03/03/04\",\"20:56:24   \",\"  121 \", 00 0825] incorrect checksum (0824) expected 0825",
                pe.getCause().
                getMessage());
        }
    }

    @Test
    public void testIncorrectHeaderLength() throws Exception {
        try {
            System.out.println("testIncorrectHeaderLength");
            String rawData = readEntireFile("testDataHeaderLength");
            OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        } catch (ParseException pe) {
            assertEquals(
                "Header incorrect length (40) expected 31",
                pe.getCause().
                getMessage());
        }
    }

    @Test
    public void testHeaderRecordOnly() throws Exception {
        System.out.println("testHeaderRecordOnly");
        String rawData = readEntireFile("testDataHeaderRecordOnly");
        OneTouchUltraParser instance = new OneTouchUltraParser(rawData);
        String expResult = "QSW421ECT";
        String result = instance.getMedicalDevice().
            getSerialNumber();
        assertEquals(expResult, result);
        assertEquals(0, instance.getMedicalDevice().
            getDataPoints().
            size());
    }
    private static String BASE_PATH = "../../application";

    private String readEntireFile(String fileName) throws FileNotFoundException, IOException {
        FileInputStream is = new FileInputStream(BASE_PATH + "/test/core/medicaldevice/lifescan/data/onetouchultra/" + fileName);
        StringBuilder sb = new StringBuilder();
        int c = -1;
        do {
            c = is.read();
            if (c > -1) {
                sb.append((char) c);
            }
        } while (c > -1);

        return sb.toString();
    }

    private List<DataPoint> getFullMeterExpectedResult() {
        List<DataPoint> dataRecords = new ArrayList<DataPoint>();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.MILLISECOND, 0);
        //P "THU","03/03/05","02:00:40   ","  209 ", 00 081F
        cal.set(2005, 2, 3, 2, 0, 40);

        dataRecords.add(getDataPoint(209, cal.getTime()));
        //P "THU","03/03/05","00:06:24   ","  096 ", 00 0829
        cal.set(2005, 2, 3, 0, 6, 24);
        dataRecords.add(getDataPoint(96, cal.getTime()));
        //P "WED","03/02/05","23:12:08   ","  082 ", 00 0816
        cal.set(2005, 2, 2, 23, 12, 8);
        dataRecords.add(getDataPoint(82, cal.getTime()));
        //P "WED","03/02/05","22:23:00   ","  081 ", 00 080E
        cal.set(2005, 2, 2, 22, 23, 00);
        dataRecords.add(getDataPoint(81, cal.getTime()));
        //P "WED","03/02/05","21:43:56   ","  088 ", 00 0821
        cal.set(2005, 2, 2, 21, 43, 56);
        dataRecords.add(getDataPoint(88, cal.getTime()));
        //P "WED","03/02/05","21:02:32   ","  077 ", 00 0814
        cal.set(2005, 2, 2, 21, 02, 32);
        dataRecords.add(getDataPoint(77, cal.getTime()));
        //P "WED","03/02/05","19:06:00   ","  091 ", 00 0816
        cal.set(2005, 2, 2, 19, 6, 0);
        dataRecords.add(getDataPoint(91, cal.getTime()));
        //P "WED","03/02/05","16:48:00   ","  108 ", 00 0818
        cal.set(2005, 2, 2, 16, 48, 00);
        dataRecords.add(getDataPoint(108, cal.getTime()));
        //P "WED","03/02/05","15:09:48   ","  244 ", 00 0821
        cal.set(2005, 2, 2, 15, 9, 48);
        dataRecords.add(getDataPoint(244, cal.getTime()));
        //P "WED","03/02/05","07:23:08   ","  087 ", 00 081F
        //P "WED","03/02/05","03:10:48   ","  139 ", 00 0819
        //P "WED","03/02/05","00:16:00   ","  131 ", 00 0808
        //P "TUE","03/01/05","21:59:48   ","  144 ", 00 082F
        //P "TUE","03/01/05","21:27:16   ","  086 ", 00 082A
        //P "TUE","03/01/05","19:44:16   ","  303 ", 00 0828
        //P "TUE","03/01/05","17:50:16   ","  149 ", 00 082B
        //P "TUE","03/01/05","15:07:56   ","  277 ", 00 0831
        //P "THU","03/17/05","03:15:08   ","  214 ", 00 082B
        //P "THU","03/17/05","00:09:48   ","  161 ", 00 0830
        //P "WED","03/16/05","21:31:00   ","  210 ", 00 080B
        //P "WED","03/16/05","20:49:32   ","  195 ", 00 0824
        //P "WED","03/16/05","19:50:08   ","  133 ", 00 081F
        //P "WED","03/16/05","17:38:00   ","  228 ", 00 0820
        //P "WED","03/16/05","14:24:32   ","  063 ", 00 081A
        //P "WED","03/16/05","12:42:24   ","  326 ", 00 081B
        //P "WED","03/16/05","12:12:40   ","  134 ", 00 0813
        //P "WED","03/16/05","11:20:16   ","  241 ", 00 0813
        //P "WED","03/16/05","09:49:24   ","  214 ", 00 0824
        //P "WED","03/16/05","07:35:16   ","  084 ", 00 0823
        //P "WED","03/16/05","03:47:56   ","  138 ", 00 0826
        //P "WED","03/16/05","01:12:08   ","  159 ", 00 081C
        //P "TUE","03/15/05","21:18:48   ","  137 ", 00 0831
        //P "TUE","03/15/05","20:30:40   ","  111 ", 00 081A
        //P "TUE","03/15/05","18:36:00   ","  071 ", 00 0828
        //P "TUE","03/15/05","15:42:56   ","  171 ", 00 082E
        //P "TUE","03/15/05","11:45:00   ","  144 ", 00 0822
        //P "TUE","03/15/05","10:18:00   ","  078 ", 00 0827
        //P "TUE","03/15/05","07:42:24   ","  073 ", 00 082B
        //P "TUE","03/15/05","03:03:08   ","  169 ", 00 082C
        //P "TUE","03/15/05","01:08:24   ","  099 ", 00 082F
        //P "MON","03/14/05","20:30:16   ","  159 ", 00 0824
        //P "MON","03/14/05","17:57:00   ","  063 ", 00 0826
        //P "MON","03/14/05","14:58:40   ","  151 ", 00 0826
        //P "MON","03/14/05","11:34:56   ","  207 ", 00 0826
        //P "MON","03/14/05","09:53:24   ","  235 ", 00 082A
        //P "MON","03/14/05","07:14:08   ","  063 ", 00 0826
        //P "MON","03/14/05","02:26:08   ","  113 ", 00 0820
        //P "SUN","03/13/05","23:03:16   ","  216 ", 00 082C
        //P "SUN","03/13/05","21:03:48   ","  341 ", 00 082E
        //P "SUN","03/13/05","19:45:32   ","  398 ", 00 0840
        //P "SUN","03/13/05","19:44:24   ","  415 ", 00 0836
        //P "SUN","03/13/05","19:03:48   ","  361 ", 00 0837
        //P "SUN","03/13/05","17:32:00   ","  129 ", 00 082D
        //P "SUN","03/13/05","14:54:40   ","  114 ", 00 082C
        //P "SUN","03/13/05","14:23:24   ","  126 ", 00 082D
        //P "SUN","03/13/05","10:32:56   ","  087 ", 00 0834
        //P "SUN","03/13/05","07:42:32   ","  065 ", 00 0831
        //P "SUN","03/13/05","00:59:40   ","  241 ", 00 082D
        //P "SAT","03/12/05","21:38:48   ","  266 ", 00 082D
        //P "SAT","03/12/05","20:30:40   ","  066 ", 00 081A
        //P "SAT","03/12/05","17:10:56   ","  065 ", 00 0824
        //P "SAT","03/12/05","15:55:32   ","  195 ", 00 0829
        //P "SAT","03/12/05","12:50:16   ","  238 ", 00 0821
        //P "SAT","03/12/05","10:49:08   ","  228 ", 00 0827
        //P "SAT","03/12/05","10:08:32   ","  259 ", 00 0823
        //P "SAT","03/12/05","07:29:56   ","  154 ", 00 082C
        //P "SAT","03/12/05","01:48:00   ","  142 ", 00 0819
        //P "FRI","03/11/05","23:17:16   ","  295 ", 00 0821
        //P "FRI","03/11/05","19:44:08   ","  312 ", 00 081D
        //P "FRI","03/11/05","17:57:40   ","  142 ", 00 081C
        //P "FRI","03/11/05","15:18:40   ","  158 ", 00 081E
        //P "FRI","03/11/05","11:37:56   ","  197 ", 00 0825
        //P "FRI","03/11/05","09:58:48   ","  244 ", 00 0829
        //P "FRI","03/11/05","06:56:56   ","  173 ", 00 0824
        //P "FRI","03/11/05","00:13:00   ","  200 ", 00 0803
        //P "THU","03/10/05","21:12:24   ","  405 ", 00 0821
        //P "THU","03/10/05","21:09:48   ","  440 ", 00 082C
        //P "THU","03/10/05","17:47:16   ","  155 ", 00 0831
        //P "THU","03/10/05","15:10:00   ","  070 ", 00 081A
        //P "THU","03/10/05","11:36:16   ","  058 ", 00 082B
        //P "THU","03/10/05","10:00:48   ","  164 ", 00 0824
        //P "THU","03/10/05","07:09:56   ","  128 ", 00 0832
        //P "THU","03/10/05","02:45:16   ","  183 ", 00 082A
        //P "THU","03/10/05","02:04:24   ","  070 ", 00 081F
        //P "WED","03/09/05","23:03:48   ","  101 ", 00 0819
        //P "WED","03/09/05","21:05:48   ","  108 ", 00 0820
        //P "WED","03/09/05","19:14:00   ","  258 ", 00 0821
        //P "WED","03/09/05","18:03:32   ","  215 ", 00 081C
        //P "WED","03/09/05","16:50:40   ","  067 ", 00 0820
        //P "WED","03/09/05","16:36:56   ","  047 ", 00 0829
        //P "WED","03/09/05","13:53:08   ","  096 ", 00 0826
        //P "WED","03/09/05","11:36:08   ","  149 ", 00 0824
        //P "WED","03/09/05","10:13:08   ","  147 ", 00 081C
        //P "WED","03/09/05","07:17:08   ","  099 ", 00 082C
        //P "WED","03/09/05","04:07:24   ","  255 ", 00 0820
        //P "WED","03/09/05","00:14:56   ","  324 ", 00 081C
        //P "TUE","03/08/05","21:40:48   ","  338 ", 00 0831
        //P "TUE","03/08/05","20:23:24   ","  132 ", 00 0823
        //P "TUE","03/08/05","19:54:48   ","  038 ", 00 083A
        //P "TUE","03/08/05","19:28:00   ","  123 ", 00 082A
        //P "TUE","03/08/05","17:10:48   ","  120 ", 00 0828
        //P "TUE","03/08/05","13:47:24   ","  129 ", 00 0831
        //P "TUE","03/08/05","11:57:00   ","  114 ", 00 0824
        //P "TUE","03/08/05","10:46:16   ","  072 ", 00 082B
        //P "TUE","03/08/05","07:10:16   ","  135 ", 00 0828
        //P "TUE","03/08/05","05:29:00   ","  286 ", 00 0830
        //P "TUE","03/08/05","02:07:40   ","  421 ", 00 0824
        //P "TUE","03/08/05","02:06:00   ","  440 ", 00 0820
        //P "MON","03/07/05","22:40:00   ","  135 ", 00 081C
        //P "MON","03/07/05","21:17:16   ","  086 ", 00 082B
        //P "MON","03/07/05","20:32:16   ","  064 ", 00 0823
        //P "MON","03/07/05","17:11:08   ","  217 ", 00 0827
        //P "MON","03/07/05","15:37:40   ","  150 ", 00 0825
        //P "MON","03/07/05","11:52:40   ","  134 ", 00 0820
        //P "MON","03/07/05","09:19:56   ","  155 ", 00 0834
        //P "MON","03/07/05","07:00:16   ","  121 ", 00 081D
        //P "MON","03/07/05","02:00:16   ","  129 ", 00 0820
        //P "MON","03/07/05","01:06:56   ","  210 ", 00 0820
        //P "SUN","03/06/05","23:12:00   ","  341 ", 00 0826
        //P "SUN","03/06/05","23:08:24   ","  332 ", 00 0831
        //P "SUN","03/06/05","20:20:56   ","  269 ", 00 0836
        //P "SUN","03/06/05","19:24:08   ","  168 ", 00 083D
        //P "SUN","03/06/05","16:52:40   ","  088 ", 00 0838
        //P "SUN","03/06/05","13:51:24   ","  278 ", 00 0837
        //P "SUN","03/06/05","11:29:00   ","  276 ", 00 0832
        //P "SUN","03/06/05","09:39:32   ","  158 ", 00 083E
        //P "SUN","03/06/05","07:06:00   ","  088 ", 00 0833
        //P "SUN","03/06/05","00:11:08   ","  193 ", 00 082D
        //P "SAT","03/05/05","20:30:08   ","  176 ", 00 0822
        //P "SAT","03/05/05","17:55:16   ","  122 ", 00 0825
        //P "SAT","03/05/05","16:04:24   ","  118 ", 00 0822
        //P "SAT","03/05/05","14:06:56   ","  058 ", 00 082A
        //P "SAT","03/05/05","12:17:24   ","  203 ", 00 081D
        //P "SAT","03/05/05","10:24:00   ","  114 ", 00 0814
        //P "SAT","03/05/05","09:40:16   ","  061 ", 00 0822
        //P "SAT","03/05/05","07:00:48   ","  081 ", 00 0823
        //P "SAT","03/05/05","03:10:40   ","  077 ", 00 081D
        //P "SAT","03/05/05","00:12:32   ","  127 ", 00 0819
        //P "FRI","03/04/05","21:50:08   ","  148 ", 00 081C
        //P "FRI","03/04/05","21:03:16   ","  061 ", 00 0813
        //P "FRI","03/04/05","18:18:24   ","  166 ", 00 0824
        //P "FRI","03/04/05","15:48:24   ","  210 ", 00 081A
        //P "FRI","03/04/05","07:19:00   ","  098 ", 00 0821
        //P "FRI","03/04/05","02:14:08   ","  258 ", 00 081D
        //P "THU","03/03/05","23:25:16   ","  229 ", 00 082E
        //P "THU","03/03/05","20:56:24   ","  121 ", 00 0825
        //P "THU","03/03/05","19:33:56   ","  172 ", 00 0833
        //P "THU","03/03/05","17:40:16   ","  079 ", 00 0831
        //P "THU","03/03/05","16:04:00   ","  067 ", 00 0826
        //P "THU","03/17/05","07:07:08   ","  131 ", 00 082E
        return dataRecords;
    }

    private DataPoint getDataPoint(int value, Date date) {
        DataPoint dp = new DataPoint();
        dp.setValue(value);
        dp.setTimestamp(date);
        return dp;
    }
}
