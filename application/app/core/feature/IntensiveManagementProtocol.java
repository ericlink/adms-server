package core.feature;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import models.MyModel;
import models.User;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "intensiveManagementProtocol")
@NamedQueries({
    @NamedQuery(name = "IntensiveManagementProtocol.findByName", query = "SELECT imp FROM IntensiveManagementProtocol imp WHERE imp.isActive = true and imp.name = :name")
})
/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class IntensiveManagementProtocol extends MyModel {

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "intensiveManagementProtocol")
    private java.util.Collection<FeatureProfile> featureProfiles = new ArrayList<FeatureProfile>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<User>();

    public IntensiveManagementProtocol() {
    }

    public boolean equals(Object obj) {
        if (obj instanceof IntensiveManagementProtocol == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IntensiveManagementProtocol rhs = (IntensiveManagementProtocol) obj;
        return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
    }

    public int compareTo(Object o) {
        IntensiveManagementProtocol rhs = (IntensiveManagementProtocol) o;
        return new CompareToBuilder().append(getName(), rhs.getName()).toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", getName()).toString();
    }

    public java.util.Collection<FeatureProfile> getFeatureProfiles() {
        return featureProfiles;
    }

    public void setFeatureProfiles(java.util.Collection<FeatureProfile> featureProfiles) {
        this.featureProfiles = featureProfiles;
    }

    public void addFeatureProfile(FeatureProfile featureProfile) {
        featureProfile.setIntensiveManagementProtocol(this);
        this.featureProfiles.add(featureProfile);
    }

    public void removeFeatureProfile(FeatureProfile featureProfile) {
        featureProfile.setIntensiveManagementProtocol(null);
        this.featureProfiles.remove(featureProfile);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        user.addIntensiveManagementProtocol(this);
        this.users.add(user);
    }

    public void removeUser(User user) {
        user.removeIntensiveManagementProtocol(this);
        this.users.remove(user);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
