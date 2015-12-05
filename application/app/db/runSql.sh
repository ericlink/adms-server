#!/bin/bash

PASS=
DB=diabetech
echo "Running ddl files"
for i in $(ls ddl.*); 
do 
	echo "running sql file: $i"
	mysql -u root -t $DB < $i; 
done;

echo "Running view files"
for i in $(ls *View.sql); 
do 
	echo "running view file: $i"
	mysql -u root -t $DB < $i; 
done;

echo "Running procedure files"
for i in $(ls *Procedure.sql); 
do 
	echo "running procedure file: $i"
	mysql -u root -t $DB < $i; 
done;

## uses views and procedures
echo "Running dml files"
for i in $(ls dml.*); 
do 
	echo "running dml file: $i"
	mysql -u root -t $DB < $i; 
done;
