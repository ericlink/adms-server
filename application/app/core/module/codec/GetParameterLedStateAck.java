package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterLedStateAck extends GetParameterAck {

    int ledState;
    final static int LED_SERIAL_COMM = 0x01;//2x green//active when there is serial communication
    final static int LED_ERROR = 0x02;//2x red//active when an error occured
    final static int LED_RF_COMM = 0x04;//2x green// active when there is reflex communication
    final static int LED_NETWORK = 0x08;//1x green//active when the device is registered on a network
    final static int LED_RADIO = 0x10;//1x orange (radio sleep)//active when radio of disabled or device in sleep
    final static int LED_CHARGER = 0x20;//on orange//active when charger is connected
    final static int LED_BATT_FULL = 0x40;//on green//active when batteries are fully charged
    final static int LED_BATT_LOW = 0x80;//1x red//active when batteries are low

    public GetParameterLedStateAck(int ledState) {
        this.ledState = ledState;
    }

    public int getledState() {
        return ledState;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append("[ledState=");
        sb.append(ledState);
        if ((ledState & LED_SERIAL_COMM) != 0) {
            sb.append(",LED_SERIAL_COMM");
        }
        if ((ledState & LED_ERROR) != 0) {
            sb.append(",LED_ERROR");
        }
        if ((ledState & LED_RF_COMM) != 0) {
            sb.append(",LED_RF_COMM");
        }
        if ((ledState & LED_NETWORK) != 0) {
            sb.append(",LED_NETWORK");
        }
        if ((ledState & LED_RADIO) != 0) {
            sb.append(",LED_RADIO");
        }
        if ((ledState & LED_CHARGER) != 0) {
            sb.append(",LED_CHARGER");
        }
        if ((ledState & LED_BATT_FULL) != 0) {
            sb.append(",LED_BATT_FULL");
        }
        if ((ledState & LED_BATT_LOW) != 0) {
            sb.append(",LED_BATT_LOW");
        }

        sb.append("]");
        return sb.toString();
    }
}
