delimiter //
DROP PROCEDURE IF EXISTS showStatusProcedure;
//
CREATE PROCEDURE showStatusProcedure ()
MAIN:
BEGIN
    select now();
    select max(created) as "Last dataPoint created", timediff(now(),max(created)) from dataPoint;
    select max(lastMessageReceivedFromModule) as "Last message received", timediff(now(),max(lastMessageReceivedFromModule))  from module;
END
//

delimiter ;


