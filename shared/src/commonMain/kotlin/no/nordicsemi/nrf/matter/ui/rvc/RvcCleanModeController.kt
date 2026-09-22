package no.nordicsemi.nrf.matter.ui.rvc

import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.nrf.matter.cluster.RvcCleanModeCluster

class RvcCleanModeController(
    cluster: RvcCleanModeCluster,
    scope: CoroutineScope,
) : ModeController(cluster, scope)
