plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
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
    implementation(libs.coil.coil)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.extension.mediasession)
    implementation(libs.exoplayer.extension.okHttp)
    implementation(libs.exoplayer.ui)

    implementation(projects.core.data)
    implementation(projects.ui.common.resources)
}
