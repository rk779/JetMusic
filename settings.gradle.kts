dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
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