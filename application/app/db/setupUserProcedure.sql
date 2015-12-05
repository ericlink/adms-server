delimiter //
DROP PROCEDURE IF EXISTS setupUserProcedure;
// 

-- assumes new clean module and user shell record
CREATE PROCEDURE setupUserProcedure (
in _login varchar(255),
in _handle varchar(255),
in _diagnosisType INT,
in _timeZoneId varchar(255),
in _pin varchar(255)
)
MAIN:
BEGIN
DECLARE _userId int;
DECLARE _moduleId int;
DECLARE _existingDataPoints int;

-- log
select 'Parameters:',_login, _handle, _diagnosisType, _timeZoneId, _pin;

-- get user id
select userId into _userId from module where pin = _pin and isActive = 1;
if _userId is null then
	select 'User not found for module ' + _pin;
	leave MAIN;
end if;

--   select count(*) into _existingDataPoints from dataPoint where userId = _userId;
--   if _existingDataPoints > 0 then
	--   select 'User has existing data points, can not setup for someone for userId ' + _userId;
	--   leave MAIN;
--   end if;

-- update user 
update user 
set 
isActive = 1,
handle = _handle,
login = _login,
timezone = _timeZoneId, 
diagnosisType = _diagnosisType,
updated = now(), 
updatedBy = 'elink' 
where id = _userId;

-- update module timezone to user time zone
update module 
set 
timezone = _timeZoneId, 
updated = now(), 
updatedBy = 'elink' 
where userId = _userId;

END
//



delimiter ;

