/*
 * Copyright © 2014-2020 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure root project.
 * Note that classpath dependencies still need to be defined in the `buildscript` block in the top-level build.gradle.kts file.
 */
internal fun Project.configureForRootProject() {
    // register task for cleaning the build directory in the root project
    tasks.register("clean", Delete::class.java) {
        delete(rootProject.buildDir)
    }
    tasks.withType<Wrapper> {
        gradleVersion = "6.8-rc-1"
        distributionType = Wrapper.DistributionType.ALL
        distributionSha256Sum = "89714fb5db6bd66fa5a2302f58d26dc33ecd3db36b24a42d84ff6ba99551eeda"
    }
}

/**
 * Configure all projects including the root project
 */
internal fun Project.configureForAllProjects() {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
            languageVersion = "1.4"
            useIR = true
        }
    }
}

/**
 * Apply configuration options for Android Application projects.
 */
@Suppress("UnstableApiUsage")
internal fun BaseAppModuleExtension.configureAndroidApplicationOptions(project: Project) {
    project.tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi"
            )
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf("proguard-android-optimize.txt", "proguard-rules.pro"))
        }
        named("debug") {
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
        }
    }
}

/**
 * Apply baseline configurations for all Android projects (Application and Library).
 */
@Suppress("UnstableApiUsage")
internal fun TestedExtension.configureCommonAndroidOptions() {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(30)
    }

    sourceSets {
        getByName("debug") {
            java.srcDirs("src/debug/kotlin")
        }
        getByName("main") {
            java.srcDirs("src/main/kotlin")
        }
    }

    packagingOptions {
        exclude("DebugProbesKt.bin")
        exclude("**/*.version")
        exclude("**/*.txt")
        exclude("**/*.kotlin_module")
        exclude("**/plugin.properties")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}