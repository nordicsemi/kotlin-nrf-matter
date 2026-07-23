plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nordic.android.kmp.library)
    alias(libs.plugins.nordic.kotlin)
    alias(libs.plugins.ksp)
}

group = "no.nordicsemi.nrf.matter.core"

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
