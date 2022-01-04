buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.android.pluginGradle)
        classpath(libs.kotlin.pluginGradle)
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
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlin.Experimental",
                "-Xopt-in=kotlin.RequiresOptIn"
            )

            // Set JVM target to 11
            jvmTarget = JavaVersion.VERSION_11.toString()

            // Set kotlin language version
            languageVersion = "1.6"
        }
    }
}