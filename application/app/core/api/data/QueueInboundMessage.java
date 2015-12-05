package core.api.data;

import java.util.Date;
import java.util.logging.Level;
import core.module.Module;
import java.util.logging.Logger;
import helper.util.DateHelper;

/**
 * Calls enqueue or onError method for document handling by subclass.
 * Confidential Information.
 * Copyright (C) 2007-2010 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class QueueInboundMessage {

    private static final Logger logger = Logger.getLogger(QueueInboundMessage.class.getName());
    final static private int MAX_OTAP_ATTEMPTS_ALLOWED = 3;

    static {
        DateHelper.setDefaultTimeZone();
    }

    /**
     * Queue the request for parsing and send back the version number and time information
     * and always queue the request.
     *
     * @param message document to enqueue
     *
     * @return response text as string in format APP_VER:RECEIVED_TIME_LOCAL:RESPONSE_TIME_LOCAL
     *
     **/
    public String enqueue(InboundMessage inboundMessage) throws Exception {
        try {
            logger.log(Level.INFO, "Received stEnvelope for moduleId {0}", inboundMessage.getModuleId());

            // respond w/ default if timestamp not available in header.
            if (inboundMessage.getTimestampMessageReceived().
                equals("0")) {
                String nopResponse = "0:0:0";
                logger.log(Level.WARNING, "No received time available, returning default response, not setting time on module[{0}]", nopResponse);
                return nopResponse;
            }

            Module module = Module.findByPin(inboundMessage.getModuleId());
            if (module != null) {
                logger.log(Level.INFO, "Found module {0}", module);
                if (isOtapIndicated(module, inboundMessage)) {
                    module.setLastOtapAttempt(new Date());
                    module.setOtapAttempts(module.getOtapAttempts() + 1);
                    module = module.save();
                    if (module.getOtapAttempts() > MAX_OTAP_ATTEMPTS_ALLOWED) {
                        logger.log(Level.SEVERE, "Max otap attempts exceeded for module {0}", module);
                        throw new RuntimeException("Max otap attempts exceeded for module");
                    }
                }
                if (module.getUser() != null) {
                    // handles calls after things like startup mesasges etc.
                    // when module is in db but not assigned to a user yet
                    // only set time for provisioned modules
                    StringBuffer returnValue = new StringBuffer();
                    returnValue.append(module.getTargetAppVersion());
                    returnValue.append(":");
                    getResponseReceivedTime(returnValue, inboundMessage, module);
                    getResponseResponseTime(returnValue, module);
                    String returnValueString = returnValue.toString();
                    logger.log(Level.INFO, "Returning response [{0}]", returnValueString);
                    return returnValueString;
                } else {
                    String appVersionOnly = module.getTargetAppVersion() + ":0:0";
                    logger.log(Level.INFO, "Do not set time for unprovisioned module, returning [{0}] ", appVersionOnly);
                    return appVersionOnly;
                }
            } else {
                String defaultResponse = ("0:0:0");
                logger.log(Level.INFO, "Module not found in system, returning default response [{0}]", defaultResponse);
                return defaultResponse;
            }
        } finally {
            // now kick off async after module updated above
            queueRequest(inboundMessage);
        }
    }

    private boolean isOtapIndicated(final Module module, final InboundMessage stEnvelope) {
        logger.log(Level.FINE, "module.getTargetAppVersion={0},stEnvelope.getSoftwareVersion={1},module.getOtapAttempts={2}",
            new Object[]{module.getTargetAppVersion(), stEnvelope.getSoftwareVersion(), module.getOtapAttempts()});
        return !module.getTargetAppVersion().
            equals("0")
            && !module.getTargetAppVersion().
            equals(stEnvelope.getSoftwareVersion());
    }

    private void getResponseReceivedTime(final StringBuffer returnValue, final InboundMessage stEnvelope, final Module module) throws NumberFormatException {
        long receivedTimestampUtc = Long.parseLong(stEnvelope.getTimestampMessageReceived());
        long receivedTimestampLocal = receivedTimestampUtc
            + module.getTimeZone().
            getOffset(receivedTimestampUtc);
        returnValue.append(receivedTimestampLocal);
        returnValue.append(":");
    }

    private void getResponseResponseTime(final StringBuffer returnValue, final Module module) {
        long responseTimestampUtc = System.currentTimeMillis();
        long responseTimestampLocal = responseTimestampUtc
            + module.getTimeZone().
            getOffset(responseTimestampUtc);
        returnValue.append(responseTimestampLocal);
    }

    // TODO how long to delay job start?  some delay seems ok to let the server finish the request before processing further
    private void queueRequest(InboundMessage inboundMessage) {
        logger.log(Level.INFO, "Queuing [{0}]", inboundMessage);
        new ProcessInboundMessage(inboundMessage).in(3);
    }
}
