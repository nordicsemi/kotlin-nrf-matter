//
//  GoogleHomeCustomClusterController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 08/05/2026.
//

import ComposeApp
import Matter
import SharedCode
import GoogleHomeSDK
import GoogleHomeTypes
import Combine

enum GoogleHomeCustomClusterError : Error {
    case missingTraits
}

@MainActor
final class GoogleHomeCustomClusterController : @MainActor MatterManufacturerCustomDataController {

    private func getTrait(deviceId: DeviceId) async throws -> NordicSemiconductor.NordicDevKitTrait {
        let controller = GoogleHomeController.instance()
        await controller.initialize()
        
        let device = await controller.getDevice(id: deviceId.stringValue)
        
        guard let device else { throw GoogleHomeCustomClusterError.missingTraits }
        
        guard let lightType = await device.parts.get(OnOffLightDeviceType.self) else {
            throw GoogleHomeCustomClusterError.missingTraits
        }
        
        guard let trait = lightType.traits[NordicSemiconductor.NordicDevKitTrait.self] else {
            throw GoogleHomeCustomClusterError.missingTraits
        }

        return trait
    }
    
    func getData(deviceId: DeviceId, endpoint: Int32) async throws -> ManufacturerSpecificData {
        SharedLogger.info("Obtaining manufacturer specific data.")
        let trait = try await getTrait(deviceId: deviceId)
        let name = trait.attributes.devKitName ?? ""
        let led = trait.attributes.userLed ?? false
        let button = trait.attributes.userButton ?? false
        
        SharedLogger.info("Manufacturer specific data: name - \(name), led - \(led), button - \(button).")
        
        return ManufacturerSpecificData(name: name, led: led, button: button)
    }
    
    func setLed(deviceId: DeviceId, isOn: Bool, endpoint: Int32) async throws {
        let trait = try await getTrait(deviceId: deviceId)
        try await trait.setLed(action: isOn ? .on : .off)
    }
    
    private var cancellables = Set<AnyCancellable>()
    
    func subscribeToButtonChanges(deviceId: DeviceId, endpoint: Int32, onUpdate: @escaping (KotlinBoolean) -> Void) async throws {
        let controller = GoogleHomeController.instance()
        await controller.initialize()
        
        let device = await controller.getDevice(id: deviceId.stringValue)
        
        guard let device else { throw GoogleHomeCustomClusterError.missingTraits }
        
        device.parts.subscribe(OnOffLightDeviceType.self)
            .map { $0.traits[NordicSemiconductor.NordicDevKitTrait.self] }
            .removeDuplicates()
            .sink(receiveCompletion: { completion in
                
            }, receiveValue: { value in
                let bool = value?.attributes.userButton ?? false
                onUpdate(KotlinBoolean(bool: bool))
            })
            .store(in: &cancellables)
    }
}
