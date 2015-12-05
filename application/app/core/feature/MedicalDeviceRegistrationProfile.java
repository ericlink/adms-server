package core.feature;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "medicalDeviceRegistrationProfile")
@NamedQueries({
    @NamedQuery(name = "MedicalDeviceRegistrationProfile.findByName", query = "SELECT fp FROM MedicalDeviceRegistrationProfile fp WHERE fp.isActive = true and fp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class MedicalDeviceRegistrationProfile extends FeatureProfile {

    public MedicalDeviceRegistrationProfile() {
    }
}
