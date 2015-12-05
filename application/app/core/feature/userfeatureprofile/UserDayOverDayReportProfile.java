package core.feature.userfeatureprofile;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "userDayOverDayReportProfile")
public class UserDayOverDayReportProfile extends UserFeatureProfile {

    public UserDayOverDayReportProfile() {
    }

    public static List<UserDayOverDayReportProfile> findActive() {
        return find(
            "  select x "
            + "from   UserDayOverDayReportProfile x "
            + "where x.isActive = true "
            + "  and x.featureProfile.intensiveManagementProtocol.isActive = true "
            + "  and x.featureProfile.isSchedulable = true").fetch();

    }
}
