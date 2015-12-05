package core.quicktip;

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

@Entity
@Table(name = "quickTip")
@NamedQueries({
    @NamedQuery(name = "QuickTip.findByTypeAndOrdinal", query = "SELECT q FROM QuickTip q WHERE q.type = :type and q.isActive = true and q.ordinal = :ordinal")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class QuickTip extends MyModel {

    @Column(name = "referenceNumber", nullable = false, unique = true)
    private String referenceNumber;
    @Column(name = "type", nullable = false)
    private QuickTipType type;
    @Column(name = "tip", nullable = false)
    private String tip;
    @Column(name = "ordinal", nullable = false)
    private int ordinal;
    @Column(name = "source", nullable = false)
    private String source;

    public QuickTip() {
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public QuickTipType getType() {
        return type;
    }

    public void setType(QuickTipType type) {
        this.type = type;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean equals(Object obj) {
        if (obj instanceof QuickTip == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        QuickTip rhs = (QuickTip) obj;
        return new EqualsBuilder().append(referenceNumber, rhs.referenceNumber).isEquals();
    }

    public int compareTo(Object o) {
        QuickTip rhs = (QuickTip) o;
        return new CompareToBuilder().append(referenceNumber, rhs.referenceNumber).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(referenceNumber).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("referenceNumber", referenceNumber).append("type", type).append("tip", tip).append("ordinal", ordinal).append("source", source).append("isActive", isActive).toString();
    }
}
