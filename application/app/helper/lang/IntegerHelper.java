package helper.lang;

/**   
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class IntegerHelper {

    private IntegerHelper() {
    }

    public static String toHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0 && i % 16 == 0) {
                sb.append("\n");
            } else if (i > 0 && i % 8 == 0) {
                sb.append(" ");
            }

            String val = Integer.toHexString(bytes[i] & 0xFF);
            if (val.length() < 2) {
                sb.append("0");
            }
            sb.append(val.toUpperCase());
            sb.append(" ");

        }
        return sb.toString();
    }

    /** truncate ints to byte size and return hex string **/
    public static String toHexString(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return toHexString(bytes);
    }

    /** truncate ints to byte size and return hex string **/
    public static String toHexString(int anInt) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (anInt >> 24);
        bytes[1] = (byte) (anInt >> 16);
        bytes[2] = (byte) (anInt >> 8);
        bytes[3] = (byte) anInt;
        return toHexString(bytes);
    }

    /** truncate ints to byte size and return hex string **/
    public static String toHexString(long aLong) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (aLong >> 54);
        bytes[1] = (byte) (aLong >> 48);
        bytes[2] = (byte) (aLong >> 40);
        bytes[3] = (byte) (aLong >> 32);
        bytes[4] = (byte) (aLong >> 24);
        bytes[5] = (byte) (aLong >> 16);
        bytes[6] = (byte) (aLong >> 8);
        bytes[7] = (byte) aLong;
        return toHexString(bytes);
    }
}
