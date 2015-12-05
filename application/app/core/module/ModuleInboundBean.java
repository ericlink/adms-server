package core.module;

import core.module.codec.EventCorruptedGlucoseMeterConnected;
import core.module.codec.InboundMessage;
import core.module.codec.GlucoMonDatabase;
import core.module.codec.GlucoMonDatabaseSerialNumberList;
import core.module.codec.FailedResponse;
import core.module.codec.EventMaxSerialNumberExceeded;
import core.module.codec.SetParameterOtaTimeSettingsAck;
import core.module.codec.GetParameterAck;
import core.module.codec.EventBatteryFull;
import core.module.codec.EventBatteryLow;
import core.module.codec.SetParameterAck;
import java.util.Date;
import java.util.logging.Level;
import models.*;

import java.util.logging.Logger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class ModuleInboundBean implements ModuleInbound {

    private static Logger logger = Logger.getLogger(ModuleInboundBean.class.getName());
    DatabaseMessageEventHandler databaseMessageEventHandler = new DatabaseMessageEventHandlerBean();

    public void onMessage(Module module, InboundMessage message) throws ModuleInboundException {
        logger.log(Level.INFO, "InboundMessage module={0} message={1}", new Object[]{module.getPin(), message.toString()});

//        if ( message instanceof EventBatteryFull ) {
//            onMessage( module, (EventBatteryFull)message );
//        } else if ( message instanceof EventBatteryLow ) {
//            onMessage( module, (EventBatteryLow)message );
//        } else if ( message instanceof GlucoMonDatabase ) {
        if (message instanceof GlucoMonDatabase) {
            onMessage(module, (GlucoMonDatabase) message);
        }
//        } else if ( message instanceof SetParameterOtaTimeSettingsAck ) {
//            onMessage( module, (SetParameterOtaTimeSettingsAck)message );
//        }
    }

    /**
     * Save the readings and send an alert if needed
     **/
    private void onMessage(Module module, GlucoMonDatabase message) throws ModuleInboundException {
        logger.log(Level.INFO, "Message is GlucoMonDatabase,module={0},{1} glucoseMeters", new Object[]{module.getPin(), message.getGlucoseMeters().size()});
        databaseMessageEventHandler.onMessage(module, message);
    }

    /**
     * Update module table with last charged time.
     **/
    private void onMessage(Module module, EventBatteryFull message) throws ModuleInboundException {
        logger.log(Level.INFO, "Message is EventBatteryFull module={0} \n message={1}", new Object[]{module.getPin(), message});
        module.setLastCharged(message.getTimeStamp());
    }

    /**
     * Update module table with last discharged time.
     **/
    public void onMessage(Module module, EventBatteryLow message) throws ModuleInboundException {
        logger.log(Level.INFO, "Message is EventBatteryLow \n module={0} \n message={1}", new Object[]{module.getPin(), message});
        module.setLastDischarged(message.getTimeStamp());
    }

    /**
     * Update module table with last time settings ack.
     **/
    public void onMessage(Module module, SetParameterOtaTimeSettingsAck message) throws ModuleInboundException {
        logger.log(Level.INFO, "Message is SetParameterOtaTimeSettingsAck \n module={0} \n message={1}", new Object[]{module.getPin(), message});
        module.setLastTimeSettingsAck(new Date());
    }

    /**
     * Update the module wctp outbound messages table; the message has been received.
     **/
    private void onMessage(Module module, SetParameterAck message) throws ModuleInboundException {
        logger.log(Level.INFO, "Message is SetParameterAck \n module={0} \n message={1}", new Object[]{module.getPin(), message});
    }

    /**
     * TBD
     **/
    private void onMessage(Module module, GetParameterAck message) throws ModuleInboundException {
        logger.log(Level.INFO, "Message is GetParameterAck \n module={0} \n message={1}", new Object[]{module.getPin(), message});
    }

    /**
     * TBD
     **/
    private void onMessage(Module module, GlucoMonDatabaseSerialNumberList message) throws ModuleInboundException {
        logger.log(Level.INFO, "Message is GlucoMonDatabaseSerialNumberList \n module={0} \n message={1}", new Object[]{module.getPin(), message});
    }

    /**
     * Log this someplace?
     **/
    private void onMessage(Module module, EventCorruptedGlucoseMeterConnected message) throws ModuleInboundException {
        logger.log(Level.WARNING, "Message is EventCorruptedGlucoseMeterConnected \n module={0} \n message={1}", new Object[]{module.getPin(), message});
    }

    /**
     * Log this someplace?
     **/
    private void onMessage(Module module, EventMaxSerialNumberExceeded message) throws ModuleInboundException {
        logger.log(Level.SEVERE, "Message is EventMaxSerialNumberExceeded \n module={0} \n message={1}", new Object[]{module.getPin(), message});
    }

    /**
     * Log this someplace?
     **/
    private void onMessage(Module module, FailedResponse message) throws ModuleInboundException {
        logger.log(Level.SEVERE, "Message is FailedResponse \n module={0} \n message={1}", new Object[]{module.getPin(), message});
    }
}
