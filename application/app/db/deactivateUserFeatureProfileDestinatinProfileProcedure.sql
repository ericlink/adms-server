delimiter //
DROP PROCEDURE IF EXISTS deactivateUserFeatureProfileDestinatinProfileProcedure;
//

-- turn off feature for someone
-- select FeatureProfile, DestinationAddress, coalesce(UserFeatureProfileSchedule,FeatureProfileSchedule) as Time, coalesce( UserRiskAlertProfile, UserAppointmentReminderProfile, UserMedicalDeviceRegistrationProfile, UserRealTimeAlertProfile, UserParticipationRequestProfile, UserFrequencyAssessmentProfile, UserDayOverDayProfile) as userFeatureProfileId, UserFeatureDestinationProfileId from profileView where login = 'USERLOGIN';
-- update userFeatureProfile set isActive=0,updated=now(),updatedBy='elink' where isActive=1 and id in ();
-- update userFeatureProfileDestinationProfile set isActive=0,updated=now(),updatedBy='elink' where isActive=1 and id in ();

CREATE PROCEDURE deactivateUserFeatureProfileDestinatinProfileProcedure (
	IN _login varchar(255),
	IN _featureName varchar(255),
	IN _featureScheduleName varchar(255), 
	IN _address varchar(255)
)
MAIN:
BEGIN
DECLARE _userId int;
DECLARE _destinationId int;
DECLARE _destinationUserId int;
DECLARE _featureProfileId int;
DECLARE _userFeatureProfileId int;
DECLARE _userFeatureProfileDestinationProfileId int;
DECLARE _featureTypeIndicator int;
DECLARE _scheduleId int;
DECLARE _impId int;
DECLARE _impUserIndicator int;
DECLARE _userFrequencyAssessmentProfileId int;

select 'Parameters:',_login, _featureName, _featureScheduleName, _address;

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

SELECT userId INTO _destinationUserId FROM destination where address = _address;
IF  _destinationUserId is NULL THEN
    select 'Destination user id not found for address ', _address;
    leave MAIN;
END IF;

if _featureScheduleName is not null then
	select id into _scheduleId from schedule where name = _featureScheduleName;
	IF  _scheduleId is NULL THEN
	    select 'Schedule not found for ', _featureScheduleName;
	    leave MAIN;
	END IF;
end if;

select  _userId, _destinationUserId, _featureProfileId, _destinationId, _scheduleId;

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

select _userFeatureProfileId;

IF  _userFeatureProfileId is NULL THEN
    select 'userFeatureProfileId not found';
    leave MAIN;
END IF;

-- turn off the feature as a whole, not per dest
-- if _userFeatureProfileId is not null THEN
--    select 'updating userFeatureProfile';
--	update userFeatureProfile
--    where userId = _userId and featureProfileId = _featureProfileId and scheduleId = _scheduleId;
-- end if
-- then turn off all dest...

select id into _userFeatureProfileDestinationProfileId
from userFeatureProfileDestinationProfile
where userFeatureProfileId = _userFeatureProfileId and destinationId = _destinationId;

select _userFeatureProfileDestinationProfileId;

IF  _userFeatureProfileDestinationProfileId is not null then
   update userFeatureProfileDestinationProfile
   set isActive = 0,
   updated = now(),
   updatedBy = 'elink'
   where userFeatureProfileId = _userFeatureProfileId and destinationId = _destinationId;
END IF;

select * from profileView where Login = _login;
select FeatureProfile, DestinationAddress, coalesce(UserFeatureProfileSchedule,FeatureProfileSchedule) as Time, coalesce( UserRiskAlertProfile, UserAppointmentReminderProfile, UserMedicalDeviceRegistrationProfile, UserRealTimeAlertProfile, UserParticipationRequestProfile, UserFrequencyAssessmentProfile, UserDayOverDayProfile) as userFeatureProfileId, UserFeatureDestinationProfileId from profileView where login = _login order by FeatureProfile;
select DestinationAddress, FeatureProfile,coalesce(UserFeatureProfileSchedule,FeatureProfileSchedule) as Time, coalesce( UserRiskAlertProfile, UserAppointmentReminderProfile, UserMedicalDeviceRegistrationProfile, UserRealTimeAlertProfile, UserParticipationRequestProfile, UserFrequencyAssessmentProfile, UserDayOverDayProfile) as userFeatureProfileId, UserFeatureDestinationProfileId from profileView where login = _login order by DestinationAddress;


END
//
delimiter ;














