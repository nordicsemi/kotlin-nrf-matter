plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nordic.android.kmp.library)
    alias(libs.plugins.nordic.kotlin)
    alias(libs.plugins.ksp)
}

group = "no.nordicsemi"

kotlin {
    android {
        namespace = "no.nordicsemi.nrf.matter.shared"

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
        }
        commonMain.dependencies {
//            api(project(":composeApp"))

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.resources)
            api("no.nordicsemi:composeApp:0.1.2")
        }
        commonTest.dependencies {
        }
    }
}
