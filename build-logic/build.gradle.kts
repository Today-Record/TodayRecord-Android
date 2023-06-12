plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradle)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "todayrecord.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "todayrecord.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidHilt") {
            id = "todayrecord.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidFirebase") {
            id = "todayrecord.android.firebase"
            implementationClass = "AndroidFirebaseConventionPlugin"
        }

        register("jvmLibrary") {
            id = "todayrecord.jvm.library"
            implementationClass = "JvmLibraryPlugin"
        }

        register("jvmHilt") {
            id = "todayrecord.jvm.hilt"
            implementationClass = "JvmHiltConventionPlugin"
        }
    }
}