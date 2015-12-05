package core.api.data;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Confidential Information.
 * Copyright (C) 2007 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class InboundMessage implements Serializable {

    private String timestampMessageReceived;
    private String hardwareId;
    private InboundMessage.MessageType messageType;
    private String softwareVersion;
    private String payload;

    public InboundMessage(String timestampMessageReceived,
        String hardwareId,
        InboundMessage.MessageType messageType,
        String softwareVersion,
        String payload) {
        this.timestampMessageReceived = timestampMessageReceived;
        this.hardwareId = hardwareId;
        this.messageType = messageType;
        this.softwareVersion = softwareVersion;
        this.payload = payload;
    }

    public String getModuleId() {
        return hardwareId;
    }

    public void setModuleId(String moduleId) {
        this.hardwareId = moduleId;
    }

    public InboundMessage.MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(InboundMessage.MessageType messageType) {
        this.messageType = messageType;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTimestampMessageReceived() {
        return timestampMessageReceived;
    }

    public void setTimestampMessageReceived(String timestampMessageReceived) {
        this.timestampMessageReceived = timestampMessageReceived;
    }

    public static enum MessageType {

        UNKNOWN,
        DATA,
        APP_STARTUP,
        OTAP_NOTIFICATION
    }

    public String toString() {
        return new ToStringBuilder(this).append("timestampMessageReceived", timestampMessageReceived).append("hardwareId", hardwareId).append("messageType", messageType).append("softwareVersion", softwareVersion).toString();
    }
}