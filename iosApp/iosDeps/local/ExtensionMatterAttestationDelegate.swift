//
//  ExtensionMatterAttestationDelegate.swift
//  iosDeps
//
//  Created by Sylwester Zielinski on 27/02/2026.
//

import Foundation
import Matter
import SharedCode

/// `MTRDeviceAttestationDelegate` implementation that ignores the attestation outcome and always
/// allows commissioning to continue.
class ExtensionMatterAttestationDelegate: NSObject, MTRDeviceAttestationDelegate {

    // MARK: - MTRDeviceAttestationDelegate

    /// Called when device attestation completes; logs the result and continues commissioning regardless of outcome.
    ///
    /// - Parameters:
    ///   - controller: The controller performing commissioning.
    ///   - opaqueDeviceHandle: The device handle to pass back to the controller when resuming commissioning.
    ///   - attestationDeviceInfo: The attestation information reported for the device.
    ///   - error: An error describing the attestation outcome, or `nil` if attestation succeeded.
    func deviceAttestationCompleted(
        for controller: MTRDeviceController,
        opaqueDeviceHandle: UnsafeMutableRawPointer,
        attestationDeviceInfo: MTRDeviceAttestationDeviceInfo,
        error: (any Error)?
    ) {
        SharedLogger.info("DeviceAttestationCompleted (error: \(error)).")
        do {
            try controller.continueCommissioningDevice(opaqueDeviceHandle, ignoreAttestationFailure: true)
        } catch {
            SharedLogger.error("Failed to continue commissioning device error: \(error).")
        }
    }

    /// Called when device attestation fails; logs the failure and continues commissioning anyway.
    ///
    /// - Parameters:
    ///   - controller: The controller performing commissioning.
    ///   - opaqueDeviceHandle: The device handle to pass back to the controller when resuming commissioning.
    ///   - error: The attestation failure.
    func deviceAttestationFailed(
        for controller: MTRDeviceController,
        opaqueDeviceHandle: UnsafeMutableRawPointer,
        error: any Error
    ) {
        SharedLogger.error("DeviceAttestationFailed with error: \(error).")
        do {
            try controller.continueCommissioningDevice(opaqueDeviceHandle, ignoreAttestationFailure: true)
        } catch {
            SharedLogger.error("Failed to continue commissioning device error: \(error).")
        }
    }
}
