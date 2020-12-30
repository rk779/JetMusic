object Dependencies {

    const val COMPOSE_VERSION = "1.0.0-alpha09"

    object Kotlin {

        object Coroutines {
            private const val version = "1.4.2"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        }
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
        const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha05"

        object Compose {
            const val material = "androidx.compose.material:material:$COMPOSE_VERSION"
            const val materialIcons = "androidx.compose.material:material-icons-extended:$COMPOSE_VERSION"
            const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha04"
            const val ui = "androidx.compose.ui:ui:$COMPOSE_VERSION"
            const val uiGraphics = "androidx.compose.ui:ui-graphics:$COMPOSE_VERSION"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$COMPOSE_VERSION"
        }

        object Lifecycle {
            private const val version = "2.3.0-rc01"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.3.0-beta01"
    }

    object ThirdParty {

    }
}