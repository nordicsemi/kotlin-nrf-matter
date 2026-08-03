plugins {
    alias(libs.plugins.nordic.android.library)
}

group = "no.nordicsemi.nrf.matter.android"

android {
    namespace = "no.nordicsemi.nrf.matter.android"

    defaultConfig {
        minSdk = 27
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs/jniLibs")
        }
    }
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.so"))))

    implementation(project(":core"))
    // ChipClient exposes coroutine types (e.g. MutableSharedFlow) in its public API. This was
    // previously provided transitively via the Play Services Home dependency; declare it directly
    // now that the Home API has been removed.
    api(libs.kotlinx.coroutines.android)
}
