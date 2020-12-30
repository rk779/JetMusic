apply(from = "buildDependencies.gradle")
val build: Map<Any, Any> by extra

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

gradlePlugin {
    plugins {
        register("able") {
            id = "able-plugin"
            implementationClass = "AblePlugin"
        }
    }
}

dependencies {
    implementation(build.getValue("kotlinGradlePlugin"))
    implementation(build.getValue("androidGradlePlugin"))
    implementation(build.getValue("androidGradlePlugin_builder"))
    implementation(build.getValue("androidGradlePlugin_builderModel"))
    implementation(build.getValue("androidGradlePlugin_lintModel"))
}