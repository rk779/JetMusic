plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 32

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
        jvmTarget = JavaVersion.VERSION_11.toString()
        languageVersion = "1.6"
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
    // Accompanist Libraries
    implementation(libs.bundles.accompanist)

    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.androidx.media)

    // Image Loader
    implementation(libs.bundles.coil)

    // Chucker Debugger
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)

    // Dependency Injection
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Kotlinx libraries
    implementation(libs.bundles.kotlinx)

    // NewPipe Extractor
    implementation(libs.newpipe.extractor)

    // OkHttp
    implementation(libs.okHttp3.okHttp)

    // Logging
    implementation(libs.logcat.lib)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
