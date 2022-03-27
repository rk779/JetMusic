plugins {
    id("com.android.library")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "ml.rk585.jetmusic.core.data"
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
    // AndroidX Paging
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.runtime)

    // Dependency Injection
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(projects.core.base)
}
