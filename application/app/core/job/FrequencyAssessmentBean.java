package core.job;

import java.util.logging.Logger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class FrequencyAssessmentBean {

    private static final Logger logger = Logger.getLogger(FrequencyAssessmentBean.class.getName());
//
//    Mailer mailer = new Mailer();
//
//    static {
//        DateHelper.setDefaultTimeZone();
//    }
//
////    @Schedule(second = "1", minute = "1", hour = "*/1", persistent = false)
//    public void run() {
//        logger.info("Running FrequencyAssessment");
//        for (UserFrequencyAssessmentProfile userFrequencyAssessmentProfile : entityDao.findUserFrequencyAssessmentProfiles() ) {
//            try {
//                if ( !userFrequencyAssessmentProfile.isTimeToRun() ) {
//                    logger.log( Level.INFO, "Not time to run for UserFrequencyAssessmentProfile {0}", userFrequencyAssessmentProfile.getId() );
//                    continue;
//                }
//                if ( !isModuleOnNetwork(userFrequencyAssessmentProfile.getUser().getModules()) ) {
//                    logger.log( Level.INFO, "Skipping frequency assessment, module not on network handle={0},userId={1}", new Object[]{userFrequencyAssessmentProfile.getUser().getHandle(),userFrequencyAssessmentProfile.getUser().getId()});
//                    continue;
//                }
//
//                boolean isFrequentChecker = isFrequentChecker(userFrequencyAssessmentProfile);
//                String handle = userFrequencyAssessmentProfile.getUser().getHandle();
//                String subject  = MessageFormat.format( "{0} - QuickTip", new Object[] {handle} );
//                QuickTip quickTip = getQuickTip(isFrequentChecker,userFrequencyAssessmentProfile);
//                String body = quickTip.getTip();
//                Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = userFrequencyAssessmentProfile.getUserFeatureProfileDestinationProfiles();
//                logger.log( Level.INFO, "Found {0} destination profiles for for UserFrequencyAssessmentProfile {1}", new Object[] { userFeatureProfileDestinationProfiles.size(), userFrequencyAssessmentProfile.getId() } );
//                logger.log( Level.FINE, "Sending frequency assessment for UserFrequencyAssessmentProfile {0}, handle={1}, isFrequentChecker={2}, quickTipId={3}", new Object[] { userFrequencyAssessmentProfile.getId(), handle, isFrequentChecker, quickTip.getId() } );
//                userFrequencyAssessmentProfile.setLastFired(new Date());
//                mailer.sendFromGlucoMON(
//                        userFeatureProfileDestinationProfiles,
//                        subject,
//                        body,
//                        null
//                        );
//            } catch (Throwable t) {
//                logger.log( Level.SEVERE, "Error generating frequency assessment", t );
//            }
//        }
//    }
//
//    /**
//     * Quick tip is always frequency for infrequent tester, alternates between diet and exercise for a frequent tester.
//     **/
//    private QuickTip getQuickTip(final boolean isFrequentChecker, final UserFrequencyAssessmentProfile userFrequencyAssessmentProfile) {
//        QuickTipType quickTipType = getNextQuickTipType(userFrequencyAssessmentProfile, isFrequentChecker);
//        int ordinal = getLastQuickTipOrdinal(userFrequencyAssessmentProfile, quickTipType);
//        QuickTip quickTip = entityDao.findNextQuickTip( quickTipType, ordinal );
//        setCurrentQuickTipOrdinal(userFrequencyAssessmentProfile, quickTipType, quickTip);
//        return quickTip;
//    }
//
//    private void setCurrentQuickTipOrdinal(final UserFrequencyAssessmentProfile userFrequencyAssessmentProfile, final QuickTipType quickTipType, final QuickTip quickTip) {
//        switch ( quickTipType ) {
//            case ACTIVITY :
//                userFrequencyAssessmentProfile.setLastQuickTipActivityTypeOrdinal(quickTip.getOrdinal());
//                break;
//            case CHECK_FREQUENCY :
//                userFrequencyAssessmentProfile.setLastQuickTipCheckTypeOrdinal(quickTip.getOrdinal());
//                break;
//            case DIET :
//                userFrequencyAssessmentProfile.setLastQuickTipDietTypeOrdinal(quickTip.getOrdinal());
//                break;
//        }
//    }
//
//    private int getLastQuickTipOrdinal(final UserFrequencyAssessmentProfile userFrequencyAssessmentProfile, final QuickTipType quickTipType) {
//        switch ( quickTipType ) {
//            case ACTIVITY :
//                return userFrequencyAssessmentProfile.getLastQuickTipActivityTypeOrdinal();
//            case CHECK_FREQUENCY :
//                return userFrequencyAssessmentProfile.getLastQuickTipCheckTypeOrdinal();
//            case DIET :
//                return userFrequencyAssessmentProfile.getLastQuickTipDietTypeOrdinal();
//        }
//        throw new IllegalArgumentException( "QuickTipType not understood (quickTipType=" +quickTipType+ ")");
//    }
//
//    private QuickTipType getNextQuickTipType(final UserFrequencyAssessmentProfile userFrequencyAssessmentProfile, final boolean isFrequentChecker) {
//        QuickTipType quickTipType = QuickTipType.CHECK_FREQUENCY;
//        if ( isFrequentChecker ) {
//            quickTipType
//                    = userFrequencyAssessmentProfile.getLastFrequentQuickTipType() == QuickTipType.ACTIVITY ?
//                        QuickTipType.DIET : quickTipType.ACTIVITY;
//            userFrequencyAssessmentProfile.setLastFrequentQuickTipType(quickTipType);
//        }
//        return quickTipType;
//    }
//
//    /**
//     * Analyze the data and determine frequent/infrequent, caller should verify module is on network before calling
//     **/
//    public boolean isFrequentChecker(UserFrequencyAssessmentProfile userFrequencyAssessmentProfile) {
//        return isFrequentChecker(userFrequencyAssessmentProfile, Calendar.getInstance(userFrequencyAssessmentProfile.getUser().getTimeZone()));
//    }
//
//    /**
//     * Analyze the data and determine frequent/infrequent, caller should verify module is on network before calling
//     **/
//    public boolean isFrequentChecker(UserFrequencyAssessmentProfile userFrequencyAssessmentProfile, Calendar dayToCheckFrom) {
//        int minimumDataPointsPerRequiredDay = userFrequencyAssessmentProfile.getMinimumDataPointsPerRequiredDay();
//        int requiredDays = userFrequencyAssessmentProfile.getRequiredDays();
//        int daysToCheck  = userFrequencyAssessmentProfile.getDaysToCheck();
//
//        Collection<DataPoint> dataPoints = entityDao.findDataPoints(
//                DateHelper.getRangeFullDaysBefore(dayToCheckFrom, daysToCheck),
//                userFrequencyAssessmentProfile.getUser()
//                );
//
//        Map<Calendar, Integer> dataPointsPerDay = tallyDataPointsPerDay(dataPoints);
//
//        logger.log( Level.FINE, "minimumDataPointsPerRequiredDay={0},requiredDays={1},daysToCheck={2},dataPointsPerDay={3}", new Object [] {
//            minimumDataPointsPerRequiredDay,
//            requiredDays,
//            daysToCheck,
//            dataPointsPerDay
//        });
//
//        boolean isFrequentChecker =
//                isMinimumNumberOfDaysOfDataAvailable(dataPointsPerDay,requiredDays)
//                &&
//                isMinimumNumberOfDataPointsAvailable(dataPointsPerDay,requiredDays,minimumDataPointsPerRequiredDay);
//
//        if ( isFrequentChecker ) {
//            userFrequencyAssessmentProfile.setLastFrequentOn(new Date());
//            userFrequencyAssessmentProfile.setTotalFrequent(userFrequencyAssessmentProfile.getTotalFrequent() + 1 );
//        } else {
//            userFrequencyAssessmentProfile.setLastInfrequentOn(new Date());
//            userFrequencyAssessmentProfile.setTotalInFrequent(userFrequencyAssessmentProfile.getTotalInFrequent() + 1 );
//        }
//
//        return isFrequentChecker;
//
//    }
//
//    private Map<Calendar, Integer> tallyDataPointsPerDay(final Collection<DataPoint> dataPoints) {
//        Map <Calendar,Integer> dataPointsPerDay = new HashMap<Calendar,Integer>();
//        for( DataPoint dataPoint : dataPoints ) {
//            DateHelper.tallyItemsPerDay(dataPointsPerDay, dataPoint.getTimestamp());
//        }
//        return dataPointsPerDay;
//    }
//
//    /**
//     * This is not a data quality (as in, is the module communicating), but is a check to meet
//     * the minimim number of days required for the frequency test
//     **/
//    private boolean isMinimumNumberOfDaysOfDataAvailable(Map<Calendar, Integer> dataPointsPerDay, int minimumDays) {
//        if ( dataPointsPerDay.size() < minimumDays ) {
//            logger.log( Level.FINE,
//                    "{0} days not enough days to be frequent (< minimumDays) [{0}<{2}] == true", new Object[]{
//                dataPointsPerDay.size(),
//                minimumDays
//            }
//            );
//            return false;
//        }
//
//        return true;
//    }
//
//    private boolean isMinimumNumberOfDataPointsAvailable(Map<Calendar, Integer> dataPointsPerDay, int requiredDays, int minimumPerRequiredDay ) {
//        logger.log( Level.FINE,
//                "{0} days of data available",
//                dataPointsPerDay.size()
//                );
//        int requiredDaysFulfilled = 0;
//        for ( int tally : dataPointsPerDay.values() ) {
//            logger.log( Level.FINE,
//                    "tests for day {0}",
//                    tally
//                    );
//            if ( tally >= minimumPerRequiredDay ) {
//                requiredDaysFulfilled++;
//            }
//        }
//        logger.log( Level.FINE, "requiredDaysFulfilled >= requiredDays [{0}>={1}", new Object [] {
//            requiredDaysFulfilled,
//            requiredDays
//        });
//        return requiredDaysFulfilled >= requiredDays;
//    }
//
//    private boolean isModuleOnNetwork(Collection<Module> modules) { //TODO promote to net.diabetech.entity.User ?
//        int isActiveCount = 0;
//        for( Module module : modules ) {
//            if (module.isActive()) {
//                isActiveCount++;
//            }
//        }
//        if ( isActiveCount != 1 ) {
//            logger.log( Level.WARNING, "{0} active modules found", isActiveCount);
//            return false;
//        }
//        boolean isModuleOnNetwork = false;
//        Module lastModuleChecked = null;
//        for( Module module : modules) {
//            // first active module is used, should only be one at any given time
//            if ( module.isActive()  ) {
//                lastModuleChecked = module;
//                if ( module.getLastMessageReceivedFromModule() == null ) {
//                    logger.log( Level.FINE, "getLastMessageReceivedFromModule == null, moduleId=", module.getId());
//                    break;
//                }
//                //// figure out user local time for midnight of the day before today
//                // now user local time
//                Calendar quietAllowedForFeature = Calendar.getInstance( module.getUser().getTimeZone() );
//                // midnight 'now' user local time
//                quietAllowedForFeature = DateHelper.asDay(quietAllowedForFeature);
//                // midnight day before user local time
//                quietAllowedForFeature.add(Calendar.DATE,-1);
//                // if any messages from module after midnight yesterday (any messages yesterday or later)
//                logger.log( Level.FINE, "moduleId={0},getLastMessageReceivedFromModule={1}",
//                        new Object[] {module.getId(),module.getLastMessageReceivedFromModule()});
//                if ( module.getLastMessageReceivedFromModule().after( quietAllowedForFeature.getTime() ) ) {
//                    logger.log( Level.FINE, "heard from module after midnight yesterday");
//                    isModuleOnNetwork = true;
//                    break;
//                } else {
//                    logger.log( Level.FINE, "did not hear from module after midnight yesterday");
//                }
//            }
//        }
//        logger.log(Level.FINE, "moduleId={0}, isModuleOnNetwork={1}", new Object[]{lastModuleChecked.getId(),isModuleOnNetwork} );
//        return isModuleOnNetwork;
//    }    
}
