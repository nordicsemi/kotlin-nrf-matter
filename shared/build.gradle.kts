@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

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

    wasmJs {
        browser()
        binaries.executable()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true

            export(project(":lib"))
//            export("no.nordicsemi.nrf.matter:matter-support:1.0.0")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.viewmodel)
            // no wasmJs artifact, so it can't live in commonMain now that this module also
            // targets wasmJs -- see ui/MatterBlur.kt for the per-platform seam.
            implementation(libs.skydoves.cloudy)
        }
        iosMain.dependencies {
            implementation(libs.skydoves.cloudy)
        }
        commonMain.dependencies {
            api(project(":lib"))
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

            implementation(libs.cmptoast)
            implementation(libs.compottie)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
