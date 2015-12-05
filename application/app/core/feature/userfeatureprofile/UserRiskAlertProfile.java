package core.feature.userfeatureprofile;

import core.feature.RiskAlertProfile;
import core.type.ComparisonType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.User;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "userRiskAlertProfile")
public class UserRiskAlertProfile extends UserFeatureProfile {

    @Column(name = "lastAlertDetectedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAlertDetectedOn;

    public UserRiskAlertProfile() {
    }

    private RiskAlertProfile getRiskAlertProfile() {
        return (RiskAlertProfile) getFeatureProfile();
    }

    public BigDecimal getResult() {
        return getRiskAlertProfile().
            getResult();
    }

    public ComparisonType getComparisonType() {
        return getRiskAlertProfile().
            getComparisonType();
    }

    public BigDecimal getComparisonValue() {
        return getRiskAlertProfile().
            getComparisonValue();
    }

    public int getHours() {
        return getRiskAlertProfile().
            getHours();
    }

    /**
     * Use last Alert as last performed for isTimeToRun() tests
     **/
    public Date getLastPerformedOn() {
        return getLastFired();
    }

    public Date getLastAlertDetectedOn() {
        return lastAlertDetectedOn;
    }

    public void setLastAlertDetectedOn(Date lastAlertDetectedOn) {
        this.lastAlertDetectedOn = lastAlertDetectedOn;
    }

    public static Collection<UserRiskAlertProfile> findBy(User user) {
        String query =
            "  select x "
            + "from UserRiskAlertProfile x "
            + "where x.isActive = true "
            + "and x.featureProfile.intensiveManagementProtocol.isActive = true "
            + "and x.featureProfile.isOnNewDataPointEvent = true "
            + "and x.user = :user ";

        return find(query).
            bind("user", user).
            fetch();

    }
}
