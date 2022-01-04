plugins {
    id("com.android.application")
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

    defaultConfig {
        applicationId = "tech.rk585.vivace"
        minSdk = 21
        targetSdk = 31
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
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        // Disable lintVital. Not needed since lint is run on CI
        checkReleaseBuilds = false
        // Ignore any tests
        ignoreTestSources = true
        // Make the build fail on any lint errors
        abortOnError = true
        // Allow lint to check dependencies
        checkDependencies = true
    }

    packagingOptions {
        resources {
            excludes += setOf(
                "**/*.version",
                "**/*.txt",
                "**/*.kotlin_module",
                "**/*.properties",
                "**/plugin.properties",
                "DebugProbesKt.bin",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil.coil)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.okHttp3.okHttp)

    implementation(projects.core.base)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.ui.common.compose)
    implementation(projects.ui.home)
    implementation(projects.ui.library)
    implementation(projects.ui.nowPlaying)
    implementation(projects.ui.search)
}
