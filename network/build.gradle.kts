import java.util.Properties

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "com.companion.lol.network"
  compileSdk { version = release(36) }

  buildFeatures {
    buildConfig = true
  }

  defaultConfig {
    // Load the property
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
      localProperties.load(localPropertiesFile.inputStream())
    }

    val apiKey = localProperties.getProperty("riotApiKey") ?: "\"\""

    // Create the BuildConfig field
    buildConfigField("String", "RIOT_API_KEY", apiKey)
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlinx.serialization.InternalSerializationApi")
    }
}

dependencies {
  implementation(libs.androidx.appcompat)

  implementation(libs.kotlinx.serialization.json)

  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.compiler)

  implementation(libs.io.timber)

  implementation(libs.io.retrofit2)
  implementation(libs.io.retrofit2.logging)
  implementation(libs.io.retrofit2.converter)
  implementation(libs.io.retrofit2.result.adapter)
}
