plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"
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
    implementation(Libraries.AndroidX.core)
    implementation(Libraries.AndroidX.appcompat)
    implementation(Libraries.AndroidX.design)
    implementation(Libraries.AndroidX.constraint)
    implementation(Libraries.AndroidX.activity)
    implementation(Libraries.AndroidX.fragment)
    implementation(Libraries.AndroidX.startUp)
    // AndroidX Paging
    implementation(Libraries.AndroidX.Paging.runtime)
    // AndroidX Navigation
    implementation(Libraries.AndroidX.Navigation.ui)
    implementation(Libraries.AndroidX.Navigation.fragment)
    // AndroidX Room
    implementation(Libraries.AndroidX.Room.runtime)
    implementation(Libraries.AndroidX.Room.coroutine)
    implementation(Libraries.AndroidX.Room.paging)
    ksp(Libraries.AndroidX.Room.compiler)
    // AndroidX DataStore
    implementation(Libraries.AndroidX.dataStore)

    // Kotlin
    implementation(Libraries.Kotlin.kotlin)
    implementation(Libraries.Kotlin.coroutine)

    // Dagger2 ( DI )
    implementation(Libraries.Dagger.androidHilt)
    kapt(Libraries.Dagger.androidHiltCompiler)
    // Dagger2 ( DI ) Android Support
    implementation(Libraries.AndroidX.Hilt.common)
    implementation(Libraries.AndroidX.Hilt.navigation)
    kapt(Libraries.AndroidX.Hilt.compiler)

    // Firebase
    implementation(platform(Libraries.Firebase.bom))
    implementation(Libraries.Firebase.analytics)
    implementation(Libraries.Firebase.storage)
    implementation(Libraries.Firebase.crashReport)

    // In-App-Update
    implementation(Libraries.PlayServices.inAppUpdate)

    // Splash Screen
    implementation(Libraries.splashScreen)

    /** etc */
    // CoreLibrary Desugar
    coreLibraryDesugaring(Libraries.desugar)

    // Image loading library
    implementation(Libraries.Glide.core)
    ksp(Libraries.Glide.compiler)

    // Easy Permission
    implementation(Libraries.permission)

    // Moshi
    implementation(Libraries.Moshi.core)
    ksp(Libraries.Moshi.codegen)

    // log tracker
    api(Libraries.timber)
    // image compressor
    api(Libraries.compressor)
    // lottie
    api(Libraries.lottie)

    // debugImplementation(Libraries.leakcanary)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}