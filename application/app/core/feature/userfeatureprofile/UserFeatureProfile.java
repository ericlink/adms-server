package core.feature.userfeatureprofile;

import core.feature.FeatureProfile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import helper.util.CalendarRange;
import helper.util.DateHelper;
import models.MyModel;
import models.Schedule;
import models.User;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "userFeatureProfile")
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class UserFeatureProfile extends MyModel {

    private static Logger logger = Logger.getLogger(UserFeatureProfile.class.getName());
    @JoinColumn(name = "featureProfileId", nullable = false)
    @ManyToOne
    private FeatureProfile featureProfile;
    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne
    private User user;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userFeatureProfile", fetch = FetchType.EAGER)
    private java.util.Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = new ArrayList<UserFeatureProfileDestinationProfile>();
    @Column(name = "lastPerformedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPerformedOn;
    @Column(name = "lastCheckToPerformOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCheckToPerformOn;
    @Column(name = "minimumIntervalDays", nullable = false)
    /**
     * -1 for default to Feature Profile,
     * 0-365 for setting here
     **/
    private int minimumIntervalDays;
    @Column(name = "lastFired")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastFired;
    @Column(name = "totalFired", nullable = false)
    private int totalFired;

    public UserFeatureProfile() {
    }

    public FeatureProfile getFeatureProfile() {
        return featureProfile;
    }

    public void setFeatureProfile(FeatureProfile featureProfile) {
        this.featureProfile = featureProfile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addUserFeatureProfileDestinationSchedule(UserFeatureProfileDestinationProfile userFeatureProfileDestinationSchedule) {
        userFeatureProfileDestinationSchedule.setFeatureProfile(this);
        this.userFeatureProfileDestinationProfiles.add(userFeatureProfileDestinationSchedule);
    }

    public void removeUserFeatureProfileDestinationSchedule(UserFeatureProfileDestinationProfile userFeatureProfileDestinationSchedule) {
        userFeatureProfileDestinationSchedule.setFeatureProfile(null);
        this.userFeatureProfileDestinationProfiles.remove(userFeatureProfileDestinationSchedule);
    }

    public Schedule getSchedule() {
        return schedule != null ? schedule : getFeatureProfile().getDefaultSchedule();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean equals(Object obj) {
        if (obj instanceof UserFeatureProfile == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        UserFeatureProfile rhs = (UserFeatureProfile) obj;
        return new EqualsBuilder().append(featureProfile, rhs.featureProfile).append(user, rhs.user).append(schedule, rhs.schedule).isEquals();
    }

    public int compareTo(Object o) {
        UserFeatureProfile rhs = (UserFeatureProfile) o;
        return new CompareToBuilder().append(featureProfile, rhs.featureProfile).append(user, rhs.user).append(schedule, rhs.schedule).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(featureProfile).append(user).append(schedule).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).append("featureProfile", featureProfile).append("user", user).append("schedule", schedule).toString();
    }

    public Date getLastPerformedOn() {
        return lastPerformedOn;
    }

    public void setLastPerformedOn(Date lastPerformedOn) {
        this.lastPerformedOn = lastPerformedOn;
    }

    /**
     * Minimum amount of time that must pass between each perfomance time,
     * can be set in user feature profile or defaults to feature profile
     **/
    public int getMinimumIntervalDays() {
        return minimumIntervalDays < 0 ? getFeatureProfile().getMinimumIntervalDays() : minimumIntervalDays;
    }

    public void setMinimumIntervalDays(int minimumIntervalDays) {
        this.minimumIntervalDays = minimumIntervalDays;
    }

    public java.util.Collection<UserFeatureProfileDestinationProfile> getUserFeatureProfileDestinationProfiles() {
        return userFeatureProfileDestinationProfiles;
    }

    public void setUserFeatureProfileDestinationProfiles(java.util.Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles) {
        this.userFeatureProfileDestinationProfiles = userFeatureProfileDestinationProfiles;
    }

    /**
     * Metrics updated:
     *      setLastCheckToPerformOn - set to 'nowUtc' on entry
     *      setLastPerformedOn      - set to 'nowUtc' if returning true
     *
     * Rules:
     *    1) check the minimum interval, e.g. 1x per day
     *    2) check the schedule
     *         - uses schedule set on the userFeatureProfile, defaulting to FeatureProfile, defaulting to WideOpen
     *         - consider the last run time when evaluating the schedule, so it is not run twice for the same scheduled time
     *    3) all Calendars for evaluating the schedules use the users time zone
     *
     * @return true if it is time to run the feature
     **/
    public boolean isTimeToRun() {
        setLastCheckToPerformOn(new Date());
        logger.log(Level.FINE, "User time zone is {0}", getUser().getTimeZone());
        Calendar userLocalTime = Calendar.getInstance(getUser().getTimeZone());
        Calendar userLocalTimeLastPerformedOn = null;
        CalendarRange intervalRange = null;
        if (getLastPerformedOn() != null) {
            userLocalTimeLastPerformedOn = Calendar.getInstance(getUser().getTimeZone());
            userLocalTimeLastPerformedOn.setTime(getLastPerformedOn());
            intervalRange = new CalendarRange(userLocalTimeLastPerformedOn, userLocalTime);
        }

        logger.log(Level.FINE, "userLocalTimeLastPerformedOn={0},userLocalTime={1}", new Object[]{
                userLocalTimeLastPerformedOn,
                userLocalTime
            });

        boolean isMinimumIntervalMet = getMinimumIntervalDays() == 0
            || userLocalTimeLastPerformedOn == null
            || DateHelper.calculateIntervalInDays(intervalRange) >= getMinimumIntervalDays();

        boolean isTimeToRun = false;
        if (isMinimumIntervalMet) {
            isTimeToRun = getSchedule().isRunnable(userLocalTimeLastPerformedOn, userLocalTime);
            if (isTimeToRun) {
                setLastPerformedOn(new Date());
            }
        }

        logger.log(Level.FINE, "isMinimumIntervalMet={0},isTimeToRun={1}", new Object[]{
                isMinimumIntervalMet,
                isTimeToRun
            });

        return isTimeToRun;
    }

    public Date getLastCheckToPerformOn() {
        return lastCheckToPerformOn;
    }

    public void setLastCheckToPerformOn(Date lastCheckToPerformOn) {
        this.lastCheckToPerformOn = lastCheckToPerformOn;
    }

    public Date getLastFired() {
        return lastFired;
    }

    public void setLastFired(Date lastFired) {
        this.lastFired = lastFired;
        totalFired++;
    }
}
