package core.event;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import core.medicaldevice.MedicalDevice;
import models.User;
import core.feature.userfeatureprofile.UserFeatureProfileDestinationProfile;
import core.feature.userfeatureprofile.UserMedicalDeviceRegistrationProfile;
import helper.util.DateHelper;
import core.messaging.Messenger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class NewMedicalDeviceAlertListener {

    private static final Logger logger = Logger.getLogger(NewMedicalDeviceAlertListener.class.getName());
    Messenger mailer = new Messenger();

    public void onNewMedicalDevice(MedicalDevice medicalDevice) {
        User user = medicalDevice.getUser();
        logger.log(Level.INFO, "Looking for UserMedicalDeviceRegistrationProfile for user {0}", new Object[]{user});

        for (UserMedicalDeviceRegistrationProfile userMedicalDeviceRegistrationProfile : UserMedicalDeviceRegistrationProfile.findBy(user)) {
            boolean isTimeToRun = userMedicalDeviceRegistrationProfile.isTimeToRun();
            userMedicalDeviceRegistrationProfile = userMedicalDeviceRegistrationProfile.save();

            if (!isTimeToRun) {
                logger.log(Level.INFO, "Not time to run for UserMedicalDeviceRegistrationProfile {0}", userMedicalDeviceRegistrationProfile.getId());
                continue;
            }

            final String subject = "easySHARE Alert new Glucose Meter";
            /**
             * Darby, a new Glucose Meter was used with your GlucoMON. Serial #PMP4BE5BT added Tue, Nov 18, 6:56 PM MST
             **/
//TODO Locale
//          String body = MessageFormat.format("{0}, a new Glucose Meter was used with your GlucoMON. Serial #{1} added {2}", new Object[]{
            String body = MessageFormat.format("{0}, a new Glucose Meter was used. Serial No. {1} added {2}", new Object[]{
                    user.getHandle(),
                    medicalDevice.getSerialNumber(),
                    DateHelper.getDateFormatDayOfWeekDayOfMonthYearAmTz(user.getTimeZone()).
                    format(new Date())
                });
            userMedicalDeviceRegistrationProfile.setLastFired(new Date());
            userMedicalDeviceRegistrationProfile = userMedicalDeviceRegistrationProfile.save();

            Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = userMedicalDeviceRegistrationProfile.getUserFeatureProfileDestinationProfiles();
            logger.log(Level.INFO, "Found {0} destination profiles for for UserMedicalDeviceRegistrationProfile {1}", new Object[]{userFeatureProfileDestinationProfiles.size(), userMedicalDeviceRegistrationProfile.getId()});
            logger.log(Level.FINE, "Sending new medical device alerts for UserMedicalDeviceRegistrationProfile {0}, handle={1}", new Object[]{userMedicalDeviceRegistrationProfile.getId(), user.getHandle()});
            mailer.sendFromGlucoMON(
                userFeatureProfileDestinationProfiles,
                subject,
                body,
                null);
        }
    }
}
