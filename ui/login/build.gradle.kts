plugins {
  id("companion.android.library.ui")
}

android {
  namespace = "com.companion.lol.ui.login"
}

dependencies {
  implementation(project(":core:io"))
}
