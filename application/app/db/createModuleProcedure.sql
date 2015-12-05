delimiter //
DROP PROCEDURE IF EXISTS createModuleProcedure;
// 
CREATE PROCEDURE createModuleProcedure (
    IN _pin varchar(255)
)
MAIN:
BEGIN
	select 'Parameters:',_pin;

	insert into module (
	id, pin,                         displayKey,                  timeZone,                    userId,                       
	updatedBy,                   updated,                     created,                     isActive,                    
	networkCode,                 
	lastOtapAttempt,             lastOtapNotification,        lastReportedSimId,           lastReportedAppVersion,      
	lastStartupMessageReceived,  lastOtapNotificationMessage, lastReportedNetwork,         lastMessageSentToModule,     
	isEncrypted,                 lastCharged,                 otapAttempts,                privateKey,                  
	maxQuietHoursAllowed,        lastMessageReceivedFromModule, lastTimeSettingsAck,       lastReportedSignalStrength,  
	sleepStopMinutes,            lastDischarged,              sleepStartMinutes           
	) values ( 
	null, _pin, _pin, 'UTC', NULL, 
	'elink', now(), now(), 1,
	7,
	null, null, null, null, 
	null, null, null, null, 
	0, null, 0, null, 
	0, null, null, null, 
	0, null, 0
	);

	select LAST_INSERT_ID() as 'moduleId';
END
//

delimiter ;


