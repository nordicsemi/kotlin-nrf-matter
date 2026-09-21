package no.nordicsemi.nrf.matter.webdemo

/**
 * No-op on Android/iOS. On the web docs demo, publishes a named interaction the docs host can
 * reveal a doc snippet for -- for the handful of UI elements that don't already go through
 * `:lib`'s `WebMatterClient` (which publishes its own events for every cluster read/write).
 */
internal expect fun notifyWebDemoInteraction(key: String)
