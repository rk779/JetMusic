pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(":app")
include(":ui:common:compose")
include(":ui:common:resources")
include(":ui:home")
include(":ui:library")
include(":ui:nowPlaying")
include(":ui:search")
rootProject.name = "Vivace"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")