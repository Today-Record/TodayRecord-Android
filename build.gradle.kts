buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(Libraries.GradlePlugin.android)
        classpath(Libraries.GradlePlugin.kotlin)
        classpath(Libraries.GradlePlugin.daggerHilt)
        classpath(Libraries.GradlePlugin.safeArgs)
        classpath(Libraries.GradlePlugin.googleService)
        classpath(Libraries.GradlePlugin.crashReport)
    }
}

tasks.register("clean") {
    delete(rootProject.buildDir)
}