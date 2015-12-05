package core.job;

import java.util.logging.Logger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class AppointmentReminderBean {

    private static final Logger logger = Logger.getLogger(AppointmentReminderBean.class.getName());
//    final private static int DAYS_IN_ADVANCE_TO_REMIND = 1;
//
//    Mailer mailer = new Mailer();
//
//    ReportManager reportManager = new ReportManagerBean();
//
//    /**
//     * Send appointment reminders in batch mode
//     **/
//    //@TransactionAttribute(TransactionAttributeType.REQUIRED)
//    //@Schedule(second = "1", minute = "1", hour = "*/1", persistent = false)
//    public void doJob() {
//        try {
//            logger.info("Running AppointmentReminders");
//
//            for (UserAppointmentReminderProfile userAppointmentReminderProfile : entityDao.findUserAppointmentReminderProfiles() ) {
//                try {
//                    if ( !userAppointmentReminderProfile.isTimeToRun() ) {
//                        logger.log( Level.INFO, "Not time to run for UserAppointmentReminderProfile {0}", userAppointmentReminderProfile.getId() );
//                        continue;
//                    }
//
//                    if ( !isTimeToRemind(userAppointmentReminderProfile) ) {
//                        continue;
//                    };
//
//                    sendReminder(userAppointmentReminderProfile);
//
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
//            }
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//    private void sendReminder(final UserAppointmentReminderProfile userAppointmentReminderProfile) {
//        userAppointmentReminderProfile.setLastFired(new Date());
//        userAppointmentReminderProfile.setIsActive(false); // one time only feature profile
//        String appointmentTime =
//                DateHelper
//                .getDateFormatShort(DateHelper.UTC_TIME_ZONE) //stored in users local time in db, don't convert use as is
//                .format(userAppointmentReminderProfile.getAppointmentTime());
//        String handle = userAppointmentReminderProfile.getUser().getHandle();
//        String subject  = MessageFormat.format( "A reminder for your {0} appointment.",
//                new Object[] { appointmentTime }
//        );
//        String body = null;
//
//        ByteArrayDataSource attachment = reportManager.getDayOverDayReport(ReportOutputType.PDF, userAppointmentReminderProfile.getUser());
//        if ( attachment != null ) {
//            body = MessageFormat.format("Please print the attached report to take to your appointment at {0}. Thanks, GlucoMON.",
//                    new Object[] { appointmentTime }
//            );
//        } else {
//            body = MessageFormat.format("There was not enough recent data for your Day Over Day Report, if so we would have attached it to take to your appointment at {0}. Thanks, GlucoMON.",
//                    new Object[] { appointmentTime }
//            );
//        }
//
//        Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = userAppointmentReminderProfile.getUserFeatureProfileDestinationProfiles();
//        logger.log( Level.INFO, "Found {0} destination profiles for for UserAppointmentReminderProfile {1}", new Object[] { userFeatureProfileDestinationProfiles.size(), userAppointmentReminderProfile.getId() } );
//        logger.log( Level.FINE, "Sending reminder with day over day report for UserAppointmentReminderProfile {0}, handle={1}", new Object[] { userAppointmentReminderProfile.getId(), handle } );
//        mailer.sendFromGlucoDYNAMIX(
//                userFeatureProfileDestinationProfiles,
//                subject,
//                body,
//                attachment
//                );
//    }
//
//    private boolean isTimeToRemind(final UserAppointmentReminderProfile userAppointmentReminderProfile) {
//        // reminder logic is particular to this reminder bean
//        Calendar appointmentTimeCalendar =
//                Calendar.getInstance(userAppointmentReminderProfile.getUser().getTimeZone());
//        appointmentTimeCalendar.setTime(userAppointmentReminderProfile.getAppointmentTime());
//
//        CalendarRange reminderRange = new CalendarRange(
//                Calendar.getInstance(userAppointmentReminderProfile.getUser().getTimeZone()),
//                appointmentTimeCalendar
//                );
//        // only remind when it's one day in advance
//        int currentInterval = DateHelper.calculateIntervalInDays(reminderRange);
//        if ( currentInterval > DAYS_IN_ADVANCE_TO_REMIND ) { // too early
//            logger.log( Level.INFO, "Not time to remind for UserAppointmentReminderProfile {0}, currentInterval={1}",
//                    new Object[]{ userAppointmentReminderProfile.getId(), currentInterval }
//            );
//            return false;
//        } else if ( currentInterval < DAYS_IN_ADVANCE_TO_REMIND ) {  // too late
//            logger.log( Level.WARNING, "Missed reminder for UserAppointmentReminderProfile {0}, currentInterval={1}",
//                    new Object[]{ userAppointmentReminderProfile.getId(), currentInterval }
//            );
//            userAppointmentReminderProfile.setIsActive(false); // missed the reminder
//            return false;
//        }
//
//        // it's time'
//        return true;
//    }
//
}
