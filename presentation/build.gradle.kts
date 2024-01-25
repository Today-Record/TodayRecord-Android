import com.todayrecord.convention.TodayRecordConfig

plugins {
    id("todayrecord.android.library")
    id("todayrecord.android.hilt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")

    id("com.google.devtools.ksp")
}

android {
    defaultConfig {
        namespace = "com.todayrecord.presentation"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("int", "VERSION_CODE", "${TodayRecordConfig.versionCode}")
        buildConfigField("String","VERSION_NAME","\"${TodayRecordConfig.versionName}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))

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

    // Android
    implementation(libs.android.material)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Dagger2 ( DI ) Android Support
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.navigation)

    // In-App-Update
    implementation(libs.playServices.inAppUpdate)

    // Image loading library
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    // Easy Permission
    implementation(libs.easyPermission)
    // log tracker
    api(libs.timber)
    // lottie
    api(libs.lottie)
}