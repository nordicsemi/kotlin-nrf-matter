plugins {
    alias(libs.plugins.nordic.android.library)
    alias(libs.plugins.nordic.publish.android)
}

group = "no.nordicsemi.nrf.matter.android"

android {
    namespace = "no.nordicsemi.nrf.matter.android"

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("src/main/jniLibs")
        }
    }
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Home API SDK dependency
    api(libs.play.services.home)
    api(libs.play.services.types)
}

nordicPublishing {
    POM_ARTIFACT_ID = "matter-android"
    POM_NAME = "Matter on Android"
    POM_DESCRIPTION = "Nordic library aggregating Matter dependencies for Android."
    POM_URL = "https://github.com/nordicsemi/KMP-nRF-Matter/"
    POM_SCM_URL = "https://github.com/nordicsemi/KMP-nRF-Matter/"
    POM_SCM_CONNECTION = "scm:git@github.com:nordicsemi/KMP-nRF-Matter.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:nordicsemi/KMP-nRF-Matter.git"
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from("Module.md")
    }
}
