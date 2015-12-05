package core.messaging;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.MyModel;
import models.Schedule;
import models.User;
import core.feature.userfeatureprofile.UserFeatureProfileDestinationProfile;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "destination")
@NamedQueries({
    @NamedQuery(name = "Destination.findByType", query = "SELECT a FROM Destination a WHERE a.type = :type"),
    @NamedQuery(name = "Destination.findByAddress", query = "SELECT a FROM Destination a WHERE a.address = :address"),
    //@NamedQuery(name = "Destination.findByIsActive", query = "SELECT a FROM Destination a WHERE a.isActive = :isActive"),
    @NamedQuery(name = "Destination.findByCreated", query = "SELECT a FROM Destination a WHERE a.created = :created"),
    @NamedQuery(name = "Destination.findByUpdated", query = "SELECT a FROM Destination a WHERE a.updated = :updated"),
    @NamedQuery(name = "Destination.findByUpdatedBy", query = "SELECT a FROM Destination a WHERE a.updatedBy = :updatedBy")
})
public class Destination extends MyModel {

    @Column(name = "type", nullable = false)
    private DestinationType type;
    @Column(name = "address", nullable = false, unique = true)
    private String address;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "defaultScheduleId")
    private Schedule defaultSchedule;
    @Column(name = "firstMessagedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstMessagedOn;
    @Column(name = "lastMessagedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMessagedOn;
    @Column(name = "totalMessagesSent", nullable = false)
    private int totalMessagesSent;
    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destination")
    private java.util.Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationSchedules = new ArrayList<UserFeatureProfileDestinationProfile>();

    /** Creates a new instance of Destination */
    public Destination() {
    }

    public Destination(DestinationType type, String destination, boolean isActive, Date created, Date updated, String updatedBy) {
        this.type = type;
        this.address = destination;
        this.isActive = isActive;
        this.created = created;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public DestinationType getType() {
        return this.type;
    }

    public void setType(DestinationType type) {
        this.type = type;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addUserFeatureProfileDestinationSchedule(UserFeatureProfileDestinationProfile userFeatureProfileDestinationSchedule) {
        userFeatureProfileDestinationSchedule.setDestination(this);
        this.userFeatureProfileDestinationSchedules.add(userFeatureProfileDestinationSchedule);
    }

    public void removeUserFeatureProfileDestinationSchedule(UserFeatureProfileDestinationProfile userFeatureProfileDestinationSchedule) {
        userFeatureProfileDestinationSchedule.setDestination(null);
        this.userFeatureProfileDestinationSchedules.remove(userFeatureProfileDestinationSchedule);
    }

    public Schedule getDefaultSchedule() {
        return defaultSchedule != null ? defaultSchedule : Schedule.DEFAULT_WIDE_OPEN_SCHEDULE;
    }

    public void setDefaultSchedule(Schedule defaultSchedule) {
        this.defaultSchedule = defaultSchedule;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Destination == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Destination rhs = (Destination) obj;
        return new EqualsBuilder().append(address, rhs.address).append(type, rhs.type).append(user, rhs.user).isEquals();
    }

    public int compareTo(Object o) {
        Destination rhs = (Destination) o;
        return new CompareToBuilder().append(address, rhs.address).append(type, rhs.type).append(user, rhs.user).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(address).append(type).append(user).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("isActive", isActive).append("destination", address).append("type", type).append("user", user).toString();
    }

    public Date getLastMessagedOn() {
        return lastMessagedOn;
    }

    public void setLastMessagedOn(Date lastMessagedOn) {
        this.lastMessagedOn = lastMessagedOn;
    }

    public Date getFirstMessagedOn() {
        return firstMessagedOn;
    }

    public void setFirstMessagedOn(Date firstMessagedOn) {
        this.firstMessagedOn = firstMessagedOn;
    }

    public int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    public void setTotalMessagesSent(int totalMessagesSent) {
        this.totalMessagesSent = totalMessagesSent;
    }
}
