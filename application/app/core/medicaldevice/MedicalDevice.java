package core.medicaldevice;

import core.datapoint.DataPoint;
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
import javax.persistence.Table;
import models.MyModel;
import models.User;
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
@Table(name = "medicalDevice")
@NamedQueries({
    @NamedQuery(name = "MedicalDevice.findByIsActive", query = "SELECT m FROM MedicalDevice m WHERE m.isActive = :isActive"),
    @NamedQuery(name = "MedicalDevice.findByCreated", query = "SELECT m FROM MedicalDevice m WHERE m.created = :created"),
    @NamedQuery(name = "MedicalDevice.findByUpdated", query = "SELECT m FROM MedicalDevice m WHERE m.updated = :updated"),
    @NamedQuery(name = "MedicalDevice.findByUpdatedBy", query = "SELECT m FROM MedicalDevice m WHERE m.updatedBy = :updatedBy")
})
public class MedicalDevice extends MyModel {

    @Column(name = "serialNumber", nullable = false)
    private String serialNumber;
    @JoinColumn(name = "userId")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicalDevice")
    private java.util.Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();

    /** Creates a new instance of MedicalDevice */
    public MedicalDevice() {
    }

    public MedicalDevice(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public MedicalDevice(String serialNumber, boolean isActive, Date created, Date updated, String updatedBy) {
        this.serialNumber = serialNumber;
        this.isActive = isActive;
        this.created = created;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public java.util.Collection<DataPoint> getDataPoints() {
        return this.dataPoints;
    }

    public void setDataPoints(java.util.Collection<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        for (DataPoint d : this.dataPoints) {
            d.setMedicalDevice(this);
        }
    }

    public void addDataPoint(DataPoint d) {
        d.setMedicalDevice(this);
        d.setUser(this.getUser());
        this.dataPoints.add(d);
    }

    public void removeDataPoint(DataPoint d) {
        d.setMedicalDevice(null);
        this.dataPoints.remove(d);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MedicalDevice == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        MedicalDevice rhs = (MedicalDevice) obj;
        return new EqualsBuilder().append(serialNumber, rhs.serialNumber).
            isEquals();
    }

    public int compareTo(Object o) {
        MedicalDevice rhs = (MedicalDevice) o;
        return new CompareToBuilder().append(serialNumber, rhs.serialNumber).
            toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(serialNumber).
            toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).
            append("isActive", isActive).
            append("serialNumber", serialNumber).
            append("userId", user == null ? "null" : String.valueOf(user.getId())).
            toString();
    }

    public static MedicalDevice findBySerialNumber(String serialNumber) {
        return find(
            "  SELECT m "
            + "FROM   MedicalDevice m "
            + "WHERE  m.serialNumber = :serialNumber "
            + "       and "
            + "       m.isActive = true").
            bind("serialNumber", serialNumber).
            first();
    }
}
