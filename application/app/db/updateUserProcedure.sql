delimiter //
DROP PROCEDURE IF EXISTS updateUserProcedure;
// 

CREATE PROCEDURE updateUserProcedure (
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
select 'Parameters:',_login, _handle, _diagnosisType, _timeZoneId, _pin;
select id into _userId from user where login = _login;
if _userId is null then
        select 'User not found for login ' + _login;
        leave MAIN;
end if;
if _pin is not null then
        select id into _moduleId from module where pin = _pin and isActive = 1;
        if _moduleId is null then
                select 'Module not found for pin ' + _pin;
                leave MAIN;
        end if;
end if;
update user
set
isActive = 1,
handle = _handle,
timezone = _timeZoneId,
diagnosisType = _diagnosisType,
updated = now(),
updatedBy = 'elink'
where id = _userId;
update module
set
timezone = _timeZoneId,
updated = now(),
updatedBy = 'elink'
where userId = _userId;
if _pin is not null then
        update module
        set
        userId = _userId,
        timezone = _timeZoneId,
        updated = now(),
        updatedBy = 'elink'
        where id = _moduleId;
	select pin,userId,timezone from module where pin = _pin;
end if;

select id, login, handle, timezone, diagnosisType, created, updated from user where login = _login;

select userId, id, pin, timezone, created, updated from module where pin = _pin;
END
//



delimiter ;

