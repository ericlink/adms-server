package core.messaging;

import core.feature.userfeatureprofile.UserFeatureProfileDestinationProfile;
import helper.activation.ByteArrayDataSource;
import helper.io.FileLogHelper;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Schedule;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import play.Play;
import play.libs.Mail;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006, 2010, 2011 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class Messenger {

    private static Logger logger = Logger.getLogger(Messenger.class.getName());
    final private static String EMAIL_GLUCOMON = "support@myeasyshare.co.uk";
    final private static String EMAIL_GLUCODYNAMIX = "support@myeasyshare.co.uk";
    final private static String EMAIL_SUPPORT = "support@myeasyshare.co.uk";

    //todo replace w/ public email address
    public void sendFromGlucoDYNAMIX(final UserFeatureProfileDestinationProfile userFeatureProfileDestinationProfile, final String subject, final String body, final ByteArrayDataSource attachment) {
        sendSingle(userFeatureProfileDestinationProfile, EMAIL_GLUCODYNAMIX, subject, body, attachment);
    }

    //todo replace w/ public email address
    public void sendFromGlucoDYNAMIX(final Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles, final String subject, final String body, final ByteArrayDataSource attachment) {
        sendMultiple(userFeatureProfileDestinationProfiles, EMAIL_GLUCODYNAMIX, subject, body, attachment);
    }

    //todo replace w/ public email address
    public void sendFromGlucoMON(final UserFeatureProfileDestinationProfile userFeatureProfileDestinationProfile, final String subject, final String body, final ByteArrayDataSource attachment) {
        sendSingle(userFeatureProfileDestinationProfile, EMAIL_GLUCOMON, subject, body, attachment);
    }

    //todo replace w/ public email address
    public void sendFromGlucoMON(final Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles, final String subject, final String body, final ByteArrayDataSource attachment) {
        sendMultiple(userFeatureProfileDestinationProfiles, EMAIL_GLUCOMON, subject, body, attachment);
    }

    private void sendSingle(final UserFeatureProfileDestinationProfile userFeatureProfileDestinationProfile, final String from, final String subject, final String body, final ByteArrayDataSource attachment) {
        send(userFeatureProfileDestinationProfile, from, subject, body, attachment);
    }

    public void sendMultiple(final Collection<UserFeatureProfileDestinationProfile> userFeatureProfileDestinationProfiles, final String from, final String subject, final String body, final ByteArrayDataSource attachment) {
        for (UserFeatureProfileDestinationProfile userFeatureProfileDestinationProfile : userFeatureProfileDestinationProfiles) {
            send(userFeatureProfileDestinationProfile, from, subject, body, attachment);
        }
    }

    private void send(UserFeatureProfileDestinationProfile userFeatureProfileDestinationProfile, String from, String subject, String body, ByteArrayDataSource attachment) {
        Destination destination = userFeatureProfileDestinationProfile.getDestination();
        Schedule schedule = userFeatureProfileDestinationProfile.getSchedule();
        Calendar userLocalTime = Calendar.getInstance(destination.getUser().
            getTimeZone());
        Date lastMessagedOn = new Date();
        if (userFeatureProfileDestinationProfile.isActive() && schedule.isActive() && destination.isActive()) {
////            if ( attachment != null && !destination.getType().isAttachmentSupported()) {
////                logger.log(Level.WARNING, "Destination does not support attachments, skipping destination for message with attachment");
////                return;
////            }
            //if ( schedule.isRunnable(userLocalTime) ) {
            sendImmediately(destination.getAddress(), from, subject, body, attachment);
            //} else {
            //    long nextRunnableTime = schedule.calculateNextRunnableTime(userLocalTime);
            //    if (nextRunnableTime> 0) {
            //        lastMessagedOn = new Date(nextRunnableTime);
            //        queueMessage(nextRunnableTime, destination.getAddress(), from, subject, body, attachment) ;
            //    }
            //}
            if (destination.getFirstMessagedOn() == null) {
                destination.setFirstMessagedOn(new Date());
            }
            destination.setLastMessagedOn(lastMessagedOn);
            destination.setTotalMessagesSent(destination.getTotalMessagesSent() + 1);
            destination = destination.save();
            userFeatureProfileDestinationProfile.setLastMessageSendAllowedOn(lastMessagedOn);
            userFeatureProfileDestinationProfile = userFeatureProfileDestinationProfile.save();
        } else {
            logger.log(Level.FINE, "userFeatureProfileDestinationProfile, schedule or destination not active (userFeatureProfileDestinationProfileId={0},scheduleId={1},destinationId={2})",
                new Object[]{userFeatureProfileDestinationProfile.getId(), schedule.getId(), destination.getId()});
        }
    }

    public void testSendImmediately(final String to, final String from, final String subject, final String body, ByteArrayDataSource attachment) {
        sendImmediately(to, from, subject, body, attachment);
    }

    private void sendImmediately(final String to, final String from, final String subject, final String body, ByteArrayDataSource attachment) {
        try {
            logger.log(Level.INFO, "Sending email to={0},from={1},subject={2},body={3},attachment={4},fileName{5},mimeType={6}", new Object[]{to, from, subject, body, attachment, attachment == null ? "null" : attachment.getName(), attachment == null ? "null" : attachment.getContentType()});
            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.addTo(to);
            email.setFrom(from);
            email.setSubject(subject);
            email.setMsg(body);
            String logLocation = Play.configuration.getProperty("log.mail");
            logger.log(Level.FINE, "logging message to {0}, attachment size={1}", new Object[]{logLocation, attachment == null ? 0 : attachment.getBytes().length});
            long timestamp = System.currentTimeMillis();
            //TODO * log email contents also (body)
            FileLogHelper.createInstance().
                log(logLocation, to + "-" + timestamp, email.getSubject().getBytes());
            if (attachment != null) {
                String attachmentName = to + "-" + timestamp + "-attachment-" + attachment.getName();
                FileLogHelper.createInstance().
                    log(logLocation, attachmentName, attachment.getBytes());
                // add the attachment
                EmailAttachment emailAttachment = new EmailAttachment();
                //write to mailerbean dir then read from there, that is fine
                emailAttachment.setPath(logLocation + "/" + attachmentName);
                emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
                emailAttachment.setDescription(attachment.getName());
                emailAttachment.setName(attachment.getName());
                email.attach(emailAttachment);
            }
            logger.log(Level.FINE, "Sending message");
            Future<Boolean> send = Mail.send(email);
        } catch (Exception ex) {
            Logger.getLogger(Messenger.class.getName()).
                log(Level.SEVERE, null, ex);
            throw new MessangerException(ex);
        }
    }

//    private MimeMessage createMimeMessage() throws NamingException {
//        InitialContext ctx = null;
//        try {
//            ctx = new InitialContext();
//            //return new MimeMessage( (Session)ctx.lookup("java:Mail") );
//            return new MimeMessage( (Session)ctx.lookup("java/Mail") );
//        } finally {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (NamingException ex) {
//                    logger.log( Level.SEVERE, "Exception cleaning up initial context for java:Mail", ex );
//                }
//            }
//        }
//    }
//
//    private void queueMessage(final long nextRunnableTime, final String to, final String from, final String subject, final String body, ByteArrayDataSource attachment ) {
//        logger.log( Level.INFO, "Queue email with timer: to={0},from={1},subject={2},,body={3},to send at {4}", new Object[] {to,from,subject,body,new Date(nextRunnableTime)});
//        MessageEnvelope envelope = new MessageEnvelope( to, from, subject, body, attachment );
//        sessionContext.getTimerService().createTimer( nextRunnableTime, envelope );
//    }
//
//    @Timeout
//    public void onTimeout(Timer t) {
//        if ( t.getInfo() instanceof MessageEnvelope ) {
//            MessageEnvelope envelope = null;
//            try {
//                envelope = (MessageEnvelope)t.getInfo() ;
//                sendImmediately(
//                        envelope.to,
//                        envelope.from,
//                        envelope.subject,
//                        envelope.body,
//                        envelope.attachment
//                        );
//            } catch (Exception e) {
//                if (envelope.retries < 3 ) {
//                    logger.log( Level.SEVERE, "Error sending message on attempt " + envelope.retries + ". Retrying in 30 minutes. Error was "+e.toString()+".", e );
//                    long retryTime = System.currentTimeMillis() + (30 * DateHelper.MILLIS_PER_MINUTE);
//                    envelope.retries += 1;
//                    sessionContext.getTimerService().createTimer( retryTime, envelope );
//                } else {
//                    logger.log( Level.SEVERE, "Error sending message on attempt " + envelope.retries + ". Giving up. Error was "+e.toString()+".", e );
//                    throw new MailerException(e);
//                }
//            }
//        }
//    }
    public void sendToSupport(String message) {
        sendImmediately(EMAIL_SUPPORT, EMAIL_SUPPORT, "GlucoDYNAMIX Support Notification", message, null);



























    }

    class MessageEnvelope implements Serializable {

        String to;
        String from;
        String subject;
        String body;
        ByteArrayDataSource attachment;
        int retries = 0;

        MessageEnvelope(final String to, final String from, final String subject, final String body, ByteArrayDataSource attachment) {
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.body = body;
            this.attachment = attachment;
        }
    }
}
