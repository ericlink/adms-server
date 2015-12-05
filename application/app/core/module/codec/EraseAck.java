package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class EraseAck extends InboundMessage {

    private String type;
    private String serialNumber;

    public EraseAck(String type) {
        this.type = type;
    }

    public EraseAck(String type, String serialNumber) {
        this.type = type;
        this.serialNumber = serialNumber;
    }

    /** Getter for property serialNumber.
     * @return Value of property serialNumber.
     *
     */
    public String getSerialNumber() {
        return this.serialNumber;
    }

    /** Setter for property serialNumber.
     * @param serialNumber New value of property serialNumber.
     *
     */
    public void setSerialNumber(String serialNumber) {
    }

    public String toString() {
        return super.toString() + "[type=" + type + ",serialNumber=" + serialNumber + "]";
    }
}
