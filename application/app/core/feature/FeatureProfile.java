package core.feature;

import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import models.MyModel;
import models.Schedule;
import core.feature.userfeatureprofile.UserFeatureProfile;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "featureProfile")
@NamedQueries({
    @NamedQuery(name = "FeatureProfile.findByName", query = "SELECT fp FROM FeatureProfile fp WHERE fp.isActive = true and fp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class FeatureProfile extends MyModel {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "isOnNewDataPointEvent", nullable = false, columnDefinition = "int")
    private boolean isOnNewDataPointEvent = true;
    @Column(name = "isOnNewMedicalDeviceEvent", nullable = false, columnDefinition = "int")
    private boolean isOnNewMedicalDeviceEvent = true;
    @Column(name = "isSchedulable", nullable = false, columnDefinition = "int")
    private boolean isSchedulable = true;
    /**
     * 0 - 365
     **/
    @Column(name = "minimumIntervalDays", nullable = false)
    private int minimumIntervalDays;
    @JoinColumn(name = "intensiveManagementProtocolId", nullable = false)
    @ManyToOne
    private IntensiveManagementProtocol intensiveManagementProtocol;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "defaultScheduleId")
    private Schedule defaultSchedule;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "featureProfile")
    private java.util.Collection<UserFeatureProfile> userFeatureProfiles = new ArrayList<UserFeatureProfile>();

    public FeatureProfile() {
    }

    public boolean equals(Object obj) {
        if (obj instanceof FeatureProfile == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        FeatureProfile rhs = (FeatureProfile) obj;
        return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
    }

    public int compareTo(Object o) {
        FeatureProfile rhs = (FeatureProfile) o;
        return new CompareToBuilder().append(getName(), rhs.getName()).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", getName()).toString();
    }

    public IntensiveManagementProtocol getIntensiveManagementProtocol() {
        return intensiveManagementProtocol;
    }

    public void setIntensiveManagementProtocol(IntensiveManagementProtocol intensiveManagementProtocol) {
        this.intensiveManagementProtocol = intensiveManagementProtocol;
    }

    public java.util.Collection<UserFeatureProfile> getUserFeatureProfiles() {
        return userFeatureProfiles;
    }

    public Schedule getDefaultSchedule() {
        return defaultSchedule != null ? defaultSchedule : Schedule.DEFAULT_WIDE_OPEN_SCHEDULE;
    }

    public void setDefaultSchedule(Schedule defaultSchedule) {
        this.defaultSchedule = defaultSchedule;
    }

    public void setUserFeatureProfiles(java.util.Collection<UserFeatureProfile> userFeatureProfiles) {
        this.userFeatureProfiles = userFeatureProfiles;
    }

    public void addUserFeatureProfile(UserFeatureProfile userFeatureProfile) {
        userFeatureProfile.setFeatureProfile(this);
        this.userFeatureProfiles.add(userFeatureProfile);
    }

    public void removeUserFeatureProfile(UserFeatureProfile userFeatureProfile) {
        userFeatureProfile.setFeatureProfile(null);
        this.userFeatureProfiles.remove(userFeatureProfile);
    }

    public boolean isOnNewDataPointEvent() {
        return isOnNewDataPointEvent;
    }

    public void setIsOnNewDataPointEvent(boolean isOnNewDataPointEvent) {
        this.isOnNewDataPointEvent = isOnNewDataPointEvent;
    }

    public boolean isOnNewMedicalDeviceEvent() {
        return isOnNewMedicalDeviceEvent;
    }

    public void setIsOnNewMedicalDeviceEvent(boolean isOnNewMedicalDeviceEvent) {
        this.isOnNewMedicalDeviceEvent = isOnNewMedicalDeviceEvent;
    }

    public boolean isSchedulable() {
        return isSchedulable;
    }

    public void setIsSchedulable(boolean isSchedulable) {
        this.isSchedulable = isSchedulable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinimumIntervalDays() {
        return minimumIntervalDays;
    }

    public void setMinimumIntervalDays(int minimumIntervalDays) {
        this.minimumIntervalDays = minimumIntervalDays;
    }
}
