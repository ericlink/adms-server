delimiter //
DROP PROCEDURE IF EXISTS showDestinationProcedure;
//
CREATE PROCEDURE showDestinationProcedure (
    IN _destination varchar(255)
)
MAIN:
BEGIN
    DECLARE __destinationId int;

	select 'Parameters:',_destination;


    SELECT id INTO __destinationId from destination where address = _destination;
    IF  __destinationId is NULL THEN
        select 'Destination not found for address ' , _destination;
        leave MAIN;
    END IF;

    select * from user where id = (select userId from destination where address = _destination);
    select * from destination where address = _destination;
    select * from profileView where DestinationAddress = _destination;

END
//

delimiter ;


