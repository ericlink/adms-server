package core.feature;

import core.type.ComparisonType;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "riskAlertProfile")
@NamedQueries({
    @NamedQuery(name = "RiskAlertProfile.findByName", query = "SELECT fp FROM FeatureProfile fp WHERE fp.isActive = true and fp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class RiskAlertProfile extends FeatureProfile {

    @Column(name = "result", nullable = false)
    private BigDecimal result;
    @Column(name = "comparisonType", nullable = false)
    private ComparisonType comparisonType;
    @Column(name = "comparisonValue", nullable = false)
    private BigDecimal comparisonValue;
    @Column(name = "hours", nullable = false)
    private int hours;

    public RiskAlertProfile() {
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public BigDecimal getComparisonValue() {
        return comparisonValue;
    }

    public void setComparisonValue(BigDecimal comparisonValue) {
        this.comparisonValue = comparisonValue;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
