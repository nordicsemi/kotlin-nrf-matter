//
//  ExtensionMatterCommissioner.swift
//  iosDeps
//
//  Created by Sylwester Zielinski on 26/02/2026.
//

import Matter
import SharedCode

/// Coordinates commissioning of a single Matter device using a locally managed controller.
///
/// This runs inside the MatterSupport "Add Device" app extension process (driven from
/// ``IosExtensionCommissioner``). The ``MTRDeviceController`` it obtains never leaves this Swift
/// layer — only primitives cross the Kotlin boundary.
class ExtensionMatterCommissioner {

    /// Provides access to the Matter controller used for commissioning.
    let provider = LocalControllerProvider(logTag: "MatterCommissioner")

    /// Starts a commissioning session for the device described by the onboarding payload and
    /// suspends until commissioning completes.
    ///
    /// - Parameters:
    ///   - payload: The Matter onboarding payload (e.g. from a QR code) describing the device to commission.
    ///   - nodeID: The node ID to assign to the device being commissioned.
    /// - Throws: An error if no controller is available or there is an error during commissioning.
    func commission(payload: String, nodeID: NSNumber) async throws {
        let controller = try provider.getController()

        try await withCheckedThrowingContinuation { (continuation: CheckedContinuation<Void, Error>) in
            let delegate = ExtensionMatterControllerDelegate(nodeID: nodeID, continuation: continuation)
            controller.setDeviceControllerDelegate(delegate, queue: .main)

            guard let payload = MTRSetupPayload(payload: payload) else {
                continuation.resume(throwing: CommissioningError.invalidPayload)
                return
            }

            do {
                try controller.setupCommissioningSession(with: payload, newNodeID: nodeID)
            } catch {
                continuation.resume(throwing: error)
            }
        }
    }

    /// Releases the underlying Matter controller and any associated resources.
    func release() {
        provider.release()
    }
}
