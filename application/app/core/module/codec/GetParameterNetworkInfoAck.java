package core.module.codec;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class GetParameterNetworkInfoAck extends GetParameterAck {

    private int serviceProviderId;
    private int zoneId;
    private int subZoneId;
    private int colorCode;

    public GetParameterNetworkInfoAck(int serviceProviderId, int zoneId, int subZoneId, int colorCode) {
        this.serviceProviderId = serviceProviderId;
        this.zoneId = zoneId;
        this.subZoneId = subZoneId;
        this.colorCode = colorCode;
    }

    public int getServiceProviderId() {
        return this.serviceProviderId;
    }

    public int getZoneId() {
        return this.zoneId;
    }

    public int getSubZoneId() {
        return this.subZoneId;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public String toString() {
        return super.toString() + "[serviceProviderId=" + serviceProviderId + ",zoneId=" + zoneId + ",subZoneId=" + subZoneId + ",colorCode=" + colorCode + "]";
    }
}
