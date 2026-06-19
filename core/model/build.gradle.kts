plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "com.companion.lol.core.model"
  compileSdk { version = release(36) }
}

dependencies {
  compileOnly(libs.androidx.compose.runtime.annotation)
}
