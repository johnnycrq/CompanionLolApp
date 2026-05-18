plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.companion.lol.network"
  compileSdk { version = release(36) }

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementation(libs.androidx.appcompat)

  implementation(libs.kotlinx.serialization)

  implementation(libs.android.hilt)
  ksp(libs.android.hilt.compiler)

  implementation(libs.io.timber)

  implementation(libs.retrofit.retrofit2)
  implementation(libs.retrofit.logging)
  implementation(libs.retrofit.serializationConverter)
}
