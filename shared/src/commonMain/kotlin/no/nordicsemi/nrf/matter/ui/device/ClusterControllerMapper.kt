package no.nordicsemi.nrf.matter.ui.device

import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.nrf.matter.cluster.BasicInfoExtCluster
import no.nordicsemi.nrf.matter.cluster.Cluster
import no.nordicsemi.nrf.matter.cluster.DoorLockCluster
import no.nordicsemi.nrf.matter.cluster.LevelControlCluster
import no.nordicsemi.nrf.matter.cluster.ManufacturerSpecCluster
import no.nordicsemi.nrf.matter.cluster.OnOffCluster
import no.nordicsemi.nrf.matter.ui.infoext.BasicInfoExtController
import no.nordicsemi.nrf.matter.ui.level.LevelControlController
import no.nordicsemi.nrf.matter.ui.light.OnOffController
import no.nordicsemi.nrf.matter.ui.lock.DoorLockController
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecController

fun Cluster.toController(scope: CoroutineScope): ClusterController = when (this) {
    is OnOffCluster -> OnOffController(this, scope)
    is LevelControlCluster -> LevelControlController(this, scope)
    is DoorLockCluster -> DoorLockController(this, scope)
    is BasicInfoExtCluster -> BasicInfoExtController(this, scope)
    is ManufacturerSpecCluster -> ManufacturerSpecController(this, scope)
}
