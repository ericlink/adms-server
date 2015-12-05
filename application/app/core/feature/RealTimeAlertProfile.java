package core.feature;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "realTimeAlertProfile")
@NamedQueries({
    @NamedQuery(name = "RealTimeAlertProfile.findByName", query = "SELECT fp FROM RealTimeAlertProfile fp WHERE fp.isActive = true and fp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class RealTimeAlertProfile extends FeatureProfile {

    public RealTimeAlertProfile() {
    }
}
