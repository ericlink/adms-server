package core.medicaldevice.lifescan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import core.datapoint.DataPoint;
import core.medicaldevice.MedicalDevice;
import core.type.UnitOfMeasureType;

/**
 * From One Touch Ultra comm spec:
 * DMP
 * HEADER
 * P nnn,“ MeterSN ”,“MG/DL ”
 * (1)     (2)           (3)
 * (1) Number of datalog records to follow (0 – 150)
 * (2) Meter serial number (9 characters)
 * (3) Unit of measure for glucose values
 *
 * Day of week (SUN, MON, TUE, WED, THU, FRI, SAT)
 * Date of reading
 * Time of reading (If two or more readings were taken within the same minute,
 * they will be
 * separated by 8 second intervals)
 *
 * (7) Result format:
 *      “ nnn ” - blood test result (mg/dL)
 *      “ HIGH ” - blood test result >600 mg/dL
 *      “C nnn ” - control solution test result (mg/dL)
 *      “CHIGH ” - control solution test result >600 mg/dL
 *
 **/
public class OneTouchUltraParser {

    private static Logger logger = Logger.getLogger(OneTouchUltraParser.class.getName());
    private String rawData;
    private int headerRecordCount;
    private UnitOfMeasureType unitOfMeasure;
    private String[] lines;
    private MedicalDevice medicalDevice = new MedicalDevice();
    static final private int HEADER_LENGTH = 31;
    static final private int LINE_LENGTH_ULTRA = 50;
    static final private int LINE_LENGTH_ULTRA2 = 59;
    static final private int MIN_RAW_DATA_LENGTH = HEADER_LENGTH;

    public OneTouchUltraParser(String rawData) throws ParseException {
        if (rawData == null || rawData.length() < MIN_RAW_DATA_LENGTH) {
            throw new ParseException("Data null or too short [" + rawData + "]");
        }
        this.rawData = rawData;
        // only create a valid OneTouchUltraParser
        // object if parse is successful
        // and an exception hasn't been thrown'
        parse();
    }

    /**
     * Parse and validate the data
     **/
    private void parse() throws ParseException {
        try {
            parseLines();
            preParseValidation();
            parseHeader();
            parseDataRecords();
            postParseValidation();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getMessage(), t);
            throw new ParseException(t);
        }
    }

    private void preParseValidation() throws ParseException {
        if (!(lines.length >= 1)) {
            throw new ParseException("Must have at least one line (header is always present)");
        }
        checkLineLength();
        validateChecksums();
    }

    private void postParseValidation() throws ParseException {
        if (headerRecordCount != medicalDevice.getDataPoints().size()) {
            throw new ParseException("Datarecords parsed (" + medicalDevice.getDataPoints().size() + ") does not match header record count (" + headerRecordCount + ")");
        }
    }

    private void checkLineLength() throws ParseException {
        // header is required
        if (lines[0].length() != HEADER_LENGTH) {
            throw new ParseException("Header incorrect length (" + lines[0].length() + ") expected " + HEADER_LENGTH);
        }

        // check any lines after header (optional)
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() != LINE_LENGTH_ULTRA && lines[i].length() != LINE_LENGTH_ULTRA2) {
                throw new ParseException("Line [" + i + "] incorrect length (" + lines[i].length() + ") expected " + LINE_LENGTH_ULTRA + " or " + LINE_LENGTH_ULTRA2);
            }
        }
    }

    private void validateChecksums() throws ParseException {
        for (String line : lines) {
            validateChecksumForLine(line);
        }
    }

    private void validateChecksumForLine(String line) throws ParseException {
        int lastDataCharacter = line.lastIndexOf(" ");
        String payloadChecksum = line.substring(lastDataCharacter + 1, line.length());
        int calculatedChecksum = 0;
        String dataWithoutChecksum = line.substring(0, lastDataCharacter);
        for (Character c : dataWithoutChecksum.toCharArray()) {
            calculatedChecksum += (int) c;
        }
        String calculatedChecksumString = String.format("%4s", Integer.toHexString(calculatedChecksum).toUpperCase());
        calculatedChecksumString = calculatedChecksumString.replace(" ", "0");
        if (!calculatedChecksumString.equals(payloadChecksum)) {
            throw new ParseException("Line [" + line + "] incorrect checksum (" + calculatedChecksumString + ") expected " + payloadChecksum);
        }
    }

    /**
     * P 150,"QSW421ECT","MG/DL " 05C7
     **/
    private void parseHeader() throws ParseException {
        headerRecordCount = Integer.parseInt(lines[0].substring(2, 5));
        medicalDevice.setSerialNumber(lines[0].substring(7, 16));
        String unitOfMeasureString = lines[0].substring(19, 25).trim();
        logger.log(Level.INFO, "Unit of measure in header is {0}", unitOfMeasureString);
//        UOM for lifescan data points is always MG/DL,
//        Display is what varies
//        if ( unitOfMeasureString.equals( "MG/DL" ) ) {
//            this.unitOfMeasure = UnitOfMeasureType.MM_DL;
//        } else {
//            throw new ParseException ( "Unknown Unit of Measure (" + unitOfMeasureString + ") found." );
//        }
        this.unitOfMeasure = UnitOfMeasureType.MG_DL;
    }

    /**
     * P "THU","03/03/05","02:00:40   ","  209 ", 00 081F
     * @see net.diabetech.entity.DataPoint.LifeScanOneTouchUltraFormatter
     **/
    private void parseDataRecords() {
        for (int i = 1; i < lines.length; i++) {
            boolean isControl = "C".equals(lines[i].substring(34, 35));
            String valueString = lines[i].substring(35, 40).trim();
            int value = 0;
            boolean isHigh = false;
            if (valueString.equals("HIGH")) {
                value = 601;
                isHigh = true;
            } else if (valueString.equals("REXC")) {
                value = 602;
                isHigh = true;
            } else {
                value = Integer.parseInt(valueString);
                isHigh = false;
            }

            // timestamp
            int month = Integer.parseInt(lines[i].substring(9, 11));
            int day = Integer.parseInt(lines[i].substring(12, 14));
            int year = Integer.parseInt(lines[i].substring(15, 17));
            int hour = Integer.parseInt(lines[i].substring(20, 22));
            int min = Integer.parseInt(lines[i].substring(23, 25));
            int sec = Integer.parseInt(lines[i].substring(26, 28));
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.set(year + 2000, month - 1, day, hour, min, sec);
            cal.set(Calendar.MILLISECOND, 0);

            DataPoint dp = new DataPoint();
            dp.setIsControl(isControl);
            dp.setValue(value);
            dp.setUnitOfMeasureCode(unitOfMeasure);
            dp.setTimestamp(cal.getTime());
            dp.setOriginated(new Date());

            medicalDevice.addDataPoint(dp);
            logger.log(Level.FINE, "Parsed datapoint timestamp={0},value={1},isControl={2},unitOfMeaure={3}", new Object[]{cal.getTime(), value, isControl, unitOfMeasure});
        }
    }

    /**
     * Remove serial noise from the lines, put them in an array that will be 
     * checked using checksum and line length tests
     **/
    private void parseLines() {
        List<String> cleanLines = new ArrayList<String>();
        String[] rawLines = rawData.split("\n");
        for (int i = 0; i < rawLines.length; i++) {
            String cleaned = rawLines[i].trim();
            if (cleaned.length() > 0) {
                cleanLines.add(cleaned);
            }
        }
        lines = new String[cleanLines.size()];
        lines = cleanLines.toArray(lines);
    }

    public MedicalDevice getMedicalDevice() {
        return medicalDevice;
    }
}
