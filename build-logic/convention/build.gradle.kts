plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("androidLibraryUi") {
            id = "companion.android.library.ui"
            implementationClass = "AndroidLibraryUiConventionPlugin" // Ensure this matches the class name
        }
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.compiler.gradlePlugin)
}
