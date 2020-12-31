plugins {
    id("com.android.application")
    kotlin("android")
    `able-plugin`
    `core-library-desugaring`
}

android {
    defaultConfig {
        applicationId = "wtf.rk585.able"
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = "1.4.21"
        kotlinCompilerExtensionVersion = Dependencies.COMPOSE_VERSION
    }
}

dependencies {
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.Compose.material)
    implementation(Dependencies.AndroidX.Compose.materialIcons)
    implementation(Dependencies.AndroidX.Compose.navigation)
    implementation(Dependencies.AndroidX.Compose.ui)
    implementation(Dependencies.AndroidX.Compose.uiGraphics)
    implementation(Dependencies.AndroidX.Compose.uiTooling)
    implementation(Dependencies.AndroidX.Lifecycle.runtimeKtx)
    implementation(Dependencies.Google.material)
    implementation(Dependencies.Kotlin.Coroutines.android)
    implementation(Dependencies.Kotlin.Coroutines.core)
}