package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.COpaquePointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFGetTypeID
import platform.CoreFoundation.CFNumberRef
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanTrue
import platform.CoreFoundation.kCFTypeDictionaryKeyCallBacks
import platform.CoreFoundation.kCFTypeDictionaryValueCallBacks
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.Security.SecKeyRef
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecKeyCreateRandomKey
import platform.Security.SecKeyGetTypeID
import platform.Security.errSecSuccess
import platform.Security.kSecAttrApplicationTag
import platform.Security.kSecAttrIsPermanent
import platform.Security.kSecAttrKeySizeInBits
import platform.Security.kSecAttrKeyType
import platform.Security.kSecAttrKeyTypeECSECPrimeRandom
import platform.Security.kSecClass
import platform.Security.kSecClassKey
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecPrivateKeyAttrs
import platform.Security.kSecReturnRef

internal class KeypairGenerationException(message: String) : Exception(message)

/**
 * Mirrors `iosApp/SharedCode/KeypairHelper.swift`. Keychain query parameters (tag, key
 * type/size) must stay identical to the Swift implementation — see [MatterConsts].
 *
 * Deliberately omits `kSecAttrAccessGroup` (relies on the implicit default from the
 * `keychain-access-groups` entitlement), matching the Swift implementation exactly.
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosKeypairHelper(private val logTag: String) {

    private val tag: NSData = (MatterConsts.KEYPAIR_TAG as platform.Foundation.NSString)
        .dataUsingEncoding(NSUTF8StringEncoding)!!

    fun generatePrivateKey(): SecKeyRef = memScoped {
        val privateAttrs = CFDictionaryCreateMutable(kCFAllocatorDefault, 0, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
        CFDictionaryAddValue(privateAttrs, kSecAttrIsPermanent, kCFBooleanTrue)
        CFDictionaryAddValue(privateAttrs, kSecAttrApplicationTag, tag as CFDataRef)

        val attributes = CFDictionaryCreateMutable(kCFAllocatorDefault, 0, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
        CFDictionaryAddValue(attributes, kSecAttrKeyType, kSecAttrKeyTypeECSECPrimeRandom)
        CFDictionaryAddValue(attributes, kSecAttrKeySizeInBits, NSNumber(int = 256) as CFNumberRef)
        CFDictionaryAddValue(attributes, kSecPrivateKeyAttrs, privateAttrs)

        val error = alloc<CFErrorRefVar>()
        val secKey = SecKeyCreateRandomKey(attributes, error.ptr)

        if (error.value != null) {
            throw KeypairGenerationException("$logTag - Error during generation of a new key.")
        }
        secKey ?: throw KeypairGenerationException("$logTag - Key generation returned nil.")
        secKey
    }

    fun getPrivateKey(): SecKeyRef? = memScoped {
        val query = CFDictionaryCreateMutable(kCFAllocatorDefault, 0, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
        CFDictionaryAddValue(query, kSecClass, kSecClassKey)
        CFDictionaryAddValue(query, kSecAttrApplicationTag, tag as CFDataRef)
        CFDictionaryAddValue(query, kSecAttrKeyType, kSecAttrKeyTypeECSECPrimeRandom)
        CFDictionaryAddValue(query, kSecReturnRef, kCFBooleanTrue)
        CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

        // `CFTypeRef` is Objective-C `id`-bridged, so reading it back through a `CFTypeRefVar`'s
        // `.value` getter (or casting that result with `as`/`as?`) triggers Kotlin/Native's
        // dynamic downcast machinery, which can throw ClassCastException — and, unlike an
        // ordinary Kotlin cast, that throw happens somewhere inside the property getter itself
        // in a way that a surrounding try/catch here does not reliably intercept (confirmed by
        // repeated crashes even with the read+cast inside a try/catch). To sidestep this
        // entirely, the output param is a plain `COpaquePointerVar` (no ObjC bridging at all),
        // and the type is checked via the C-level `CFGetTypeID`/`SecKeyGetTypeID` before ever
        // treating the pointer as a `SecKeyRef` — this never boxes the result into a Kotlin
        // object, so there is nothing for the downcast machinery to fail on.
        val item = alloc<COpaquePointerVar>()
        val status = SecItemCopyMatching(query, item.ptr.reinterpret())
        if (status != errSecSuccess) {
            return null
        }

        val rawPtr = item.value
        if (rawPtr == null) {
            deletePrivateKey()
            return null
        }
        if (CFGetTypeID(rawPtr) != SecKeyGetTypeID()) {
            // Not actually a SecKey — e.g. a stale/malformed entry from an earlier bug. Release
            // what we got, wipe the bad entry, and let the caller generate a fresh key.
            CFRelease(rawPtr)
            deletePrivateKey()
            return null
        }
        rawPtr.reinterpret<cnames.structs.__SecKey>()
    }

    fun deletePrivateKey() {
        memScoped {
            val deleteQuery = CFDictionaryCreateMutable(kCFAllocatorDefault, 0, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
            CFDictionaryAddValue(deleteQuery, kSecClass, kSecClassKey)
            CFDictionaryAddValue(deleteQuery, kSecAttrApplicationTag, tag as CFDataRef)
            SecItemDelete(deleteQuery)
        }
    }
}
