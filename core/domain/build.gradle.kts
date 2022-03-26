plugins {
    id("com.android.library")
    kotlin("android")
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

dependencies {

}
