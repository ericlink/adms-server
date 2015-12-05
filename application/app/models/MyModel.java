/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

/**
 *
 * @author elink
 */
@MappedSuperclass
public class MyModel extends Model {

    @Column(name = "isActive", nullable = false, columnDefinition = "int")
    protected boolean isActive = true;
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date created = new Date();//not all called by saved/e.g. children
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updated = new Date();//not all called by saved/e.g. children
    @Column(name = "updatedBy", nullable = false)
    protected String updatedBy = "anonymous";

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean val) {
        this.isActive = val;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public <T extends JPABase> T save() {
        setAudtiableFields();
        return super.save();
    }

    private void setAudtiableFields() {
        if (getCreated() == null) {
            setCreated(new Date());
        }
        setUpdated(new Date());
        setUpdatedBy("system");
    }
}
