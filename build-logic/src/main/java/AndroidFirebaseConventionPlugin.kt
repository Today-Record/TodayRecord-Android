import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

internal class AndroidFirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                val bom = libs.findLibrary("firebase.bom").get()
                add("implementation", platform(bom))
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
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
                            the<CrashlyticsExtension>().mappingFileUploadEnabled = true
                        }
                    }
                }
            }
        }
    }
}
