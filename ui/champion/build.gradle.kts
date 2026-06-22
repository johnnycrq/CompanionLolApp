plugins {
  id("companion.android.library.ui")
}

android {
  namespace = "com.companion.lol.ui.champion"
}

dependencies {
  implementation(project(":core:model"))
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.io.lottie)
}
