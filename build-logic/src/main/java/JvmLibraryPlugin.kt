import com.todayrecord.convention.TodayRecordConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class JvmLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = TodayRecordConfig.javaCompileTarget
                targetCompatibility = TodayRecordConfig.javaCompileTarget
            }

            tasks.withType<KotlinCompile> {
                with(kotlinOptions) {
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-opt-in=kotlin.RequiresOptIn",
                        // Enable experimental coroutines APIs, including Flow
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-opt-in=kotlinx.coroutines.FlowPreview",
                        "-opt-in=kotlin.Experimental",
                    )

                    jvmTarget = TodayRecordConfig.javaCompileTarget.toString()
                }
            }
        }
    }
}