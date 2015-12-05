package core.feature.userfeatureprofile;

import core.messaging.Destination;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.MyModel;
import models.Schedule;
import models.Threshold;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "userFeatureProfileDestinationProfile")
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class UserFeatureProfileDestinationProfile extends MyModel {

    @JoinColumn(name = "userFeatureProfileId", nullable = false)
    @ManyToOne
    private UserFeatureProfile userFeatureProfile;
    @JoinColumn(name = "destinationId", nullable = false)
    @ManyToOne
    private Destination destination;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "thresholdId")
    private Threshold threshold;
    @Column(name = "lastMessageSendAllowedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMessageSendAllowedOn;

    public UserFeatureProfileDestinationProfile() {
    }

    public UserFeatureProfile getFeatureProfile() {
        return userFeatureProfile;
    }

    public void setFeatureProfile(UserFeatureProfile userFeatureProfile) {
        this.userFeatureProfile = userFeatureProfile;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Schedule getSchedule() {
        return schedule != null ? schedule : getDestination().getDefaultSchedule();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public void setThreshold(Threshold threshold) {
        this.threshold = threshold;
    }

    public boolean equals(Object obj) {
        if (obj instanceof UserFeatureProfileDestinationProfile == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        UserFeatureProfileDestinationProfile rhs = (UserFeatureProfileDestinationProfile) obj;
        return new EqualsBuilder().append(userFeatureProfile, rhs.userFeatureProfile).append(destination, rhs.destination).append(schedule, rhs.schedule).append(threshold, rhs.threshold).isEquals();
    }

    public int compareTo(Object o) {
        UserFeatureProfileDestinationProfile rhs = (UserFeatureProfileDestinationProfile) o;
        return new CompareToBuilder().append(userFeatureProfile, rhs.userFeatureProfile).append(destination, rhs.destination).append(schedule, rhs.schedule).append(threshold, rhs.threshold).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(userFeatureProfile).append(destination).append(schedule).append(threshold).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).append("userFeatureProfile", userFeatureProfile).append("destination", destination).append("schedule", schedule).append("threshold", threshold).toString();
    }

    public Date getLastMessageSendAllowedOn() {
        return lastMessageSendAllowedOn;
    }

    public void setLastMessageSendAllowedOn(Date lastMessageSendAllowedOn) {
        this.lastMessageSendAllowedOn = lastMessageSendAllowedOn;
    }
}
