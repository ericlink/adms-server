package core.module;
////
////import java.io.Serializable;
////import java.util.Date;
////import javax.persistence.Column;
////import javax.persistence.Entity;
////import javax.persistence.EntityListeners;
////import javax.persistence.GeneratedValue;
////import javax.persistence.Id;
////import javax.persistence.JoinColumn;
////import javax.persistence.ManyToOne;
////import javax.persistence.NamedQueries;
////import javax.persistence.NamedQuery;
////import javax.persistence.Table;
////import javax.persistence.Temporal;
////import javax.persistence.TemporalType;
////import org.apache.commons.lang.builder.CompareToBuilder;
////import org.apache.commons.lang.builder.EqualsBuilder;
////import org.apache.commons.lang.builder.HashCodeBuilder;
////import org.apache.commons.lang.builder.ToStringBuilder;
////
/////**   
//// * Confidential Information.
//// * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
//// * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
////**/
////@Entity
////@Table(name = "moduleOutboundMessage")
////@NamedQueries({
////    @NamedQuery(name = "ModuleOutboundMessage.findById", query = "SELECT m FROM ModuleOutboundMessage m WHERE m.id = :id"),
////    @NamedQuery(name = "ModuleOutboundMessage.findByStatus", query = "SELECT m FROM ModuleOutboundMessage m WHERE m.status = :status"),
////    @NamedQuery(name = "ModuleOutboundMessage.findByPayload", query = "SELECT m FROM ModuleOutboundMessage m WHERE m.payload = :payload"),
////    @NamedQuery(name = "ModuleOutboundMessage.findByCreated", query = "SELECT m FROM ModuleOutboundMessage m WHERE m.created = :created"),
////    @NamedQuery(name = "ModuleOutboundMessage.findByUpdated", query = "SELECT m FROM ModuleOutboundMessage m WHERE m.updated = :updated"),
////    @NamedQuery(name = "ModuleOutboundMessage.findByUpdatedBy", query = "SELECT m FROM ModuleOutboundMessage m WHERE m.updatedBy = :updatedBy")
////})
////
////public class ModuleOutboundMessage implements Auditable, Serializable {
public class ModuleOutboundMessage {
////    
////    @Id
////    @GeneratedValue
////    @Column(name = "id", nullable = false)
////    private int id;
////    
////    @Column(name = "status", nullable = false)
////    private MessageStatusType status = MessageStatusType.NEW;
////    
////    @Column(name = "payload", nullable = false)
////    private byte [] payload;
////    
////    @Column(name = "created", nullable = false)
////    @Temporal(TemporalType.TIMESTAMP)
////    private Date created;
////    
////    @Column(name = "updated", nullable = false)
////    @Temporal(TemporalType.TIMESTAMP)
////    private Date updated;
////    
////    @Column(name = "updatedBy", nullable = false)
////    private String updatedBy;
////    
////    @JoinColumn(name = "recipientModuleId")
////    @ManyToOne
////    private Module recipientModule;
////    
////    /** Creates a new instance of ModuleOutboundMessage */
////    public ModuleOutboundMessage() {
////    }
////    
////    public ModuleOutboundMessage(MessageStatusType status, byte [] payload, Date created, Date updated, String updatedBy) {
////        this.status = status;
////        this.payload = payload;
////        this.created = created;
////        this.updated = updated;
////        this.updatedBy = updatedBy;
////    }
////    
////    public int getId() {
////        return id;
////    }
////    
////    public void setId(int id) {
////        this.id = id;
////    }
////    
////    public MessageStatusType getStatus() {
////        return this.status;
////    }
////    
////    public void setStatus(MessageStatusType status) {
////        this.status = status;
////    }
////    
////    public byte [] getPayload() {
////        return this.payload;
////    }
////    
////    public void setPayload(byte [] payload) {
////        this.payload = payload;
////    }
////    
////    public Date getCreated() {
////        return this.created;
////    }
////    
////    public void setCreated(Date created) {
////        this.created = created;
////    }
////    
////    public Date getUpdated() {
////        return this.updated;
////    }
////    
////    public void setUpdated(Date updated) {
////        this.updated = updated;
////    }
////    
////    public String getUpdatedBy() {
////        return this.updatedBy;
////    }
////    
////    public void setUpdatedBy(String updatedBy) {
////        this.updatedBy = updatedBy;
////    }
////    
////    public Module getRecipientModule() {
////        return this.recipientModule;
////    }
////    
////    public void setRecipientModule(Module recipientModule) {
////        this.recipientModule = recipientModule;
////    }
////    
////    public boolean equals(Object obj) {
////        if ( obj instanceof ModuleOutboundMessage == false) {
////            return false;
////        }
////        if (this == obj) {
////            return true;
////        }
////        ModuleOutboundMessage rhs = (ModuleOutboundMessage)obj;
////        return new EqualsBuilder()
////        .append(payload,rhs.payload)
////        .append(recipientModule,rhs.recipientModule)
////        .append(status,rhs.status)
////        .isEquals();
////    }
////    
////    public int compareTo(Object o) {
////        ModuleOutboundMessage rhs = (ModuleOutboundMessage) o;
////        return new CompareToBuilder()
////        .append(payload,rhs.payload)
////        .append(recipientModule,rhs.recipientModule)
////        .append(status,rhs.status)
////        .toComparison();
////    }
////
////    public int hashCode() {
////        return new HashCodeBuilder()
////        .append(payload)
////        .append(recipientModule)
////        .append(status)
////        .toHashCode();
////    }
////    
////    public String toString() {
////        return new ToStringBuilder(this)
////        .append("id",id)
////        .append("payload",payload)
////        .append("recipientModule",recipientModule)
////        .append("status",status)
////        .toString();
////    }
////   
////    
}
