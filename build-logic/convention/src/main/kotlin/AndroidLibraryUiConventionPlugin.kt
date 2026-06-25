import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
                }

                buildFeatures {
                    compose = true
                }
            }

            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
                    freeCompilerArgs.addAll(
                        "-XXLanguage:+ExplicitBackingFields"
                    )
                }
            }

            dependencies {
                // Use explicit add to avoid ambiguity in convention plugins
                add("implementation", project(":core:ui"))
                add("implementation", project(":domain"))
                add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
                add("implementation", libs.findLibrary("androidx.compose.ui").get())
                add("implementation", libs.findLibrary("androidx.compose.material3").get())
                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
                add("implementation", libs.findLibrary("kotlinx.coroutines").get())
                add("implementation", libs.findLibrary("dagger.hilt.android").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())

                add("ksp", libs.findLibrary("dagger.hilt.compiler").get())

                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("app-cash-turbine").get())

                add("testImplementation", testFixtures(project(":domain")))
            }
        }
    }
}

private fun VersionCatalog.findVersion(name: String) = findVersion(name)
