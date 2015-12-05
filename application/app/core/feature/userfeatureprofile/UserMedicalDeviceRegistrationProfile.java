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
@Table(name = "userMedicalDeviceRegistrationProfile")
public class UserMedicalDeviceRegistrationProfile extends UserFeatureProfile {

    public static Collection<UserMedicalDeviceRegistrationProfile> findBy(User user) {
        String query =
            "  select x "
            + "from UserMedicalDeviceRegistrationProfile x "
            + "where x.isActive = true "
            + "and x.featureProfile.intensiveManagementProtocol.isActive = true "
            + "and x.featureProfile.isOnNewMedicalDeviceEvent = true "
            + "and x.user = :user ";

        return find(query).
            bind("user", user).
            fetch();

    }

    public UserMedicalDeviceRegistrationProfile() {
    }
}
