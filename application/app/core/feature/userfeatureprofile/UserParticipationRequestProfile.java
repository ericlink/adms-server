package core.feature.userfeatureprofile;

import core.feature.ParticipationRequestProfile;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "userParticipationRequestProfile")
@NamedQueries({
    @NamedQuery(name = "UserParticipationRequestProfile.findAllActive", query = "select x from UserParticipationRequestProfile x where x.isActive = true and x.featureProfile.intensiveManagementProtocol.isActive = true and x.featureProfile.isSchedulable = true ")
})
public class UserParticipationRequestProfile extends UserFeatureProfile {

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
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "userFrequencyAssessmentProfileId", nullable = false)
    private UserFrequencyAssessmentProfile userFrequencyAssessmentProfile;

    public UserParticipationRequestProfile() {
    }

    private ParticipationRequestProfile getParticipationRequestProfile() {
        return (ParticipationRequestProfile) getFeatureProfile();
    }

    public BigDecimal getIsFrequentThreshold() {
        return getParticipationRequestProfile().getIsFrequentThreshold();
    }

    public int getDaysToCheck() {
        return getParticipationRequestProfile().getDaysToCheck();
    }

    public UserFrequencyAssessmentProfile getUserFrequencyAssessmentProfile() {
        return userFrequencyAssessmentProfile;
    }

    public void setUserFrequencyAssessmentProfile(UserFrequencyAssessmentProfile userFrequencyAssessmentProfile) {
        this.userFrequencyAssessmentProfile = userFrequencyAssessmentProfile;
    }

    public int getNumberOfRangesToCheck() {
        // type1 = 30 - 2;
        // type2 = 30 - 7;
        // check 28 ranges for type 1
        // check 21 ranges for type 2
        int daysToCheck = getParticipationRequestProfile().getDaysToCheck();
        int protocolDataPeriod = getUserFrequencyAssessmentProfile().getDaysToCheck();
        return daysToCheck - protocolDataPeriod;
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
}
