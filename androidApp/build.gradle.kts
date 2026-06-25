plugins {
    alias(libs.plugins.nordic.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "no.nordicsemi.nrf.matter.app"

    defaultConfig {
        minSdk = 27
    }

    defaultConfig {
        applicationId = "no.nordicsemi.nrf.matter"
    }
}

dependencies {
    implementation(project(":androidDeps"))
    implementation(project(":composeApp"))
    implementation(project(":core"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.jetbrains.compose.viewmodel)
    implementation(libs.jetbrains.compose.runtime)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
}
