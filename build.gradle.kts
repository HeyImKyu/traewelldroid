plugins {
    kotlin("android") version "2.2.10" apply false
    id("com.android.application") version "9.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "14.0.0-b02" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
