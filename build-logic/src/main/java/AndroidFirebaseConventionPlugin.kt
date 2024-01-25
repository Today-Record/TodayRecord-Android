import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.todayrecord.convention.implementation
import com.todayrecord.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

internal class AndroidFirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                implementation(platform(libs.firebase.bom))
                implementation(libs.firebase.crashlytics)
            }

            extensions.configure<ApplicationAndroidComponentsExtension> {
                finalizeDsl {
                    it.buildTypes {
                        getByName("debug") {
                            manifestPlaceholders["crashlyticsEnabled"] = false
                            the<CrashlyticsExtension>().mappingFileUploadEnabled = false
                        }
                        getByName("release") {
                            manifestPlaceholders["crashlyticsEnabled"] = true
                            the<CrashlyticsExtension>().mappingFileUploadEnabled = false
                        }
                    }
                }
            }
        }
    }
}
