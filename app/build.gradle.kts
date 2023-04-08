plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile("proguard-rules.pro")
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
    kapt(Libraries.AndroidX.Room.compiler)
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

    /** etc */
    // CoreLibrary Desugar
    coreLibraryDesugaring(Libraries.desugar)
    // log tracker
    api(Libraries.timber)
    // image compressor
    api(Libraries.compressor)

    // Moshi
    implementation(Libraries.Moshi.core)
    kapt(Libraries.Moshi.codegen)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}