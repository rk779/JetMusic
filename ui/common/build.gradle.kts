plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "ml.rk585.jetmusic.ui.common"
    compileSdk = 32
    defaultConfig.minSdk = 21

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.get()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Accompanist Libraries
    implementation(libs.accompanist.systemUiController)

    // AndroidX Libraries
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
