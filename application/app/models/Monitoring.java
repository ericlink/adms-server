package models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "monitoring")
/**
 * Confidential Information.
 * Copyright (C) 2007 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class Monitoring extends MyModel {

    public Monitoring() {
    }
    @Column(name = "lastPing")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPing;

    public boolean equals(Object obj) {
        if (obj instanceof Monitoring == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Monitoring rhs = (Monitoring) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    public int compareTo(Object o) {
        Monitoring rhs = (Monitoring) o;
        return new CompareToBuilder().append(id, rhs.id).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("created", created).append("updated", updated).append("updatedBy", updatedBy).toString();
    }

    public Date getLastPing() {
        return lastPing;
    }

    public void setLastPing(Date lastPing) {
        this.lastPing = lastPing;
    }
}
