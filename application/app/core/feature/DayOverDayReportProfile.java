package core.feature;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "dayOverDayReportProfile")
@NamedQueries({
    @NamedQuery(name = "DayOverDayReportProfile.findByName", query = "SELECT fp FROM DayOverDayReportProfile fp WHERE fp.isActive = true and fp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class DayOverDayReportProfile extends FeatureProfile {

    public DayOverDayReportProfile() {
    }
}
