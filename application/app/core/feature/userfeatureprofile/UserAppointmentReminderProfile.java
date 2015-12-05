package core.feature.userfeatureprofile;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "userAppointmentReminderProfile")
@NamedQueries({
    @NamedQuery(name = "UserAppointmentReminderProfile.findAllActive", query = "select x from UserAppointmentReminderProfile x where x.isActive = true and x.featureProfile.intensiveManagementProtocol.isActive = true and x.featureProfile.isSchedulable = true"),
    @NamedQuery(name = "UserAppointmentReminderProfile.findPending", query = "select x from UserAppointmentReminderProfile x where x.isActive = true and x.featureProfile.intensiveManagementProtocol.isActive = true and x.featureProfile.isSchedulable = true and x.appointmentTime < :latestAppointmentTime")
})
public class UserAppointmentReminderProfile extends UserFeatureProfile {

    @Column(name = "appointmentTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentTime;

    public UserAppointmentReminderProfile() {
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
