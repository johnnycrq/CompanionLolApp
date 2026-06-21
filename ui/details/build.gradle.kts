import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "com.companion.lol.ui.details"
  compileSdk { version = release(36) }
}

tasks.withType<KotlinCompile> {
  compilerOptions.freeCompilerArgs.addAll(
    "-XXLanguage:+ExplicitBackingFields"
  )
}

dependencies {
  implementation(project(":core:ui"))
  implementation(project(":core:io"))
  implementation(project(":core:model"))
  implementation(project(":domain"))
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.compose.material.icons.extended)

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.dagger.hilt.android)
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.androidx.compose.ui.tooling.preview)

  ksp(libs.dagger.hilt.compiler)

  testImplementation(libs.junit)
}
