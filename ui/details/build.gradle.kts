import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("companion.android.library.ui")
}

android {
  namespace = "com.companion.lol.ui.details"
}

dependencies {
  implementation(project(":core:model"))
  implementation(libs.androidx.compose.material.icons.extended)
  testImplementation(libs.junit)
}
