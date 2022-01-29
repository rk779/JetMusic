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

    val keystoreConfigFile = rootProject.layout.projectDirectory.file("keystore.properties")
    if (keystoreConfigFile.asFile.exists()) {
        val contents = providers.fileContents(keystoreConfigFile).asText.forUseAtConfigurationTime()
        val keystoreProperties = org.jetbrains.kotlin.konan.properties.Properties()
        keystoreProperties.load(contents.get().byteInputStream())

        signingConfigs {
            register("release") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
        buildTypes.all { signingConfig = signingConfigs.getByName("release") }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifecycle)

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

    debugImplementation(libs.androidx.compose.ui.tooling)
}
