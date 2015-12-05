delimiter //
DROP PROCEDURE IF EXISTS createImpWithStandardFeaturesProcedure;
// 
CREATE PROCEDURE createImpWithStandardFeaturesProcedure (IN _impName varchar(255))
MAIN:
BEGIN
	select  _impName;
	INSERT INTO intensiveManagementProtocol 
		(`id`,`isActive`,`created`,`updated`,`name`,`updatedBy`) VALUES 
		(null, 1,         now(),    now(),   _impName, 'elink' );
	select LAST_INSERT_ID() into @impId;
	select @impId;
	select * from intensiveManagementProtocol;

	IF  @impId is NULL THEN
	    select 'ERROR: could not create IMP';
	    leave main;
	END IF;

	/** RTA **/
	select concat( _impName, ' - RTA') into @featureName;
	select 'RealTimeAlertProfile' into @featureDType;
	/** inserts **/
	INSERT INTO featureProfile (`id`,`DTYPE`,
	`isOnNewMedicalDeviceEvent`, `isOnNewDataPointEvent`, `isSchedulable`,
	`updatedBy`,`isActive`,`created`,`updated`,`name`,
	`minimumIntervalDays`,`intensiveManagementProtocolId`,`defaultScheduleId`) VALUES 
	 (null,@featureDType,
	 0, 1, 0,
	 'elink',1,now(),now(),@featureName,
	0,@impId,null);
	INSERT INTO realTimeAlertProfile (`id`) VALUES (LAST_INSERT_ID());
	/** check **/
	select * from featureProfile where name = @featureName;
	select * from realTimeAlertProfile where id = LAST_INSERT_ID();


	/** Medical Device Registration **/
	select concat( _impName, ' - Medical Device Registration') into @featureName;
	select 'MedicalDeviceRegistrationProfile' into @featureDType;
	/** inserts **/
	INSERT INTO featureProfile (`id`,`DTYPE`,
	`isOnNewMedicalDeviceEvent`, `isOnNewDataPointEvent`, `isSchedulable`,
	`updatedBy`,`isActive`,`created`,`updated`,`name`,
	`minimumIntervalDays`,`intensiveManagementProtocolId`,`defaultScheduleId`) VALUES 
	 (null,@featureDType,
	 1, 0, 0,
	 'elink',1,now(),now(),@featureName,
	0,@impId,null);
	INSERT INTO medicalDeviceRegistrationProfile (`id`) VALUES (LAST_INSERT_ID());
	/** check **/
	select * from featureProfile where name = @featureName;
	select * from medicalDeviceRegistrationProfile where id = LAST_INSERT_ID();


	
	/** BGN Reference
	+----+--------+----------------+-----------------+-------+
	| id | result | comparisonType | comparisonValue | hours |
	+----+--------+----------------+-----------------+-------+
	| 11 |   3.00 |              0 |          225.00 |    36 | 
	| 12 |   3.00 |              1 |           70.00 |    36 | 
	| 14 |   4.00 |              0 |          200.00 |    96 | 
	| 15 |   4.00 |              1 |           60.00 |    96 | 
	| 59 |   4.00 |              0 |          200.00 |    96 | 
	| 60 |   4.00 |              1 |           60.00 |    96 | 
	+----+--------+----------------+-----------------+-------+
	**/

	/** BGN Immediate **/
	select concat( _impName, ' - Blood Glucose Notification (Immediate)') into @featureName;
	select 'RiskAlertProfile' into @featureDType;
	/** inserts **/
	INSERT INTO featureProfile (`id`,`DTYPE`,
	`isOnNewMedicalDeviceEvent`, `isOnNewDataPointEvent`, `isSchedulable`,
	`updatedBy`,`isActive`,`created`,`updated`,`name`,
	`minimumIntervalDays`,`intensiveManagementProtocolId`,`defaultScheduleId`) VALUES 
	 (null,@featureDType,
	 0, 1, 0,
	 'elink',1,now(),now(),@featureName,
	0,@impId,null);
	INSERT INTO riskAlertProfile (`id`, `result`, `comparisonType`, `hours`, `comparisonValue`) 
	          VALUES (LAST_INSERT_ID(), '1',         0,                48,       '0'
	);
	/** check **/
	select * from featureProfile where name = @featureName;
	select * from riskAlertProfile where id = LAST_INSERT_ID();

	/** BGN Low **/
	select concat( _impName, ' - Blood Glucose Notification (Low)') into @featureName;
	select 'RiskAlertProfile' into @featureDType;
	/** inserts **/
	INSERT INTO featureProfile (`id`,`DTYPE`,
	`isOnNewMedicalDeviceEvent`, `isOnNewDataPointEvent`, `isSchedulable`,
	`updatedBy`,`isActive`,`created`,`updated`,`name`,
	`minimumIntervalDays`,`intensiveManagementProtocolId`,`defaultScheduleId`) VALUES 
	 (null,@featureDType,
	 0, 1, 0,
	 'elink',1,now(),now(),@featureName,
	0,@impId,null);
	INSERT INTO riskAlertProfile (`id`, `result`, `comparisonType`, `hours`, `comparisonValue`) 
	          VALUES (LAST_INSERT_ID(), '3',         1,                36,       '70'
	);
	/** check **/
	select * from featureProfile where name = @featureName;
	select * from riskAlertProfile where id = LAST_INSERT_ID();

	/** BGN High **/
	select concat( _impName, ' - Blood Glucose Notification (High)') into @featureName;
	select 'RiskAlertProfile' into @featureDType;
	/** inserts **/
	INSERT INTO featureProfile (`id`,`DTYPE`,
	`isOnNewMedicalDeviceEvent`, `isOnNewDataPointEvent`, `isSchedulable`,
	`updatedBy`,`isActive`,`created`,`updated`,`name`,
	`minimumIntervalDays`,`intensiveManagementProtocolId`,`defaultScheduleId`) VALUES 
	 (null,@featureDType,
	 0, 1, 0,
	 'elink',1,now(),now(),@featureName,
	0,@impId,null);
	INSERT INTO riskAlertProfile (`id`, `result`, `comparisonType`, `hours`, `comparisonValue`) 
	          VALUES (LAST_INSERT_ID(), '3',         0,                36,       '225.0'
	);
	/** check **/
	select * from featureProfile where name = @featureName;
	select * from riskAlertProfile where id = LAST_INSERT_ID();

	/** DOD **/
	select concat( _impName, ' - Day Over Day Report' ) into @featureName;
	select 'DayOverDayReportProfile' into @featureDType;
	/** inserts **/
	INSERT INTO featureProfile (`id`,`DTYPE`,
	`isOnNewMedicalDeviceEvent`, `isOnNewDataPointEvent`, `isSchedulable`,
	`updatedBy`,`isActive`,`created`,`updated`,`name`,
	`minimumIntervalDays`,`intensiveManagementProtocolId`,`defaultScheduleId`) VALUES 
	 (null,@featureDType,
	 0, 0, 1,
	 'elink',1,now(),now(),@featureName,
	0,@impId,null);
	INSERT INTO dayOverDayReportProfile(`id`) 
	          VALUES (LAST_INSERT_ID()
	);
	/** check **/
	select * from featureProfile where name = @featureName;
	select * from dayOverDayReportProfile where id = LAST_INSERT_ID();

	/** Frequency Assessment / Quicktip 
	+----+---------------------------------+--------------+-------------+
	| id | minimumDataPointsPerRequiredDay | requiredDays | daysToCheck |
	+----+---------------------------------+--------------+-------------+
	|  9 |                               4 |            2 |           2 | 
	| 10 |                               1 |            5 |           7 | 
	| 58 |                               1 |            5 |           7 | 
	+----+---------------------------------+--------------+-------------+
	**/

	select concat( _impName, ' - Quick Tips' ) into @featureName;
	select 'FrequencyAssessmentProfile' into @featureDType;
	/** inserts **/
	INSERT INTO featureProfile (`id`,`DTYPE`,
	`isOnNewMedicalDeviceEvent`, `isOnNewDataPointEvent`, `isSchedulable`,
	`updatedBy`,`isActive`,`created`,`updated`,`name`,
	`minimumIntervalDays`,`intensiveManagementProtocolId`,`defaultScheduleId`) VALUES 
	 (null,@featureDType,
	 0, 0, 1,
	 'elink',1,now(),now(),@featureName,
	0,@impId,null);
	INSERT INTO frequencyAssessmentProfile(`id`,`minimumDataPointsPerRequiredDay`,`requiredDays`,`daysToCheck`) 
	          VALUES (LAST_INSERT_ID(), 1, 5, 7
	);
	/** check **/
	select * from featureProfile where name = @featureName;
	select * from frequencyAssessmentProfile where id = LAST_INSERT_ID();

	/** Participation Request Profile **/
	/** Appointment Reminder Profile **/

	/** Summary **/
	select '**** Summary ****' as '*****************';
	select name from intensiveManagementProtocol;
	select fp.name, sp.* from realTimeAlertProfile sp join featureProfile fp on fp.id = sp.id where fp.intensiveManagementProtocolId = @impId;
	select fp.name, sp.* from medicalDeviceRegistrationProfile sp join featureProfile fp on fp.id = sp.id where fp.intensiveManagementProtocolId = @impId;
	select fp.name, sp.* from dayOverDayReportProfile sp join featureProfile fp on fp.id = sp.id where fp.intensiveManagementProtocolId = @impId;
	select fp.name, sp.* from riskAlertProfile sp join featureProfile fp on fp.id = sp.id where fp.intensiveManagementProtocolId = @impId;
	select fp.name, sp.* from frequencyAssessmentProfile sp join featureProfile fp on fp.id = sp.id where fp.intensiveManagementProtocolId = @impId;

END
//
delimiter ;
