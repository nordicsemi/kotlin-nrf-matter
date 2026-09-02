//
//  LocalMatterCommissioner.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 23/02/2026.
//

import Matter
import MatterSupport

/// The step of the commissioning flow an error was raised in, reported to Kotlin so the failure
/// screen can say what was being attempted.
///
/// Kotlin resolves these by *ordinal*, not by name — `CommissioningExceptionMapper` does
/// `Stage.entries[rawValue]` against `no.nordicsemi.nrf.matter.commission.Stage`. The cases here
/// must therefore stay in the same order as that enum, and new cases must be appended rather than
/// inserted.
@objc public enum CommissioningStage: Int {
    /// Failed while the system add-device flow was running, or the user cancelled it.
    case commissioning
    /// Failed while reading the Basic Information cluster on endpoint 0.
    case readBaseInfo
    /// Failed while reading the Descriptor cluster to enumerate endpoints and clusters.
    case readDescriptorCluster
}

/// Commissions a new Matter device into the local fabric.
///
/// A new device can be commissioned over Wi-Fi or Thread. Commissioning a Thread device
/// requires a Thread Border Router available on the local network and Thread network
/// credentials stored on the phone. Once commissioned, the device is added to the local fabric
/// and managed by the phone.
@objc public final class LocalMatterCommissioner: NSObject {

    /// Commissions a new Matter device into the local fabric using Apple's MatterSupport
    /// add-device flow.
    ///
    /// During the process, control moves to the system app extension, which provides the UI
    /// for scanning the QR code and choosing among available Thread networks. The local fabric
    /// is shared between the main app and the app extension via App Groups.
    ///
    /// Pairing is all this does: reading the commissioned device back is a separate step, done by
    /// ``LocalMatterClusterDiscovery``, so that the caller decides when - and whether - to pay for
    /// those reads.
    ///
    /// - Parameter deviceId: The Matter node ID to assign to the newly commissioned device.
    /// - Throws: An `NSError` carrying the ``withMoreUserInfo(deviceId:stage:displayMessage:)``
    ///   payload if the system add-device flow fails or the extension did not report success (the
    ///   user cancelled).
    @objc public func startIosCommissioning(deviceId: NSNumber) async throws {
        let homes = [MatterAddDeviceRequest.Home(displayName: "Nordic Home")]
        let topology = MatterAddDeviceRequest.Topology(ecosystemName: "Nordic Ecosystem", homes: homes)
        
        let request = MatterAddDeviceRequest(topology: topology, shouldScanNetworks: true)
        
        let storage = SharedStorage()
        let _ = storage.removeStorageData(forKey: SharedConsts.resultKey)
        storage.storeString(key: SharedConsts.matterEnvStorageKey, value: MatterEnv.local.rawValue)
        storage.storeNumber(key: SharedConsts.nodeIdKey, value: deviceId)
        
        do {
            try await request.perform()
        } catch {
            let nsError = error as NSError
            throw nsError.withMoreUserInfo(
                deviceId: deviceId,
                stage: CommissioningStage.commissioning
            )
        }
        
        let result = storage.getBool(key: SharedConsts.resultKey) ?? false
        guard result else {
            let nsError = NSError()
            throw nsError.withMoreUserInfo(
                deviceId: deviceId,
                stage: CommissioningStage.commissioning,
                displayMessage: "Cancelled.",
            )
        }
    }
}

extension NSError {

    /// Copies this error, adding the `userInfo` keys Kotlin needs to build a `CommissioningException`.
    ///
    /// This is the contract read by `NSError.toCommissioningException()` on the Kotlin side: it
    /// requires all four of `deviceId`, `stage`, `displayMessage` and `fabricId` to be present, and
    /// returns `null` — losing the commissioning-specific detail — if any one of them is missing.
    /// So an error thrown out of this package without going through here surfaces in the app as a
    /// generic failure.
    ///
    /// - Parameters:
    ///   - deviceId: The node ID the failed operation was targeting.
    ///   - stage: The step that failed. Stored as its raw value.
    ///   - displayMessage: Message to show the user. Defaults to `localizedDescription`, which for a
    ///     bare `NSError()` is a generic Cocoa string rather than anything Matter-specific.
    /// - Returns: A new error with the same domain and code, and the enriched `userInfo`.
    func withMoreUserInfo(
        deviceId: NSNumber,
        stage: CommissioningStage,
        displayMessage: String? = nil,
    ) -> NSError {
        var userInfo = userInfo
        userInfo["deviceId"] = deviceId
        userInfo["stage"] = stage.rawValue
        userInfo["displayMessage"] = displayMessage ?? localizedDescription
        userInfo["fabricId"] = 1

        return NSError(
            domain: domain,
            code: code,
            userInfo: userInfo
        )
    }
}
