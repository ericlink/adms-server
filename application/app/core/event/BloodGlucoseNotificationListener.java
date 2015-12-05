package core.event;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import helper.activation.ByteArrayDataSource;
import core.type.ComparisonType;
import core.datapoint.DataPoint;
import core.type.UnitOfMeasureType;
import models.User;
import core.feature.userfeatureprofile.UserFeatureProfileDestinationProfile;
import core.feature.userfeatureprofile.UserRiskAlertProfile;
import helper.util.DateHelper;
import core.messaging.Messenger;
import core.report.ReportManager;
import core.report.ReportManagerBean;
import core.report.ReportOutputType;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class BloodGlucoseNotificationListener {

    private static final Logger logger = Logger.getLogger(BloodGlucoseNotificationListener.class.getName());
    Messenger mailer = new Messenger();
    ReportManager reportManager = new ReportManagerBean();

    /**
     * Assumptions:
     *    Multiple messages generated by a BloodGlucoseNotificationListener will go to same set of destinations.
     *    May be many criteria defined for one BloodGlucoseNotificationListener feature/imp.
     *    DataPoint is managed and should be returned by entityDao queries.
     *
     ************************************************
     * if risk alert already sent in maxPeriod, sendingVolumePeriod (1 per day), return
     *
     * for each RiskAlertCriteria   // may be more than just high/low, diff periods for example
     *    message = checkCriteria(RiskAlertCriteria)
     *    if ( message != null )
     *          add message to message list
     * done
     *
     * for each message send to destinations
     **/
    public void onNewDataPoint(DataPoint dataPoint) {
        User user = dataPoint.getUser();
        logger.log(Level.INFO, "Checking for user {0}", user);

        for (UserRiskAlertProfile userRiskAlertProfile : UserRiskAlertProfile.findBy(user)) {
            boolean isTimeToRun = userRiskAlertProfile.isTimeToRun();
            userRiskAlertProfile = userRiskAlertProfile.save();

            if (!isTimeToRun) {
                logger.log(Level.INFO, "Not time to run for UserRiskAlertProfile {0}", userRiskAlertProfile.getId());
                continue;
            }
            String body = getMessage(userRiskAlertProfile);
            if (body != null) {
                logger.log(Level.INFO, "Body: {0}", body);
                //userRiskAlertProfile.setLastAlertDetectedOn(new Date()); // TODO remove db column after superclass table updated
                userRiskAlertProfile.setLastFired(new Date());
                userRiskAlertProfile = userRiskAlertProfile.save();
// Rename risk alert to something w/o any charged words
//                String subject  = MessageFormat.format( "{0} - Risk Alert", user.getHandle() );
                String subject = MessageFormat.format("{0} - Day Over Day Log Report with Blood Glucose Notification", user.getHandle());
                logger.log(Level.INFO, "Subject: {0}", subject);

                ByteArrayDataSource attachment = reportManager.getDayOverDayReport(ReportOutputType.PDF, user);

                Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = userRiskAlertProfile.getUserFeatureProfileDestinationProfiles();
                logger.log(Level.INFO, "Found {0} destination profiles for for UserRiskAlertProfile {1}", new Object[]{userFeatureProfileDestinationProfiles.size(), userRiskAlertProfile.getId()});
                logger.log(Level.FINE, "Sending risk alert for UserRiskAlertProfile {0}, handle={1}", new Object[]{userRiskAlertProfile.getId(), user.getHandle()});
                mailer.sendFromGlucoDYNAMIX(
                    userFeatureProfileDestinationProfiles,
                    subject,
                    body,
                    attachment);
            }
        }
    }

    private String getMessage(UserRiskAlertProfile userRiskAlertProfile) {
        if (isCriteriaMet(userRiskAlertProfile)) {
            String formattedComparisonValue = getFormattedComparisonValue(userRiskAlertProfile);
            if (userRiskAlertProfile.getComparisonValue().
                intValue() == 0) {
                return MessageFormat.format(
                    "New blood glucose checks in last {3} hours (as of {4})",
                    new Object[]{
                        userRiskAlertProfile.getResult().
                        intValue(),
                        format(userRiskAlertProfile.getComparisonType()),
                        formattedComparisonValue,
                        userRiskAlertProfile.getHours(),
                        DateHelper.getDateFormatDayOfWeekDayOfMonthYearAmTz(
                        userRiskAlertProfile.getUser().
                        getTimeZone()).
                        format(new Date())
                    });
            } else {
                return MessageFormat.format(
                    "{0} blood glucose checks {1} {2} in last {3} hours (as of {4})",
                    new Object[]{
                        userRiskAlertProfile.getResult().
                        intValue(),
                        format(userRiskAlertProfile.getComparisonType()),
                        formattedComparisonValue,
                        userRiskAlertProfile.getHours(),
                        DateHelper.getDateFormatDayOfWeekDayOfMonthYearAmTz(
                        userRiskAlertProfile.getUser().
                        getTimeZone()).
                        format(new Date())
                    });
            }
        } else {
            return null;
        }
    }

    private String getFormattedComparisonValue(UserRiskAlertProfile userRiskAlertProfile) {
        String formattedComparisonValue;
        if (userRiskAlertProfile.getUser().
            getPreferredUnitOfMeasure().
            equals(UnitOfMeasureType.MMOL_L)) {
            DecimalFormat formatter = new DecimalFormat("#0.0");
            formattedComparisonValue = formatter.format((double) userRiskAlertProfile.getComparisonValue().
                intValue() / (double) 18);
        } else {
            formattedComparisonValue = String.valueOf(userRiskAlertProfile.getComparisonValue());
        }
        return formattedComparisonValue;
    }

    private String format(ComparisonType comparisonType) {
        switch (comparisonType) {
            case GREATER_THAN:
                return "greater than";
            case LESS_THAN:
                return "less than";
            default:
                return comparisonType.toString();
        }
    }

    private boolean isCriteriaMet(UserRiskAlertProfile userRiskAlertProfile) {
        Collection<DataPoint> dataPoints =
            DataPoint.findDataPoints(
            DateHelper.getRangeHoursBeforeNow(userRiskAlertProfile.getUser().getTimeZone(), userRiskAlertProfile.getHours()),
            userRiskAlertProfile.getUser());
        int tally = 0;
        for (DataPoint dataPoint : dataPoints) {
            if (userRiskAlertProfile.getComparisonType() == ComparisonType.GREATER_THAN
                && dataPoint.getValue() > userRiskAlertProfile.getComparisonValue().
                intValue()) {
                logger.log(Level.FINE, "Incrementing tally, data point {0} with value {1} > {2}", new Object[]{
                        dataPoint.getId(),
                        dataPoint.getValue(),
                        userRiskAlertProfile.getComparisonValue().
                        intValue()
                    });
                tally++;
            } else if (userRiskAlertProfile.getComparisonType() == ComparisonType.LESS_THAN
                && dataPoint.getValue() < userRiskAlertProfile.getComparisonValue().
                intValue()) {
                logger.log(Level.FINE, "Incrementing tally, data point {0} with value {1} < {2}", new Object[]{
                        dataPoint.getId(),
                        dataPoint.getValue(),
                        userRiskAlertProfile.getComparisonValue().
                        intValue()
                    });
                tally++;
            }
        }

        logger.log(Level.INFO, "tally >= userRiskAlertProfile.getResult().intValue() [{0}>={1}] == {2}", new Object[]{
                tally,
                userRiskAlertProfile.getResult().
                intValue(),
                (tally >= userRiskAlertProfile.getResult().
                intValue())
            });

        return tally >= userRiskAlertProfile.getResult().
            intValue();
    }
}