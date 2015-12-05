package core.feature;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "participationRequestProfile")
@NamedQueries({
    @NamedQuery(name = "ParticipationRequestProfile.findByName", query = "SELECT fp FROM ParticipationRequestProfile fp WHERE fp.isActive = true and fp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class ParticipationRequestProfile extends FeatureProfile {

    @Column(name = "isFrequentThreshold", nullable = false)
    private BigDecimal isFrequentThreshold;
    @Column(name = "daysToCheck", nullable = false)
    private int daysToCheck;

    public ParticipationRequestProfile() {
    }

    public BigDecimal getIsFrequentThreshold() {
        return isFrequentThreshold;
    }

    public void setIsFrequentThreshold(BigDecimal isFrequentThreshold) {
        this.isFrequentThreshold = isFrequentThreshold;
    }

    public int getDaysToCheck() {
        return daysToCheck;
    }

    public void setDaysToCheck(int daysToCheck) {
        this.daysToCheck = daysToCheck;
    }
}
