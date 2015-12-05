delimiter //
DROP PROCEDURE IF EXISTS deactivateModuleProcedure;
// 
CREATE PROCEDURE deactivateModuleProcedure (
    IN _pin varchar(255)
)
MAIN:
BEGIN
	select 'Parameters:',_pin;

	select * from  `module` 
	where pin = _pin;

	update  `module` 
        set isActive = 1,
	timezone="UTC",
	userId=null,
	updated=now(),
	updatedBy='elink'
	where pin = _pin;
	
	
	select * from  `module` 
	where pin = _pin;
END
//

delimiter ;


