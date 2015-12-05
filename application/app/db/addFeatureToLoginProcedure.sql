delimiter //
DROP PROCEDURE IF EXISTS addFeatureToLoginProcedure;
// 

CREATE PROCEDURE addFeatureToLoginProcedure (
	IN _login varchar(255), 
	IN _featureName varchar(255),
	IN _featureProfileMinimiumIntervalDays int, -- -1 will default to feature profile value
	IN _featureScheduleName varchar(255)      , -- null will default to feature profile schedule
	IN _appointmentTime varchar(255),           -- only required for appointmentFeatureTypes
	IN _address varchar(255)
)
MAIN:
BEGIN
DECLARE _userId int;
DECLARE _destinationId int;
DECLARE _featureProfileId int;
DECLARE _userFeatureProfileId int;
DECLARE _userFeatureProfileDestinationProfileId int;
DECLARE _featureTypeIndicator int;
DECLARE _scheduleId int;
DECLARE _impId int;
DECLARE _impUserIndicator int;
DECLARE _userFrequencyAssessmentProfileId int;

select 'Parameters:',_login, _featureName,_featureProfileMinimiumIntervalDays,_featureScheduleName, _appointmentTime, _address;

SELECT id INTO _userId from user where login = _login;
IF  _userId is NULL THEN
    select 'User not found for login ', _login;
    leave MAIN;
END IF;

SELECT id INTO _featureProfileId FROM featureProfile where name = _featureName;
IF  _featureProfileId is NULL THEN
    select 'Feature not found for featureName ', _featureName;
    leave MAIN;
END IF;  

SELECT id INTO _destinationId FROM destination where address = _address;
IF  _destinationId is NULL THEN
    select 'Destination not found for address ', _address;
    leave MAIN;
END IF;  

if _featureScheduleName is not null then
	select id into _scheduleId from schedule where name = _featureScheduleName;
	IF  _scheduleId is NULL THEN
	    select 'Schedule not found for ', _featureScheduleName;
	    leave MAIN;
	END IF;  
end if;

select intensiveManagementProtocolId into _impId from featureProfile where id = _featureProfileId;
select count(*) into _impUserIndicator from intensiveManagementProtocol_user 
where intensiveManagementProtocols_id = _impId and users_id = _userId;

select _impUserIndicator;
if _impUserIndicator < 1 then
	insert into intensiveManagementProtocol_user (
	`intensiveManagementProtocols_id`, `users_id`
	) values ( _impId,_userId ); 
end if;


if _scheduleId is null THEN
    select id INTO _userFeatureProfileId
    from userFeatureProfile
    where userId = _userId and featureProfileId = _featureProfileId and scheduleId is null 
    limit 1;
    -- limit 1 to allow for old data before fix where these were duplicated
else
    select id INTO _userFeatureProfileId
    from userFeatureProfile
    where userId = _userId and featureProfileId = _featureProfileId and scheduleId = _scheduleId;
end if;

-- get base userFeatureProfileId and put it into the concrete feature subclass table user<Concrete>Profile
select _userFeatureProfileId;


if _userFeatureProfileId is null THEN
    select 'creating userFeatureProfile';
	insert into userFeatureProfile 
	(
  `id`,
  `DTYPE`,
  `lastCheckToPerformOn`,
  `totalFired`,
  `lastPerformedOn`,
  `updatedBy`,
  `isActive`,
  `updated`,
  `created`,
  `minimumIntervalDays`,
  `lastFired`,
  `userId`,
  `featureProfileId`,
  `scheduleId`
	) values (
	null,
	'UserFeatureProfile',
	null,
	0,
	null,
	'elink',
	1,
	now(),
	now(),
	1,
	null,
	_userId,
        _featureProfileId,
	_scheduleId
	);

	select LAST_INSERT_ID() into _userFeatureProfileId;
	select _userFeatureProfileId;


	select count(*) into _featureTypeIndicator from realTimeAlertProfile where id = _featureProfileId;
	if _featureTypeIndicator > 0 then
	    select 'real time alert profile';
		insert into userRealTimeAlertProfile values (_userFeatureProfileId);
		update userFeatureProfile set DTYPE = 'UserRealTimeAlertProfile', minimumIntervalDays = 0 where id = _userFeatureProfileId;
	else
	 select count(*) into _featureTypeIndicator from dayOverDayReportProfile where id = _featureProfileId;
	 if _featureTypeIndicator > 0 then
	    select 'day over day report profile';
	 	insert into userDayOverDayReportProfile values (_userFeatureProfileId);
		update userFeatureProfile set DTYPE = 'UserDayOverDayReportProfile', minimumIntervalDays = 0 where id = _userFeatureProfileId;
	 else
	  select count(*) into _featureTypeIndicator from 
		frequencyAssessmentProfile where id = _featureProfileId;
 	    if _featureTypeIndicator > 0 then
	 	   select 'frequency assessment profile';
           insert into userFrequencyAssessmentProfile values (_userFeatureProfileId,null,null,0,0,0,-1,-1,-1);
    		update userFeatureProfile set DTYPE = 'UserFrequencyAssessmentProfile', minimumIntervalDays = 1 where id = _userFeatureProfileId;
        else
	     select count(*) into _featureTypeIndicator from 
	     medicalDeviceRegistrationProfile where id = _featureProfileId;
         if _featureTypeIndicator > 0 then
           	select 'medical device registration profile';
            insert into userMedicalDeviceRegistrationProfile values (_userFeatureProfileId);
    		update userFeatureProfile set DTYPE = 'UserMedicalDeviceRegistrationProfile', minimumIntervalDays = 0 where id = _userFeatureProfileId;
         else
	       select count(*) into _featureTypeIndicator from 
           participationRequestProfile where id = _featureProfileId;
           if _featureTypeIndicator > 0 then
           	select 'user participation profile';
            select id into _userFrequencyAssessmentProfileId from userFrequencyAssessmentProfile where userId = _userId;
            insert into userParticipationRequestProfile values (_userFeatureProfileId,null,null,0,0,_userFrequencyAssessmentProfileId);
    		update userFeatureProfile set DTYPE = 'UserParticipationRequestProfile', minimumIntervalDays = 1 where id = _userFeatureProfileId;
           else
	         select count(*) into _featureTypeIndicator from 
             riskAlertProfile  where id = _featureProfileId;
             if _featureTypeIndicator > 0 then
              select 'risk alert profile';
              insert into userRiskAlertProfile  values (_userFeatureProfileId,null);
              update userFeatureProfile set DTYPE = 'UserRiskAlertProfile', minimumIntervalDays = 0 where id = _userFeatureProfileId;
             else
	           select count(*) into _featureTypeIndicator from 
               appointmentReminderProfile  where id = _featureProfileId;
               if _featureTypeIndicator > 0 then
                select 'apoointment reminder profile';
                if _appointmentTime is null then
                    select '_appointmentTime must not be null for feature profile id' + _featureProfileId;
             	    leave MAIN;
                end if;                
                insert into userAppointmentReminderProfile  values (_userFeatureProfileId,_appointmentTime);
                update userFeatureProfile set DTYPE = 'UserAppointmentReminderProfile', minimumIntervalDays = 1 where id = _userFeatureProfileId;
              end if; 
             end if; 
           end if; 
	     end if;
	    end if; 
	  end if; 
	end if; 
END IF; 
    
select id into _userFeatureProfileDestinationProfileId 
from userFeatureProfileDestinationProfile 
where userFeatureProfileId = _userFeatureProfileId and destinationId = _destinationId;

select _userFeatureProfileDestinationProfileId;

IF  _userFeatureProfileDestinationProfileId is null then 
	select 'createUserFeatureProfileDestinationProfile', _userId, _userFeatureProfileId, _destinationId, _address, _featureProfileId;
	insert into userFeatureProfileDestinationProfile (
  `id`,
  `DTYPE`,
  `isActive`,
  `created`,
  `updated`,
  `lastMessageSendAllowedOn`,
  `updatedBy`,
  `userFeatureProfileId`,
  `destinationId`,
  `scheduleId`,
  `thresholdId`
) values (
	null,
	'UserFeatureProfileDestinationProfile',
	1,
	now(),
	now(),
	null,
	'elink',
	_userFeatureProfileId,
	_destinationId,
	null,
	null
	);
   select LAST_INSERT_ID() as _userFeatureProfileDestinationProfileId;
END IF;

select * from profileView where Login = _login;
select FeatureProfile, DestinationAddress, coalesce(UserFeatureProfileSchedule,FeatureProfileSchedule) as Time, coalesce( UserRiskAlertProfile, UserAppointmentReminderProfile, UserMedicalDeviceRegistrationProfile, UserRealTimeAlertProfile, UserParticipationRequestProfile, UserFrequencyAssessmentProfile, UserDayOverDayProfile) as userFeatureProfileId, UserFeatureDestinationProfileId from profileView where login = _login order by FeatureProfile;
select DestinationAddress, FeatureProfile,coalesce(UserFeatureProfileSchedule,FeatureProfileSchedule) as Time, coalesce( UserRiskAlertProfile, UserAppointmentReminderProfile, UserMedicalDeviceRegistrationProfile, UserRealTimeAlertProfile, UserParticipationRequestProfile, UserFrequencyAssessmentProfile, UserDayOverDayProfile) as userFeatureProfileId, UserFeatureDestinationProfileId from profileView where login = _login order by DestinationAddress;


END
//
delimiter ;














