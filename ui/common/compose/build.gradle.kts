plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    api(libs.androidx.compose.activity)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.materialIcons)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling)
    api(libs.google.accompanist.insets)
    api(projects.ui.common.resources)

    implementation(libs.google.accompanist.systemUiController)
}