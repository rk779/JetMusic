plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "ml.rk585.jetmusic.core.base"
    compileSdk = 32
    defaultConfig.minSdk = 21

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.core.ktx)

    // Kotlin Coroutines
    api(libs.bundles.kotlinx.coroutines)

    // Youtube Provider
    api(libs.newpipe.extractor)

    // Network
    api(libs.okHttp3.okHttp)
}
