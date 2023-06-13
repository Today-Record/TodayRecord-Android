import com.todayrecord.convention.TodayRecordConfig

plugins {
    id("todayrecord.android.application")
    id("todayrecord.android.firebase")
    id("todayrecord.android.hilt")
}

android {

    defaultConfig {
        namespace = TodayRecordConfig.applicationId
        applicationId = TodayRecordConfig.applicationId
        versionCode = TodayRecordConfig.versionCode
        versionName = TodayRecordConfig.versionName
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

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Firebase
    implementation(libs.firebase.analytics)

    // log tracker
    api(libs.timber)
    // debugImplementation(libs.leakCanary)
}