package no.nordicsemi.nrf.matter.docs.platform

import kotlinx.browser.window

actual fun openUrl(url: String) {
    window.open(url, "_blank")
}
