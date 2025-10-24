#!/bin/bash

# Skript beenden, wenn ein Befehl fehlschlägt
set -e

# Ordner "release" löschen und neu erstellen
rm -rf release
mkdir release

# FOSS Release APK bauen
./gradlew clean
./gradlew assembleFossRelease
mv app/build/outputs/apk/foss/release/app-foss-release.apk release/

# Play Release APK bauen
./gradlew clean
./gradlew assemblePlayRelease
mv app/build/outputs/apk/play/release/app-play-release.apk release/

# Play Release Bundle (AAB) bauen
./gradlew bundlePlayRelease
mv app/build/outputs/bundle/playRelease/app-play-release.aab release/
