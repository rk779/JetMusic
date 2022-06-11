plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    kotlin("android")
}

android {
    namespace = "ml.rk585.jetmusic.ui.home"
    compileSdk = 32
    defaultConfig.minSdk = 21

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.get()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    libraryVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/${name}/kotlin")
            }
        }
    }

    ksp {
        arg("compose-destinations.moduleName", "home")
        arg("compose-destinations.mode", "destinations")
    }
}

dependencies {
    // AndroidX Libraries
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Navigation
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    // Projects
    implementation(projects.ui.common)
}
