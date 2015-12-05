DROP VIEW IF EXISTS admModulesView;

create view admModulesView as 
select 
m.id as moduleId,
m.userId as moduleDefaultUserId,
m.updated as updated,
m.pin as pin,
m.displayKey as displayKey,
m.lastMessageReceivedFromModule as lastMessageReceivedFromModule,
m.lastMessageSentToModule  as lastMessageSentToModule
from module m 
where m.isActive and m.networkCode = 5 and 
m.pin in 
(
select pin from module m join user u on u.id = m.userid where u.handle like 'ADM%' and m.isActive = 1 and u.isActive = 1
)
;

