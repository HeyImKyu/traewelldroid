import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.mikepenz.aboutlibraries.plugin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"
}

aboutLibraries {
    export.excludeFields.add("generated")
    collect.configPath.set(rootProject.file("config"))
}

android {
    val packageName = "de.hbch.traewelling"
    val name = "2.21.3"
    val code = 204
    compileSdk = 36

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

    defaultConfig {
        applicationId = packageName
        minSdk = 26
        targetSdk = 36
        versionCode = code
        versionName = name

        manifestPlaceholders["appAuthRedirectScheme"] = "app.traewelldroid.de"

        testInstrumentationRunner
    }

    signingConfigs {
        create("release") {
           try {
               val keystorePropertiesFile = rootProject.file("keystore.properties")
               val keystoreProperties = Properties()
               keystoreProperties.load(FileInputStream(keystorePropertiesFile))

               storeFile = file(keystoreProperties.getProperty("storeFile"))
               storePassword = keystoreProperties.getProperty("storePassword")
               keyAlias = keystoreProperties.getProperty("keyAlias")
               keyPassword = keystoreProperties.getProperty("keyPassword")
           } catch (_: Exception) { }
        }
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isMinifyEnabled = false

            buildConfigField("String", "OAUTH_REDIRECT_URL", "\"https://app.traewelldroid.de/oauth2redirect\"")
            buildConfigField("String", "OAUTH_CLIENT_ID", "\"43\"")
            buildConfigField("String", "REPO_URL", "\"https://github.com/Traewelldroid/traewelldroid\"")
            buildConfigField("String", "PRIVACY_URL", "\"https://traewelldroid.de/privacy\"")
            buildConfigField("String", "UNLEASH_URL", "\"https://unleash.traewelldroid.de/api/frontend\"")
            buildConfigField("String", "UNLEASH_KEY", "\"default:production.286fca3aac1497f85ed886b3339c65cdfea9d5f52450524325398461\"")
            buildConfigField("String", "WEBHOOK_URL", "\"https://webhook.traewelldroid.de\"")
            buildConfigField("String", "UP_FCM_PROXY", "\"https://push.traewelldroid.de/FCM\"")
            buildConfigField("Boolean", "ENABLE_ACRA", "true")
            buildConfigField("String", "ACRA_ENDPOINT", "\"https://bugs.traewelldroid.de/report\"")
            buildConfigField("String", "ACRA_USERNAME", "\"59iEVqcvTqy1M9rr\"")
            buildConfigField("String", "ACRA_PASSWORD", "\"tA4VekBGnHuWQuGS\"")
            buildConfigField("String", "ACRA_REPORT_MAIL", "\"bugs@traewelldroid.de\"")

            signingConfig = signingConfigs["release"]
        }
        debug {
            buildConfigField("String", "OAUTH_REDIRECT_URL", "\"https://app.traewelldroid.de/oauth2redirect\"")
            buildConfigField("String", "OAUTH_CLIENT_ID", "\"43\"")
            buildConfigField("String", "REPO_URL", "\"https://github.com/Traewelldroid/traewelldroid\"")
            buildConfigField("String", "PRIVACY_URL", "\"https://traewelldroid.de/privacy\"")
            buildConfigField("String", "UNLEASH_URL", "\"https://unleash.traewelldroid.de/api/frontend\"")
            buildConfigField("String", "UNLEASH_KEY", "\"default:development.52c54a43ebad9b9a668a69410b57cc19e44e19ab1ee40b4dd3f49b38\"")
            buildConfigField("String", "WEBHOOK_URL", "\"https://webhook.traewelldroid.de\"")
            buildConfigField("String", "UP_FCM_PROXY", "\"https://push.traewelldroid.de/FCM\"")
            buildConfigField("Boolean", "ENABLE_ACRA", "false")
            buildConfigField("String", "ACRA_ENDPOINT", "\"https://bugs.traewelldroid.de/report\"")
            buildConfigField("String", "ACRA_USERNAME", "\"59iEVqcvTqy1M9rr\"")
            buildConfigField("String", "ACRA_PASSWORD", "\"tA4VekBGnHuWQuGS\"")
            buildConfigField("String", "ACRA_REPORT_MAIL", "\"bugs@traewelldroid.de\"")
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    namespace = "de.hbch.traewelling"
    flavorDimensions += "libs"
    productFlavors {
        create("play") {
            dimension = "libs"
            versionNameSuffix = "-play"
            apply(plugin = "com.google.gms.google-services")
        }
        create("foss") {
            dimension = "libs"
            // Breaks F-Droid build
            // versionNameSuffix = "-foss"
        }
    }
}

dependencies {
    // Jetpack Compose
    val composeVersion = "1.9.3"
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.foundation:foundation-layout:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("com.google.accompanist:accompanist-themeadapter-material3:0.36.0")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.browser:browser:1.9.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.auth0.android:jwtdecode:2.0.2")
    implementation("net.openid:appauth:0.11.1")
    implementation("org.greenrobot:eventbus:3.3.1")

    // Widgets
    val glanceVersion = "1.1.1"
    implementation("androidx.glance:glance-appwidget:$glanceVersion")
    implementation("androidx.glance:glance-material3:$glanceVersion")

    // OSM integration
    implementation("org.osmdroid:osmdroid-android:6.1.20")

    // Secure Storage
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.github.jcloquell:android-secure-storage:0.1.3")

    // Navigation Component
    implementation("androidx.navigation:navigation-compose:2.9.5")

    // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-gif:2.7.0")

    // Emoji pack support
    implementation("de.c1710:filemojicompat-autoinit:3.3.1")

    // OSS licenses
    implementation("com.mikepenz:aboutlibraries-compose:13.1.0")

    // Feature flags
    implementation("io.getunleash:unleash-android-proxy-sdk:1.0.0")

    // Unified Push
    implementation("com.github.UnifiedPush:android-connector:2.1.1")
    "playImplementation"("com.github.UnifiedPush:android-embedded_fcm_distributor:2.2.0") {
        exclude("com.google.firebase", "firebase-core")
        exclude("com.google.firebase", "firebase-analytics")
        exclude("com.google.firebase", "firebase-measurement-connector")
    }

    // CSV reader
    implementation("com.jsoizo:kotlin-csv-jvm:1.10.0")

    // Reviews
    "playImplementation"("com.google.android.play:review:2.0.2")
    "playImplementation"("com.google.android.play:review-ktx:2.0.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    val acraVersion = "5.13.1"
    "playImplementation"("ch.acra:acra-http:$acraVersion")
    "fossImplementation"("ch.acra:acra-mail:$acraVersion")
}


if (getGradle().startParameter.getTaskNames().stream().anyMatch { f -> f.contains("Play") }) {
    println("Play services will be applied!")
} else {
    println("Play services were not applied!")
}
