package no.nordicsemi.nrf.matter.ui.rvc

import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.nrf.matter.cluster.RvcRunModeCluster

class RvcRunModeController(
    cluster: RvcRunModeCluster,
    scope: CoroutineScope,
) : ModeController(cluster, scope)
