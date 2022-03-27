plugins {
    id("com.android.library")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ml.rk585.jetmusic.core.domain"
    compileSdk = 32
    defaultConfig.minSdk = 21

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    // AndroidX libraries
    implementation(libs.androidx.paging.common)

    // Dependency Injection
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Projects
    implementation(projects.core.base)
    implementation(projects.core.data)
}
