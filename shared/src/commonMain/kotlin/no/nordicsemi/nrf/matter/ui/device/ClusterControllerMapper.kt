package no.nordicsemi.nrf.matter.ui.device

import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.nrf.matter.cluster.Cluster
import no.nordicsemi.nrf.matter.cluster.ContactSensorCluster
import no.nordicsemi.nrf.matter.cluster.DoorLockCluster
import no.nordicsemi.nrf.matter.cluster.LevelControlCluster
import no.nordicsemi.nrf.matter.cluster.OnOffCluster
import no.nordicsemi.nrf.matter.cluster.RvcCleanModeCluster
import no.nordicsemi.nrf.matter.cluster.RvcOperationalStateCluster
import no.nordicsemi.nrf.matter.cluster.RvcRunModeCluster
import no.nordicsemi.nrf.matter.cluster.TemperatureMeasurementCluster
import no.nordicsemi.nrf.matter.nordic.BasicInfoExtCluster
import no.nordicsemi.nrf.matter.nordic.ManufacturerSpecCluster
import no.nordicsemi.nrf.matter.ui.contact.ContactSensorController
import no.nordicsemi.nrf.matter.ui.infoext.BasicInfoExtController
import no.nordicsemi.nrf.matter.ui.level.LevelControlController
import no.nordicsemi.nrf.matter.ui.light.OnOffController
import no.nordicsemi.nrf.matter.ui.lock.DoorLockController
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecController
import no.nordicsemi.nrf.matter.ui.rvc.RvcCleanModeController
import no.nordicsemi.nrf.matter.ui.rvc.RvcOperationalStateController
import no.nordicsemi.nrf.matter.ui.rvc.RvcRunModeController
import no.nordicsemi.nrf.matter.ui.temperature.TemperatureSensorController

fun Cluster.toController(scope: CoroutineScope): ClusterController? = when (this) {
    is OnOffCluster -> OnOffController(this, scope)
    is LevelControlCluster -> LevelControlController(this, scope)
    is DoorLockCluster -> DoorLockController(this, scope)
    is BasicInfoExtCluster -> BasicInfoExtController(this, scope)
    is ManufacturerSpecCluster -> ManufacturerSpecController(this, scope)
    is ContactSensorCluster -> ContactSensorController(this, scope)
    is TemperatureMeasurementCluster -> TemperatureSensorController(this, scope)
    is RvcRunModeCluster -> RvcRunModeController(this, scope)
    is RvcCleanModeCluster -> RvcCleanModeController(this, scope)
    is RvcOperationalStateCluster -> RvcOperationalStateController(this, scope)
    else -> null
}
