package com.todayrecord.convention


import org.gradle.api.JavaVersion

@Suppress("unused")
object TodayRecordConfig {
    const val applicationId = "com.todayrecord.todayrecord"

    const val minSdk = 24
    const val targetSdk = 34
    const val compileSdk = 34
    val javaCompileTarget = JavaVersion.VERSION_17

    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 2

    const val versionName = "$versionMajor.$versionMinor.$versionPatch"
    const val versionCode = versionMajor.times(1000000) + versionMinor.times(1000) + versionPatch
}