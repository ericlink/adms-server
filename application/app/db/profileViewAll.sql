DROP VIEW IF EXISTS profileViewAll;

CREATE VIEW profileViewAll AS
SELECT 
imp.name as 'IMP', 
fp.name as 'FeatureProfile',
fps.name as 'FeatureProfileSchedule',
ufps.name as 'UserFeatureProfileSchedule',
u.login as 'Login', 
u.handle as 'Handle', 
u.id as 'UserId',
rap.id as 'RiskAlertProfile',
dodp.id as 'DayOverDayProfile',
fap.id as 'FrequencyAssessmentProfile',
prp.id as 'ParticipationRequestProfile',
rtap.id as 'RealTimeAlertProfile',
mdrp.id as 'MedicalDeviceRegistrationProfile',
arp.id as 'AppointmentReminderProfile',
urap.id as 'UserRiskAlertProfile',
udodp.id as 'UserDayOverDayProfile',
ufap.id as 'UserFrequencyAssessmentProfile',
uprp.id as 'UserParticipationRequestProfile',
urtap.id as 'UserRealTimeAlertProfile',
umdrp.id as 'UserMedicalDeviceRegistrationProfile',
uarp.id as 'UserAppointmentReminderProfile',
ufpdp.id as 'UserFeatureDestinationProfileId',
d.address as 'DestinationAddress',
das.name as 'DestinationAddressSchedule'
FROM intensiveManagementProtocol imp
join featureProfile fp on fp.intensiveManagementProtocolId = imp.id
join userFeatureProfile ufp on ufp.featureProfileId = fp.id
join user u on u.id = ufp.userId
join intensiveManagementProtocol_user imp_user on imp_user.users_id = u.id and imp_user.intensiveManagementProtocols_id = imp.id
left outer join userFeatureProfileDestinationProfile ufpdp on ufpdp.userFeatureProfileId = ufp.id 
left outer join schedule ufps on ufps.id = ufp.scheduleId
left outer join schedule fps on fps.id = fp.defaultScheduleId
left outer join destination d on d.id = ufpdp.destinationId
left outer join riskAlertProfile rap on rap.id = fp.id
left outer join dayOverDayReportProfile dodp on dodp.id = fp.id
left outer join frequencyAssessmentProfile fap on fap.id = fp.id
left outer join participationRequestProfile prp on prp.id = fp.id
left outer join realTimeAlertProfile rtap on rtap.id = fp.id
left outer join medicalDeviceRegistrationProfile mdrp on mdrp.id = fp.id
left outer join appointmentReminderProfile arp on arp.id = fp.id
left outer join userRiskAlertProfile urap on urap.id = ufp.id
left outer join userDayOverDayReportProfile udodp on udodp.id = ufp.id
left outer join userFrequencyAssessmentProfile ufap on ufap.id = ufp.id
left outer join userParticipationRequestProfile uprp on uprp.id = ufp.id
left outer join userRealTimeAlertProfile urtap on urtap.id = ufp.id
left outer join userMedicalDeviceRegistrationProfile umdrp on umdrp.id = ufp.id
left outer join userAppointmentReminderProfile uarp on uarp.id = ufp.id
left outer join schedule das on das.id = d.defaultScheduleId;