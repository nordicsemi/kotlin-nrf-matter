import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
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
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.koin.android)
            implementation(libs.accompanist.permissions)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.material3)
            implementation(libs.androidx.material)
            implementation(libs.androidx.runtime.livedata)
            // Home API SDK dependency
            implementation(libs.play.services.types)
            implementation(libs.play.services.home)
            // Matter Android Demo SDK
            implementation(libs.matter.android.demo.sdk)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(libs.material.icons.extended)
            implementation(libs.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.datetime)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.androidx.compose)
            // Nav 3
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            // serialization
            implementation(libs.kotlinx.serialization.json)
            // data store
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.datastore)

            // Cloudy to have blur effect.
            implementation(libs.compose.cloudy)
            // Logger
            implementation(libs.napier)
            // QR Code Scanner
            implementation(libs.qr.kit)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "no.nordicsemi.nrf.matter"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "no.nordicsemi.nrf.matter"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            pickFirsts += setOf("lib/**/libc++_shared.so")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

