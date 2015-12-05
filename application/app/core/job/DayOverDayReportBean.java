package core.job;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import helper.activation.ByteArrayDataSource;
import core.feature.userfeatureprofile.UserDayOverDayReportProfile;
import core.feature.userfeatureprofile.UserFeatureProfileDestinationProfile;
import core.messaging.Messenger;
import core.report.ReportManager;
import core.report.ReportManagerBean;
import core.report.ReportOutputType;
import play.Logger;
import play.jobs.Job;
import play.jobs.On;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
//http://localhost:9000/@documentation/jobs#scheduling
//@Every("1h")
//http://www.quartz-scheduler.org/docs/tutorials/crontrigger.html
//min @On("0 * * * * ?")
// hr @On("0 0 * * * ?")
@On("0 * * * * ?")
public class DayOverDayReportBean extends Job {

    Messenger mailer = new Messenger();
    ReportManager reportManager = new ReportManagerBean();

    /**
     * Create day over day reports in batch mode
     **/
    @Override
    public void doJob() {
        try {
            Logger.info("Running DayOverDayReports");
            for (UserDayOverDayReportProfile userDayOverDayReportProfile : UserDayOverDayReportProfile.findActive()) {
                try {
                    boolean isTimeToRun = userDayOverDayReportProfile.isTimeToRun();
                    userDayOverDayReportProfile = userDayOverDayReportProfile.save();
                    if (!isTimeToRun) {
                        Logger.info("Not time to run for UserDayOverDayReportProfile %s", userDayOverDayReportProfile.getId());
                        continue;
                    }

                    String handle = userDayOverDayReportProfile.getUser().getHandle();
                    String subject = MessageFormat.format("easySHARE Day Over Day Log Report for {0}",
                        new Object[]{handle});
                    String body = MessageFormat.format("Please find the following report attached: \n  easySHARE Day Over Day Log Report for {0}",
                        new Object[]{handle});

                    ByteArrayDataSource attachment = reportManager.getDayOverDayReport(ReportOutputType.PDF, userDayOverDayReportProfile.getUser());
                    if (attachment != null) {
                        userDayOverDayReportProfile.setLastFired(new Date());
                        userDayOverDayReportProfile = userDayOverDayReportProfile.save();
                        Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles = userDayOverDayReportProfile.getUserFeatureProfileDestinationProfiles();
                        Logger.info("Found %s destination profiles for for UserDayOverDayReportProfile %s", userFeatureProfileDestinationProfiles.size(), userDayOverDayReportProfile.getId());
                        Logger.info("Sending day over day report for UserDayOverDayReportProfile %s, handle=%s", userDayOverDayReportProfile.getId(), handle);
                        mailer.sendFromGlucoDYNAMIX(
                            userFeatureProfileDestinationProfiles,
                            subject,
                            body,
                            attachment);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
