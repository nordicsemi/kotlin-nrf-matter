plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nordic.android.kmp.library)
    alias(libs.plugins.nordic.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nordic.publish.kmp)
}

group = "no.nordicsemi"

nordicPublishing {
    POM_ARTIFACT_ID = "core"
    POM_NAME = "Nordic library for Matter connectivitgy."

    POM_DESCRIPTION = "Nordic Matter library"
    POM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
}

kotlin {
    android {
        namespace = "no.nordicsemi.nrf.matter.core"

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Core"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}
