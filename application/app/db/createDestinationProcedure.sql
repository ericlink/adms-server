delimiter //
DROP PROCEDURE IF EXISTS createDestinationProcedure;
// 
CREATE PROCEDURE createDestinationProcedure (
    IN _login varchar(255),
    IN _address varchar(255)
)
MAIN:
BEGIN
DECLARE _destinationId int;
DECLARE _userId int;

select 'Parameters:',_login, _address;

SELECT id INTO _userId from user where login = _login;
IF  _userId is NULL THEN
    select 'User not found for login ' , _login;
    leave MAIN;
END IF;
  
SELECT d.id INTO _destinationId 
FROM destination d
where d.address = _address;

IF  _destinationId is NULL THEN
	insert into destination ( 
  `id`,
  `type`,
  `address`,
  `isActive`,
  `totalMessagesSent`,
  `created`,
  `updated`,
  `updatedBy`,
  `userId`
 ) values (
   null,0,_address,1,0,now(),now(),'elink',_userId
	);
ELSE
    select 'Destination already exists:';
END IF;

SELECT * FROM destination where address = _address;

END
//

delimiter ;


