/**
delete from realTimeAlertProfile;
delete from medicalDeviceRegistrationProfile;
delete from riskAlertProfile;
delete from dayOverDayReportProfile;
delete from frequencyAssessmentProfile;
delete from featureProfile;
delete from intensiveManagementProtocol;

call createImpWithStandardFeaturesProcedure('IMP Name');
--call createImpWithStandardFeaturesProcedure('Salford Royal Trust - Pregnancy Pilot');
--call createImpWithStandardFeaturesProcedure('Salford Royal Trust - Youth Pilot');
**/


/**
call createUserProcedure('testlogin3', 'testhandle', 'America/Chicago');
select * from user;
**/

/**
354745031182951 
354745031185301 
354745031185426 
354745031186002 
354745031216437 
354745031216478 
354745031216510 
354745031216551 
354745031232608 
354745031232731 
354745031232848 
354745031232855 
354745031232954 
354745031233002 
354745031233192 
354745031233366 
354745031233440 
354745031233572 
354745031233747 
354745031233796 
354745031216395 
354745031216536 

call deactivateUserProcedure('testlogin3');
call deactivateModuleProcedure('354745031216536');
call createModuleProcedure('y354745031216536');
call updateUserProcedure(  'elink', 'elink',1,'Europe/London','PIN');
call updateUserProcedure(  'elink', 'elink',1,'Europe/London',null);


select * from realTimeAlertProfile;
select * from medicalDeviceRegistrationProfile;
select * from riskAlertProfile;
select * from dayOverDayReportProfile;
select * from frequencyAssessmentProfile;
select * from featureProfile;
select * from intensiveManagementProtocol;

IMP Name - RTA                                   
IMP Name - Medical Device Registration           
IMP Name - Blood Glucose Notification (Immediate)
IMP Name - Blood Glucose Notification (Low)      
IMP Name - Blood Glucose Notification (High)     
IMP Name - Day Over Day Report                   
IMP Name - Quick Tips                            

CREATE PROCEDURE addFeatureToLoginProcedure (
	IN _login varchar(255), 
	IN _featureName varchar(255),
	IN _featureProfileMinimiumIntervalDays int, -- -1 will default to feature profile value
	IN _featureScheduleName varchar(255)      , -- null will default to feature profile schedule
	IN _appointmentTime varchar(255),           -- only required for appointmentFeatureTypes
	IN _address varchar(255)
)

call createDestinationProcedure('elink','dest');
call createDestinationProcedure('elink','dest2');
delete from userFeatureProfileDestinationProfile;
delete from userRealTimeAlertProfile;
delete from userMedicalDeviceRegistrationProfile;
delete from userRiskAlertProfile;
delete from userDayOverDayReportProfile;
delete from userFrequencyAssessmentProfile;
delete from userFeatureProfile;
call addFeatureToLoginProcedure('elink', 'IMP Name - RTA', -1, null, null, 'dest');
call addFeatureToLoginProcedure('elink', 'IMP Name - Medical Device Registration', -1, null, null, 'dest');
call addFeatureToLoginProcedure('elink', 'IMP Name - Blood Glucose Notification (Immediate)', -1, null, null, 'dest');
call addFeatureToLoginProcedure('elink', 'IMP Name - Day Over Day Report', -1, null, null, 'dest');
call addFeatureToLoginProcedure('elink', 'IMP Name - Medical Device Registration', -1, null, null, 'dest2');
call addFeatureToLoginProcedure('elink', 'IMP Name - RTA', -1, null, null, 'dest2');
call addFeatureToLoginProcedure('elink', 'IMP Name - Blood Glucose Notification (Immediate)', -1, null, null, 'dest2');
call addFeatureToLoginProcedure('elink', 'IMP Name - Day Over Day Report', -1, null, null, 'dest2');
select * from userFeatureProfile;
select * from userFeatureProfileDestinationProfile;

**/
