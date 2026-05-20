//
//  GoogleHomeProvider.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 24/03/2026.
//

import Combine
import ComposeApp
import Matter
import SharedCode
import GoogleHomeSDK

enum GoogleHomeControllerError: Error {
    case noHomeFound
    case noStructureFound
}

/**
 * A helper class for communication with Google cloud.
 */
class GoogleHomeController {

    nonisolated(unsafe) private static var controller: GoogleHomeController? = nil
    
    private var structure: Structure? = nil
    private var home: Home? = nil
    private var cancellables: Set<AnyCancellable> = []
    
    public static func instance() -> GoogleHomeController {
        if let controller = Self.controller {
            return controller
        } else {
            let controller = GoogleHomeController()
            Self.controller = controller
            return controller
        }
    }
    
    func initialize() async {
        guard home == nil else {
            return
        }
        
        do {
            home = try await Home.connect()
            
            let allStructuresChanges = home!.structures()
            let allStructures = try await allStructuresChanges.list()
            structure = allStructures.first
        } catch {
        }
    }
    
    func getHubs() async throws -> [GoogleHub] {
        guard let home else { throw GoogleHomeControllerError.noHomeFound }
        let hubs = await home.discoverAvailableHubs(duration: .seconds(5))
        
        hubs.forEach { hub in
            SharedLogger.debug("Hub discovered: name - \(hub.serviceInstanceName), type - \(hub.serviceType), canBeActivated - \(hub.canBeActivated)")
        }
        
        let result = hubs.map { GoogleHub(name: $0.serviceInstanceName, domainObject: $0) }
        return result
    }
    
    func activateHub(_ googleHub: GoogleHub) async throws {
        guard let home else { throw GoogleHomeControllerError.noHomeFound }
        guard let structure else { throw GoogleHomeControllerError.noStructureFound }
        
        let hub = googleHub.domainObject as! Hub
        try await home.startHubActivation(hub, structureID: structure.id)
    }
    
    func getStructure() async -> Structure {
        try! await withCheckedThrowingContinuation { continuation in
            var cancellable: AnyCancellable?

            cancellable = home!
                .structures()
                .batched()
                .receive(on: DispatchQueue.main)
                .map { Array($0) }
                .filter { !$0.isEmpty }
                .prefix(1)
                .sink(
                    receiveCompletion: { completion in
                        if case .failure(let error) = completion {
                            continuation.resume(throwing: error)
                        }
                        cancellable?.cancel()
                    },
                    receiveValue: { structures in
                        if let first = structures.first {
                            continuation.resume(returning: first)
                        } else {
                            continuation.resume(throwing: GoogleHomeControllerError.noStructureFound)
                        }
                        cancellable?.cancel()
                    }
                )
        }
    }
    
    func getDevice(id: String) async -> HomeDevice? {
        try? await home?.devices().list().first(where: { $0.id == id })
    }
}
