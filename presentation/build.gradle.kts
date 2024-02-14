import com.todayrecord.convention.TodayRecordConfig

plugins {
    id("todayrecord.android.library")
    id("todayrecord.android.library.compose")
    id("todayrecord.android.hilt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    defaultConfig {
        namespace = "com.todayrecord.presentation"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("int", "VERSION_CODE", "${TodayRecordConfig.versionCode}")
        buildConfigField("String", "VERSION_NAME", "\"${TodayRecordConfig.versionName}\"")
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
    implementation(project(":presentation:designsystem"))
    implementation(project(":domain"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashScreen)
    implementation(libs.androidx.constraint)
    implementation(libs.androidx.startup)
    // AndroidX Paging
    implementation(libs.androidx.paging.runtime)
    // AndroidX Paging Compose
    implementation(libs.androidx.paging.compose)
    // AndroidX Lifecycle
    implementation(libs.bundles.androidx.lifecycle)
    // AndroidX Navigation
    implementation(libs.bundles.androidx.navigation)

    // AndroidX Compose
    implementation(libs.androidx.compose.runtime)
    // AndroidX Activity
    implementation(libs.androidx.activity.compose)
    // AndroidX Lifecycle Compose
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Android
    implementation(libs.android.material)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Dagger2 ( DI ) Android Support
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.androidx.hilt.navigation.compose)

    // In-App-Update
    implementation(libs.playServices.inAppUpdate)

    // Image loading library
    implementation(libs.coil.core)
    implementation(libs.coil.compose)

    // Easy Permission
    implementation(libs.easyPermission)

    // lottie
    api(libs.lottie)
    api(libs.lottie.compose)

    // log tracker
    api(libs.timber)
}