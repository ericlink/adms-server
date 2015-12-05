package core.feature.userfeatureprofile;

import core.feature.FrequencyAssessmentProfile;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import core.quicktip.QuickTipType;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "userFrequencyAssessmentProfile")
@NamedQueries({
    @NamedQuery(name = "UserFrequencyAssessmentProfile.findAllActive", query = "select x from UserFrequencyAssessmentProfile x where x.isActive = true and x.featureProfile.intensiveManagementProtocol.isActive = true and x.featureProfile.isSchedulable = true ")
})
public class UserFrequencyAssessmentProfile extends UserFeatureProfile {

    @Column(name = "lastInfrequentOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastInfrequentOn;
    @Column(name = "lastFrequentOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastFrequentOn;
    @Column(name = "totalInFrequent", nullable = false)
    private int totalInFrequent;
    @Column(name = "totalFrequent", nullable = false)
    private int totalFrequent;
    @Column(name = "lastFrequentQuickTipType")
    private QuickTipType lastFrequentQuickTipType;
    @Column(name = "lastQuickTipDietTypeOrdinal")
    private int lastQuickTipDietTypeOrdinal;
    @Column(name = "lastQuickTipActivityTypeOrdinal")
    private int lastQuickTipActivityTypeOrdinal;
    @Column(name = "lastQuickTipCheckTypeOrdinal")
    private int lastQuickTipCheckTypeOrdinal;

    public UserFrequencyAssessmentProfile() {
    }

    private FrequencyAssessmentProfile getFrequencyAssessmentProfile() {
        return (FrequencyAssessmentProfile) getFeatureProfile();
    }

    public int getMinimumDataPointsPerRequiredDay() {
        return getFrequencyAssessmentProfile().getMinimumDataPointsPerRequiredDay();
    }

    public int getRequiredDays() {
        return getFrequencyAssessmentProfile().getRequiredDays();
    }

    public int getDaysToCheck() {
        return getFrequencyAssessmentProfile().getDaysToCheck();
    }

    public Date getLastInfrequentOn() {
        return lastInfrequentOn;
    }

    public void setLastInfrequentOn(Date lastInfrequentOn) {
        this.lastInfrequentOn = lastInfrequentOn;
    }

    public Date getLastFrequentOn() {
        return lastFrequentOn;
    }

    public void setLastFrequentOn(Date lastFrequentOn) {
        this.lastFrequentOn = lastFrequentOn;
    }

    public int getTotalInFrequent() {
        return totalInFrequent;
    }

    public void setTotalInFrequent(int totalInFrequent) {
        this.totalInFrequent = totalInFrequent;
    }

    public int getTotalFrequent() {
        return totalFrequent;
    }

    public void setTotalFrequent(int totalFrequent) {
        this.totalFrequent = totalFrequent;
    }

    public QuickTipType getLastFrequentQuickTipType() {
        return lastFrequentQuickTipType;
    }

    public void setLastFrequentQuickTipType(QuickTipType lastFrequentQuickTipType) {
        this.lastFrequentQuickTipType = lastFrequentQuickTipType;
    }

    public int getLastQuickTipDietTypeOrdinal() {
        return lastQuickTipDietTypeOrdinal;
    }

    public void setLastQuickTipDietTypeOrdinal(int lastQuickTipDietTypeOrdinal) {
        this.lastQuickTipDietTypeOrdinal = lastQuickTipDietTypeOrdinal;
    }

    public int getLastQuickTipActivityTypeOrdinal() {
        return lastQuickTipActivityTypeOrdinal;
    }

    public void setLastQuickTipActivityTypeOrdinal(int lastQuickTipActivityTypeOrdinal) {
        this.lastQuickTipActivityTypeOrdinal = lastQuickTipActivityTypeOrdinal;
    }

    public int getLastQuickTipCheckTypeOrdinal() {
        return lastQuickTipCheckTypeOrdinal;
    }

    public void setLastQuickTipCheckTypeOrdinal(int lastQuickTipCheckTypeOrdinal) {
        this.lastQuickTipCheckTypeOrdinal = lastQuickTipCheckTypeOrdinal;
    }
}
