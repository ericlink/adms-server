package core.job;

import java.util.logging.Logger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class ParticipationRequestBean {

    private static final Logger logger = Logger.getLogger(ParticipationRequestBean.class.getName());
//    FrequencyAssessmentBean frequencyAssessment = new FrequencyAssessmentBean();
//
//    Mailer mailer = new Mailer();
//
//
//
//    /**
//     * Run for all users with this feature
//     **/
////    @Schedule(second = "1", minute = "1", hour = "*/1", persistent = false)
//    public void run() {
//        logger.info("Running ParticipationRequest");
//        for (UserParticipationRequestProfile userParticipationRequestProfile : entityDao.findUserParticipationRequestProfiles() ) {
//            try {
//                if ( !userParticipationRequestProfile.isTimeToRun() ) {
//                    logger.log( Level.INFO, "Not time to run for UserParticipationRequestProfile {0}", userParticipationRequestProfile.getId() );
//                    continue;
//                }
//
//                String handle = userParticipationRequestProfile.getUser().getHandle();
//                String subject  = MessageFormat.format( "{0} - Participation Request", new Object[] { handle } );
//                String body = null;
//
//                userParticipationRequestProfile.setLastFired(new Date());
//                if ( isFrequentChecker(userParticipationRequestProfile) ) {
//                    userParticipationRequestProfile.setLastFrequentOn(new Date());
//                    userParticipationRequestProfile.setTotalFrequent( userParticipationRequestProfile.getTotalFrequent() + 1 );
//                    body = MessageFormat.format(
//                            "{0} has been checking frequently. " +
//                            "Now would be a good time to contact " +
//                            "{0} to congratulate continued blood sugar checks.",
//                            new Object[] { handle } );
//                } else {
//                    userParticipationRequestProfile.setLastInfrequentOn(new Date());
//                    userParticipationRequestProfile.setTotalInFrequent( userParticipationRequestProfile.getTotalInFrequent() + 1 );
//                    body = MessageFormat.format(
//                            "{0} has been checking infrequently. " +
//                            "Now would be a good time to contact " +
//                            "{0} to encourage more frequent blood sugar checks.",
//                            new Object[] { handle } );
//                }
//
//                Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = userParticipationRequestProfile.getUserFeatureProfileDestinationProfiles();
//                logger.log( Level.INFO, "Found {0} destination profiles for for UserParticipationRequestProfile {1}", new Object[] { userFeatureProfileDestinationProfiles.size(), userParticipationRequestProfile.getId() } );
//                logger.log( Level.FINE, "Sending participation request for UserParticipationRequestProfile {0}, handle={1}", new Object[] { userParticipationRequestProfile.getId(), handle } );
//                mailer.sendFromGlucoMON(
//                        userFeatureProfileDestinationProfiles,
//                        subject,
//                        body,
//                        null
//                        );
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//        }
//    }
//
//    private boolean isFrequentChecker(UserParticipationRequestProfile userParticipationRequestProfile) {
//        double isFrequentThreshold = userParticipationRequestProfile.getIsFrequentThreshold().doubleValue();
//        int daysToCheck = userParticipationRequestProfile.getDaysToCheck();
//        int rangesToCheck = userParticipationRequestProfile.getNumberOfRangesToCheck();
//
//        UserFrequencyAssessmentProfile userFrequencyAssessmentProfile
//                = userParticipationRequestProfile.getUserFrequencyAssessmentProfile();
//
//        int frequentDays = 0;
//        Calendar dayToCheckFrom = Calendar.getInstance(userParticipationRequestProfile.getUser().getTimeZone());
//
//        logger.log( Level.FINE, "isFrequentThreshold={0},daysToCheck={1},rangesToCheck={2},dayToCheckFrom={3}, detail:{4}", new Object [] {
//            isFrequentThreshold,
//            daysToCheck,
//            rangesToCheck,
//            dayToCheckFrom.getTime(),
//            dayToCheckFrom
//        });
//
//        for( int i = 0; i < rangesToCheck; i++ ) {
//            if ( frequencyAssessment.isFrequentChecker(userFrequencyAssessmentProfile,dayToCheckFrom) ) {
//                frequentDays++;
//            }
//            dayToCheckFrom.add(Calendar.DATE,-1);
//        }
//        logger.log( Level.FINE,
//                "frequentDays / daysToCheck >= isFrequentThreshold [{0}/{1}>={2}] == {3}", new Object [] {
//            frequentDays,
//            daysToCheck,
//            isFrequentThreshold,
//            (frequentDays / daysToCheck >= isFrequentThreshold)
//        });
//
//        return frequentDays / daysToCheck >= isFrequentThreshold;
//    }
//
}
