package core.api.data;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import core.module.Module;
import core.module.NetworkType;
import core.medicaldevice.lifescan.OneTouchUltraParser;
import core.medicaldevice.lifescan.ParseException;
import core.module.codec.GlucoMonDatabase;
import helper.util.DateHelper;
import core.module.ModuleInboundBean;
import play.jobs.Job;

/**
 * Confidential Information.
 * Copyright (C) 2007 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class ProcessInboundMessage extends Job {

    private static final Logger logger = Logger.getLogger(ProcessInboundMessage.class.getName());
    InboundMessage inboundMessage = null;

    static {
        DateHelper.setDefaultTimeZone();
    }

    public ProcessInboundMessage() {
        super();
    }

    public ProcessInboundMessage(InboundMessage inboundMessage) {
        super();
        this.inboundMessage = inboundMessage;
    }

    /**
     * Main dispatcher to determine what kind of message we have and dispatch
     * it to a handler.
     **/
    @Override
    public void doJob() {
        try {
            logger.log(Level.FINE, "Inbound message {0}", inboundMessage);
            if (inboundMessage.getMessageType() == InboundMessage.MessageType.DATA) {
                handleDataMessage(inboundMessage);
            } else if (inboundMessage.getMessageType() == InboundMessage.MessageType.APP_STARTUP) {
                handleAppStartupMessage(inboundMessage);
            } else if (inboundMessage.getMessageType() == InboundMessage.MessageType.OTAP_NOTIFICATION) {
                handleOtapNotificationMessage(inboundMessage);
            } else {
                throw new IllegalArgumentException(
                    "MessageType not understood for type " + inboundMessage.getMessageType());
            }
            return;
        } catch (Throwable t) {
            logger.log(Level.SEVERE,
                "Resubmit st message, transaction rolled back due to error: {0} \n UNPROCESSED_MESSAGE\n",
                new Object[]{t.toString()});
            logger.log(Level.SEVERE, "Exception details", t);
        }
    }

    public void handleAppStartupMessage(final InboundMessage inboundMessage) throws Exception {
        logger.log(Level.INFO, "Handling startup message for module {0}", inboundMessage.getModuleId());
        Module module = getModule(inboundMessage.getModuleId());
        module.setLastStartupMessageReceived(new Date());
        module.setLastMessageReceivedFromModule(new Date());
        module.setLastReportedAppVersion(inboundMessage.getSoftwareVersion());
        String[] payloadItems = inboundMessage.getPayload().
            split(":");
        module.setLastReportedSimId(payloadItems[0]);
        module.setLastReportedSignalStrength(payloadItems[1]);
        module.setLastReportedNetwork(payloadItems[2]);
        module.save();
    }

    private void handleOtapNotificationMessage(InboundMessage inboundMessage) {
        logger.log(Level.INFO, "Handling otap notification message for module {0}", inboundMessage.getModuleId());
        Module module = getModule(inboundMessage.getModuleId());
        module.setLastMessageReceivedFromModule(new Date());
        module.setLastOtapNotification(new Date());
        String payload = inboundMessage.getPayload();
        module.setLastOtapNotificationMessage(payload);
        if (payload != null && payload.contains("900 Success")) {
            module.setOtapAttempts(0);
        }
        module.save();
    }

    public void handleDataMessage(final InboundMessage inboundMessage) throws Exception {
        logger.log(Level.INFO, "Handling data message for module {0}", inboundMessage.getModuleId());
        Module module = getModule(inboundMessage.getModuleId());
        if (module.getUser() != null) {
            logger.log(Level.INFO, "Module is provisioned, process data");
            module.setLastMessageReceivedFromModule(new Date());
            module = module.save();
            core.module.codec.InboundMessage glucoMonInboundMessage =
                decodeDataMessage(inboundMessage.getPayload());
            new ModuleInboundBean().onMessage(module, glucoMonInboundMessage);
        } else {
            logger.log(Level.WARNING, "Module is NOT provisioned, do not process data");
        }
    }

    private core.module.codec.InboundMessage decodeDataMessage(final String payload) throws ParseException {
        GlucoMonDatabase glucoMonDatabase = new GlucoMonDatabase();
        OneTouchUltraParser parser = new OneTouchUltraParser(payload);
        glucoMonDatabase.addGlucoseMeter(parser.getMedicalDevice());
        return glucoMonDatabase;
    }

    /**
     *@return A managed Module object that was retrieved from persistence or created.
     **/
    Module getModule(final String pin) {
        logger.fine("Getting module");
        Module module = Module.findByPin(pin);
        if (module != null) {
            logger.fine("Module found");
        } else {
            logger.fine("Module not found; create default module data");
            module = new Module();
            module.setPin(pin);
            module.setDisplayKey(pin);
            module.setNetworkType(NetworkType.MediSIM);
// do not automatically provision the user, TZ must be explicitly set
//            User unknownUser = new User();
//            unknownUser.addModule(module);
//            unknownUser.setHandle( "Unknown" );
//            unknownUser.setTimeZone( TimeZone.getTimeZone("UTC") );
//            unknownUser.setType(UserType.PARTICIPANT);
            module = module.save();
        }

        logger.log(Level.INFO, "From module id={0},pin={1},displayKey={2},userId={3}", new Object[]{module.getId(), module.getPin(), module.getDisplayKey(), module.getUser()});

        return module;
    }
}
