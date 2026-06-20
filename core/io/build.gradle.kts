plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "com.companion.lol.core.io"
  compileSdk { version = release(36) }
}

dependencies{
  implementation(libs.kotlinx.coroutines)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}