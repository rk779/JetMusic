plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 32
    namespace = "ml.rk585.jetmusic"

    defaultConfig {
        applicationId = "ml.rk585.jetmusic"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {
        val versionName = this.versionName
        outputs.all {
            if (name.contains("release")) {
                val output = (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl)
                output.outputFileName = "JetMusic-$versionName.apk"
            }
        }
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.get()

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        disable.add("UnsafeOptInUsageError")
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    // Android Desugaring
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // AndroidX Libraries
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.androidx.media)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Image Loader
    implementation(libs.coil)

    // Chucker Debugger
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)

    // Compose Destinations
    implementation(libs.compose.destinations.core)

    // Dependency Injection
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Projects
    implementation(projects.core.base)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.media)
    implementation(projects.ui.artist)
    implementation(projects.ui.common)
    implementation(projects.ui.home)
    implementation(projects.ui.library)
    implementation(projects.ui.player)
    implementation(projects.ui.playlist)
    implementation(projects.ui.search)
    implementation(projects.ui.settings)
}
