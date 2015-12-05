package core.datapoint;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "dataMark")
@NamedQueries({
    @NamedQuery(name = "DataMark.findByTypeCode", query = "SELECT d FROM DataMark d WHERE d.typeCode = :typeCode"),
    @NamedQuery(name = "DataMark.findByValue", query = "SELECT d FROM DataMark d WHERE d.value = :value"),
    @NamedQuery(name = "DataMark.findByIsActive", query = "SELECT d FROM DataMark d WHERE d.isActive = :isActive"),
    @NamedQuery(name = "DataMark.findByCreated", query = "SELECT d FROM DataMark d WHERE d.created = :created"),
    @NamedQuery(name = "DataMark.findByUpdated", query = "SELECT d FROM DataMark d WHERE d.updated = :updated"),
    @NamedQuery(name = "DataMark.findByUpdatedBy", query = "SELECT d FROM DataMark d WHERE d.updatedBy = :updatedBy")
})
public class DataMark extends MyModel {

    @Column(name = "typeCode", nullable = false)
    private DataMarkType typeCode;
    @Column(name = "value", nullable = false)
    private int value;
    @JoinColumn(name = "dataPointId", nullable = false)
    @ManyToOne
    private DataPoint dataPoint;

    /** Creates a new instance of DataMark */
    public DataMark() {
    }

    public DataMark(DataMarkType typeCode, int value, boolean isActive, Date created, Date updated, String updatedBy) {
        this.typeCode = typeCode;
        this.value = value;
        this.isActive = isActive;
        this.created = created;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public DataMarkType getType() {
        return this.typeCode;
    }

    public void setTypeCode(DataMarkType typeCode) {
        this.typeCode = typeCode;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DataPoint getDataPoint() {
        return this.dataPoint;
    }

    public void setDataPoint(DataPoint dataPoint) {
        this.dataPoint = dataPoint;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DataMark == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        DataMark rhs = (DataMark) obj;
        return new EqualsBuilder().append(typeCode, rhs.typeCode).append(value, rhs.value).append(dataPoint, rhs.dataPoint).isEquals();
    }

    public int compareTo(Object o) {
        DataMark rhs = (DataMark) o;
        return new CompareToBuilder().append(typeCode, rhs.typeCode).append(value, rhs.value).append(dataPoint, rhs.dataPoint).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(typeCode).append(value).append(dataPoint).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("isActive", isActive).append("typeCode", typeCode).append("value", value).append("dataPoint", dataPoint).toString();
    }
}
