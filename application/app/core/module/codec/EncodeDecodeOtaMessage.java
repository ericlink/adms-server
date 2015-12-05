package core.module.codec;

import java.io.*;
import java.security.InvalidKeyException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import helper.lang.IntegerHelper;
import core.medicaldevice.MedicalDevice;
import core.datapoint.DataPoint;
import helper.util.DateHelper;
import org.apache.commons.codec.binary.Base64;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class EncodeDecodeOtaMessage {

    private static Logger logger = Logger.getLogger(EncodeDecodeOtaMessage.class.getName());
    // PARAMETER
    private final static int SET_PARAMETER = 0x10;
    private final static int SET_PARAMETER_ACK = 0x1A;
    private final static int SET_PARAMETER_FAILED = 0x1F;
    private final static int GET_PARAMETER = 0x20;
    private final static int GET_PARAMETER_ACK = 0x2A;
    private final static int GET_PARAMETER_FAILED = 0x2F;
    private final static int PARAMETER_GET_ALL_PARAMETERS = 0x00;
    private final static int PARAMETER_OTA_TIME_SETTINGS = 0x10;
    private final static int PARAMETER_RADIO_SLEEP = 0x20;
    private final static int PARAMETER_ALARM_LIMIT = 0x30;
    private final static int PARAMETER_AUTODELETE = 0x40;
    private final static int PARAMETER_BATTERY_STATUS = 0x50;
    private final static int PARAMETER_SOFTWARE_VERSION = 0x51;
    private final static int PARAMETER_LED_STATE = 0x52;
    private final static int PARAMETER_MAIL_TO_ADDRESS = 0x60;
    private final static int PARAMETER_AUTO_SEND_NETWORK_INFO = 0x70;
    private final static int PARAMETER_NETWORK_INFO = 0x71;
    private final static int PARAMETER_ENABLE_FLEXSUITE_HEADER = 0x72;
    private final static int PARAMETER_MESSAGE_COUNTS = 0x73;
    private final static int PARAMETER_ENABLE_ENCRYPTION = 0x80;
    private final static int PARAMETER_ENCRYPTION_KEY = 0x81;
    private final static int PARAMETER_RESET_ENCRYPTION_KEY = 0x82;
    private final static int PARAMETER_SET_DEBUG_INFO = 0x90;
    private final static int PARAMETER_BATTERY_VOLTAGE_MV = 0x91;
    private final static int PARAMETER_DUTY_CYCLES = 0x92;
    private final static int PARAMETER_LAST_CHARGE_TIME = 0x93;
    private final static int PARAMETER_BATTERY_LOW = 0x94;
    private final static int PARAMETER_BATTERY_OFFSET = 0x95;
    private final static int PARAMETER_BATTERY_MAX = 0x96;
    //
    //0x91 battery Voltage ( in mV )  0x0FA0 = 4000mV
    //0x92 duty cycles	byte1 = trickle charge; byte2 = fast charge
    //0x93 battery last charge time ( 3 byte )
    //		LastChargeTime = Date19bit(&Time, &Date);
    // ERASE
    private final static int ERASE_COMMAND = 0x30;
    private final static int ERASE_COMMAND_ACK = 0x3A;
    private final static int ERASE_COMMAND_FAILED = 0x3F;
    private final static int ERASE_ALL_READINGS = 0x10; //Erase all readings, no [SN] needed. [SN] is not deleted
    private final static int ERASE_ALL_READINGS_FOR_SN = 0x11; //Erase all readings of specified [SN]. [SN] is not deleted
    private final static int ERASE_ALL_DELIVERED_READINGS = 0x20; //Erase all delivered readings, no [SN] needed. [SN] is kept
    private final static int ERASE_ALL_DELIVERED_READINGS_FOR_SN = 0x21; //Erase all delivered readings of specified [SN]. [SN] is not deleted
    private final static int ERASE_ALL_SN_ALL_ENTRIES = 0x30; //Erase  all [SN] and all entries
    private final static int ERASE_ALL_ENTRIES_FOR_SN = 0x31; //Erase [SN] and all entries of specific [SN]
    // RETRIEVE
    private final static int RETRIEVE_COMMAND = 0x40;
    private final static int RETRIEVE_COMMAND_RESPONSE = 0x4A;
    private final static int RETRIEVE_COMMAND_RESPONSE_FAILED = 0x4F;
    private final static int RETRIEVE_ALL_READINGS = 0x10;
    private final static int RETRIEVE_ALL_READINGS_FOR_SN = 0x11;
    private final static int RETRIEVE_UNDELIVERED_READINGS = 0x20;
    private final static int RETRIEVE_UNDELIVERED_READINGS_FOR_SN = 0x21;
    private final static int RETRIEVE_SN_LIST = 0x30;
    private final static int SEND_READING_NORMAL_RESPONSE = 0x5A;
    private final static int SEND_READING_ALARM_RESPONSE = 0x5F;
    private final static int TIME_CORRECTION_READING_FLAG_VALUE = 1020;
    private final static int CT_MASK = 0x0800; //Control Test bit flag
    private final static int TC_MASK = 0x0400; //Time Correct bit flag
    private final static int TC_VALUE_MASK = 0x03FC; //1020 in reading value for a tc (2 flags?)
    private final static int READING_VALUE_MASK = 0x03FF; // 10 bit reading from 2 bytes
    private final static int GET_DIAGNOSTIC_COMMAND = 0x60;
    // EVENT
    private final static int EVENT_READ_GM = 0xF0;
    private final static int EVENT_BATTERY_LOW = 0xF1;
    private final static int EVENT_BATTERY_FULL = 0xF2;
    private final static int EVENT_MAX_SN_EXCEEDED = 0xF3;
    private final static int EVENT_CORRUPTED_GM_CONNECTED = 0xF4;
    private final static int EVENT_RADIO_RESET = 0xF6;
    private final static int EVENT_DELETE_PENDING_COMMANDS = 0xFF;
    // FAILED
    private final static int FAILED_INVALID_COMMAND = 0x01;
    private final static int FAILED_INVALID_PARAMETER_ID = 0x02;
    private final static int FAILED_INVALID_PARAMETER_VALUE = 0x03;
    private final static int FAILED_INVALID_COMMAND_LENGTH = 0x04;
    private final static int FAILED_PARAMETER_NOT_CHANGEABLE = 0x05;
    private final static int FAILED_ERROR_NOT_SPECIFIED = 0x06;
    // INVALID
    private final static int INVALID_MSG_CONTENT = 0x0F;

    private EncodeDecodeOtaMessage() {
    }

    static {
        DateHelper.setDefaultTimeZone();
    }

    /*################# Helper Methods ##################*/
    public static String toHexString(int b) {
        String hexString = Integer.toHexString(b);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString.toUpperCase();
    }

    public static String toHexString(byte b) {
        String hexString = Integer.toHexString((byte) b);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        } else if (hexString.length() > 2) {
            hexString = hexString.substring(hexString.length() - 2, hexString.length());
        }

        return hexString.toUpperCase();
    }

    public static String toHexString(byte[] b) {
        String hexString = "";
        for (int i = 0; i < b.length; i++) {
            hexString += toHexString(b[i]);
        }
        return hexString.toUpperCase();
    }

    public static String toHexString(int[] b) {
        String hexString = "";

        for (int i = 0; i < b.length; i++) {
            hexString += toHexString(b[i]);
        }
        return hexString.toUpperCase();
    }

    public static String toIntString(int[] b) {
        String intString = "";

        for (int i = 0; i < b.length; i++) {
            intString += Integer.valueOf(String.valueOf(b[i])) + ",";
        }
        return intString;
    }

    public static String toCharString(int[] b) {
        String charString = "";

        for (int i = 0; i < b.length; i++) {
            charString += String.valueOf((char) b[i]);
        }
        return charString;
    }

    private static boolean isEncryptedOutboundMessage(int[] message) {
        return (message[0] == SET_PARAMETER && message[2] == PARAMETER_ENCRYPTION_KEY)
            || (message[0] == RETRIEVE_COMMAND && message[2] == RETRIEVE_ALL_READINGS_FOR_SN)
            || (message[0] == RETRIEVE_COMMAND && message[2] == RETRIEVE_UNDELIVERED_READINGS_FOR_SN)
            || (message[0] == ERASE_COMMAND && message[2] == ERASE_ALL_ENTRIES_FOR_SN)
            || (message[0] == ERASE_COMMAND && message[2] == ERASE_ALL_READINGS_FOR_SN)
            || (message[0] == ERASE_COMMAND && message[2] == ERASE_ALL_DELIVERED_READINGS_FOR_SN);
    }

    private static boolean isEncryptedInboundMessage(byte[] message) {
        return (message[0] == (byte) SEND_READING_NORMAL_RESPONSE)
            || (message[0] == (byte) SEND_READING_ALARM_RESPONSE)
            || (message[0] == (byte) RETRIEVE_COMMAND_RESPONSE && message.length > 2) //only encrypted if serial numbers are in the list
            || (message[0] == (byte) GET_PARAMETER_ACK && message[2] == (byte) PARAMETER_ENCRYPTION_KEY) //all parms is not encrypted.||
            //(message[0] == (byte)GET_PARAMETER_ACK && message[2] == (byte)PARAMETER_GET_ALL_PARAMETERS)
            ;
    }

    public static byte[] encryptOtaMessage(GlucoMon glucoMon, int[] encodedOtaMessage) throws InvalidKeyException {
        byte[] encryptedMessage;

        if (glucoMon.isEncrypted() && isEncryptedOutboundMessage(encodedOtaMessage)) {
            logger.finer(new String(glucoMon.getSecretKey()));
            logger.finer("Encrypt message");
            // convert the int array to a byte array. yuck.
            int messageLength = encodedOtaMessage.length;
            byte[] encodedOtaMessageByteArray = new byte[messageLength];
            for (int i = 0; i < messageLength; i++) {
                encodedOtaMessageByteArray[i] = (byte) encodedOtaMessage[i];
            }

            encryptedMessage = null;
            byte command = encodedOtaMessageByteArray[0];
            //int originalLength  = encodedOtaMessage[1];
            byte parameter = encodedOtaMessageByteArray[2];
            int offset = 3; //always three with current commands

            // pad and encrypt the payload portion of the message
            byte[] plainText = new byte[encodedOtaMessageByteArray.length - offset];
            System.arraycopy(encodedOtaMessageByteArray, offset, plainText, 0, plainText.length);
            Crypt crypt = new Crypt();
            byte[] cipherText = crypt.encrypt(plainText, glucoMon.getSecretKey());
            // put it back together into the encrypted message array
            encryptedMessage = new byte[cipherText.length + offset];
            encryptedMessage[0] = command;
            encryptedMessage[1] = (byte) (cipherText.length + 1); //length include parameter and payload.
            encryptedMessage[2] = parameter;
            System.arraycopy(cipherText, 0, encryptedMessage, offset, cipherText.length);
        } else {
            // convert the int array to a byte array. yuck.
            int messageLength = encodedOtaMessage.length;
            encryptedMessage = new byte[messageLength];
            for (int i = 0; i < messageLength; i++) {
                encryptedMessage[i] = (byte) encodedOtaMessage[i];
            }
        }

        logger.finer("Encrypted message bytes=\n" + IntegerHelper.toHexString(encryptedMessage));

        //        try {
        //            FileOutputStream out = new FileOutputStream( "/test.bin" );
        //            out.write( encryptedMessage );
        //            out.flush();
        //            out.close();
        //        } catch ( Exception e ) {}

        return encryptedMessage;
    }

    public static InboundMessage decodeOtaMessage(GlucoMon glucoMon, Date messageSubmitDate, InputStream is) throws IOException, InvalidKeyException {
        InputStream clearTextInputStream;
        boolean is19BitDateFormat = false;//remove dependency on /etc/opt/diabetech/...  these are not in production any more and this is safest way to modify code (e.g. don't modify it much) GlucoMonTimeStampFormatUtil.is19BitDateFormat( glucoMon );
        //2A 11 81 FF4EB3AD 54A5E14A ECB2108B 0E0A6580

        int messageId = is19BitDateFormat ? 0 : is.read();
        logger.finer("messageId=" + messageId);

        if (glucoMon.isEncrypted()) {
            logger.finer("Decrypt message");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = is.read()) != -1) {
                baos.write(b);
            }
            byte[] cipherText = baos.toByteArray();

            if (isEncryptedInboundMessage(cipherText)) {
                byte command = cipherText[0];
                byte length = cipherText[1];
                byte parameter = cipherText[2];

                // based on which command and parameter combination; set offset to 2 or 3.
                int offset = 0;
                switch (command) {
                    case (byte) RETRIEVE_COMMAND_RESPONSE:
                    case (byte) SEND_READING_NORMAL_RESPONSE:
                    case (byte) SEND_READING_ALARM_RESPONSE:
                        // subtract message id for new format
                        offset = is19BitDateFormat ? 2 : 1;
                        break;
                    case (byte) GET_PARAMETER_ACK:
                        // subtract message id for new format
                        // parms all working ok w/ 3 offset
                        // offset = is19BitDateFormat ? 3 : 2;
                        offset = 3;
                        break;
                    default:
                        throw new IllegalArgumentException("Incorrectly set offset in decryption routine");
                }

                byte[] payload = new byte[cipherText.length - offset];
                System.arraycopy(cipherText, offset, payload, 0, payload.length);

                // put together in cleartext message array
                Crypt crypt = new Crypt();
                byte[] plainTextPayload = crypt.decrypt(payload, glucoMon.getSecretKey());

                logger.log(Level.FINER, "plainTextPayload={0}\npayload={1}\ncipherText={2}",
                    new Object[]{plainTextPayload.length, payload.length, cipherText.length});
                // copy plaintext payload back in to payload array.
                System.arraycopy(plainTextPayload, 0, cipherText, offset, plainTextPayload.length);

                clearTextInputStream = new ByteArrayInputStream(cipherText);
            } else {
                logger.finer("Encrypted glucoMon, message type is cleartext");
                clearTextInputStream = new ByteArrayInputStream(cipherText);
            }
        } else {
            logger.finer("glucoMon is NOT encrypted");
            clearTextInputStream = is;
        }

        // once it is decrypted, decode as normal.
        return decode(messageSubmitDate, clearTextInputStream, is19BitDateFormat);
    }

    private static int[] encryptSerialNumber(String serialNumber) {
        if (serialNumber.length() != 9) {
            throw new IllegalArgumentException("A serialNumber must be exactly 9 characters " + serialNumber);
        }

        byte[] nineByte = serialNumber.getBytes();
        int[] nineInt = new int[9];
        for (int i = 0; i < 9; i++) {
            nineInt[i] = nineByte[i];
        }
        int[] eightByte = new int[8];
        int[] rotated = new int[8];

        core.module.codec.EncodeDecodeOtaMessage.byte9to8(nineInt, eightByte);
        core.module.codec.EncodeDecodeOtaMessage.rotate(eightByte, rotated);

        return rotated;
    }

    private static String decryptSerialNumber(InputStream is) throws IOException {
        int[] serialNumber8ByteRotated = new int[8];
        serialNumber8ByteRotated[0] = is.read();
        serialNumber8ByteRotated[1] = is.read();
        serialNumber8ByteRotated[2] = is.read();
        serialNumber8ByteRotated[3] = is.read();
        serialNumber8ByteRotated[4] = is.read();
        serialNumber8ByteRotated[5] = is.read();
        serialNumber8ByteRotated[6] = is.read();
        serialNumber8ByteRotated[7] = is.read();

        int[] eightByte = new int[8];
        int[] nineByte = new int[9];

        rotate(serialNumber8ByteRotated, eightByte);
        byte8to9(eightByte, nineByte);

        StringBuffer sb = new StringBuffer();

        sb.append((char) nineByte[0]);
        sb.append((char) nineByte[1]);
        sb.append((char) nineByte[2]);
        sb.append((char) nineByte[3]);
        sb.append((char) nineByte[4]);
        sb.append((char) nineByte[5]);
        sb.append((char) nineByte[6]);
        sb.append((char) nineByte[7]);
        sb.append((char) nineByte[8]);

        return sb.toString();
    }

    /*################# Advantra Helper Methods ##################*/
    /**
     * Port of advantra function.
     * <code>
     * void Byted9_to_8 (char* pIn, char* pOut)
     * {
     * for(int i=1; i<8 ;++i)
     * {
     *pOut = *pIn++ <<i;
     *pOut++ += *pIn >> (7-i);
     * }
     *pOut = *pIn++ <<i;
     *pOut++ += *pIn <<1;
     *pOut=0;
     * }
     * </code>
     */
    private static void byte9to8(int[] in, int[] out) {
        int i;
        for (i = 1; i < 8; i++) {
            out[i - 1] = (in[i - 1] << i);
            out[i - 1] += (in[i] >> (7 - i));
        }

        out[7] = (in[7] << i);
        out[7] += (in[8] << 1);
        //        out[8]=0;

        for (i = 0; i < out.length; i++) {
            out[i] = out[i] & 0x00ff;
        }
    }

    /**
     * Port of advantra function.
     * <code>
     * void Byted8_to_9 (char* pIn, char* pOut)
     * {
     * for(int i=1; i<8;++i)
     * {
     *pOut += (*pIn >> i) & 0xFF >> i;
     *pOut++ &= 0x7F;
     *pOut =  *pIn++ << (7-i);
     * }
     *pOut++ &= 0x7F;
     *pOut = *pIn >> 1;
     *pOut++ &= 0x7F;
     *pOut =  0;
     * }
     * </code>
     */
    private static void byte8to9(int[] in, int[] out) {
        int i = 1;
        for (; i < 8; i++) {
            out[i - 1] += (in[i - 1] >>> i) & 0xFF >>> i;
            out[i - 1] = out[i - 1] & 0x007F;
            out[i] = in[i - 1] << (7 - i);
        }
        out[7] = out[7] & 0x007F;
        out[8] = in[7] >>> 1;
        out[8] = out[8] & 0x007F;
        //        out[9] = 0;
        //
//        out[0] += 0x2F;
    }

    /**
     * Port of advantra function.
     * <code>
     * void Rotate (char* pIn,char* pOut)
     * {
     * unsigned int Matrix [8][8];
     *
     * for( int i=0; i<8;++i)
     * for( int j=0; j<8;++j)
     * {
     * if ( *(pIn+i) & (1<<j) )
     * Matrix[i][j] = 1;
     * else
     * Matrix[i][j] = 0;
     * }
     *
     * for( int j=0; j<8; ++j)
     * {
     *(pOut+j) = 0;
     * for(i=0; i<8;++i)
     * {
     *(pOut+j) += Matrix[i][j] * (1<<i);
     * }
     * }
     *(pOut+8)=0;
     * }
     * </code>
     */
    private static void rotate(byte[] in, int[] out) {
        int[][] Matrix = new int[8][8];

        int i = 0;
        int j = 0;

        for (i = 0; i < 8; ++i) {
            for (j = 0; j < 8; ++j) {
                if ((in[i] & (1 << j)) != 0) {
                    Matrix[i][j] = 1;
                } else {
                    Matrix[i][j] = 0;
                }
            }
        }

        for (j = 0; j < 8; ++j) {
            out[j] = 0;
            for (i = 0; i < 8; ++i) {
                out[j] += Matrix[i][j] * (1 << i);
            }
        }
        //        out[8] = 0;

        //shuffle to end
        int outLength = out.length;
        int offset = outLength - 8;
        for (i = outLength; i > offset; i--) {
            out[i - 1] = out[i - 1 - offset];
        }
        for (i = 0; i < offset; i++) {
            out[i] = 0;
        }
    }

    private static void rotate(int[] in, int[] out) {
        int[][] Matrix = new int[8][8];

        int i = 0;
        int j = 0;

        for (i = 0; i < 8; ++i) {
            for (j = 0; j < 8; ++j) {
                if ((in[i] & (1 << j)) != 0) {
                    Matrix[i][j] = 1;
                } else {
                    Matrix[i][j] = 0;
                }
            }
        }

        for (j = 0; j < 8; ++j) {
            out[j] = 0;
            for (i = 0; i < 8; ++i) {
                out[j] += Matrix[i][j] * (1 << i);
            }
        }
        //        out[8] = 0;

        //shuffle to end
        int outLength = out.length;
        int offset = outLength - 8;
        for (i = outLength; i > offset; i--) {
            out[i - 1] = out[i - 1 - offset];
        }
        for (i = 0; i < offset; i++) {
            out[i] = 0;
        }
    }

    /**
     * Port of advantra function.
     * <code>
     * int Date19bit(int month,int day,int hour,int minute)
     * {
     * switch (month)
     * {
     * case 12: // december
     * day+=30;
     * case 11: // november
     * day+=31;
     * case 10: // oktober
     * day+=30;
     * case 9:	// september
     * day+=31;
     * case 8:	// august
     * day+=31;
     * case 7:	// july
     * day+=30;
     * case 6:	// june
     * day+=31;
     * case 5:	// may
     * day+=30;
     * case 4:	// april
     * day+=31;
     * case 3:	// march
     * day+=29;
     * case 2:	// februari
     * day+=31;
     * case 1: // januari
     * // do nothing here
     * break;
     * default:
     * //ASSERT;
     * break;
     * }
     * hour += --day * 24;
     * minute += hour * 60;
     * minute -= hour /3;
     * return minute;
     * }
     * </code>
     */
    //    private static int encode19BitDate( int month, int day, int hour, int minute) {
    //        switch (month) {
    //            // falls through and adds days for previous month (for each month down to Jan.)
    //            case 12: // december
    //                day+=30;
    //            case 11: // november
    //                day+=31;
    //            case 10: // oktober
    //                day+=30;
    //            case 9:	// september
    //                day+=31;
    //            case 8:	// august
    //                day+=31;
    //            case 7:	// july
    //                day+=30;
    //            case 6:	// june
    //                day+=31;
    //            case 5:	// may
    //                day+=30;
    //            case 4:	// april
    //                day+=31;
    //            case 3:	// march
    //                day+=29;
    //            case 2:	// februari
    //                day+=31;
    //            case 1: // januari
    //                // do nothing here
    //                break;
    //            default:
    //                //ASSERT;
    //                break;
    //        }
    //        hour += --day * 24;
    //        minute += hour * 60;
    //        minute -= hour /3;
    //        return minute;
    //    }
    /**
     * Port of advantra function.
     * <code>
     * void ReDate19bit(int& minute,int& hour,int& day,int& month)
     * {
     * int conv1, conv2, conv3;
     * month = 0;
     * conv1 = minute / 180; // conversion factor
     * minute +=  conv1;
     * conv2 = minute / 180;
     *
     * if(conv1!= conv2)
     * {
     * minute += ( conv2 - conv1 ); // conversion mistake
     * conv3 = minute / 180;
     * if(conv3 != conv2)
     * minute += ( conv3 - conv2 ); // conversion mistake
     * }
     *
     * hour = minute / 60;
     * minute %= 60;	// minutes correct
     *
     * if(!minute)
     * if ( !( hour % 3 ) )
     * minute = 1;
     *
     * if( ( minute == 59 ) && ( ( hour % 3 ) == 2 ) ) // rounding up!!
     * {
     * minute = 0;
     * ++hour;
     * }
     *
     * day = hour / 24;
     * hour %= 24;	// hours correct
     *
     * if( day < 31 )	// januari
     * month = 1;
     * else
     * day -= 31;
     *
     * if(!month)
     * if( day < 29 )	// februari
     * month = 2;
     * else
     * day -= 29;
     *
     * if(!month)
     * if( day < 31 )	// maart
     * month = 3;
     * else
     * day -= 31;
     *
     * if(!month )
     * if( day < 30 )	// april
     * month = 4;
     * else
     * day -= 30;
     *
     * if( !month )
     * if( day < 31 )	// mei
     * month = 5;
     * else
     * day -= 31;
     *
     * if( !month )
     * if( day < 30 )	// juni
     * month = 6;
     * else
     * day -= 30;
     *
     * if( !month)
     * if(day < 31 )	// juli
     * month = 7;
     * else
     * day -= 31;
     *
     * if( !month )
     * if( day < 31 )	// augustus
     * month = 8;
     * else
     * day -= 31;
     *
     * if( !month)
     * if(day < 30 )	// september
     * month = 9;
     * else
     * day -= 30;
     *
     * if( !month)
     * if(day < 31 )	// oktober
     * month = 10;
     * else
     * day -= 31;
     *
     * if( !month)
     * if( day < 30 )	// november
     * month = 11;
     * else
     * day -= 30;
     *
     * if( !month)
     * if( day < 31 )	// december
     * month = 12;
     * else
     * day -= 31;
     *
     * // if(!month)
     * // ASSERT;
     * ++day;
     * }
     * </code>
     */
    private static long convert19BitDateMinutes(long minute) {
        long conv1, conv2, conv3;
        conv1 = minute / 180; // conversion factor
        minute += conv1;
        conv2 = minute / 180;

        if (conv1 != conv2) {
            minute += (conv2 - conv1); // conversion mistake
            conv3 = minute / 180;
            if (conv3 != conv2) {
                minute += (conv3 - conv2); // conversion mistake
            }
        }

        return minute;
    }

    public static Calendar decode19BitDate(long minute) {
        long hour;
        long day;
        long month;

        month = 0;

        minute = convert19BitDateMinutes(minute);
        hour = minute / 60;
        minute %= 60;	// minutes correct

        if (0 == minute) {
            if (!(hour % 3 != 0)) {
                minute = 1;
            }
        }

        if ((minute == 59) && ((hour % 3) == 2)) // rounding up!!
        {
            minute = 0;
            ++hour;
        }

        day = hour / 24;
        hour %= 24;	// hours correct

        if (day < 31) // januari
        {
            month = 1;
        } else {
            day -= 31;
        }

        if (0 == month) {
            if (day < 29) // februari
            {
                month = 2;
            } else {
                day -= 29;
            }
        }

        if (0 == month) {
            if (day < 31) // maart
            {
                month = 3;
            } else {
                day -= 31;
            }
        }

        if (0 == month) {
            if (day < 30) // april
            {
                month = 4;
            } else {
                day -= 30;
            }
        }

        if (0 == month) {
            if (day < 31) // mei
            {
                month = 5;
            } else {
                day -= 31;
            }
        }

        if (0 == month) {
            if (day < 30) // juni
            {
                month = 6;
            } else {
                day -= 30;
            }
        }

        if (0 == month) {
            if (day < 31) // juli
            {
                month = 7;
            } else {
                day -= 31;
            }
        }

        if (0 == month) {
            if (day < 31) // augustus
            {
                month = 8;
            } else {
                day -= 31;
            }
        }

        if (0 == month) {
            if (day < 30) // september
            {
                month = 9;
            } else {
                day -= 30;
            }
        }

        if (0 == month) {
            if (day < 31) // oktober
            {
                month = 10;
            } else {
                day -= 31;
            }
        }

        if (0 == month) {
            if (day < 30) // november
            {
                month = 11;
            } else {
                day -= 30;
            }
        }

        if (0 == month) {
            if (day < 31) // december
            {
                month = 12;
            } else {
                day -= 31;
            }
        }

        ++day;
        Calendar cal = Calendar.getInstance();

        // if month > this month then year is last year
        // if month <= this month, then year is this year
        int currentYear = cal.get(cal.YEAR);
        if (month > cal.get(cal.MONTH) + 1) {
            currentYear--;
        }

        // FOR TESTING AGAINST FILE DATES... currentYear = 2004;

        cal.clear();
        cal.set(currentYear, (int) (month - 1), (int) day, (int) hour, (int) minute, 0);
        //logger.finer( "mm/dd/yyyy hh:mm=" + month + "/" + day + "/" + currentYear + " " + hour + ":" + minute );
        return cal;
    }

    /*################# ENCODE Methods ##################*/
    public static int[] encodeParameterSetTimeSetting(boolean updateGlucoseMeterWithNetworkTime, int profileTimeZoneUtcOffsetMinutes) {
        /* bit
         *  7	Network Time update	0 = disable [OTA] Time updates of [GM] 1 = enable [OTA] Time updates of [GM]
        6	Time correction sign	0 = behind UTC time   1 = ahead of UTC time
        5 to 0	Time correction value	Number of 30 minute intervals difference
        from UTC time
         */
        int timeSettings = 0;
        if (updateGlucoseMeterWithNetworkTime) {
            timeSettings |= 0x80; // flip to 1000 0000 to enable update glucose meter with DMD time
            // only set the time offset if we are going to use OTA time
            if (profileTimeZoneUtcOffsetMinutes > 0) {
                timeSettings |= 0x40; // flip to 0100 0000 because minutes are ahead of UTC
            } else {
                profileTimeZoneUtcOffsetMinutes *= -1; //flip the sign so we can add this - number to the timeSetting int
            }

            timeSettings += profileTimeZoneUtcOffsetMinutes / 30;
        }

        return new int[]{SET_PARAMETER, 0x02, PARAMETER_OTA_TIME_SETTINGS, timeSettings};
    }

    public static int[] encodeParameterGetTimeSetting() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_OTA_TIME_SETTINGS};
    }

    public static int[] encodeParameterSetEnableFlexSuiteHeader(boolean enableFlexSuiteHeader) {
        return new int[]{SET_PARAMETER, 0x02, PARAMETER_ENABLE_FLEXSUITE_HEADER, enableFlexSuiteHeader ? 1 : 0};
    }

    public static int[] encodeParameterGetEnableFlexSuiteHeader() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_ENABLE_FLEXSUITE_HEADER};
    }

    public static int[] encodeParameterSetMessageCounts(byte stored, byte sent, byte failed) {
        return new int[]{SET_PARAMETER, 0x04, PARAMETER_MESSAGE_COUNTS, stored, sent, failed};
    }

    public static int[] encodeParameterGetMessageCounts() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_MESSAGE_COUNTS};
    }

    public static int[] encodeParameterSetEnableEncryption(boolean enableEncryption) {
        return new int[]{SET_PARAMETER, 0x02, PARAMETER_ENABLE_ENCRYPTION, enableEncryption ? 1 : 0};
    }

    public static int[] encodeParameterGetEnableEncryption() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_ENABLE_ENCRYPTION};
    }

    public static int[] encodeParameterSetEncryptionKey(byte[] secretKey) {
        if (secretKey.length != 16) {
            throw new IllegalArgumentException("secretKey must be 16 bytes (128bits) long");
        }

        return new int[]{
                SET_PARAMETER, 0x11, PARAMETER_ENCRYPTION_KEY,
                secretKey[0], secretKey[1], secretKey[2], secretKey[3],
                secretKey[4], secretKey[5], secretKey[6], secretKey[7],
                secretKey[8], secretKey[9], secretKey[10], secretKey[11],
                secretKey[12], secretKey[13], secretKey[14], secretKey[15]
            };
    }

    public static int[] encodeParameterGetEncryptionKey() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_ENCRYPTION_KEY};
    }

    public static int[] encodeParameterResetEncryptionKey() {
        return new int[]{SET_PARAMETER, 0x02, PARAMETER_RESET_ENCRYPTION_KEY, 0x01};
    }

    public static int[] encodeParameterSetRadioSleep(int startTimeMinutes, int endTimeMinutes) {
        return new int[]{SET_PARAMETER, 0x03, PARAMETER_RADIO_SLEEP, startTimeMinutes / 6, endTimeMinutes / 6};
    }

    public static int[] encodeParameterGetRadioSleep() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_RADIO_SLEEP};
    }

    public static int[] encodeParameterSetAlarmLimit(int lowLimit, int highLimit) {
        return new int[]{SET_PARAMETER, 0x03, PARAMETER_ALARM_LIMIT, lowLimit / 3, highLimit / 3};
    }

    public static int[] encodeParameterGetAlarmLimit() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_ALARM_LIMIT};
    }

    public static int[] encodeParameterSetAutoDeleteGlucoseMeter(boolean autoDeleteGlucoseMeter) {
        return new int[]{SET_PARAMETER, 0x02, PARAMETER_AUTODELETE, autoDeleteGlucoseMeter ? 1 : 0};
    }

    public static int[] encodeParameterGetAutoDeleteGlucoseMeter() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_AUTODELETE};
    }

    public static int[] encodeParameterGetBatteryStatus() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_BATTERY_STATUS};
    }

    public static int[] encodeParameterGetBatteryVoltage() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_BATTERY_VOLTAGE_MV};
    }

    public static int[] encodeParameterGetDutyCycles() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_DUTY_CYCLES};
    }

    public static int[] encodeParameterGetLastChargeTime() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_LAST_CHARGE_TIME};
    }

    //    public static int[] encodeParameterGetBatteryLow() {
    //        return new int[] { GET_PARAMETER, 0x01, PARAMETER_BATTERY_LOW };
    //    }
    public static int[] encodeParameterSetBatteryOffset(short offsetVoltage) {
        return new int[]{SET_PARAMETER, 0x03, PARAMETER_BATTERY_OFFSET, (offsetVoltage >>> 0x08) & 0xFF, offsetVoltage & 0xFF};
    }
    //    public static int[] encodeParameterGetBatteryOffset() {
    //        return new int[] { GET_PARAMETER, 0x01, PARAMETER_BATTERY_OFFSET };
    //    }
    //    public static int[] encodeParameterGetBatteryMax() {
    //        return new int[] { GET_PARAMETER, 0x01, PARAMETER_BATTERY_MAX };
    //    }

    public static int[] encodeParameterGetSoftwareVersion() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_SOFTWARE_VERSION};
    }

    public static int[] encodeParameterSetMailToAddress(String eMailAddress) {
        byte[] emailBytes = eMailAddress.getBytes();
        int eMailAddressLength = emailBytes.length;

        if (eMailAddressLength > 50) {
            throw new IllegalArgumentException("eMail address may not be over 50 bytes");
        }

        int[] otaMessage = new int[eMailAddressLength + 3];
        otaMessage[0] = SET_PARAMETER;
        otaMessage[1] = eMailAddressLength + 1;
        otaMessage[2] = PARAMETER_MAIL_TO_ADDRESS;

        for (int i = 0; i < eMailAddressLength; i++) {
            otaMessage[i + 3] = emailBytes[i];
        }

        return otaMessage;
    }

    public static int[] encodeParameterGetMailToAddress() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_MAIL_TO_ADDRESS};
    }

    public static int[] encodeParameterSetAutoSendNetworkInfo(boolean autoSendNetoworkInfo) {
        return new int[]{SET_PARAMETER, 0x02, PARAMETER_AUTO_SEND_NETWORK_INFO, autoSendNetoworkInfo ? 1 : 0};
    }

    public static int[] encodeParameterGetAutoSendNetworkInfo() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_AUTO_SEND_NETWORK_INFO};
    }

    public static int[] encodeParameterGetNetworkInfo() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_NETWORK_INFO};
    }

    public static int[] encodeParameterSetDebugInfo(boolean isDebugOn) {
        return new int[]{SET_PARAMETER, 0x02, PARAMETER_SET_DEBUG_INFO, isDebugOn ? 1 : 0};
    }

    public static int[] encodeParameterGetDebugInfo() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_SET_DEBUG_INFO};
    }

    public static int[] encodeEraseGlucoMon(boolean onlyEraseDeliveredReadings) {
        /*
         *0x20	Erase all delivered readings, no [SN] needed. [SN] is kept
         *0x10	Erase all readings, no [SN] needed. [SN] is not deleted
         */
        return new int[]{ERASE_COMMAND, 0x01, onlyEraseDeliveredReadings ? ERASE_ALL_DELIVERED_READINGS : ERASE_ALL_READINGS};
    }

    public static int[] encodeEraseGlucoMon(boolean onlyEraseDeliveredReadings, String serialNumber) {

        int[] encryptedSerialNumber = encryptSerialNumber(serialNumber);
        /*
         *0x11	Erase all readings of specified [SN]. [SN] is not deleted
         *0x21	Erase all delivered readings of specified [SN]. [SN] is not deleted
         */
        return new int[]{ERASE_COMMAND,
                0x09,
                onlyEraseDeliveredReadings ? ERASE_ALL_DELIVERED_READINGS_FOR_SN : ERASE_ALL_READINGS_FOR_SN,
                encryptedSerialNumber[0],
                encryptedSerialNumber[1],
                encryptedSerialNumber[2],
                encryptedSerialNumber[3],
                encryptedSerialNumber[4],
                encryptedSerialNumber[5],
                encryptedSerialNumber[6],
                encryptedSerialNumber[7]
            };
    }

    public static int[] encodeEraseGlucoMonAllSerialNumbersAndReadings() {
        /*
         *0x30	Erase  all [SN] and all entries
         */
        return new int[]{ERASE_COMMAND, 0x01, ERASE_ALL_SN_ALL_ENTRIES};
    }

    public static int[] encodeEraseGlucoMonSerialNumberAndReadings(String serialNumber) {
        int[] encryptedSerialNumber = encryptSerialNumber(serialNumber);
        /*
         *0x31	Erase [SN] and all entries of specific [SN]
         */
        return new int[]{ERASE_COMMAND,
                0x09,
                ERASE_ALL_ENTRIES_FOR_SN,
                encryptedSerialNumber[0],
                encryptedSerialNumber[1],
                encryptedSerialNumber[2],
                encryptedSerialNumber[3],
                encryptedSerialNumber[4],
                encryptedSerialNumber[5],
                encryptedSerialNumber[6],
                encryptedSerialNumber[7]
            };
    }

    public static int[] encodeGetReadings(boolean onlySendUndeliveredReadings, String serialNumber) {
        int retrieveParameter;

        if (serialNumber != null) {
            int[] encryptedSerialNumber = encryptSerialNumber(serialNumber);
            /*
             *0x31	Erase [SN] and all entries of specific [SN]
             */
            return new int[]{RETRIEVE_COMMAND,
                    0x09,
                    onlySendUndeliveredReadings ? RETRIEVE_UNDELIVERED_READINGS_FOR_SN : RETRIEVE_ALL_READINGS_FOR_SN,
                    encryptedSerialNumber[0],
                    encryptedSerialNumber[1],
                    encryptedSerialNumber[2],
                    encryptedSerialNumber[3],
                    encryptedSerialNumber[4],
                    encryptedSerialNumber[5],
                    encryptedSerialNumber[6],
                    encryptedSerialNumber[7]
                };
        } else {
            return new int[]{RETRIEVE_COMMAND, 0x01, onlySendUndeliveredReadings ? RETRIEVE_UNDELIVERED_READINGS : RETRIEVE_ALL_READINGS};
        }
    }

    public static int[] encodeGetGlucoseMeterSerialNumbers() {
        return new int[]{RETRIEVE_COMMAND, 0x01, RETRIEVE_SN_LIST};
    }

    public static int[] encodeGetDiagnosticInfo(byte diagnosticIndex) {
        return new int[]{GET_DIAGNOSTIC_COMMAND, 0x01, diagnosticIndex};
    }

    public static int[] encodeGetAllParameters() {
        return new int[]{GET_PARAMETER, 0x01, PARAMETER_GET_ALL_PARAMETERS};
    }

    public static int[] encodeEventReadGlucoseMeter() {
        return new int[]{EVENT_READ_GM};
    }

    public static int[] encodeEventDeletePendingCommands() {
        return new int[]{EVENT_DELETE_PENDING_COMMANDS};
    }

    /*################# DECODE Methods ##################*/
    //Table 5 [OTA] Commands and Responses
    //Code	Command	Code	Response
    //0x10	Set Parameter   0x1A 	Set Parameter Ack
    //                          0x1F 	Set_Parameter failed
    //0x20	Get Parameter   0x2A 	Get Parameter Ack
    //                          0x2F 	Get_Parameter failed
    //0x30	Erase Command   0x3A 	Erase Ack
    //                          0x3F 	Erase failed
    //0x40	Retrieve Command    0x4A	Retrieve Response
    //                              0x4F 	Retrieve Response failed
    //                          0x5A	Send_Reading Normal
    //                          0x5F	Send_Reading Alarm
    //0xFx	Send Event      0xFx 	Send Event
    private static InboundMessage decode(Date messageSubmitDate, InputStream is, boolean is19BitDateFormat) throws IOException {
        int b = is.read();
        switch (b) {
            case RETRIEVE_COMMAND_RESPONSE:
            case SEND_READING_NORMAL_RESPONSE:
            case SEND_READING_ALARM_RESPONSE:
                if (is19BitDateFormat) {
                    return decodeRetrieveCommand19Bit(messageSubmitDate, b, is);
                } else {
                    return decodeRetrieveCommand(messageSubmitDate, b, is);
                }
            case SET_PARAMETER_ACK:
                return decodeSetParamterAck(is);
            case GET_PARAMETER_ACK:
                return decodeGetParamterAck(is, 0, is19BitDateFormat);
            case EVENT_BATTERY_LOW:
                return new EventBatteryLow(messageSubmitDate);
            case EVENT_BATTERY_FULL:
                return new EventBatteryFull(messageSubmitDate);
            case EVENT_MAX_SN_EXCEEDED:
                return new EventMaxSerialNumberExceeded(messageSubmitDate);
            case EVENT_CORRUPTED_GM_CONNECTED:
                return new EventCorruptedGlucoseMeterConnected(messageSubmitDate);
            case EVENT_RADIO_RESET:
                return new EventRadioReset(messageSubmitDate);
            case ERASE_COMMAND_ACK:
                return decodeEraseCommandAck(is);
            case SET_PARAMETER_FAILED:
                return decodeSetParameterFailed(is);
            case GET_PARAMETER_FAILED:
                return decodeGetParameterFailed(is);
            case ERASE_COMMAND_FAILED:
                return decodeEraseCommandFailed(is);
            case RETRIEVE_COMMAND_RESPONSE_FAILED:
                return decodeRetrieveCommandResponseFailed(is);
            case GET_DIAGNOSTIC_COMMAND:
                // no-op this can be stored in db later if actually used for ops support
                // for now, look at the hex dump of the message in the log to
                // see the diagnostic values.
                return null;
            case INVALID_MSG_CONTENT:
                logger.log(Level.FINER, "Invalid content message  0x{0}", EncodeDecodeOtaMessage.toHexString(b));
                return new InvalidContentMessage();
            default:
                throw new IllegalArgumentException("Command not understood 0x" + EncodeDecodeOtaMessage.toHexString(b));
        }
    }

    //    // FAILED
    private static SetParameterFailedResponse decodeSetParameterFailed(InputStream is) throws IOException {
        is.read(); //length always 2
        int parameterId = is.read();
        int failedReason = is.read();
        return new SetParameterFailedResponse(parameterId, decodeFailedReason(failedReason));
    }

    private static GetParameterFailedResponse decodeGetParameterFailed(InputStream is) throws IOException {
        is.read(); //length always 2
        int parameterId = is.read();
        int failedReason = is.read();
        return new GetParameterFailedResponse(parameterId, decodeFailedReason(failedReason));
    }

    private static EraseCommandFailedResponse decodeEraseCommandFailed(InputStream is) throws IOException {
        is.read(); //length always 2
        int parameterId = is.read();
        int failedReason = is.read();
        return new EraseCommandFailedResponse(parameterId, decodeFailedReason(failedReason));
    }

    private static RetrieveCommandFailedResponse decodeRetrieveCommandResponseFailed(InputStream is) throws IOException {
        is.read(); //length always 2
        int parameterId = is.read();
        int failedReason = is.read();
        return new RetrieveCommandFailedResponse(parameterId, decodeFailedReason(failedReason));
    }

    private static String decodeFailedReason(int failedReason) {
        switch (failedReason) {
            case FAILED_INVALID_COMMAND:
                return "Invalid command.";
            case FAILED_INVALID_PARAMETER_ID:
                return "Invalid parameter id.";
            case FAILED_INVALID_PARAMETER_VALUE:
                return "Invalid parameter value.";
            case FAILED_INVALID_COMMAND_LENGTH:
                return "Invalid command length.";
            case FAILED_PARAMETER_NOT_CHANGEABLE:
                return "Paramter not changeable.";
            case FAILED_ERROR_NOT_SPECIFIED:
                return "Error not specified.";
        }
        return "Unknown failed reason.";
    }

    private static GlucoMonDatabase decodeRetrieveCommand(Date messageSubmitDate, int retrieveType, InputStream is) throws IOException {
        /*
        The structure of a reading sent trough the network has a 6 byte compressed format.
        
        Reserved (4 bits)
        Extra info(2 bits):
        CT, Controll Test (1 bit)
        0 means a blood sample
        1 means  a controll test
        TC, Time Correct (1 bit)
        0 means time stamp of measurement in NOK
        1 means time stamp of measurement is OK
        Value of reading (10 bit) :
        value from 000 to 600
        601 : HIGH, Value higher then 600
        602 : REXC, Range exceeded, no valid reading
        1020 : TCORR, Time correction
        Date and Time (4byte):
        Standard 4 byte time format
        
        int reading2bytes;
        reading2bytes = 0x0800; //CT true
        reading2bytes = 0xF7FF; //CT false
        
        reading2bytes = 0x0400; //TC true
        reading2bytes = 0xFBFF; //TC false
        
        reading2bytes = 0x03FF; //10 bit all on reading value
        
        reading2bytes = 80;
        
        int CT_MASK = 0x0800;
        int TC_MASK = 0x0400;
        int TC_VALUE_MASK = 0x03FC; //1020 in reading value for a tc (2 flags?)
        
        int READING_VALUE_MASK = 0x03FF;
        System.out.println("ct=" + ((reading2bytes & CT_MASK) > 0) );
        System.out.println("tc=" + ((reading2bytes & TC_MASK) > 0) );
        System.out.println("readingValue=" + (reading2bytes & READING_VALUE_MASK) );
        System.out.println("10bit all on reading = 0x03FF");
         */
        int numberOfGlucometers = is.read();
        logger.finer("numberOfGlucometers=" + numberOfGlucometers);
        if (numberOfGlucometers > 64) {
            throw new IllegalStateException("Too many glucose meters (" + numberOfGlucometers + "); invalid data");
        }

        //(retrieveType == SEND_READING_NORMAL_RESPONSE) either alarm or normal send
        GlucoMonDatabase db = new GlucoMonDatabase((retrieveType == SEND_READING_ALARM_RESPONSE));

        Calendar glucoMonTimeStamp = null;

        for (int i = 0; i < numberOfGlucometers; i++) {
            String serialNumber = decryptSerialNumber(is);
            logger.log(Level.FINE, "serialNumber={0}", serialNumber);
            MedicalDevice currentGlucoseMeter = new MedicalDevice(serialNumber);
            int numberOfReadings = (short) ((is.read() << 8) + (is.read() << 0));  // DIY readShort() to avoid creating DataInputStream just for this
            logger.finer("currentGlucoseMeter(#" + i + ")=" + currentGlucoseMeter + ",numberOfReadings=" + numberOfReadings);

            // There will be > 150 readings when getting module database (up to 4k readings...)
            //            if ( numberOfReadings > 150 ) {
            //                throw new IllegalStateException( "Too many readings (" + numberOfReadings + "); invalid data" );
            //            }

            for (int j = 0; j < numberOfReadings; j++) {
                int reading2bytes = ((is.read() << 8) + (is.read() << 0));
                long reading4ByteTimeSeconds = ((is.read() << 24) + (is.read() << 16) + (is.read() << 8) + (is.read() << 0));
                logger.finer("reading2bytes=" + IntegerHelper.toHexString(reading2bytes) + ",reading4ByteTimeSeconds=" + reading4ByteTimeSeconds + ",reading4ByteTimeSeconds=" + IntegerHelper.toHexString(reading4ByteTimeSeconds));

                if ((reading2bytes & TC_MASK) == 0 && (reading2bytes & TC_VALUE_MASK) == TC_VALUE_MASK) {
                    // save the offset for subsequent readings
                    glucoMonTimeStamp = Calendar.getInstance();
                    glucoMonTimeStamp.setTimeInMillis(reading4ByteTimeSeconds * 1000);
                    logger.finer("readingOffset=" + j + ",glucoMonTimeStamp=" + glucoMonTimeStamp.getTime().toString());
                } else {
                    Calendar timeStamp = Calendar.getInstance();
                    timeStamp.setTimeInMillis(reading4ByteTimeSeconds * 1000);
                    currentGlucoseMeter.addDataPoint(decodeReading(j, messageSubmitDate, reading2bytes, timeStamp, glucoMonTimeStamp));
                }
            }
            db.addGlucoseMeter(currentGlucoseMeter);
        }

        return db;
    }

    // arg for time bytes
    private static DataPoint decodeReading(int readingOffset, Date messageSubmitDate, int reading2bytes, Calendar timeStamp, Calendar glucoMonTimeStamp) {
        logger.finer("readingOffset=" + readingOffset + ",reading2bytes=" + IntegerHelper.toHexString(reading2bytes) + ",timeStamp=" + timeStamp.getTime().toString());
        if (glucoMonTimeStamp != null) {
            logger.finer("glucoMonTimeStamp=" + glucoMonTimeStamp.getTime().toString());
        }

        DataPoint reading = new DataPoint();
        reading.setValue(reading2bytes & READING_VALUE_MASK);

        if ((reading2bytes & TC_MASK) == 0 && glucoMonTimeStamp != null) { //only set if time is suspect
            //reading.setMedicalDeviceTimestamp( glucoMonTimeStamp.getTime() );
        }

        reading.setTimestamp(timeStamp.getTime());
        reading.setIsControl((reading2bytes & CT_MASK) > 0);
        reading.setOriginated(messageSubmitDate);

        return reading;
    }

    private static GlucoMonDatabase decodeRetrieveCommand19Bit(Date messageSubmitDate, int retrieveType, InputStream is) throws IOException {
        /*
        The structure of a reading sent trough the network has a 4 byte compressed format.
        
        Value of reading (10 bit) :
        value from 000 to 600
        601  : HIGH, Value higher then 600
        602  : REXC, Range exceeded, no valid reading
        1020 : TCORR, Time correction
        Date and Time (19bit):
        resolution 1 minute
        Every 3 hours, the 59th minute will be rounded to the 58th or 00th minute (Compression).
        saved in minutes
        Extra info(3 bit):
        TC, Time Correct (1 bit)
        0 means time stamp of measurement in NOK
        1 means time stamp of measurement is OK
        CT, Controll Test (1 bit)
        0 means a blood sample
        1 means  a controll test
        Reserved (1 bit)
         */
        int numberOfGlucometers = is.read();
        //(retrieveType == SEND_READING_NORMAL_RESPONSE) either alarm or normal send
        GlucoMonDatabase db = new GlucoMonDatabase((retrieveType == SEND_READING_ALARM_RESPONSE));

        long timeCorrectionOffsetMinutes = 0;

        for (int i = 0; i < numberOfGlucometers; i++) {
            MedicalDevice currentGlucoseMeter = new MedicalDevice(decryptSerialNumber(is));
            int numberOfReadings = (short) ((is.read() << 8) + (is.read() << 0));  // DIY readShort() to aoid creating DataInputStream just for this

            for (int j = 0; j < numberOfReadings; j++) {
                // DIY readInt() to aoid creating DataInputStream just for this
                int reading = ((is.read() << 24) + (is.read() << 16) + (is.read() << 8) + (is.read() << 0));
                if (((reading >>> 22) & TIME_CORRECTION_READING_FLAG_VALUE) == TIME_CORRECTION_READING_FLAG_VALUE) {
                    int offset = (reading & 0x3ffff8) >> 3;
                    if ((0x40000 & offset) != 0) {
                        logger.finer("OFFSET NEGATIVE (sign bit (19th) is flipped, convert to negative number)");
                        offset = (offset) - 0x80000;
                    }

                    timeCorrectionOffsetMinutes = convert19BitDateMinutes(offset); //add back minutes lost in compression
                    logger.finer("OFFSET=" + offset + ",bin=" + Integer.toBinaryString(offset) + ",convert19BitDateMinutes=" + timeCorrectionOffsetMinutes);
                } else {
                    DataPoint dp = decodeReading19Bit(messageSubmitDate, reading, timeCorrectionOffsetMinutes);
                    currentGlucoseMeter.addDataPoint(dp);
                }
            }
            db.addGlucoseMeter(currentGlucoseMeter);
        }

        return db;
    }

    private static DataPoint decodeReading19Bit(Date messageSubmitDate, int readingBytes, long timeCorrectionOffsetMinutes) {
        logger.finer("readingBytes=" + IntegerHelper.toHexString(readingBytes) + ",timeCorrectionOffsetMinutes=" + timeCorrectionOffsetMinutes);

        DataPoint reading = new DataPoint();

        reading.setValue(readingBytes >>> 22);
        if ((readingBytes & 0x04) == 0) { //only set if time is suspect
            reading.setTimeCorrectionOffset((int) timeCorrectionOffsetMinutes);
        }
        reading.setTimestamp(decode19BitDate((readingBytes & 0x3ffff8) >>> 3).getTime());
        reading.setIsControl(((readingBytes & 0x02) > 0));
        //
        //        System.out.println( reading.getDateTime() );
        //        System.out.println( reading.getCorrectedDateTime() );
        //        System.out.println( reading.getTimeCorrectionOffset() );
        //
        reading.setOriginated(messageSubmitDate);
        return reading;
    }

    private static SetParameterAck decodeSetParamterAck(InputStream is) throws IOException {
        //parameter length is always 0x01
        is.read();
        int parameterId = is.read();

        switch (parameterId) {
            case PARAMETER_OTA_TIME_SETTINGS:
                return new SetParameterOtaTimeSettingsAck();
            case PARAMETER_ENABLE_FLEXSUITE_HEADER:
                return new SetParameterEnableFlexSuiteHeaderAck();
            case PARAMETER_MESSAGE_COUNTS:
                return new SetParameterMessageCountAck();
            case PARAMETER_ENABLE_ENCRYPTION:
                return new SetParameterEnableEncryptionAck();
            case PARAMETER_ENCRYPTION_KEY:
                return new SetParameterEncryptionKeyAck();
            case PARAMETER_RESET_ENCRYPTION_KEY:
                return new SetParameterResetEncryptionKeyAck();
            case PARAMETER_RADIO_SLEEP:
                return new SetParameterRadioSleepAck();
            case PARAMETER_ALARM_LIMIT:
                return new SetParameterAlarmLimitAck();
            case PARAMETER_AUTODELETE:
                return new SetParameterAutoDeleteAck();
            case PARAMETER_BATTERY_STATUS:
                return new SetParameterBatteryStatusAck();
            case PARAMETER_MAIL_TO_ADDRESS:
                return new SetParameterMailToAck();
            case PARAMETER_AUTO_SEND_NETWORK_INFO:
                return new SetParameterAutoSendNetworkInfoAck();
            case PARAMETER_SET_DEBUG_INFO:
                return new SetParameterDebugInfoAck();
            case PARAMETER_BATTERY_OFFSET:
                return new SetParameterBatteryOffsetAck();
            default:
                throw new IllegalArgumentException("parameterId not understood 0x" + EncodeDecodeOtaMessage.toHexString(parameterId));
        }
    }

    //Command	0x2A	Get_Parameter command
    //Length	length	Get_Parameter length (1 byte )
    //Parameter ID	ID	See Parameter ID list ( 1 byte )
    //Value	value	Get_Parameter value
    //0x10	Time Setting	1 byte	See description
    //0x20	Radio_Sleep	2 byte, as described	2 compressed time values, see Compressed 1 byte time.
    //0x30	Alarm_Limit	2 byte, as described	Low limit (1 byte) , high limit ( 1 byte ) values 0 201 , default  0, 201
    //0x40	Autodelete [GM]	1 byte	TRUE (1) when enabled  FALSE (0) is disabled
    //0x50 	Battery status	1 byte	Status value: 0  15
    /**
     * @param parameterLength special parm for this because individual gets must read length, while get all do not use length in format, and must pass a calculated length for email.
     **/
    private static GetParameterAck decodeGetParamterAck(InputStream is, int parameterLength) throws IOException {
        return decodeGetParamterAck(is, parameterLength, false);
    }

    private static GetParameterAck decodeGetParamterAck(InputStream is, int parameterLength, boolean is19BitDateFormat) throws IOException {
        if (parameterLength == 0) {
            parameterLength = is.read();
        }

        int parameterId = is.read();

        switch (parameterId) {
            case PARAMETER_OTA_TIME_SETTINGS:
                int timeSetting = is.read();
                boolean updateGlucoseMeterWithNetworkTime = (timeSetting & 0x80) == 0x80;
                boolean timeCorrectionPositive = (timeSetting & 0x40) == 0x40;
                int profileTimeZoneUtcOffsetMinutes = timeSetting & 0x3F;
                profileTimeZoneUtcOffsetMinutes = timeCorrectionPositive ? profileTimeZoneUtcOffsetMinutes : profileTimeZoneUtcOffsetMinutes * -1;
                profileTimeZoneUtcOffsetMinutes *= 30;
                return new GetParameterOtaTimeSettingsAck(updateGlucoseMeterWithNetworkTime, profileTimeZoneUtcOffsetMinutes);
            case PARAMETER_ENABLE_ENCRYPTION:
                boolean enableEncryption = (is.read() == 1);
                return new GetParameterEnableEncryptionAck(enableEncryption);
            case PARAMETER_ENCRYPTION_KEY:
                byte[] secretKey = {
                    (byte) is.read(), (byte) is.read(), (byte) is.read(), (byte) is.read(),
                    (byte) is.read(), (byte) is.read(), (byte) is.read(), (byte) is.read(),
                    (byte) is.read(), (byte) is.read(), (byte) is.read(), (byte) is.read(),
                    (byte) is.read(), (byte) is.read(), (byte) is.read(), (byte) is.read()
                };
                return new GetParameterEncryptionKeyAck(secretKey);
            case PARAMETER_ENABLE_FLEXSUITE_HEADER:
                boolean enableFlexSuiteHeader = (is.read() == 1);
                return new GetParameterEnableFlexSuiteHeaderAck(enableFlexSuiteHeader);
            case PARAMETER_MESSAGE_COUNTS:
                int stored = is.read();
                int sent = is.read();
                int failed = is.read();
                return new GetParameterMessageCountAck(stored, sent, failed);
            case PARAMETER_RADIO_SLEEP:
                int startTimeMinutes = is.read() * 6;
                int endTimeMinutes = is.read() * 6;
                return new GetParameterRadioSleepAck(startTimeMinutes, endTimeMinutes);
            case PARAMETER_ALARM_LIMIT:
                int lowLimit = is.read() * 3;
                int highLimit = is.read() * 3;
                return new GetParameterAlarmLimitAck(lowLimit, highLimit);
            case PARAMETER_AUTODELETE:
                boolean autoDeleteGlucoseMeter = (is.read() == 1);
                return new GetParameterAutoDeleteAck(autoDeleteGlucoseMeter);
            case PARAMETER_BATTERY_STATUS:
                int batteryStatus = is.read();
                return new GetParameterBatteryStatusAck(batteryStatus);
            case PARAMETER_MAIL_TO_ADDRESS:
                byte[] emailAddress = new byte[parameterLength - 1];
                is.read(emailAddress);
                return new GetParameterMailToAck(new String(emailAddress));
            case PARAMETER_AUTO_SEND_NETWORK_INFO:
                boolean autoSendNetworkInfo = (is.read() == 1);
                return new GetParameterAutoSendNetworkInfoAck(autoSendNetworkInfo);
            case PARAMETER_NETWORK_INFO:
                int serviceProviderId = ((is.read() << 8) + (is.read() << 0));
                int zoneId = ((is.read() << 8) + (is.read() << 0));
                int subZoneId = is.read();
                int colorCode = is.read();
                return new GetParameterNetworkInfoAck(serviceProviderId, zoneId, subZoneId, colorCode);
            case PARAMETER_SOFTWARE_VERSION:
                int softwareVersion = is.read();
                return new GetParameterSoftwareVersionAck(softwareVersion);
            case PARAMETER_LED_STATE:
                int ledState = is.read();
                return new GetParameterLedStateAck(ledState);
            case PARAMETER_SET_DEBUG_INFO:
                boolean isDebugOn = (is.read() == 1);
                return new GetParameterDebugInfoAck(isDebugOn);
            case PARAMETER_BATTERY_VOLTAGE_MV:
                int batteryVoltage = ((is.read() << 8) + (is.read() << 0));
                return new GetParameterBatteryVoltageAck(batteryVoltage);
            case PARAMETER_BATTERY_LOW:
                int batteryVoltageLow = ((is.read() << 8) + (is.read() << 0));
                return new GetParameterBatteryLowAck(batteryVoltageLow);
            case PARAMETER_BATTERY_OFFSET:
                int batteryVoltageOffset = ((is.read() << 8) + (is.read() << 0));
                return new GetParameterBatteryOffsetAck(batteryVoltageOffset);
            case PARAMETER_BATTERY_MAX:
                int batteryVoltageMax = ((is.read() << 8) + (is.read() << 0));
                return new GetParameterBatteryMaxAck(batteryVoltageMax);
            case PARAMETER_DUTY_CYCLES:
                int trickleCycle = is.read();
                int fastCycle = is.read();
                return new GetParameterDutyCyclesAck(trickleCycle, fastCycle);
            case PARAMETER_LAST_CHARGE_TIME:
                if (is19BitDateFormat) {
                    int lastChargeTime = ((is.read() << 16) + (is.read() << 8) + (is.read() << 0));
                    return new GetParameterLastChargeTimeAck(decode19BitDate(lastChargeTime & 0x7FFFF));
                } else {
                    // 4 byte c time
                    long lastChargeTimeSeconds = ((is.read() << 24) + (is.read() << 16) + (is.read() << 8) + (is.read() << 0));
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(lastChargeTimeSeconds * 1000);
                    return new GetParameterLastChargeTimeAck(cal);
                }
            case PARAMETER_GET_ALL_PARAMETERS:
                return decodeRetrieveAllParameters(is, parameterLength, is19BitDateFormat);
            default:
                throw new IllegalArgumentException("parameterId not understood 0x" + EncodeDecodeOtaMessage.toHexString(parameterId));
        }
    }

    private static GetParameterAllAck decodeRetrieveAllParameters(InputStream is, int totalLength, boolean decodeReading19Bit) throws IOException {
        GetParameterOtaTimeSettingsAck ota = (GetParameterOtaTimeSettingsAck) decodeGetParamterAck(is, -1);
        GetParameterRadioSleepAck radioSleep = (GetParameterRadioSleepAck) decodeGetParamterAck(is, -1);
        GetParameterAlarmLimitAck alarmLimit = (GetParameterAlarmLimitAck) decodeGetParamterAck(is, -1);
        GetParameterAutoDeleteAck autoDelete = (GetParameterAutoDeleteAck) decodeGetParamterAck(is, -1);
        GetParameterBatteryStatusAck batteryStatus = (GetParameterBatteryStatusAck) decodeGetParamterAck(is, -1);
        GetParameterSoftwareVersionAck softwareVersion = (GetParameterSoftwareVersionAck) decodeGetParamterAck(is, -1);
        GetParameterLedStateAck ledState = (GetParameterLedStateAck) decodeGetParamterAck(is, -1);
        GetParameterAutoSendNetworkInfoAck autoSend = (GetParameterAutoSendNetworkInfoAck) decodeGetParamterAck(is, -1);
        GetParameterNetworkInfoAck infoAck = (GetParameterNetworkInfoAck) decodeGetParamterAck(is, -1);
        GetParameterEnableFlexSuiteHeaderAck enableFlexSuiteHeader = (GetParameterEnableFlexSuiteHeaderAck) decodeGetParamterAck(is, -1);
        GetParameterMessageCountAck messageCountAck = (GetParameterMessageCountAck) decodeGetParamterAck(is, -1);
        GetParameterEnableEncryptionAck enableEncryption = (GetParameterEnableEncryptionAck) decodeGetParamterAck(is, -1);
        GetParameterDebugInfoAck debugInfo = (GetParameterDebugInfoAck) decodeGetParamterAck(is, -1);
        GetParameterBatteryVoltageAck batteryVoltage = (GetParameterBatteryVoltageAck) decodeGetParamterAck(is, -1);
        GetParameterDutyCyclesAck dutyCycles = (GetParameterDutyCyclesAck) decodeGetParamterAck(is, -1);
        GetParameterLastChargeTimeAck chargeTime = (GetParameterLastChargeTimeAck) decodeGetParamterAck(is, -1, decodeReading19Bit);
        GetParameterBatteryLowAck batteryLowVoltage = (GetParameterBatteryLowAck) decodeGetParamterAck(is, -1);
        GetParameterBatteryOffsetAck batteryOffsetVoltage = (GetParameterBatteryOffsetAck) decodeGetParamterAck(is, -1);
        GetParameterBatteryMaxAck batteryMaxVoltage = (GetParameterBatteryMaxAck) decodeGetParamterAck(is, -1);
        GetParameterMailToAck mailTo = (GetParameterMailToAck) decodeGetParamterAck(is, totalLength - 7);

        return new GetParameterAllAck(
            ota,
            radioSleep,
            alarmLimit,
            autoDelete,
            batteryStatus,
            softwareVersion,
            ledState,
            autoSend,
            infoAck,
            enableFlexSuiteHeader,
            messageCountAck,
            enableEncryption,
            debugInfo,
            batteryVoltage,
            dutyCycles,
            chargeTime,
            batteryLowVoltage,
            batteryOffsetVoltage,
            batteryMaxVoltage,
            mailTo);
    }

    private static EraseAck decodeEraseCommandAck(InputStream is) throws IOException {
        int length = is.read();
        int typeCode = is.read();
        String type = null;
        switch (typeCode) {
            case ERASE_ALL_READINGS:
                type = "ALL_READINGS";
                break;
            case ERASE_ALL_READINGS_FOR_SN:
                type = "ALL_READINGS_FOR_SN";
                break;
            case ERASE_ALL_DELIVERED_READINGS:
                type = "ALL_DELIVERED";
                break;
            case ERASE_ALL_DELIVERED_READINGS_FOR_SN:
                type = "ALL_DELIVERED_FOR_SN";
                break;
            case ERASE_ALL_SN_ALL_ENTRIES:
                type = "ALL_SN_AND_ALL_READINGS";
                break;
            case ERASE_ALL_ENTRIES_FOR_SN:
                type = "SN_AND_READINGS";
                break;
            default:
                type = "UNKNOWN";
        }

        if (1 == length) {
            return new EraseAck(type);
        } else {
            return new EraseAck(type, decryptSerialNumber(is));
        }
    }

    // TEST MAIN
    public static void main(String[] args) throws Exception {
        //        int reading;
        //        reading = 0xFF001B30;
        //        reading = 0xFF3D85D0;
        //        reading = 0xfff54b18;
        //        logger.fine( "reading >>> 22" + (reading >>> 22) + "," + ((reading >>> 22) & 1020) );
        //        if ( ((reading >>> 22) & TIME_CORRECTION_READING_FLAG_VALUE) == TIME_CORRECTION_READING_FLAG_VALUE ) {
        //            int offset = (reading & 0x3ffff8) >> 3;
        //            if ( (0x40000 & offset ) != 0 ) {
        //                logger.fine( "OFFSET NEGATIVE (sign bit (19th) is flipped, convert to negative number)" );
        //                offset = (offset) - 0x80000;
        //            }
        //
        //            int timeCorrectionOffsetMinutes = convert19BitDateMinutes( offset ); //add back minutes lost in compression
        //            logger.fine( "OFFSET=" + offset + ",bin=" + Integer.toBinaryString( offset ) + ",convert19BitDateMinutes=" + timeCorrectionOffsetMinutes );
        //            //currentGlucoseMeter.addReading( decodeReading( reading,  timeCorrectionOffsetMinutes ) ); //DEBUG REMOVE@!!!!!
        //        }
        //
        //        //          Reading : 0x32 0x0F 0x9E 0x34
        //        //          Date : 03/30/04 08:16:00
        //        //          Value : 200 ,  TC = 1 CT = 0
        //        logger.fine(  decodeReading( 0x320F9E34, 0xff ).toString() );
        //        //
        //        //          Reading : 0x35 0xDF 0xA1 0xB4
        //        //          Date : 03/30/04 10:09:00
        //        //          Value : 215, TC = 1 CT = 0
        //        logger.fine(  decodeReading( 0x35DFA1B4, 0xff ).toString() );
        //        //
        //        //          Reading : 0x96 0x5F 0xA7 0x24
        //        //          Date : 03/30/04 13:04:30
        //        //          Value :  HIGH, TC = 1 CT = 0
        //        logger.fine(  decodeReading( 0x965FA724, 0xff).toString() );
        //        //
        //        //          Reading : 0xFF 0x00 0x1B 0x30
        //        //          TimeOffset : - 14h  34min
        //        //          Value :  TCORR, TC = 0 CT = 0
        //        // this is the time offset amount, indicating the time is changed and followoing readings time correct = 0 (NOK) based on this offset
        //        logger.fine(  decodeReading( 0xFF001B30, 0xff ).toString() );
        //
        //        logger.fine(  decodeReading( 0x965FA720, 0xff ).toString() );
        //        logger.fine(  decodeReading( 0x92AFFF27, 0xff ).toString() );
        //

        //
        //        //This is the difference between OTA time and [GM] time with a resolution of 1 minute.
        //        //        It is a signed value going from 150 days to +150 days.
        //        //        It is only reported if the time difference between the [GM] and the [DMD] is larger then 10 minutes and if OTA_Time settings are used.
        //        //        This is saved as a reading if a time offset has been detected.
        //        //
        //        //Example:
        //        //	Time on the [GM] changed, timeoffset = -15days, 4hour, 7 minutes
        //        //
        //        //1.	Equal to 367 hours 7 minutes, equals to  21847 minutes.
        //        //2.	every 3 hours, we loose a minute, meaning we loose 122 minutes
        //        //Converted into minutes: - 20294
        //        //	Shifted into a reading: 0x00 0x03D 0x85 0xD0
        //        encryptSerialNumber( "QKP097ECT" );
        //        encryptSerialNumber( "QRG412BCT" );
        //        encryptSerialNumber( "RGZ48D6FT" );
        //        encodeGetReadings( false, "RGZ48D6FT" );
        //        //3D6E4BA236814C85
        //        //3D 6E 4B A2 36 81 4C 85
        //        byte[] b = new byte[8];
        //        b[0] = (byte)0x3d;
        //        b[1] = (byte)0x6e;
        //        b[2] = (byte)0x4b;
        //        b[3] = (byte)0xa2;
        //        b[4] = (byte)0x36;
        //        b[5] = (byte)0x81;
        //        b[6] = (byte)0x4c;
        //        b[7] = (byte)0x85;
        //        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream( b );
        //

        //        //time offset after reading?
        //        byte[] msg = new byte[] { (byte)0x5A, (byte) 0x01, (byte) 0x75, (byte) 0x4F, (byte) 0x08, (byte) 0xA4, (byte) 0x36, (byte) 0x95, (byte) 0x52, (byte) 0xF5, (byte) 0x00, (byte) 0x02, (byte) 0x22, (byte) 0xC0, (byte) 0x39, (byte) 0x50, (byte) 0xFF, (byte) 0x32, (byte) 0x23, (byte) 0xE0 };
        //        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream( msg );
        //        logger.fine( decode( in ).toString() );

        // DIY DATE FILE
        //        logger.setLevel( java.util.logging.Level.OFF );
        //        java.io.FileOutputStream out = new java.io.FileOutputStream( "x:\\ericdate.txt" );
        //        for ( int i = 0; i < 524287; i++ ) {
        //            out.write( (decode19BitDate( i ).toString() + "," + i + "\r\n").getBytes() );
        //        }
        //        out.close();
        int[] msg = encodeParameterSetMailToAddress("wctp.diabetech.net/wctp/");
        byte[] byteMsg = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            byteMsg[i] = (byte) msg[i];
        }
        System.out.println(new String(Base64.encodeBase64(byteMsg)));

        msg = encodeParameterSetAutoDeleteGlucoseMeter(true);
        byteMsg = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            byteMsg[i] = (byte) msg[i];
        }
        System.out.println(new String(Base64.encodeBase64(byteMsg)));
        msg = encodeParameterSetAutoDeleteGlucoseMeter(false);
        byteMsg = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            byteMsg[i] = (byte) msg[i];
        }
        System.out.println(new String(Base64.encodeBase64(byteMsg)));
        msg = encodeEventReadGlucoseMeter();
        byteMsg = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            byteMsg[i] = (byte) msg[i];
        }
        System.out.println("encodeEventReadGlucoseMeter:" + new String(Base64.encodeBase64(byteMsg)));

        msg = encodeParameterSetEnableEncryption(false);
        byteMsg = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            byteMsg[i] = (byte) msg[i];
        }
        System.out.println("encodeParameterSetEnableEncryption(false):" + new String(Base64.encodeBase64(byteMsg)));
        msg = encodeParameterSetEnableEncryption(true);
        byteMsg = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            byteMsg[i] = (byte) msg[i];
        }
        System.out.println("encodeParameterSetEnableEncryption(true):" + new String(Base64.encodeBase64(byteMsg)));
    }
}
