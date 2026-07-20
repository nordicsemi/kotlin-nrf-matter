//
//  MatterControllerProvider.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 05/03/2026.
//

import Matter
import os.log

/// Errors that can occur while setting up the local ``MTRDeviceController``.
enum ControllerError : Error {
    /// The controller factory or fabric could not be initialized.
    case initializationError
}

/// A helper class for managing access to ``MTRDeviceController``.
///
/// The controller is shared between the app extension and the main app.
///
/// A local fabric is identified by its fabric ID, IPK, and the private/public keys used by the
/// NOC signer. If those don't match, the controller may return a fabric with no devices even if
/// the fabric ID matches a previously used one.
///
/// The controller ensures that only one active controller is in use at a time. If an instance
/// already exists, it is returned from the cached value. If a new instance needs to be created,
/// it first tries to create a controller on a fabric assuming that fabric already exists. If the
/// IPK, fabric ID, and NOC signer match, the new controller instance is created and cached for
/// later use. If creation on the existing fabric fails, a controller on a new fabric is created
/// instead. This can happen even when the fabric ID was previously used, if the IPK or NOC signer
/// no longer match.
public class LocalControllerProvider {

    private let logTag: String
    private let factory = MTRDeviceControllerFactory.sharedInstance()
    
    private static var controller: MTRDeviceController? = nil
    
    /// Creates a provider for accessing the shared local device controller.
    ///
    /// - Parameter logTag: Tag used to prefix log messages emitted by this instance.
    public init(logTag: String) {
        self.logTag = logTag
    }

    /// Releases the ``MTRDeviceControllerFactory`` and every ``MTRDeviceController`` it created.
    ///
    /// After calling this, no ``MTRDeviceController`` can be used.
    public func release() {
        factory.stop()
    }

    /// Returns the shared local device controller, creating it if necessary.
    ///
    /// If a running controller already exists, it is returned from the cache. Otherwise, this
    /// starts the controller factory if needed, loads or creates the IPK, and attempts to create
    /// a controller on the existing fabric before falling back to creating one on a new fabric.
    ///
    /// - Returns: The active ``MTRDeviceController``.
    /// - Throws: `ControllerError.initializationError` if the IPK could not be loaded or created,
    ///   or an error from the underlying controller factory if the controller cannot be created.
    public func getController() throws -> MTRDeviceController {
        if let controller = Self.controller, controller.isRunning {
            return controller
        }

        let storage = SharedStorage(suitName: SharedConsts.localStorage)
        let factoryParams = MTRDeviceControllerFactoryParams(storage: storage)
        
        if (!factory.isRunning) {
            try factory.start(factoryParams)
        }
        
        guard let ipk = loadOrCreateIPK(storage: storage) else {
            throw ControllerError.initializationError
        }
        
        let params = MTRDeviceControllerStartupParams(
            ipk: ipk as Data,
            fabricID: 1, // todo
            nocSigner: MatterKeypair(),
        )
        params.vendorID = 0xFFF1
        
        let controller: MTRDeviceController

        do {
            SharedLogger.debug("\(self.logTag) - Controller from existing fabric")
            controller = try factory.createController(onExistingFabric: params)
        } catch {
            SharedLogger.debug("\(self.logTag) - Controller from new fabric")
            controller = try factory.createController(onNewFabric: params)
        }

        Self.controller = controller
        return controller
    }
    
    /// Loads the stored IPK, generating and persisting a new one if none exists.
    ///
    /// The IPK must be unique per fabric. It is used for CASE (Certificate Authenticated Session
    /// Establishment).
    ///
    /// - Parameter storage: The storage to load the IPK from and persist it to.
    /// - Returns: The IPK data, or `nil` if it could not be loaded or generated.
    private func loadOrCreateIPK(storage: SharedStorage) -> Data? {
        if let storedIpk = storage.getKey(forKey: "MatterIPK") {
            return storedIpk as Data
        }

        guard let ipkMutable = NSMutableData(length: 16) else {
            SharedLogger.debug("\(self.logTag) Coulnd't create NSMutableData dla IPK")
            return nil
        }

        let status = SecRandomCopyBytes(kSecRandomDefault, ipkMutable.length, ipkMutable.mutableBytes)
        guard status == errSecSuccess else {
            SharedLogger.debug("\(self.logTag) Error during generating IPK: \(status)")
            return nil
        }

        _ = storage.setKey(ipkMutable as Data, forKey: "MatterIPK")

        return ipkMutable as Data
    }
}
