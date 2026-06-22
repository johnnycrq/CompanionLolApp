plugins {
  id("companion.android.library.ui")
}

android {
  namespace = "com.companion.lol.ui.settings"
}

dependencies {
  implementation(project(":core:io"))
}
