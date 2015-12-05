package models;

import core.medicaldevice.MedicalDevice;
import core.datapoint.DataPoint;
import core.feature.userfeatureprofile.UserFeatureProfile;
import core.feature.IntensiveManagementProtocol;
import core.messaging.Destination;
import core.module.Module;
import core.type.DiagnosisType;
import core.type.UnitOfMeasureType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import play.Play;

/**
 * 
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findByExternalEmrId", query = "SELECT u FROM User u WHERE u.externalEmrId = :externalEmrId"),
    @NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByHandle", query = "SELECT u FROM User u WHERE u.handle = :handle"),
    @NamedQuery(name = "User.findByTimeZone", query = "SELECT u FROM User u WHERE u.timeZone = :timeZone"),
    @NamedQuery(name = "User.findByCallBackNumber", query = "SELECT u FROM User u WHERE u.callBackNumber = :callBackNumber"),
    @NamedQuery(name = "User.findByEmergencyNumber", query = "SELECT u FROM User u WHERE u.emergencyNumber = :emergencyNumber"),
    @NamedQuery(name = "User.findByFaxNumber", query = "SELECT u FROM User u WHERE u.faxNumber = :faxNumber"),
    @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive"),
    @NamedQuery(name = "User.findByCreated", query = "SELECT u FROM User u WHERE u.created = :created"),
    @NamedQuery(name = "User.findByUpdated", query = "SELECT u FROM User u WHERE u.updated = :updated")
//@NamedQuery(name = "User.findByUpdatedBy", query = "SELECT u FROM User u WHERE u.updatedBy = :updatedBy")
})
public class User extends MyModel {

    @Column(name = "externalEmrId")
    private String externalEmrId;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "handle", nullable = false)
    private String handle;
    @Column(name = "timeZone", nullable = false)
    private String timeZone = "UTC";
    @Column(name = "callBackNumber")
    private String callBackNumber;
    @Column(name = "emergencyNumber")
    private String emergencyNumber;
    @Column(name = "faxNumber")
    private String faxNumber;
    private int targetBg;
    private int lowBg;
    private int highBg;
    private String lowMessage;
    private String highMessage;
    private String onTargetMessage;
    private String aboveTargetMessage;
    private String belowTargetMessage;
    @Column(name = "type", nullable = false)
    private UserType type;
    // ICD codes (9/10) in future, or link to an open emr system with those
    @Column(name = "diagnosisType", nullable = false)
    private DiagnosisType diagnosisType = DiagnosisType.UNKNOWN;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private java.util.Collection<Destination> destinations = new ArrayList<Destination>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private java.util.Collection<MedicalDevice> medicalDevices = new ArrayList<MedicalDevice>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private java.util.Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private java.util.Collection<Module> modules = new ArrayList<Module>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "users")
    private java.util.Set<IntensiveManagementProtocol> intensiveManagementProtocols = new HashSet<IntensiveManagementProtocol>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private java.util.Collection<UserFeatureProfile> userFeatureProfiles = new ArrayList<UserFeatureProfile>();

    /** Creates a new instance of User */
    public User() {
    }

    public User(String handle, String timeZone, boolean isActive, Date created, Date updated, String updatedBy) {
        this.handle = handle;
        this.timeZone = timeZone;
        this.isActive = isActive;
        this.created = created;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public String getExternalEmrId() {
        return this.externalEmrId;
    }

    public void setExternalEmrId(String externalEmrId) {
        this.externalEmrId = externalEmrId;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(this.timeZone);
    }

    public void setTimeZone(TimeZone tz) {
        this.timeZone = tz.getID();
    }

    public String getCallBackNumber() {
        return this.callBackNumber;
    }

    public void setCallBackNumber(String callBackNumber) {
        this.callBackNumber = callBackNumber;
    }

    public String getEmergencyNumber() {
        return this.emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public java.util.Collection<Destination> getDestinations() {
        return this.destinations;
    }

    public void setDestinations(java.util.Collection<Destination> alertDestination) {
        this.destinations = destinations;
        for (Destination ad : this.destinations) {
            ad.setUser(this);
        }
    }

    public void addDestination(Destination alertDestination) {
        alertDestination.setUser(this);
        this.destinations.add(alertDestination);
    }

    public void removeDestination(Destination alertDestination) {
        alertDestination.setUser(null);
        this.destinations.remove(alertDestination);
    }

    public java.util.Collection<MedicalDevice> getMedicalDevices() {
        return this.medicalDevices;
    }

    public void setMedicalDevices(java.util.Collection<MedicalDevice> medicalDevices) {
        this.medicalDevices = medicalDevices;
        for (MedicalDevice md : this.medicalDevices) {
            md.setUser(this);
        }
    }

    public void addMedicalDevice(MedicalDevice medicalDevice) {
        medicalDevice.setUser(this);
        for (DataPoint dp : medicalDevice.getDataPoints()) {
            if (dp.getUser() == null) {
                dp.setUser(this);
            }
        }
//        //todo ***
//        // An attempt was made to traverse a relationship using indirection that had a null Session.  This often occurs when an entity with an uninstantiated LAZY relationship is serialized and that lazy relationship is traversed after serialization.  To avoid this issue, instantiate the LAZY relationship prior to serialization.
//        this.medicalDevices.add(medicalDevice);
    }

    public void removeMedicalDevice(MedicalDevice medicalDevice) {
        medicalDevice.setUser(null);
        this.medicalDevices.remove(medicalDevice);
    }

    public java.util.Collection<DataPoint> getDataPoints() {
        return this.dataPoints;
    }

    public void setDataPoints(java.util.Collection<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        for (DataPoint d : this.dataPoints) {
            d.setUser(this);
        }
    }

    public void addDataPoint(DataPoint d) {
        d.setUser(this);
        this.dataPoints.add(d);
    }

    public void removeDataPoint(DataPoint d) {
        d.setUser(null);
        this.dataPoints.remove(d);
    }

    public java.util.Collection<Module> getModules() {
        return this.modules;
    }

    public void setModules(java.util.Collection<Module> modules) {
        this.modules = modules;
        for (Module m : this.modules) {
            m.setUser(this);
        }
    }

    public void addModule(Module m) {
        m.setUser(this);
        this.modules.add(m);
    }

    public void removeModule(Module m) {
        m.setUser(null);
        this.modules.remove(m);
    }

    public boolean equals(Object obj) {
        if (obj instanceof User == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        User rhs = (User) obj;
        return new EqualsBuilder().append(login, rhs.login) //really this is the key; email address;login some don't have email address, so use login
            .append(handle, rhs.handle).isEquals();
    }

    public int compareTo(Object o) {
        User rhs = (User) o;
        return new CompareToBuilder().append(login, rhs.login) //really this is the key; email address
            .append(handle, rhs.handle).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(login).append(handle).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("isActive", isActive).append("login", login).append("handle", handle).append("timeZoneId", timeZone).toString();
    }

    public java.util.Set<IntensiveManagementProtocol> getIntensiveManagementProtocols() {
        return intensiveManagementProtocols;
    }

    public void setIntensiveManagementProtocols(java.util.Set<IntensiveManagementProtocol> intensiveManagementProtocols) {
        this.intensiveManagementProtocols = intensiveManagementProtocols;
    }

    public void addIntensiveManagementProtocol(IntensiveManagementProtocol intensiveManagementProtocol) {
        intensiveManagementProtocol.addUser(this);
        this.getIntensiveManagementProtocols().add(intensiveManagementProtocol);
    }

    public void removeIntensiveManagementProtocol(IntensiveManagementProtocol intensiveManagementProtocol) {
        intensiveManagementProtocol.removeUser(this);
        this.getIntensiveManagementProtocols().remove(intensiveManagementProtocol);
    }

    public java.util.Collection<UserFeatureProfile> getUserFeatureProfiles() {
        return userFeatureProfiles;
    }

    public void setUserFeatureProfiles(java.util.Collection<UserFeatureProfile> userFeatureProfiles) {
        this.userFeatureProfiles = userFeatureProfiles;
    }

    public void addUserFeatureProfile(UserFeatureProfile userFeatureProfile) {
        userFeatureProfile.setUser(this);
        this.userFeatureProfiles.add(userFeatureProfile);
    }

    public void removeUserFeatureProfile(UserFeatureProfile userFeatureProfile) {
        userFeatureProfile.setUser(null);
        this.userFeatureProfiles.remove(userFeatureProfile);
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public DiagnosisType getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(DiagnosisType diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    /**
     * @return the targetBg
     */
    public int getTargetBg() {
        return targetBg;
    }

    /**
     * @param targetBg the targetBg to set
     */
    public void setTargetBg(int targetBg) {
        this.targetBg = targetBg;
    }

    /**
     * @return the lowBg
     */
    public int getLowBg() {
        return lowBg;
    }

    /**
     * @param lowBg the lowBg to set
     */
    public void setLowBg(int lowBg) {
        this.lowBg = lowBg;
    }

    /**
     * @return the highBg
     */
    public int getHighBg() {
        return highBg;
    }

    /**
     * @param highBg the highBg to set
     */
    public void setHighBg(int highBg) {
        this.highBg = highBg;
    }

    /**
     * @return the lowMessage
     */
    public String getLowMessage() {
        return lowMessage;
    }

    /**
     * @param lowMessage the lowMessage to set
     */
    public void setLowMessage(String lowMessage) {
        this.lowMessage = lowMessage;
    }

    /**
     * @return the highMessage
     */
    public String getHighMessage() {
        return highMessage;
    }

    /**
     * @param highMessage the highMessage to set
     */
    public void setHighMessage(String highMessage) {
        this.highMessage = highMessage;
    }

    /**
     * @return the onTargetMessage
     */
    public String getOnTargetMessage() {
        return onTargetMessage;
    }

    /**
     * @param onTargetMessage the onTargetMessage to set
     */
    public void setOnTargetMessage(String onTargetMessage) {
        this.onTargetMessage = onTargetMessage;
    }

    /**
     * @return the aboveTargetMessage
     */
    public String getAboveTargetMessage() {
        return aboveTargetMessage;
    }

    /**
     * @param aboveTargetMessage the aboveTargetMessage to set
     */
    public void setAboveTargetMessage(String aboveTargetMessage) {
        this.aboveTargetMessage = aboveTargetMessage;
    }

    /**
     * @return the belowTargetMessage
     */
    public String getBelowTargetMessage() {
        return belowTargetMessage;
    }

    /**
     * @param belowTargetMessage the belowTargetMessage to set
     */
    public void setBelowTargetMessage(String belowTargetMessage) {
        this.belowTargetMessage = belowTargetMessage;
    }

    public UnitOfMeasureType getPreferredUnitOfMeasure() {
        return UnitOfMeasureType.valueOf(Play.configuration.getProperty("uom"));
    }
}
