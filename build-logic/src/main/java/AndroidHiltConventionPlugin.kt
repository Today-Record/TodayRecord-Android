import com.todayrecord.convention.implementation
import com.todayrecord.convention.kapt
import com.todayrecord.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidHiltConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                implementation(libs.hilt.android)
                kapt(libs.hilt.android.compiler)
            }
        }
    }
}