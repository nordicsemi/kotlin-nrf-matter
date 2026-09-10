package no.nordicsemi.nrf.matter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import no.nordicsemi.nrf.matter.adapters.IOSLoggerImpl
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.api.initializeLogger
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.nordic.registerNordicClusters
import org.koin.compose.viewmodel.koinViewModel
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {

    NordicMatters.initializeLogger()
    registerNordicClusters()

    initKoin()

    return ComposeUIViewController {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            App(koinViewModel())
        }
    }
}
