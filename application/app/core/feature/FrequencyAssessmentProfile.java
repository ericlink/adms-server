package core.feature;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "frequencyAssessmentProfile")
@NamedQueries({
    @NamedQuery(name = "FrequencyAssessmentProfile.findByName", query = "SELECT fp FROM FrequencyAssessmentProfile fp WHERE fp.isActive = true and fp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class FrequencyAssessmentProfile extends FeatureProfile {

    @Column(name = "minimumDataPointsPerRequiredDay", nullable = false)
    private int minimumDataPointsPerRequiredDay;
    @Column(name = "requiredDays", nullable = false)
    private int requiredDays;
    @Column(name = "daysToCheck", nullable = false)
    private int daysToCheck;

    public FrequencyAssessmentProfile() {
    }

    public int getMinimumDataPointsPerRequiredDay() {
        return minimumDataPointsPerRequiredDay;
    }

    public void setMinimumDataPointsPerRequiredDay(int minimumDataPointsPerRequiredDay) {
        this.minimumDataPointsPerRequiredDay = minimumDataPointsPerRequiredDay;
    }

    public int getRequiredDays() {
        return requiredDays;
    }

    public void setRequiredDays(int requiredDays) {
        this.requiredDays = requiredDays;
    }

    public int getDaysToCheck() {
        return daysToCheck;
    }

    public void setDaysToCheck(int daysToCheck) {
        this.daysToCheck = daysToCheck;
    }
}
