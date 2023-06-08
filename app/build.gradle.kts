import com.todayrecord.convention.TodayRecordConfig

plugins {
    id("todayrecord.android.application")
    id("todayrecord.android.firebase")
    id("todayrecord.android.hilt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
}

android {

    defaultConfig {
        namespace = TodayRecordConfig.applicationId
        applicationId = TodayRecordConfig.applicationId
        versionCode = TodayRecordConfig.versionCode
        versionName = TodayRecordConfig.versionName
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile("proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashScreen)
    implementation(libs.androidx.constraint)
    implementation(libs.androidx.startup)
    // AndroidX Paging
    implementation(libs.androidx.paging.runtime)
    // AndroidX Lifecycle
    implementation(libs.bundles.androidx.lifecycle)
    // AndroidX Navigation
    implementation(libs.bundles.androidx.navigation)
    // AndroidX Room
    implementation(libs.bundles.androidx.room)
    kapt(libs.androidx.room.compiler)
    // AndroidX DataStore
    implementation(libs.androidx.dataStore)

    // Android
    implementation(libs.android.material)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Dagger2 ( DI ) Android Support
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.navigation)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.storage)

    // In-App-Update
    implementation(libs.playServices.inAppUpdate)

    /** etc */
    // Image loading library
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    // Easy Permission
    implementation(libs.easyPermission)
    // Moshi
    implementation(libs.moshi.core)
    ksp(libs.moshi.codegen)
    // log tracker
    api(libs.timber)
    // image compressor
    api(libs.compressor)
    // lottie
    api(libs.lottie)

    // debugImplementation(libs.leakCanary)
}