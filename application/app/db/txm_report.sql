-- frequent blood glucose testing, Count per day
-- time of day distribution of that testing, count per hour
-- The glucose levels will be analyzed with regard to the magnitude of glucose excursions and fluctuations Standard Dev across all data points
-- and the mean daily glucose value over the 6-month period following the enrollment in the pilot study.  We can do this as average of (average of each day)
-- average number of alert days per participant Count of risk alerts


DROP VIEW IF EXISTS txmView;

create view txmView as 
select 	dp.*,
 	u.handle,
	u.login,
	m.pin
from dataPoint dp
join user u on u.id = dp.userid
join module m on m.id = dp.moduleId
where u.handle like 'TXM%' 
and dp.isControl = 0
and u.handle != 'TXM-001'
;

select  
handle, 
date_format( timestamp, '%Y %c %d %H' ) as yyyymmddhh,
date_format( timestamp, '%Y' ) as yyyy,
date_format( timestamp, '%c' ) as mm,
date_format( timestamp, '%d' ) as dd,
date_format( timestamp, '%H' ) as hh,
count( value ),
avg( value ),
stddev( value ),
min( value ),
max( value ),
abs(max(value)-min(value)),
min( timestamp ),
max( timestamp )
from txmView dp
group by handle, yyyymmddhh;

select  
handle, 
date_format( timestamp, '%Y %c %d' ) as yyyymmdd,
date_format( timestamp, '%Y' ) as yyyy,
date_format( timestamp, '%c' ) as mm,
date_format( timestamp, '%d' ) as dd,
count( value ),
avg( value ),
stddev( value ),
min( value ),
max( value ),
abs(max(value)-min(value)),
min( timestamp ),
max( timestamp )
from txmView dp
group by handle, yyyymmdd;

select  
handle, 
count( value ),
avg( value ),
stddev( value ),
min( value ),
max( value ),
abs(max(value)-min(value)),
min( timestamp ),
max( timestamp )
from txmView dp
group by handle;


SELECT distinct login, fp.name, ufp.totalFired FROM userFeatureProfile ufp 
join featureProfile fp on fp.id = ufp.featureProfileId
join user u on u.id = ufp.userId
where ufp.userId in (select distinct userId from txmView)
and fp.name like '%risk alert%'
and totalFired > 0 
order by login
