package models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import models.MyModel;
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
@Table(name = "threshold")
@NamedQueries({
    @NamedQuery(name = "Threshold.findByLowerLimit", query = "SELECT a FROM Threshold a WHERE a.lowerLimit = :lowerLimit"),
    //@NamedQuery(name = "Threshold.findByUpperLimit", query = "SELECT a FROM Threshold a WHERE a.upperLimit = :upperLimit"),
    //@NamedQuery(name = "Threshold.findByIsActive", query = "SELECT a FROM Threshold a WHERE a.isActive = :isActive"),
    @NamedQuery(name = "Threshold.findByCreated", query = "SELECT a FROM Threshold a WHERE a.created = :created")
//@NamedQuery(name = "Threshold.findByUpdated", query = "SELECT a FROM Threshold a WHERE a.updated = :updated"),
//@NamedQuery(name = "Threshold.findByUpdatedBy", query = "SELECT a FROM Threshold a WHERE a.updatedBy = :updatedBy")
})
public class Threshold extends MyModel {

    @Column(name = "lowerLimit", nullable = false)
    private int lowerLimit = Integer.MAX_VALUE;
    @Column(name = "upperLimit", nullable = false)
    private int upperLimit = Integer.MIN_VALUE;

    public Threshold() {
    }

    public Threshold(int low, int high, boolean isActive, Date created, Date updated, String updatedBy) {
        this.lowerLimit = low;
        this.upperLimit = high;
        this.isActive = isActive;
        this.created = created;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Threshold == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Threshold rhs = (Threshold) obj;
        return new EqualsBuilder().append(lowerLimit, rhs.lowerLimit).append(upperLimit, rhs.upperLimit).isEquals();
    }

    public int compareTo(Object o) {
        Threshold rhs = (Threshold) o;
        return new CompareToBuilder().append(lowerLimit, rhs.lowerLimit).append(upperLimit, rhs.upperLimit).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(lowerLimit).append(upperLimit).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("isActive", isActive).append("lowerLimit", lowerLimit).append("upperLimit", upperLimit).toString();
    }

    // hmmm these are inconsistent re: inclusion of limit values
    public boolean isInsideLimits(int value) {
        return value > lowerLimit && value < upperLimit;
    }

    // this is as it was before
    public boolean isOutsideLimits(int value) {
        return value < lowerLimit || value > upperLimit;
    }
}
