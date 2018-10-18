#!/usr/bin/env bash
./gradlew expansionpanel:clean
./gradlew expansionpanel:assembleDebug
./gradlew expansionpanel:install
./gradlew expansionpanel:bintrayUpload