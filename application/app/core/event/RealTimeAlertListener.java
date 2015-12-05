package core.event;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import core.datapoint.DataPoint;
import models.Threshold;
import models.User;
import core.feature.userfeatureprofile.UserFeatureProfileDestinationProfile;
import core.feature.userfeatureprofile.UserRealTimeAlertProfile;
import helper.util.DateHelper;
import core.messaging.Messenger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class RealTimeAlertListener {

    private static Logger logger = Logger.getLogger(RealTimeAlertListener.class.getName());
    Messenger mailer = new Messenger();

    public void onNewDataPoint(DataPoint dataPoint) {
        User user = dataPoint.getUser();
        DateFormat dateFormat = DateHelper.getDateFormatShort(DateHelper.UTC_TIME_ZONE);
        logger.fine("Getting users with real time alerts");
        for (UserRealTimeAlertProfile userRealTimeAlertProfile : UserRealTimeAlertProfile.findBy(user)) {
            boolean isTimeToRun = userRealTimeAlertProfile.isTimeToRun();
            userRealTimeAlertProfile = userRealTimeAlertProfile.save();
            if (!isTimeToRun) {
                logger.log(Level.INFO, "Not time to run for UserRealTimeAlertProfile {0}", userRealTimeAlertProfile.getId());
                continue;
            }

            String subject = MessageFormat.format("easySHARE Alert for {0}", user.getHandle());
            String body = MessageFormat.format("BG={0} taken {1}", new Object[]{
                    dataPoint.getFormattedValue(),
                    dateFormat.format(dataPoint.getTimestamp())
                });

            //todo remove
            logger.log(Level.INFO, "{0} {1}", new Object[]{subject, body});


            Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = userRealTimeAlertProfile.getUserFeatureProfileDestinationProfiles();
            logger.log(Level.INFO, "Found {0} destination profiles for for UserRealTimeAlertProfile {1}", new Object[]{userFeatureProfileDestinationProfiles.size(), userRealTimeAlertProfile.getId()});
            userRealTimeAlertProfile.setLastFired(new Date());
            userRealTimeAlertProfile = userRealTimeAlertProfile.save();
            for (UserFeatureProfileDestinationProfile userFeatureProfileDestinationProfile : userFeatureProfileDestinationProfiles) {
                logger.log(Level.FINE, "Checking thresholds for for UserFeatureProfileDestinationProfile {0}", userFeatureProfileDestinationProfile.getId());
                Threshold threshold = userFeatureProfileDestinationProfile.getThreshold();
                if (threshold == null || threshold.isOutsideLimits(dataPoint.getValue())) {
                    logger.log(Level.FINE, "Sending alert for UserFeatureProfileDestinationProfile {0}, handle={1}", new Object[]{userFeatureProfileDestinationProfile.getId(), user.getHandle()});
                    mailer.sendFromGlucoMON(
                        userFeatureProfileDestinationProfile,
                        subject,
                        body,
                        null);
                }
            }
        }
    }
}
