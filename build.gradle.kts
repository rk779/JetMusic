buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.1.0-beta05")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    configurations.configureEach {
        /**
         * We forcefully exclude AppCompat + MDC from any transitive dependencies.
         * This is a Compose app, so there's no need for these.
         */
        exclude(group = "androidx.appcompat")
        exclude(group = "com.google.android.material", module = "material")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true

            // Enable experimental coroutines APIs, including Flow
            freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.FlowPreview"
            freeCompilerArgs += "-Xopt-in=kotlin.Experimental"

            // Set JVM target to 11
            jvmTarget = JavaVersion.VERSION_11.toString()

            // Set kotlin language version
            languageVersion = "1.6"
        }
    }
}