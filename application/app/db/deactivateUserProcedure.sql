delimiter //
DROP PROCEDURE IF EXISTS deactivateUserProcedure;
// 
CREATE PROCEDURE deactivateUserProcedure (
    IN _login varchar(255)
)
MAIN:
BEGIN
	select 'Parameters:',_login;

	select * from  `user` 
	where login = _login;

	update  `user` 
        set isActive = 0,
	updated=now(),
	updatedBy='elink'
	where login = _login;
	
	
	select * from  `user` 
	where login = _login;
END
//

delimiter ;


