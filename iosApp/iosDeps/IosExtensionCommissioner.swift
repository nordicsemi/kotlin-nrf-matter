//
//  IosExtensionCommissioner.swift
//  iosDeps
//
//  Created by Sylwester Zielinski on 24/07/2026.
//

import Foundation
import SharedCode

/// Primitives-only facade exposing the MatterSupport "Add Device" extension flow to Kotlin.
///
/// This is the seam that lets the `nrfMatter` app extension consume commissioning from the
/// published library (`import shared`) instead of compiling `SharedCode`/`iosDeps` source
/// directly. Everything crossing the Kotlin cinterop boundary here is a primitive
/// (`String`/`Bool`/closures): the live `MTRDeviceController`, `MTRStorage`, and keypair stay
/// inside this Swift layer — which the Kotlin framework links and runs inside the extension's
/// own process — because none of those Matter framework types can be bridged through Kotlin.
@objc public class IosExtensionCommissioner: NSObject {

    private let commissioner = ExtensionMatterCommissioner()

    @objc public override init() {
        super.init()
    }

    /// Reads the target node ID from shared storage and commissions the device described by the
    /// onboarding payload.
    ///
    /// - Parameter payload: The Matter onboarding payload read from the commissioning QR code.
    /// - Throws: ``CommissioningError/missingNodeId`` if no node ID is available in shared
    ///   storage, or an error if commissioning fails.
    @objc public func commissionDevice(payload: String) async throws {
        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        guard let nodeId = storage.getNumber(key: SharedConsts.nodeIdKey) else {
            throw CommissioningError.missingNodeId
        }
        try await commissioner.commission(payload: payload, nodeID: nodeId)
    }

    /// Records that configuration completed successfully and releases the underlying controller.
    @objc public func finishConfigure() {
        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        storage.storeBool(key: SharedConsts.resultKey, value: true)
        commissioner.release()
    }

    /// The display names of the rooms a newly added device may be placed in.
    @objc public func roomNames() -> [String] {
        ["Living Room", "Bedroom", "Office", "Kitchen", "Dining Room"]
    }

    /// Logs an info-level message through the shared logger.
    ///
    /// - Parameter message: The message to log.
    @objc public func log(_ message: String) {
        SharedLogger.info(message)
    }
}
