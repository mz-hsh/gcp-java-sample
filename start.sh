#!/bin/bash

# metadata
BUCKET=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/BUCKET" -H "Metadata-Flavor: Google")
GIT_REPO=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/GIT_REPO" -H "Metadata-Flavor: Google")

# Install dependencies from apt
apt-get update
apt-get install -t jessie-backports -yq openjdk-8-jdk git

git clone ${GIT_REPO} app
cd app
./gradlew build
mv ./build/libs/*.jar ./app.jar

java -jar ./app.jar
