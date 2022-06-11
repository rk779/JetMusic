import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.android.pluginGradle)
        classpath(libs.dagger.hilt.pluginGradle)
        classpath(libs.ksp.pluginGradle)
        classpath(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    }
}

allprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            // Require OptIn
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlin.Experimental",
                "-opt-in=kotlin.RequiresOptIn"
            )

            // Set JVM target to 11
            jvmTarget = JavaVersion.VERSION_11.toString()

            // Set kotlin language version
            languageVersion = "1.6"
        }
    }
}
