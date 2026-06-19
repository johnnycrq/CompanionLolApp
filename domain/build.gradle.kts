plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.companion.lol.domain"
    compileSdk { version = release(36) }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":data:network"))
    implementation(project(":data:storage:impl"))
    implementation(libs.io.timber)
    implementation(libs.dagger.hilt.android)

    compileOnly(libs.androidx.compose.runtime.annotation)
    ksp(libs.dagger.hilt.compiler)
}
