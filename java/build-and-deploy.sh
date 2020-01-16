#!/bin/bash

echo "Building source"
mvn clean install || exit 1

echo "Packaging application"
cd backend
mvn assembly:single || exit 1

echo "Updating Lambda function code"
file=$(ls target/soccerclub-backend-*-jar-with-dependencies.jar)

[[ -n "${file}" ]] || {
  echo "Did not find jar with dependencies"
  exit 1
}

aws lambda update-function-code --function-name soccerclub --zip-file fileb://${file} --publish
