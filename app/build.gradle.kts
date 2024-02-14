import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.todayrecord.convention.TodayRecordConfig

plugins {
    id("todayrecord.android.application")
    id("todayrecord.android.application.compose")
    id("todayrecord.android.firebase")
    id("todayrecord.android.hilt")
}

android {
    val localProperties = gradleLocalProperties(rootDir)

    defaultConfig {
        namespace = TodayRecordConfig.applicationId
        applicationId = TodayRecordConfig.applicationId
        versionCode = TodayRecordConfig.versionCode
        versionName = TodayRecordConfig.versionName
    }

    signingConfigs {
        create("release") {
            storeFile = file("todayrecord.keystore")
            storePassword = localProperties["STORE_PASSWORD"].toString()
            keyAlias = localProperties["KEY_ALIAS"].toString()
            keyPassword = localProperties["KEY_PASSWORD"].toString()
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            manifestPlaceholders["icon_app_launcher"] = "@mipmap/ic_launcher_dev"
            manifestPlaceholders["icon_app_launcher_round"] = "@mipmap/ic_launcher_dev_round"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }

        getByName("release") {
            isMinifyEnabled = false
            manifestPlaceholders["icon_app_launcher"] = "@mipmap/ic_launcher"
            manifestPlaceholders["icon_app_launcher_round"] = "@mipmap/ic_launcher_round"
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile("proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // AndroidX
    implementation(libs.androidx.startup)
    // AndroidX Compose
    implementation(libs.androidx.compose.runtime)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Firebase
    implementation(libs.firebase.analytics)

    // Coil
    implementation(libs.coil.core)

    // log tracker
    api(libs.timber)
    // debugImplementation(libs.leakCanary)
}