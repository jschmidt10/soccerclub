#!/bin/bash

mvn clean install

if [[ $? -ne 0 ]]
then
  echo "Failed during 'mvn clean install'"
  exit 1
fi

cd backend

mvn assembly:single

if [[ $? -ne 0 ]]
then
  echo "Failed during 'mvn assembly:single'"
  exit 1
fi

aws lambda update-function-code --function-name soccerclub --zip-file fileb://target/soccerclub-backend-1.0.0-SNAPSHOT-jar-with-dependencies.jar --publish
