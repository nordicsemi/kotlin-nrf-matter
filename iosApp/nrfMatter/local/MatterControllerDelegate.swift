//
//  MatterControllerDelegate.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 26/02/2026.
//

import Matter

/// `MTRDeviceControllerDelegate` implementation that drives commissioning of a specific node and
/// resumes an async continuation once commissioning finishes.
class MatterControllerDelegate : NSObject, MTRDeviceControllerDelegate {

    /// The node ID being commissioned.
    let nodeID: NSNumber
    /// The continuation resumed once commissioning finishes.
    let continuation: CheckedContinuation<Void, Error>

    /// Creates a delegate that commissions the given node and resumes the continuation once commissioning finishes.
    ///
    /// - Parameters:
    ///   - nodeID: The node ID to commission.
    ///   - continuation: The continuation to resume once commissioning finishes.
    init(nodeID: NSNumber, continuation: CheckedContinuation<Void, Error>) {
        self.nodeID = nodeID
        self.continuation = continuation
    }

    /// Called when the commissioning status changes; logs the new status.
    ///
    /// - Parameters:
    ///   - controller: The controller reporting the status update.
    ///   - status: The current commissioning status.
    func controller(_ controller: MTRDeviceController, statusUpdate status: MTRCommissioningStatus) {
        SharedLogger.debug("Status update: \(status.rawValue).")
    }

    /// Called once the commissioning session handshake completes; starts commissioning the node,
    /// delegating device attestation to ``MatterAttestationDelegate``.
    ///
    /// - Parameters:
    ///   - controller: The controller that established the commissioning session.
    ///   - error: An error if session establishment failed, or `nil` on success. Not currently checked before proceeding.
    func controller(_ controller: MTRDeviceController, commissioningSessionEstablishmentDone error: Error?) {
        SharedLogger.debug("Commissioning session establishement done.")
        do {
            let commissioningParams = MTRCommissioningParameters()
            commissioningParams.deviceAttestationDelegate = MatterAttestationDelegate()

            if #available(iOS 26.2, *) {
                commissioningParams.forceThreadScan = true
            }

            try controller.commissionNode(
                withID: nodeID,
                commissioningParams: commissioningParams,
            )
            SharedLogger.debug("Succcessfully commissioned device.")
        } catch {
            SharedLogger.debug("Commissioning node failed.")
        }
    }

    /// Called when commissioning finishes; resumes the continuation and shuts down the controller.
    ///
    /// - Parameters:
    ///   - controller: The controller that performed commissioning.
    ///   - error: An error if commissioning failed, or `nil` on success. Not currently checked.
    ///   - nodeID: The ID of the commissioned node, if available.
    func controller(_ controller: MTRDeviceController, commissioningComplete error: Error?, nodeID: NSNumber?) {
        SharedLogger.debug("Commissioning complete.")
        continuation.resume()
        controller.shutdown()
    }
}
