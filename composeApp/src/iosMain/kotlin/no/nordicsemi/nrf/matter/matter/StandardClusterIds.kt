package no.nordicsemi.nrf.matter.matter

/**
 * Standard Matter cluster/attribute/command IDs (from the Matter spec, matching
 * `Matter.framework`'s `MTRClusterIDType`/`MTRAttributeIDType`/`MTRCommandIDType` enums).
 *
 * These are used with `MTRBaseDevice`'s generic read/write/subscribe/invoke APIs directly,
 * rather than the typed `MTRBaseCluster*` wrapper classes: Kotlin/Native's cinterop does not
 * expose those wrapper classes' `initWithDevice:endpointID:queue:` initializers (declared in an
 * Objective-C `(Availability)` category on each class) as usable constructors. This mirrors the
 * approach the original Swift `LocalMatterClusterExtController`/`LocalMatterCustomClusterController`
 * already used for vendor-specific clusters.
 */
internal object StandardClusterIds {
    object OnOff {
        const val CLUSTER = 0x0006
        const val ATTR_ON_OFF = 0x0000
        const val CMD_OFF = 0x0000
        const val CMD_ON = 0x0001
    }

    object LevelControl {
        const val CLUSTER = 0x0008
        const val ATTR_CURRENT_LEVEL = 0x0000
        const val CMD_MOVE_TO_LEVEL_WITH_ON_OFF = 0x0004
    }

    object DoorLock {
        const val CLUSTER = 0x0101
        const val ATTR_LOCK_STATE = 0x0000
        const val CMD_LOCK_DOOR = 0x0000
        const val CMD_UNLOCK_DOOR = 0x0001
    }

    object Binding {
        const val CLUSTER = 0x001E
        const val ATTR_BINDING = 0x0000
    }

    object AccessControl {
        const val CLUSTER = 0x001F
        const val ATTR_ACL = 0x0000
    }

    object OperationalCredentials {
        const val CLUSTER = 0x003E
        const val ATTR_FABRICS = 0x0001
        const val CMD_REMOVE_FABRIC = 0x000A
    }

    object BasicInformation {
        const val CLUSTER = 0x0028
    }

    object Descriptor {
        const val CLUSTER = 0x001D
    }
}
