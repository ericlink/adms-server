package core.module;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.*;
import core.datapoint.DataPoint;
import models.MyModel;
import models.User;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
@Entity
@Table(name = "module")
@NamedQueries({
    @NamedQuery(name = "Module.findByPin", query = "SELECT m FROM Module m WHERE m.pin = :pin and m.isActive = true"),
    @NamedQuery(name = "Module.findAllActive", query = "SELECT m FROM Module m WHERE m.isActive = true"),
    //@NamedQuery(name = "Module.findByDisplayKey", query = "SELECT m FROM Module m WHERE m.displayKey = :displayKey"),
    @NamedQuery(name = "Module.findByNetworkType", query = "SELECT m FROM Module m WHERE m.networkType = :networkType"),
    @NamedQuery(name = "Module.findByIsEncrypted", query = "SELECT m FROM Module m WHERE m.isEncrypted = :isEncrypted"),
    @NamedQuery(name = "Module.findByPrivateKey", query = "SELECT m FROM Module m WHERE m.privateKey = :privateKey"),
    @NamedQuery(name = "Module.findByTimeZone", query = "SELECT m FROM Module m WHERE m.timeZone = :timeZone"),
    @NamedQuery(name = "Module.findBySleepStartMinutes", query = "SELECT m FROM Module m WHERE m.sleepStartMinutes = :sleepStartMinutes"),
    @NamedQuery(name = "Module.findBySleepStopMinutes", query = "SELECT m FROM Module m WHERE m.sleepStopMinutes = :sleepStopMinutes"),
    @NamedQuery(name = "Module.findByLastCharged", query = "SELECT m FROM Module m WHERE m.lastCharged = :lastCharged"),
    @NamedQuery(name = "Module.findByLastDischarged", query = "SELECT m FROM Module m WHERE m.lastDischarged = :lastDischarged"),
    //@NamedQuery(name = "Module.findByIsActive", query = "SELECT m FROM Module m WHERE m.isActive = :isActive"),
    @NamedQuery(name = "Module.findByCreated", query = "SELECT m FROM Module m WHERE m.created = :created"),
    @NamedQuery(name = "Module.findByUpdated", query = "SELECT m FROM Module m WHERE m.updated = :updated"),
    @NamedQuery(name = "Module.findByUpdatedBy", query = "SELECT m FROM Module m WHERE m.updatedBy = :updatedBy")
})
public class Module extends MyModel {

    @Column(name = "pin", nullable = false)
    private String pin;
    @Column(name = "displayKey")
    private String displayKey;
    @Column(name = "networkCode", nullable = false)
    private NetworkType networkType;
    @Column(name = "isEncrypted", nullable = false, columnDefinition = "int")
    private boolean isEncrypted;
    @Column(name = "privateKey")
    private String privateKey;
    @Column(name = "timeZone", nullable = false)
    private String timeZone = "UTC";
    @Column(name = "sleepStartMinutes")
    private int sleepStartMinutes;
    @Column(name = "sleepStopMinutes")
    private int sleepStopMinutes;
    @Column(name = "lastCharged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCharged;
    @Column(name = "lastDischarged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDischarged;
    @Column(name = "lastMessageSentToModule")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMessageSentToModule;
    @Column(name = "lastStartupMessageReceived")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastStartupMessageReceived;
    @Column(name = "lastReportedAppVersion")
    private String lastReportedAppVersion;
    @Column(name = "lastReportedSimId")
    private String lastReportedSimId;
    @Column(name = "lastReportedSignalStrength")
    private String lastReportedSignalStrength;
    @Column(name = "lastReportedNetwork")
    private String lastReportedNetwork;
    @Column(name = "otapAttempts")
    private int otapAttempts;
    @Column(name = "lastOtapAttempt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastOtapAttempt;
    @Column(name = "lastOtapNotification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastOtapNotification;
    @Column(name = "lastOtapNotificationMessage")
    private String lastOtapNotificationMessage;
    @Column(name = "targetAppVersion")
    private String targetAppVersion;
    @Column(name = "lastMessageReceivedFromModule")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMessageReceivedFromModule;
    @Column(name = "lastTimeSettingsAck")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTimeSettingsAck;
    @Column(name = "maxQuietHoursAllowed")
    private int maxQuietHoursAllowed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "module")
    private java.util.Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
    @JoinColumn(name = "userId")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    /** Creates a new instance of Module */
    public Module() {
    }

    public Module(String pin, NetworkType networkType, boolean isEncrypted, String timeZone, boolean isActive, Date created, Date updated, String updatedBy) {
        this.pin = pin;
        this.networkType = networkType;
        this.isEncrypted = isEncrypted;
        this.timeZone = timeZone;
        this.isActive = isActive;
        this.created = created;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDisplayKey() {
        return this.displayKey;
    }

    public void setDisplayKey(String displayKey) {
        this.displayKey = displayKey;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(this.timeZone);
    }

    public void setTimeZone(TimeZone tz) {
        this.timeZone = tz.getID();
    }

    public int getSleepStartMinutes() {
        return this.sleepStartMinutes;
    }

    public void setSleepStartMinutes(int sleepStartMinutes) {
        this.sleepStartMinutes = sleepStartMinutes;
    }

    public int getSleepStopMinutes() {
        return this.sleepStopMinutes;
    }

    public void setSleepStopMinutes(int sleepStopMinutes) {
        this.sleepStopMinutes = sleepStopMinutes;
    }

    public Date getLastCharged() {
        return this.lastCharged;
    }

    public void setLastCharged(Date lastCharged) {
        this.lastCharged = lastCharged;
    }

    public Date getLastDischarged() {
        return this.lastDischarged;
    }

    public void setLastDischarged(Date lastDischarged) {
        this.lastDischarged = lastDischarged;
    }

    public java.util.Collection<DataPoint> getDataPoints() {
        return this.dataPoints;
    }

    public void setDataPoints(java.util.Collection<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        for (DataPoint d : this.dataPoints) {
            d.setModule(this);
        }
    }

    public void addDataPoint(DataPoint d) {
        d.setModule(this);
        this.dataPoints.add(d);
    }

    public void removeDataPoint(DataPoint d) {
        d.setModule(null);
        this.dataPoints.remove(d);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Module == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Module rhs = (Module) obj;
        return new EqualsBuilder().append(pin, rhs.pin).append(networkType, rhs.networkType).isEquals();
    }

    public int compareTo(Object o) {
        Module rhs = (Module) o;
        return new CompareToBuilder().append(pin, rhs.pin).append(networkType, rhs.networkType).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(pin).append(networkType).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("isActive", isActive).append("pin", pin).append("displayKey", displayKey).append("lastReportedAppVersion", lastReportedAppVersion).append("targetAppVersion", targetAppVersion).append("timeZoneId", timeZone).append("created", created).append("updated", updated).toString();
    }

    public Date getLastMessageReceivedFromModule() {
        return lastMessageReceivedFromModule;
    }

    public void setLastMessageReceivedFromModule(Date lastMessageReceivedFromModule) {
        this.lastMessageReceivedFromModule = lastMessageReceivedFromModule;
    }

    public int getMaxQuietHoursAllowed() {
        return maxQuietHoursAllowed;
    }

    public void setMaxQuietHoursAllowed(int maxQuietHoursAllowed) {
        this.maxQuietHoursAllowed = maxQuietHoursAllowed;
    }

    public Date getLastMessageSentToModule() {
        return lastMessageSentToModule;
    }

    public void setLastMessageSentToModule(Date lastMessageSentToModule) {
        this.lastMessageSentToModule = lastMessageSentToModule;
    }

    public Date getLastTimeSettingsAck() {
        return lastTimeSettingsAck;
    }

    public void setLastTimeSettingsAck(Date lastTimeSettingsAck) {
        this.lastTimeSettingsAck = lastTimeSettingsAck;
    }

    public Date getLastStartupMessageReceived() {
        return lastStartupMessageReceived;
    }

    public void setLastStartupMessageReceived(Date lastStartupMessageReceived) {
        this.lastStartupMessageReceived = lastStartupMessageReceived;
    }

    public String getLastReportedAppVersion() {
        return lastReportedAppVersion;
    }

    public void setLastReportedAppVersion(String lastReportedAppVersion) {
        this.lastReportedAppVersion = lastReportedAppVersion;
    }

    public String getLastReportedSimId() {
        return lastReportedSimId;
    }

    public void setLastReportedSimId(String lastReportedSimId) {
        this.lastReportedSimId = lastReportedSimId;
    }

    public String getLastReportedSignalStrength() {
        return lastReportedSignalStrength;
    }

    public void setLastReportedSignalStrength(String lastReportedSignalStrength) {
        this.lastReportedSignalStrength = lastReportedSignalStrength;
    }

    public String getLastReportedNetwork() {
        return lastReportedNetwork;
    }

    public void setLastReportedNetwork(String lastReportedNetwork) {
        this.lastReportedNetwork = lastReportedNetwork;
    }

    public Date getLastOtapNotification() {
        return lastOtapNotification;
    }

    public void setLastOtapNotification(Date lastOtapNotification) {
        this.lastOtapNotification = lastOtapNotification;
    }

    public String getLastOtapNotificationMessage() {
        return lastOtapNotificationMessage;
    }

    public void setLastOtapNotificationMessage(String lastOtapNotificationMessage) {
        this.lastOtapNotificationMessage = lastOtapNotificationMessage;
    }

    public Date getLastOtapAttempt() {
        return lastOtapAttempt;
    }

    public void setLastOtapAttempt(Date lastOtapAttempt) {
        this.lastOtapAttempt = lastOtapAttempt;
    }

    public int getOtapAttempts() {
        return otapAttempts;
    }

    public void setOtapAttempts(int otapAttempts) {
        this.otapAttempts = otapAttempts;
    }

    public static Module findByPin(String moduleId) {
        return find(
            "  SELECT m "
            + "FROM   Module m "
            + "WHERE  m.pin = :pin "
            + "       and "
            + "       m.isActive = true ").bind("pin", moduleId).first();
    }

    public String getTargetAppVersion() {
        return targetAppVersion == null ? "0" : targetAppVersion;
    }
}
