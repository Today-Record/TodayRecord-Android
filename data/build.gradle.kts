plugins {
    id("todayrecord.android.library")
    id("todayrecord.android.hilt")
    id("com.google.devtools.ksp")
}


android {
    defaultConfig {
        namespace = "com.todayrecord.data"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":domain"))

    // AndroidX Room
    implementation(libs.bundles.androidx.room)
    ksp(libs.androidx.room.compiler)
    // AndroidX DataStore
    implementation(libs.androidx.dataStore)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)

    // Moshi
    implementation(libs.moshi.core)
    ksp(libs.moshi.codegen)
    // log tracker
    api(libs.timber)
    // image compressor
    api(libs.compressor)
}