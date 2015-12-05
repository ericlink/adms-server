package core.module;

import java.util.Properties;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class WirelessNetwork {

    public static final String SKYTEL = "skytel";
    public static final String SKYTEL_TEST = "skytel.test";
    public static final String WEBLINK = "weblink";
    public static final String USA_MOBILITY = "usamobility";
    public static final String DIABTECH_TEST = "diabetech.test";
    public static final String WCTP = "wctp";
    public static final String SMTP = "smtp";
    public static final String AMBIENT = "ambient";
    private final static Properties networkProperties = new Properties();

    static {
        //this doesn't really change, so just load table here.
        //networkProperties.load( new FileInputStream( "wirelessNetwork.properties" ) );
        networkProperties.put("skytel.network.code", NetworkType.SKYTEL.toString());
        networkProperties.put("skytel.wctp.preferred", "true");
        networkProperties.put("skytel.email.server", "skytel.com");
        networkProperties.put("skytel.wctp.server", "http://wctp.skytel.com/wctp");
        networkProperties.put("skytel.wctp.orignator.senderId", "http://wctp.diabetech.net/wctp/");
        networkProperties.put("skytel.wctp.orignator.miscInfo", "");
        networkProperties.put("skytel.wctp.messageControl.sendResponsesToID", "http://wctp.diabetech.net/wctp/");
        networkProperties.put("skytel.wctp.messageControl.notifyWhenDelivered", "true");
        networkProperties.put("skytel.wctp.messageControl.notifyWhenQueued", "true");

        networkProperties.put("skytel.test.network.code", NetworkType.SKYTEL_TEST.toString());
        networkProperties.put("skytel.test.wctp.preferred", "false");
        networkProperties.put("skytel.test.email.server", "vendornoc.skytelatc.com");
        networkProperties.put("skytel.test.wctp.server", "http://viptest-1.skytelatc.com/servlet/WCTPVendorProject");
        networkProperties.put("skytel.test.wctp.orignator.senderId", "http://diabetech.dyndns.org/wctp");
        networkProperties.put("skytel.test.wctp.orignator.miscInfo", "");
        networkProperties.put("skytel.test.wctp.messageControl.sendResponsesToID", "http://diabetech.dyndns.org/wctp");
        networkProperties.put("skytel.test.wctp.messageControl.notifyWhenDelivered", "true");
        networkProperties.put("skytel.test.wctp.messageControl.notifyWhenQueued", "true");

        networkProperties.put("weblink.network.code", NetworkType.WEBLINK.toString());
        networkProperties.put("weblink.wctp.preferred", "true");
        networkProperties.put("weblink.email.server", "airmessage.net");
        networkProperties.put("weblink.wctp.server", "http://wctp.wirelesscontrol.net/_vti_bin/wctp_isapi.dll?WCTPPost");
        networkProperties.put("weblink.wctp.orignator.senderId", "141@wirelesscontrol.net");
        networkProperties.put("weblink.wctp.orignator.miscInfo", "[WCTP]http://wctp.diabetech.net/wctp/");
        networkProperties.put("weblink.wctp.messageControl.sendResponsesToID", "http://wctp.diabetech.net/wctp/");
        networkProperties.put("weblink.wctp.messageControl.notifyWhenDelivered", "true");
        networkProperties.put("weblink.wctp.messageControl.notifyWhenQueued", "true");

        networkProperties.put("diabetech.test.network.code", NetworkType.DIABETECH_TEST.toString());
        networkProperties.put("diabetech.test.wctp.preferred", "true");
        networkProperties.put("diabetech.test.email.server", "diabetech.net");
        networkProperties.put("diabetech.test.wctp.server", "http://wctp.diabetech.net/wctp/");
        networkProperties.put("diabetech.test.wctp.orignator.senderId", "141@wirelesscontrol.net");
        networkProperties.put("diabetech.test.wctp.orignator.miscInfo", "[WCTP]http://wctp.diabetech.net/wctp/");
        networkProperties.put("diabetech.test.wctp.messageControl.sendResponsesToID", "http://wctp.diabetech.net/wctp/");
        networkProperties.put("diabetech.test.wctp.messageControl.notifyWhenDelivered", "true");
        networkProperties.put("diabetech.test.wctp.messageControl.notifyWhenQueued", "true");

        networkProperties.put("usamobility.network.code", NetworkType.USA_MOBILITY.toString());
        networkProperties.put("usamobility.wctp.preferred", "true");
        networkProperties.put("usamobility.email.server", "airmessage.net");
        networkProperties.put("usamobility.wctp.server", "http://wctp.airmessage.net/wctp");
        networkProperties.put("usamobility.wctp.orignator.senderId", "wctp.diabetech.net/wctp/");
        //null networkProperties.put( "usamobility.wctp.orignator.miscInfo",                "" );
        networkProperties.put("usamobility.wctp.messageControl.sendResponsesToID", "wctp.diabetech.net/wctp/");
        networkProperties.put("usamobility.wctp.messageControl.notifyWhenDelivered", "true");
        networkProperties.put("usamobility.wctp.messageControl.notifyWhenQueued", "true");

        networkProperties.put("ambient.network.code", NetworkType.AMBIENT.toString());
        networkProperties.put("ambient.wctp.preferred", "false");
        networkProperties.put("ambient.email.server", "diabetech.net");
        networkProperties.put("ambient.wctp.server", "http://myambient.com/java/my_devices/submitdata.jsp");
        networkProperties.put("ambient.wctp.orignator.senderId", "self");
        //null networkProperties.put( "ambient.wctp.orignator.miscInfo",                "n/a" );
        networkProperties.put("ambient.wctp.messageControl.sendResponsesToID", "self");
        networkProperties.put("ambient.wctp.messageControl.notifyWhenDelivered", "false");
        networkProperties.put("ambient.wctp.messageControl.notifyWhenQueued", "false");

    }

    private WirelessNetwork() {
    } //static class only

    public static String getGatewayEmailAddress(String pin, String wirelessNetworkId) {
        StringBuffer sb = new StringBuffer();
        sb.append(pin);
        sb.append("@");
        sb.append(networkProperties.getProperty(wirelessNetworkId + ".email.server"));
        return sb.toString();
    }

    public static String getNetworkCode(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".network.code");
    }

    public static String getGatewayWctpUrl(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".wctp.server");
    }

    public static boolean isWctpPreferred(String wirelessNetworkId) {
        return Boolean.getBoolean(networkProperties.getProperty(wirelessNetworkId + ".wctp.preferred"));
    }

    public static String getPreferredTransportType(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".wctp.preferred").equals("true") ? WCTP : SMTP;
    }

    public static String getWctpOriginatorSenderId(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".wctp.orignator.senderId");
    }

    public static String getWctpOriginatorMiscInfo(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".wctp.orignator.miscInfo");
    }

    public static String getWctpMessageControlSendResponsesToId(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".wctp.messageControl.sendResponsesToID");
    }

    public static String getWctpMessageControlNotifyWhenDelivered(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".wctp.messageControl.notifyWhenDelivered");
    }

    public static String getWctpMessageControlNotifyWhenQueued(String wirelessNetworkId) {
        return networkProperties.getProperty(wirelessNetworkId + ".wctp.messageControl.notifyWhenQueued");
    }

    public static String getNetworkId(String senderId) {
        if (senderId.indexOf("airmessage.net") != -1) {
            return WirelessNetwork.USA_MOBILITY;
        } else if (senderId.indexOf("skytel.com") != -1) {
            return WirelessNetwork.SKYTEL;
        } else if (senderId.indexOf("vendornoc.skytelatc.com") != -1) {
            return WirelessNetwork.SKYTEL_TEST;
        } else {
            return WirelessNetwork.WEBLINK;
        }
    }

    /**
     * @return plain Pin, stripping off everythign after @ (if @ is there)
     **/
    public static String getPin(String senderId) {
        int ampersandIndex = senderId.indexOf("@");
        if (ampersandIndex != -1) {
            // strip off everything after @
            return senderId.substring(0, ampersandIndex);
        } else {
            return senderId;
        }
    }

    public static String getDefaultDisplayKey(String senderId) {
        if (!getNetworkId(senderId).equals(WirelessNetwork.USA_MOBILITY)) {
            return getPin(senderId);
        } else {
            return null;
        }
    }
}
