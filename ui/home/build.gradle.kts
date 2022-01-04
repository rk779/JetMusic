plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig.minSdk = 21

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.ui.common.compose)
}