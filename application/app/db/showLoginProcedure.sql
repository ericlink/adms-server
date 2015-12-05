delimiter //
DROP PROCEDURE IF EXISTS showLoginProcedure;
//
CREATE PROCEDURE showLoginProcedure (
    IN _login varchar(255)
)
MAIN:
BEGIN

    DECLARE _userId int;

    SELECT id INTO _userId from user where login = _login;
    IF  _userId is NULL THEN
        select 'User not found for login ' , _login;
        leave MAIN;
    END IF;

	select 'Parameters:',_login;

	select * from  `user`
	where login = _login;

    select * from supportView
	where login = _login;

    select FeatureProfile, DestinationAddress, coalesce(UserFeatureProfileSchedule,FeatureProfileSchedule) as Time, coalesce( UserRiskAlertProfile, UserAppointmentReminderProfile, UserMedicalDeviceRegistrationProfile, UserRealTimeAlertProfile, UserParticipationRequestProfile, UserFrequencyAssessmentProfile, UserDayOverDayProfile) as userFeatureProfileId, UserFeatureDestinationProfileId from profileView where login = _login order by FeatureProfile;
    select DestinationAddress, FeatureProfile,coalesce(UserFeatureProfileSchedule,FeatureProfileSchedule) as Time, coalesce( UserRiskAlertProfile, UserAppointmentReminderProfile, UserMedicalDeviceRegistrationProfile, UserRealTimeAlertProfile, UserParticipationRequestProfile, UserFrequencyAssessmentProfile, UserDayOverDayProfile) as userFeatureProfileId, UserFeatureDestinationProfileId from profileView where login = _login order by DestinationAddress;

    select * from medicalDevice where userId = _userId;

    select * from module where userId = _userId;

END
//

delimiter ;


