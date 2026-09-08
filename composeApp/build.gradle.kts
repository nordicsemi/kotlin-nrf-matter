@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nordic.android.kmp.library)
    alias(libs.plugins.nordic.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nordic.publish.kmp)
}

apply(plugin = "io.github.ttypic.swiftklib")

extensions.configure<NamedDomainObjectContainer<io.github.ttypic.swiftklib.gradle.SwiftKlibEntry>>("swiftklib") {
    create("iosMatter") {
        path.set(file("../ios-matter/ios-matter"))
        packageName("iosMatter")
        minIos.set(26)
    }
}

group = "no.nordicsemi.nrf.matter"

nordicPublishing {
    POM_ARTIFACT_ID = "matter-support"
    POM_NAME = "Nordic library for Matter connectivity."

    POM_DESCRIPTION = "Nordic Android Matter Library"
    POM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        namespace = "no.nordicsemi.nrf.matter.lib"

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
            baseName = "ComposeApp"
            isStatic = true
        }

        iosTarget.compilations.getByName("main").cinterops.create("iosMatter") {
            val moduleDir = layout.buildDirectory.dir(
                "swiftklib/iosMatter/${iosTarget.name}/swiftBuild/.build/release/iosMatter.build/include"
            )
            extraOpts("-compiler-option", "-I${moduleDir.get().asFile.absolutePath}")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
            // Home API SDK, resolved from the flat `mavenLocal` directory in this repo.
            implementation(libs.play.services.home)
            implementation(libs.play.services.types)
            implementation(libs.androidx.activity.compose)
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
            implementation(libs.androidx.startup)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.ui)

            // Data time
            implementation(libs.kotlinx.datetime)
            // serialization
            implementation(libs.kotlinx.serialization.json)
            // data store
            implementation(libs.androidx.dataStore.preferences)
            implementation(libs.androidx.dataStore.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

androidComponents {
    onVariants { variant ->
        val jniLibs = requireNotNull(variant.sources.jniLibs)
        jniLibs.addStaticSourceDirectory("libs/jniLibs")
    }
}
