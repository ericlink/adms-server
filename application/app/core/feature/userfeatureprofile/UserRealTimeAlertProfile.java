package core.feature.userfeatureprofile;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Table;
import models.User;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "userRealTimeAlertProfile")
public class UserRealTimeAlertProfile extends UserFeatureProfile {

    public static Collection<UserRealTimeAlertProfile> findBy(User user) {
        String query =
            "  select x "
            + "from UserRealTimeAlertProfile x "
            + "where x.isActive = true "
            + "and x.featureProfile.intensiveManagementProtocol.isActive = true "
            + "and x.featureProfile.isOnNewDataPointEvent = true "
            + "and x.user = :user ";


        return find(query).
            bind("user", user).
            fetch();

    }

    public UserRealTimeAlertProfile() {
    }
}
