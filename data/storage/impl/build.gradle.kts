plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.companion.lol.storage.impl"
  compileSdk = libs.versions.compileSdk.get().toInt()
}

dependencies {
  implementation(project(":core:io"))
  implementation(project(":core:model"))
  api(project(":data:storage:sqldelight"))

  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.compiler)

  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.io.sqldelight.sqlite)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
}
