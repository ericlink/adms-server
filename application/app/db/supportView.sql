DROP VIEW IF EXISTS supportView;

CREATE VIEW supportView AS

select 
m.id as moduleId, m.timezone as moduleTz, m.pin, m.displayKey, m.lastMessageReceivedFromModule as lastMessageReceivedFromModule, m.lastMessageSentToModule  as lastMessageSentToModule,m.lastCharged,m.lastDischarged,
u.id as userId, u.timezone as userTz, u.login, u.handle,
(select count(*) from dataPoint dp where dp.userId = u.id and dp.isActive=1) as DataPointCount,
(select max(created) from dataPoint dp where dp.userId = u.id and dp.isActive=1) as LastDataPointCreated,
(select max(timestamp) from dataPoint dp where dp.userId = u.id and dp.isActive=1) as LastDataPointTimestamp


from module m

join user u on u.id = m.userId

where m.isActive = 1

;

