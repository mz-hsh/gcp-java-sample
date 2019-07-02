#!/bin/bash

# metadata
GIT_REPO=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/GIT_REPO" -H "Metadata-Flavor: Google")
export BUCKET=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/BUCKET" -H "Metadata-Flavor: Google")
export DB_INSTANCE_CONNECTION_NAME=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/DB_INSTANCE_CONNECTION_NAME" -H "Metadata-Flavor: Google")
export DB_DATABASE_NAME=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/DB_DATABASE_NAME" -H "Metadata-Flavor: Google")
export DB_USERNAME=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/DB_USERNAME" -H "Metadata-Flavor: Google")
export SPRING_PROFILES_ACTIVE=production

echo "GIT_REPO: ${GIT_REPO}"
echo "BUCKET: ${BUCKET}"

# Install dependencies from apt
apt-get update
apt-get install -yq openjdk-8-jdk git

if [ -d app ]
then
  rm -rf app
  rm app.jar
fi

if [ -e app.jar ]
then
  rm app.jar
fi

git clone ${GIT_REPO} app
cd app
./gradlew build
mv ./build/libs/*.jar ./app.jar

java -jar ./app.jar --server.port=80
