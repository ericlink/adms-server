package core.module.codec;

//////package net.diabetech.module.glucomon;
//////
//////import java.util.logging.Level;
//////import java.util.logging.Logger;
//////import javax.ejb.Stateless;
//////import net.diabetech.wctp.*;
//////
//////@Stateless
///////**
////// * Confidential Information.
////// * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
////// * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
////// **/
//////public class SendOtaMessageBean implements SendOtaMessage {
//////    private static Logger logger = Logger.getLogger(SendOtaMessage.class.getName());
//////
//////    /**** SET / GET PARAMETERS ****/
//////    public void setParameterTimeSetting( GlucoMon glucoMon, boolean updateGlucoseMeterWithNetworkTime, int profileTimeZoneUtcOffsetMinutes ) {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterSetTimeSetting(  updateGlucoseMeterWithNetworkTime, profileTimeZoneUtcOffsetMinutes )  );
//////    }
//////    public void getParameterTimeSetting( GlucoMon glucoMon  ) {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetTimeSetting()  );
//////    }
//////
//////    public void setParameterEnableFlexSuiteHeader( GlucoMon glucoMon, boolean enableFlexSuiteHeader )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeParameterSetEnableFlexSuiteHeader( enableFlexSuiteHeader ) );
//////    }
//////    public void getParameterEnableFlexSuiteHeader( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetEnableFlexSuiteHeader()  );
//////    }
//////
//////    public void setParameterEnableEncryption( GlucoMon glucoMon, boolean enableEncryption )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeParameterSetEnableEncryption( enableEncryption ) );
//////    }
//////    public void getParameterEnableEncryption( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetEnableEncryption()  );
//////    }
//////
//////    public void setParameterMessageCount( GlucoMon glucoMon, byte stored, byte sent, byte failed )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeParameterSetMessageCounts( stored, sent, failed ) );
//////    }
//////    public void getParameterMessageCount( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetMessageCounts()  );
//////    }
//////
//////    public void setParameterEncryptionKey( GlucoMon glucoMon, byte[] secretKey )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterSetEncryptionKey( secretKey ) );
//////    }
//////    public void getParameterEncryptionKey( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetEncryptionKey()  );
//////    }
//////    public void setParameterResetEncryptionKey( GlucoMon glucoMon )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterResetEncryptionKey() );
//////    }
//////
//////    public void setParameterRadioSleep( GlucoMon glucoMon, int startTimeMinutes, int endTimeMinutes )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterSetRadioSleep( startTimeMinutes, endTimeMinutes )  );
//////    }
//////    public void getParameterRadioSleep( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetRadioSleep()  );
//////    }
//////
//////    public void setParameterAlarmLimit( GlucoMon glucoMon, int lowLimit, int highLimit )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterSetAlarmLimit( lowLimit, highLimit )  );
//////    }
//////    public void getParameterAlarmLimit( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetAlarmLimit()  );
//////    }
//////
//////
//////    public void setParameterAutoDeleteGlucoseMeter( GlucoMon glucoMon, boolean autoDeleteGlucoseMeter )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeParameterSetAutoDeleteGlucoseMeter( autoDeleteGlucoseMeter )  );
//////    }
//////    public void getParameterAutoDeleteGlucoseMeter( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetAutoDeleteGlucoseMeter()  );
//////    }
//////
//////
//////    public void setParameterMailToAddress( GlucoMon glucoMon, String newMailToAddress )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterSetMailToAddress( newMailToAddress )  );
//////    }
//////    public void getParameterMailToAddress( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetMailToAddress()  );
//////    }
//////
//////    public void getParameterBatteryStatus( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetBatteryStatus()  );
//////    }
//////    public void getParameterBatteryVoltage( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetBatteryVoltage()  );
//////    }
//////    public void getParameterDutyCycles( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetDutyCycles()  );
//////    }
//////    public void getParameterLastChargeTime( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetLastChargeTime()  );
//////    }
//////    public void setParameterBatteryOffset( GlucoMon glucoMon, short batteryOffset  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterSetBatteryOffset( batteryOffset )  );
//////    }
//////
//////    public void getParameterSoftwareVersion( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetSoftwareVersion()  );
//////    }
//////
//////    public void setParameterAutoSendNetworkInfo( GlucoMon glucoMon, boolean autoSendNetoworkInfo )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeParameterSetAutoSendNetworkInfo(autoSendNetoworkInfo ) );
//////    }
//////    public void getParameterAutoSendNetworkInfo( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetAutoSendNetworkInfo()  );
//////    }
//////
//////    public void getParameterNetworkInfo( GlucoMon glucoMon  )  {
//////        sendOtaMessage(   glucoMon, EncodeDecodeOtaMessage.encodeParameterGetNetworkInfo()  );
//////    }
//////
//////    public void setParameterDebugInfo( GlucoMon glucoMon, boolean isDebugOn )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeParameterSetDebugInfo( isDebugOn )  );
//////    }
//////    public void getParameterDebugInfo( GlucoMon glucoMon )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeParameterGetDebugInfo()  );
//////    }
//////
//////    /****  ERASE DMD COMMANDS ****/
//////    public void eraseGlucoMon( GlucoMon glucoMon, boolean onlyEraseUndeliveredReadings   )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeEraseGlucoMon( onlyEraseUndeliveredReadings )  );
//////    }
//////    public void eraseGlucoMon( GlucoMon glucoMon, boolean onlyEraseUndeliveredReadings, String serialNumber )  {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeEraseGlucoMon( onlyEraseUndeliveredReadings, serialNumber )  );
//////    }
//////    public void eraseGlucoMonAllSerialNumbersAndReadings( GlucoMon glucoMon ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeEraseGlucoMonAllSerialNumbersAndReadings()  );
//////    }
//////    public void eraseGlucoMonSerialNumberAndReadings( GlucoMon glucoMon, String serialNumber ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeEraseGlucoMonSerialNumberAndReadings( serialNumber )  );
//////    }
//////
//////
//////    /**** GET DATABASE COMMANDS ****/
//////    public void getReadings( GlucoMon glucoMon, boolean onlySendUndeliveredReadings ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeGetReadings( onlySendUndeliveredReadings, null )  );
//////    }
//////    public void getReadings( GlucoMon glucoMon, boolean onlySendUndeliveredReadings, String serialNumber ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeGetReadings( onlySendUndeliveredReadings, serialNumber )  );
//////    }
//////
//////    public void getGlucoseMeterSerialNumbers( GlucoMon glucoMon ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeGetGlucoseMeterSerialNumbers()  );
//////    }
//////
//////    public void getDiagnosticInfo( GlucoMon glucoMon, byte diagnosticIndex ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeGetDiagnosticInfo( diagnosticIndex )  );
//////    }
//////
//////    public void getAllParameters( GlucoMon glucoMon ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeGetAllParameters()  );
//////    }
//////
//////    /**** SEND EVENTS ****/
//////    public void sendEventReadGlucoseMeter( GlucoMon glucoMon ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeEventReadGlucoseMeter()  );
//////    }
//////    public void sendEventDeletePendingCommands( GlucoMon glucoMon ) {
//////        sendOtaMessage( glucoMon, EncodeDecodeOtaMessage.encodeEventDeletePendingCommands()  );
//////    }
//////    public void sendAcknowledgement( GlucoMon glucoMon ) {
//////        try {
//////            logger.fine( "WCTP" );
//////            WctpEnvelope wctpEnvelope = new WctpEnvelope();
//////
//////            // WirelessNetworkProperties added here... could be glucoMon properties that delegate to carrier? YES?
//////            wctpEnvelope.setMiscInfo( glucoMon.getWctpOriginatorMiscInfo() );
//////            wctpEnvelope.setNotifyWhenDelivered( glucoMon.getWctpMessageControlNotifyWhenDelivered() );
//////            wctpEnvelope.setNotifyWhenQueued( glucoMon.getWctpMessageControlNotifyWhenQueued() );
//////            wctpEnvelope.setRecipientId( glucoMon.getWctpRecipientId() );
//////            wctpEnvelope.setSendResponsesToId( glucoMon.getWctpMessageControlSendResponsesToId() );
//////            wctpEnvelope.setSenderId( glucoMon.getWctpOriginatorSenderId() );
//////            wctpEnvelope.setWctpGatewayUrl( glucoMon.getWctpGatewayUrl() );
//////            WctpOutboundAdaptor wctpOut = new WctpOutboundAdaptor();
//////            String message = "Log entry received";
//////            wctpOut.sendSubmitRequest( wctpEnvelope, message );
//////        } catch ( Exception e ) {
//////            //wrap exception
//////            e.printStackTrace();
//////        }
//////    }
//////
//////
//////
//////    private void sendOtaMessage( GlucoMon glucoMon, int[] encodedOtaMessage ) {
//////        try {
//////            logger.fine(  "SendOtaMessage.sendOtaMessage[to=" + glucoMon.getDeviceEmailAddress() + ",message=0x" +  EncodeDecodeOtaMessage.toHexString( encodedOtaMessage ) + "]" );
//////            byte[] encryptedOtaMessage = EncodeDecodeOtaMessage.encryptOtaMessage( glucoMon, encodedOtaMessage );
//////            //logger.fine( "SendOtaMessage.sendOtaMessage[to=" + glucoMon.getDeviceEmailAddress() + ",message=0x" +  EncodeDecodeOtaMessage.toHexString( encryptedOtaMessage ) + "]" );
//////
//////            if ( !glucoMon.getTransportType().equals( glucoMon.WCTP ) ) {
//////                throw new IllegalArgumentException( "Unsupported glucoMon transport " + glucoMon.getTransportType() );
//////            }
//////
//////            logger.fine( "WCTP" );
//////            WctpEnvelope wctpEnvelope = new WctpEnvelope();
//////            wctpEnvelope.setMiscInfo( glucoMon.getWctpOriginatorMiscInfo() );
//////            wctpEnvelope.setNotifyWhenDelivered( glucoMon.getWctpMessageControlNotifyWhenDelivered() );
//////            wctpEnvelope.setNotifyWhenQueued( glucoMon.getWctpMessageControlNotifyWhenQueued() );
//////            wctpEnvelope.setRecipientId( glucoMon.getWctpRecipientId() );
//////            wctpEnvelope.setSendResponsesToId( glucoMon.getWctpMessageControlSendResponsesToId() );
//////            wctpEnvelope.setSenderId( glucoMon.getWctpOriginatorSenderId() );
//////            wctpEnvelope.setWctpGatewayUrl( glucoMon.getWctpGatewayUrl() );
//////            WctpOutboundAdaptor wctpOut = new WctpOutboundAdaptor();
//////            byte[] payload = new byte[encryptedOtaMessage.length];
//////            for( int i = 0; i < encryptedOtaMessage.length; i++ ) {
//////                payload[i] = (byte)encryptedOtaMessage[i];
//////            }
//////            wctpOut.sendSubmitRequest( wctpEnvelope, payload );
//////        } catch ( Exception e ) {
//////            logger.log( Level.SEVERE, "Error sending message to glucomon", e );
//////        }
//////
//////    }
//////}
