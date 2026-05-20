//
//  GoogleCommissioner.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 12/03/2026.
//

import ComposeApp
import Matter
import MatterSupport
import nrfMatter
import SharedCode
import GoogleHomeSDK
import GoogleHomeTypes

enum PairingError: Error {
    case test
}

/**
 * Starts commissioning of a new device to Google Home.
 * It requires account in Google Home and Google Home Hub to be available in the network.
 * All communication will go through the hub so when the hub is offline then there is no possibility
 * to control the device from the phone.
 * On the other hand, because fabric is managed by the hub, it is easy to share access to already
 * created network using Google Home app.
 * All devices added using this class should be also visible in Google Home app.
 */
@MainActor
class GoogleHomeCommissioner : @MainActor MatterCommissioner {
    
    func startIosCommissioning(onError: @escaping () -> Void) async throws -> Device? {
        return await commission()
    }
    
    func commission() async -> Device? {
        let controller = await GoogleHomeController.instance()
        let structure = await controller.getStructure()
        
        do {
            let topology = MatterAddDeviceRequest.Topology(
              ecosystemName: "Google Home",
              homes: [MatterAddDeviceRequest.Home(displayName: structure.name)]
            )
            
            let request = MatterAddDeviceRequest(topology: topology)
            
            try await structure.prepareForMatterCommissioning()
            
            let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
            storage.storeString(key: SharedConsts.matterEnvStorageKey, value: MatterEnv.google.rawValue)
            
            SharedLogger.debug("Moving controll to the app extension.")
            
            try await request.perform()
            
            SharedLogger.debug("Controll is back in the main app.")
            
            guard let commissionedDeviceID = (try await structure.completeMatterCommissioning()).first else {
                SharedLogger.debug("Comisioned device id is nil.")
                return nil
            }
            
            SharedLogger.debug("Obtaining device details.")
            
            guard let device = await controller.getDevice(id: commissionedDeviceID) else {
                SharedLogger.debug("Device not found.")
                return nil
            }
            
            let rootDevice = await device.parts.get(RootNodeDeviceType.self)
            if let basicInformationTrait = rootDevice?.traits[Matter.BasicInformationTrait.self] {
                let vendorName = basicInformationTrait.attributes.vendorName
                let productName = basicInformationTrait.attributes.productName
                let productID = basicInformationTrait.attributes.productID
                let vendorID = basicInformationTrait.attributes.vendorID
                let softwareVersionString = basicInformationTrait.attributes.softwareVersionString
                let deviceId = DeviceId(value: commissionedDeviceID)
                
                SharedLogger.debug("Returning device data")
                
                let msdController = GoogleHomeCustomClusterController()
                let data = try await msdController.getData(deviceId: deviceId, endpoint: 1)
                let deviceMatterInfo = DeviceMatterInfo(endpoint: 1, types: [], serverClusters: [0xfff1fc01], clientClusters: [], manufacturerSpecificData: data)
                
                return Device(
                    deviceId: deviceId,
                    dateCommissioned: KotlinLong(value: Int64(Date().timeIntervalSince1970 * 1000)),
                    vendorId: vendorID != nil ? String(vendorID!) : "unknown",
                    productId: productID != nil ? String(productID!) : "unknown",
                    deviceType: .manufacturerSpecificDevice, //todo
                    name: device.name,
                    productName: productName,
                    vendorName: vendorName,
                    uniqueId: "", //todo
                    softwareVersion: "", //todo
                    specificationVersion: KotlinLong(value: 0), //todo
                    deviceMatterInfo: [deviceMatterInfo] //todo
                )
            }
        } catch {
            SharedLogger.debug("Caught error")
            let result = structure.markMatterCommissioningFailed(error: error)
            SharedLogger.error("Failed to complete MatterAddDeviceRequest: \(result.detailedError).")
        }
        SharedLogger.debug("Returning nil.")
        return nil
    }
}
