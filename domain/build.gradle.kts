plugins {
    id("todayrecord.jvm.library")
    id("todayrecord.jvm.hilt")
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Paging
    implementation(libs.androidx.paging.common)
}