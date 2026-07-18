package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFRetain
import platform.CoreFoundation.CFTypeRef
import platform.Foundation.NSData
import platform.Matter.MTRKeypairProtocol
import platform.Security.SecKeyAlgorithm
import platform.Security.SecKeyCopyPublicKey
import platform.Security.SecKeyCreateSignature
import platform.Security.SecKeyRef
import platform.Security.kSecKeyAlgorithmECDSASignatureMessageX962SHA256
import platform.darwin.NSObject

/**
 * Mirrors `iosApp/SharedCode/MatterKeypair.swift`. Holds a private/public keypair
 * (persisted in the Keychain via [IosKeypairHelper]) used for NOC signing.
 */
@OptIn(ExperimentalForeignApi::class)
internal class MatterKeypairImpl : NSObject(), MTRKeypairProtocol {

    private val privateKey: SecKeyRef
    private val publicKeyRef: SecKeyRef

    init {
        val helper = IosKeypairHelper(logTag = "MatterKeypairImpl")
        val existing = helper.getPrivateKey()
        privateKey = existing ?: helper.generatePrivateKey()
        publicKeyRef = SecKeyCopyPublicKey(privateKey)
            ?: error("Could not derive public key from private key")
    }

    override fun signMessageECDSA_DER(message: NSData): NSData = memScoped {
        val error = alloc<CFErrorRefVar>()
        val algorithm: SecKeyAlgorithm? = kSecKeyAlgorithmECDSASignatureMessageX962SHA256
        val signature = SecKeyCreateSignature(privateKey, algorithm, message as CFDataRef, error.ptr)
        if (error.value != null || signature == null) {
            return@memScoped NSData()
        }
        signature as NSData
    }

    /**
     * Returns a retained reference, matching the `CF_RETURNS_RETAINED`/`Unmanaged.passRetained`
     * contract the Swift implementation followed for this (deprecated but still supported)
     * selector — the caller is responsible for releasing it.
     */
    override fun publicKey(): SecKeyRef {
        CFRetain(publicKeyRef as CFTypeRef)
        return publicKeyRef
    }
}
