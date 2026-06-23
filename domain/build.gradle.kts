plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.companion.lol.domain"
    compileSdk = 37
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":data:network"))
    implementation(project(":data:storage:impl"))
    implementation(libs.io.timber)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    compileOnly(libs.androidx.compose.runtime.annotation)
}
