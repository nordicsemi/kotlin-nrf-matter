package no.nordicsemi.nrf.matter.hub

import no.nordicsemi.nrf.matter.model.GoogleHub

interface GoogleHubController {

    suspend fun getHubs(): List<GoogleHub>

    suspend fun activateHub(hub: GoogleHub)
}
