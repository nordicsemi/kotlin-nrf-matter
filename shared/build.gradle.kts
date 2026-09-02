plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nordic.android.kmp.library)
    alias(libs.plugins.nordic.kotlin)
}

group = "no.nordicsemi.nrf.matter"

kotlin {
    android {
        namespace = "no.nordicsemi.nrf.matter.shared"

        minSdk = 27

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

            export(project(":composeApp"))
//            export("no.nordicsemi.nrf.matter:matter-support:1.0.0")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.viewmodel)
        }
        commonMain.dependencies {
            api(project(":composeApp"))
//            api("no.nordicsemi.nrf.matter:matter-support:1.0.0")

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.foundation)
            implementation(libs.jetbrains.icons.extended)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.resources)
            implementation(libs.jetbrains.compose.viewmodel)

            implementation(libs.jetbrains.ui.tooling.preview)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.collections.immutable)

            implementation(libs.jetbrains.navigation)
            implementation(libs.jetbrains.adaptive.navigation)
            implementation(libs.jetbrains.lifecycle.navigation)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.skydoves.cloudy)

            implementation(libs.cmptoast)
            implementation(libs.compottie)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
