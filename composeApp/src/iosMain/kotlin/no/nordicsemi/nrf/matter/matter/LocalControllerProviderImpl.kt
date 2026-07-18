package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDataCreate
import platform.CoreFoundation.kCFAllocatorDefault
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.Matter.MTRDeviceController
import platform.Matter.MTRDeviceControllerFactory
import platform.Matter.MTRDeviceControllerFactoryParams
import platform.Matter.MTRDeviceControllerStartupParams
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault

internal class ControllerInitializationException(message: String) : Exception(message)

/**
 * Mirrors `iosApp/SharedCode/LocalControllerProvider.swift`.
 *
 * The `nrfMatter` app extension is a separate OS process with its own independent copy of
 * this bootstrap logic (still in Swift, untouched) — this object is only ever the single
 * source of the `MTRDeviceController` singleton within the *main app* process. It must be
 * the only such source in that process: see `IosClusterDiscovery` for why.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal object LocalControllerProviderImpl {

    private val factory = MTRDeviceControllerFactory.sharedInstance()
    private var controller: MTRDeviceController? = null

    fun release() {
        factory.stopControllerFactory()
        controller = null
    }

    fun getController(logTag: String): MTRDeviceController = memScoped {
        controller?.let { existing -> if (existing.isRunning()) return existing }

        val storage = SharedStorageImpl(suiteName = MatterConsts.LOCAL_STORAGE_GROUP)
        val factoryParams = MTRDeviceControllerFactoryParams(storage = storage)

        if (!factory.isRunning()) {
            val startError = alloc<ObjCObjectVar<NSError?>>()
            val started = factory.startControllerFactory(factoryParams, startError.ptr)
            if (!started) {
                throw ControllerInitializationException(
                    "$logTag - Could not start controller factory: ${startError.value?.localizedDescription}"
                )
            }
        }

        val ipk = loadOrCreateIPK(storage)
            ?: throw ControllerInitializationException("$logTag - Could not load or create IPK.")

        val startupParams = MTRDeviceControllerStartupParams(
            iPK = ipk,
            fabricID = NSNumber(long = MatterConsts.FABRIC_ID),
            nocSigner = MatterKeypairImpl(),
        )
        startupParams.vendorID = NSNumber(int = MatterConsts.VENDOR_ID)

        val existingFabricError = alloc<ObjCObjectVar<NSError?>>()
        var newController = factory.createControllerOnExistingFabric(startupParams, existingFabricError.ptr)

        if (newController == null) {
            val newFabricError = alloc<ObjCObjectVar<NSError?>>()
            newController = factory.createControllerOnNewFabric(startupParams, newFabricError.ptr)
        }

        val result = newController
            ?: throw ControllerInitializationException("$logTag - Could not create controller on existing or new fabric.")

        controller = result
        result
    }

    private fun loadOrCreateIPK(storage: SharedStorageImpl): NSData? {
        storage.storageDataForKey(MatterConsts.IPK_KEY)?.let { return it }

        val bytes = ByteArray(16)
        val status = bytes.usePinned { pinned ->
            SecRandomCopyBytes(kSecRandomDefault, bytes.size.toULong(), pinned.addressOf(0))
        }
        if (status != errSecSuccess) return null

        val ipk = bytes.usePinned { pinned ->
            CFDataCreate(kCFAllocatorDefault, pinned.addressOf(0).reinterpret(), bytes.size.toLong())
        } as NSData
        storage.setStorageData(ipk, forKey = MatterConsts.IPK_KEY)
        return ipk
    }
}
