package no.nordicsemi.nrf.matter.docs

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform