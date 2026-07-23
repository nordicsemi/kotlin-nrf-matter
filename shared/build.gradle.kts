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
            // Re-export composeApp's own API so symbols like MainViewControllerKt are visible
            // from Swift via `import shared`. Do NOT use transitiveExport: it would also export
            // transitive deps such as cmptoast, whose own MainViewController.kt collides with
            // composeApp's and forces MainViewController() into a "MainViewControllerKt_" class.
            export("no.nordicsemi:composeApp:0.1.2")
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
