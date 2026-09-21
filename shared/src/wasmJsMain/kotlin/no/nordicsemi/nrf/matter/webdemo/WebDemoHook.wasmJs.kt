package no.nordicsemi.nrf.matter.webdemo

internal actual fun notifyWebDemoInteraction(key: String) {
    WebDemoEvents.publish(WebDemoAction.NamedInteraction(key))
}
