plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "com.companion.lol.core.ui"
  compileSdk = 37
}

dependencies {
  implementation(project(":core:model"))
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.adaptive.layout)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)
  implementation(libs.io.coil)
  implementation(libs.io.timber)
  implementation(libs.kotlinx.serialization.json)
  compileOnly(libs.androidx.compose.runtime.annotation)

  testImplementation(libs.junit)

}
