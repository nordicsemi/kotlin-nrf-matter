plugins {
    alias(libs.plugins.nordic.android.library)
    alias(libs.plugins.nordic.publish.android)
}

group = "no.nordicsemi.nrf.matter.android"

nordicPublishing {
    POM_ARTIFACT_ID = "android-deps"
    POM_NAME = "Nordic library for Matter connectivitgy."

    POM_DESCRIPTION = "Nordic Matter library"
    POM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
}

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
