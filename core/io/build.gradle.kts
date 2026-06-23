plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "com.companion.lol.core.io"
  compileSdk = libs.versions.compileSdk.get().toInt()
}

dependencies{
  implementation(libs.kotlinx.coroutines)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}
