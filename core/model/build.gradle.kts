plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "com.companion.lol.core.model"
  compileSdk = 37
}

dependencies {
  compileOnly(libs.androidx.compose.runtime.annotation)
  implementation(libs.kotlinx.serialization.json)
}
