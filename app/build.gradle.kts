plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}
android {

    defaultConfig {
        namespace = Config.applicationId
        applicationId = Config.applicationId
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        compileSdk = Config.compileSdk
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile("proguard-rules.pro")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = Config.javaCompileTarget
        targetCompatibility = Config.javaCompileTarget
    }
    kotlinOptions {
        jvmTarget = Config.javaCompileTarget.toString()
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
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Dagger2 ( DI )
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    // Dagger2 ( DI ) Android Support
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.navigation)
    kapt(libs.androidx.hilt.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)

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