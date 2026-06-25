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
    // Home API SDK dependency
    api(libs.play.services.home)
    api(libs.play.services.types)
}
