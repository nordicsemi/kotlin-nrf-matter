plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
//    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.nordic.android.application) apply false
    alias(libs.plugins.nordic.android.kmp.library) apply false
    alias(libs.plugins.nordic.android.library) apply false
    alias(libs.plugins.nordic.publish.android) apply false
    alias(libs.plugins.nordic.kotlin) apply false
    alias(libs.plugins.nordic.feature.compose) apply false
    alias(libs.plugins.nordic.publish.kmp) apply false

    alias(libs.plugins.nordic.dokka) apply true
}

dokka {
    pluginsConfiguration.html {
        homepageLink.set("https://github.com/NordicSemiconductor/KMP-nRF-Matter")
    }
}
