package core.datapoint;

import core.module.Module;
import core.type.UnitOfMeasureType;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import helper.util.CalendarRange;
import helper.util.DateHelper;
import core.medicaldevice.MedicalDevice;
import models.MyModel;
import models.User;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import play.Logger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
@Entity
@Table(name = "dataPoint")
@NamedQueries({
    @NamedQuery(name = "DataPoint.findByModuleOutboundMessageId", query = "SELECT d FROM DataPoint d WHERE d.moduleOutboundMessageId = :moduleOutboundMessageId"),
    @NamedQuery(name = "DataPoint.findByTimestamp", query = "SELECT d FROM DataPoint d WHERE d.timestamp = :timestamp"),
    @NamedQuery(name = "DataPoint.findByTimestampValueAndUser", query = "SELECT d FROM DataPoint d WHERE d.timestamp = :timestamp and d.value = :value and d.user = :user"),
    //@NamedQuery(name = "DataPoint.findByIsControl", query = "SELECT d FROM DataPoint d WHERE d.isControl = :isControl"),
    //@NamedQuery(name = "DataPoint.findByUnitOfMeasureCode", query = "SELECT d FROM DataPoint d WHERE d.unitOfMeasureCode = :unitOfMeasureCode"),
    @NamedQuery(name = "DataPoint.findByValue", query = "SELECT d FROM DataPoint d WHERE d.value = :value"),
    @NamedQuery(name = "DataPoint.findByTimeCorrectionOffset", query = "SELECT d FROM DataPoint d WHERE d.timeCorrectionOffset = :timeCorrectionOffset"),
    @NamedQuery(name = "DataPoint.findByTimeCorrectedTimestamp", query = "SELECT d FROM DataPoint d WHERE d.timeCorrectedTimestamp = :timeCorrectedTimestamp"),
    @NamedQuery(name = "DataPoint.findByOriginated", query = "SELECT d FROM DataPoint d WHERE d.originated = :originated"),
    @NamedQuery(name = "DataPoint.findByIsActive", query = "SELECT d FROM DataPoint d WHERE d.isActive = :isActive"),
    @NamedQuery(name = "DataPoint.findByCreated", query = "SELECT d FROM DataPoint d WHERE d.created = :created"),
    @NamedQuery(name = "DataPoint.findByUpdated", query = "SELECT d FROM DataPoint d WHERE d.updated = :updated"),
    @NamedQuery(name = "DataPoint.findByUpdatedBy", query = "SELECT d FROM DataPoint d WHERE d.updatedBy = :updatedBy")
})
public class DataPoint extends MyModel {

    @Column(name = "moduleOutboundMessageId", nullable = false)
    private int moduleOutboundMessageId;
    @Column(name = "timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "isControl", nullable = false, columnDefinition = "int")
    private boolean isControl;
    @Column(name = "unitOfMeasureCode", nullable = false)
    private UnitOfMeasureType uom = UnitOfMeasureType.MG_DL;
    @Column(name = "value", nullable = false)
    private int value;
    @Column(name = "timeCorrectionOffset", nullable = false)
    private int timeCorrectionOffset;
    @Column(name = "timeCorrectedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCorrectedTimestamp;
    @Column(name = "originated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date originated;
    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @JoinColumn(name = "moduleId", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Module module;
    @JoinColumn(name = "medicalDeviceId", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private MedicalDevice medicalDevice;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataPoint")
//    private java.util.Collection <DataMark> dataMarks  = new ArrayList<DataMark>();
//
    public DataPoint() {
        DateHelper.setDefaultTimeZone();
    }

    public DataPoint(int moduleOutboundMessageId, Date timestamp, boolean isControl, UnitOfMeasureType uom, int value, int timeCorrectionOffset, Date originated, boolean isActive, Date created, Date updated, String updatedBy) {
        this.moduleOutboundMessageId = moduleOutboundMessageId;
        this.timestamp = timestamp;
        this.isControl = isControl;
        this.uom = uom;
        this.value = value;
        this.timeCorrectionOffset = timeCorrectionOffset;
        this.originated = originated;
        this.isActive = isActive;
        this.created = created;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public int getModuleOutboundMessageId() {
        return this.moduleOutboundMessageId;
    }

    public void setModuleOutboundMessageId(int moduleOutboundMessageId) {
        this.moduleOutboundMessageId = moduleOutboundMessageId;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isControl() {
        return this.isControl;
    }

    public void setIsControl(boolean isControl) {
        this.isControl = isControl;
    }

    public UnitOfMeasureType getUnitOfMeasureType() {
        return this.uom;
    }

    public void setUnitOfMeasureCode(UnitOfMeasureType uom) {
        this.uom = uom;
    }

    public int getValue() {
        return this.value;
    }

    public double getValueMmol() {
        DecimalFormat formatterValueMmol = new DecimalFormat("#0.0");
        return Double.valueOf(formatterValueMmol.format((double) value / (double) 18));
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTimeCorrectionOffset() {
        return this.timeCorrectionOffset;
    }

    public void setTimeCorrectionOffset(int timeCorrectionOffset) {
        this.timeCorrectionOffset = timeCorrectionOffset;
    }

    public Date getTimeCorrectedTimestamp() {
        return this.timeCorrectedTimestamp;
    }

    public void setTimeCorrectedTimestamp(Date timeCorrectedTimestamp) {
        this.timeCorrectedTimestamp = timeCorrectedTimestamp;
    }

    public Date getOriginated() {
        return this.originated;
    }

    public void setOriginated(Date originated) {
        this.originated = originated;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public MedicalDevice getMedicalDevice() {
        return this.medicalDevice;
    }

    public void setMedicalDevice(MedicalDevice medicalDevice) {
        this.medicalDevice = medicalDevice;
    }

//    public java.util.Collection <DataMark> getDataMarks() {
//        return this.dataMarks;
//    }
//
//    public void setDataMark(java.util.Collection <DataMark> dataMarks) {
//        this.dataMarks = dataMarks;
//        for(DataMark dm : this.dataMarks) {
//            dm.setDataPoint(this);
//        }
//    }
//    public void addDataMark(DataMark dm) {
//        dm.setDataPoint(this);
//        this.dataMarks.add(dm);
//    }
//    public void removeDataMark(DataMark dm) {
//        dm.setDataPoint(null);
//        this.dataMarks.remove(dm);
//    }
    public boolean equals(Object obj) {
        if (obj instanceof DataPoint == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        DataPoint rhs = (DataPoint) obj;
        return new EqualsBuilder().append(value, rhs.value).
            append(timestamp, rhs.timestamp).
            append(user, rhs.user).
            isEquals();
    }

    public int compareTo(Object o) {
        DataPoint rhs = (DataPoint) o;
        return new CompareToBuilder().append(value, rhs.value).
            append(timestamp, rhs.timestamp).
            append(user, rhs.user).
            toComparison();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(value).
            append(timestamp).
            append(user).
            toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id).
            append("isActive", isActive).
            append("value", value).
            append("timestamp", timestamp).
            append("userId", user.getId()).
            append("formattedValue", getFormattedValue()).
            toString();
    }

    public String getFormattedValue() {
        // really a subclass would be nice, but bigger effort, and only one type until short codes come into play.
        // solve when short codes are implemented
        //if (getMedicalDevice().isActive()) { //LATER if MedicalDeviceType is one touch ultra... right now all are
        return new LifeScanOneTouchUltraFormatter(this.getValue()).getFormattedValue();
        //} else {
        //    return String.valueOf( this.getValue() );
        /// }
    }

    /**
     * @return true if the data point with these parameters exists in the data store
     **/
    public static boolean isDataPointPersisted(User user, Date timestamp, int value) {
        if (user == null || timestamp == null || value == 0) {
            return false;
        }

        Integer val = Integer.valueOf(value);

        boolean isPersisted = (Long) find(
            "SELECT count(d) "
            + "FROM DataPoint d "
            + "WHERE d.timestamp = :timestamp "
            //+ "and d.value = :value "
            + "and d.user = :user").
            bind("timestamp", timestamp).
            //TODO * binding prob w/ integer, but should this also be per/include medical device?
            //bind("value", String.valueOf(value)).
            bind("user", user).
            first() > 0;

        Logger.trace(
            "Datapoint isPersisted={0} for userId={1},timestamp={2},value={3}",
            isPersisted,
            user.getId(), timestamp,
            value);

        return isPersisted;
    }

    public static Collection<DataPoint> findDataPoints(CalendarRange calendarRange, User user) {
        if (user == null) {
            return null;
            //todo new ArrayList<DataPoint>();
        }
        String query =
            "  select  dp "
            + "from DataPoint dp "
            + "where dp.user = :user "
            + "and dp.timestamp > :start "
            + "and dp.timestamp < :end "
            + "and dp.isActive = true "
            + "and dp.isControl = false "
            + "order by dp.timestamp asc";

        Collection<DataPoint> dataPoints =
            find(query).
            bind("user", user).
            bind("start", calendarRange.getStart().
            getTime()).
            bind("end", calendarRange.getEnd().
            getTime()).
            fetch();

        return filterUnique(dataPoints);
    }

    /**
     * Filter collection using the business entities equals method.
     **/
    private static Collection<DataPoint> filterUnique(Collection<DataPoint> objects) {
        Set<DataPoint> uniqueObjects = new LinkedHashSet<DataPoint>();
        uniqueObjects.addAll(objects);
        return uniqueObjects;
    }

    class LifeScanOneTouchUltraFormatter {

        double value;
        DecimalFormat formatterMmol = new DecimalFormat("#0.0");
        DecimalFormat formatterMgdl = new DecimalFormat("#0");

        public LifeScanOneTouchUltraFormatter(double value) {
            this.value = value;
        }

        /**
         *@return true if 602 : REXC, Range exceeded, no valid reading
         **/
        public boolean isOutOfRangeError() {
            return 602 == value;
        }

        /**
         *@return true if 601 : HIGH, Value higher then 600
         */
        public boolean isHighValue() {
            return 601 == value || 603 == value;
        }

        public String getFormattedValue() {
            StringBuilder sb = new StringBuilder();

            // control may be applied to any value
            if (isControl()) {
                sb.append("C");
            }

            // exclusive values
            if (isHighValue()) {
                sb.append("HIGH");
            } else if (isOutOfRangeError()) {
                sb.append("REXC");
            } else {
                if (user != null && user.getPreferredUnitOfMeasure().
                    equals(UnitOfMeasureType.MMOL_L)) {
                    sb.append(formatterMmol.format((double) value / (double) 18));
                } else if (user != null && user.getPreferredUnitOfMeasure().
                    equals(UnitOfMeasureType.MG_DL)) {
                    sb.append(formatterMgdl.format(value));
                } else {
                    throw new RuntimeException("Unknown unit of measure");
                }
            }

            return sb.toString();
        }
    }
}
