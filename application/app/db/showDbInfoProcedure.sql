delimiter //
DROP PROCEDURE IF EXISTS showDbInfoProcedure;
//
CREATE PROCEDURE showDbInfoProcedure ()
MAIN:
BEGIN
    SELECT routine_name FROM information_schema.routines WHERE routine_schema = DATABASE();

    show tables;

END
//

delimiter ;


