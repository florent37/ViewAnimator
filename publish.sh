#!/usr/bin/env bash
. ~/.bash_profile
./gradlew viewanimator:clean
./gradlew viewanimator:assembleDebug
./gradlew viewanimator:install
./gradlew viewanimator:bintrayUpload