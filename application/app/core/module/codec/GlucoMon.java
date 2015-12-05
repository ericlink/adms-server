package core.module.codec;

import core.module.WirelessNetwork;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GlucoMon {

    public static String SMTP = WirelessNetwork.SMTP;
    public static String WCTP = WirelessNetwork.WCTP;
    private String deviceEmailAddress;
    private String pin;
    private String wirelessNetwork;
    private boolean isEncrypted;
    private byte[] secretKey;
    private String transportType;

    public GlucoMon(String pin, String wirelessNetwork) {
        this(pin, wirelessNetwork, null);
    }

    public GlucoMon(String pin, String wirelessNetwork, byte[] secretKey) {
        this.pin = pin;
        this.wirelessNetwork = wirelessNetwork;
        this.transportType = WirelessNetwork.getPreferredTransportType(wirelessNetwork);
        if (secretKey != null) {
            isEncrypted = true;
            if (secretKey.length != 16) {
                throw new IllegalArgumentException(this.getClass().getName() + ": encryption key must be 16 bytes (128 bits).");
            }
            this.secretKey = secretKey;
        }
    }

    public String getDeviceEmailAddress() {
        return WirelessNetwork.getGatewayEmailAddress(pin, wirelessNetwork);
    }

    public String getPin() {
        return this.pin;
    }

    public String getNetwork() {
        return this.wirelessNetwork;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public String toString() {
        return this.getClass().getName() + " [pin=" + this.pin + ",wirelessNetwork=" + wirelessNetwork + ",getDeviceEmailAddress=" + getDeviceEmailAddress() + ",isEncrypted=" + isEncrypted + "]";
    }

    /** Getter for property transportType.
     * @return Value of property transportType.
     *
     */
    public String getTransportType() {
        return this.transportType;
    }

    /** Only used to overried network setting
     * @param transportType New value of property transportType.
     *@see net.diabetech.module.WirelessNetwork
     */
    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getWctpRecipientId() {
        if (wirelessNetwork.equals(WirelessNetwork.USA_MOBILITY)) {
            return WirelessNetwork.getGatewayEmailAddress(getPin(), WirelessNetwork.USA_MOBILITY);
        } else {
            return getPin();
        }
    }

    public String getWctpOriginatorSenderId() {
        return WirelessNetwork.getWctpOriginatorSenderId(wirelessNetwork);
    }

    public String getWctpOriginatorMiscInfo() {
        return WirelessNetwork.getWctpOriginatorMiscInfo(wirelessNetwork);
    }

    public String getWctpMessageControlSendResponsesToId() {
        return WirelessNetwork.getWctpMessageControlSendResponsesToId(wirelessNetwork);
    }

    public String getWctpMessageControlNotifyWhenDelivered() {
        return WirelessNetwork.getWctpMessageControlNotifyWhenDelivered(wirelessNetwork);
    }

    public String getWctpMessageControlNotifyWhenQueued() {
        return WirelessNetwork.getWctpMessageControlNotifyWhenQueued(wirelessNetwork);
    }

    public String getWctpGatewayUrl() {
        return WirelessNetwork.getGatewayWctpUrl(wirelessNetwork);
    }
}
