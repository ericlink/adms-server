#!/bin/bash
EXPECTED_ARGS=1
E_BADARGS=-1
if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` {testCaseDir}"
  exit $E_BADARGS
fi
cp ./$1/* conf
echo "Testing case $1"
java -classpath . net.diabetech.st.Client

