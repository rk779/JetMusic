plugins {
    id("com.android.library")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ml.rk585.jetmusic.ui.search"
    compileSdk = 32
    defaultConfig.minSdk = 21

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.get()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    ksp {
        arg("compose-destinations.moduleName", "search")
        arg("compose-destinations.mode", "destinations")
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

dependencies {
    // Accompanist libraries
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // AndroidX Libraries
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewModel.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Navigation
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    // Dependency Injection
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Projects
    implementation(projects.core.base)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.ui.common)
}
