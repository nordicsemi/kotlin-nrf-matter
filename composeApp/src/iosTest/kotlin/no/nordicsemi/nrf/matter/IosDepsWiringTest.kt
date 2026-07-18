package no.nordicsemi.nrf.matter

import kotlinx.cinterop.ExperimentalForeignApi
import no.nordicsemi.nrf.matter.iosdeps.createSwiftCodeProvider
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Exercises the real composeApp <-> iosDeps wiring end to end (not throwaway spike classes),
 * to confirm the cinterop + adapter chain actually works at runtime, not just compiles.
 *
 * Note: actually invoking a controller method (decommission, setDeviceOnOff, etc.) isn't
 * exercisable from this bare test binary — every one of them logs through SharedCode's
 * SharedLogger, which force-unwraps an App Group container URL only available when running
 * inside the real signed app. Constructing every controller via cinterop without crashing is
 * what's meaningful to verify here; behavior beyond that needs the real app (see Xcode build).
 */
class IosDepsWiringTest {

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun swiftCodeProviderConstructsAllControllersWithoutCrashing() {
        val provider = createSwiftCodeProvider()
        assertTrue(provider.getMatterCommissioner() != null)
        assertTrue(provider.getMatterOnOffController() != null)
        assertTrue(provider.getDecommissioner() != null)
        assertTrue(provider.getMatterBinder() != null)
        assertTrue(provider.getMatterDoorController() != null)
        assertTrue(provider.getMatterManufacturerCustomDataController() != null)
        assertTrue(provider.getMatterClusterExtensionController() != null)
        assertTrue(provider.getLogger() != null)
    }
}
