delimiter //
DROP PROCEDURE IF EXISTS createUserProcedure;
// 
CREATE PROCEDURE createUserProcedure (
    IN _login varchar(255),
    IN _handle varchar(255),
    IN _tz varchar(255)
)
MAIN:
BEGIN
	select 'Parameters:',_login, _handle, _tz;

	INSERT INTO `user` 
	(`id`,`handle`,`ABOVETARGETMESSAGE`,`HIGHMESSAGE`,`BELOWTARGETMESSAGE`,`externalEmrId`,
	`timeZone`,
	`type`,`password`,`updatedBy`,`faxNumber`,`created`,`updated`,`login`,
	`HIGHBG`,`emergencyNumber`,`callBackNumber`,`TARGETBG`, `ONTARGETMESSAGE`,
	`diagnosisType`,`isActive`,`LOWBG`,`LOWMESSAGE`
        ) VALUES (
	null,
	_handle,NULL,NULL,NULL,NULL,
	_tz,
	1,NULL,'elink',NULL,now(),now(),
	_login,
	0,NULL,NULL,0,NULL,
	1,1,0,NULL
        );

	select LAST_INSERT_ID() as 'userId';
END
//

delimiter ;


