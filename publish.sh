#!/usr/bin/env bash
./gradlew viewanimator:clean
./gradlew viewanimator:assembleDebug
./gradlew viewanimator:install
./gradlew viewanimator:bintrayUpload