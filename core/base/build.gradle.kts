plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig.minSdk = 21

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    api(libs.dagger.dagger)

    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.core)
}
