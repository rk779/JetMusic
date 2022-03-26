dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") {
            mavenContent {
                includeGroup("com.github.TeamNewPipe")
                includeGroup("com.github.TeamNewPipe.NewPipeExtractor")
            }
        }
    }
}

rootProject.name = "JetMusic"
include(":app")
include(":core:base")
include(":core:data")
include(":core:domain")
include(":core:media")
include(":ui:artist")
include(":ui:common")
include(":ui:home")
include(":ui:library")
include(":ui:player")
include(":ui:playlist")
include(":ui:search")
include(":ui:settings")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
