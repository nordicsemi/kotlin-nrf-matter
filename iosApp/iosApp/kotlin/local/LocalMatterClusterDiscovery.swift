//
//  LocalMatterClusterDiscovery.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 10/03/2026.
//

import ComposeApp
import Matter
import OSLog
import SharedCode

class LocalMatterClusterDiscovery {
    
    private let nodeId: NSNumber
    private let device: MTRDevice
    private let baseDevice: MTRBaseDevice
    
    private let logger = Logger(subsystem: "nrf.matter", category: "MatterClusterDiscovery")
    
    init(nodeId: NSNumber) {
        self.nodeId = nodeId
        let controller = try! LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        device = MTRDevice(nodeID: nodeId, controller: controller)
        baseDevice = MTRBaseDevice(nodeID: nodeId, controller: controller)
    }
    
    func getName() async -> String {
        let information = MTRBaseClusterBasicInformation(device: baseDevice, endpointID: 0, queue: .main)
        let name = (try? await information?.readAttributeNodeLabel()) ?? "unknown"
        logger.debug("Name: \(name)")
        return name
    }
    
    func getProductName() async -> String {
        let information = MTRBaseClusterBasicInformation(device: baseDevice, endpointID: 0, queue: .main)
        let productName = (try? await information?.readAttributeProductName()) ?? "unknown"
        logger.debug("ProductName: \(productName)")
        return productName
    }
    
    func getProductId() async -> NSNumber? {
        let information = MTRBaseClusterBasicInformation(device: baseDevice, endpointID: 0, queue: .main)
        let productId = try? await information?.readAttributeProductID() ?? nil
        logger.debug("ProductId: \(productId)")
        return productId
    }
    
    func getVendorName() async -> String {
        let information = MTRBaseClusterBasicInformation(device: baseDevice, endpointID: 0, queue: .main)
        let vendorName = (try? await information?.readAttributeVendorName()) ?? "unknown"
        logger.debug("VendorName: \(vendorName)")
        return vendorName
    }
    
    func getVendorId() async -> NSNumber? {
        let information = MTRBaseClusterBasicInformation(device: baseDevice, endpointID: 0, queue: .main)
        let vendorId = try? await information?.readAttributeVendorID() ?? nil
        logger.debug("VendorId: \(vendorId)")
        return vendorId
    }
    
    func discoverClusters() async -> Device {
        let deviceId = DeviceId(value: nodeId.stringValue)
        let name = "Matter device: \(nodeId)"
        let vendorId = await getVendorId()
        let vendorName = await getVendorName()
        let productId = await getProductId()
        let productName = await getProductName()
        
        let deviceTypes = await getDeviceType(endpoint: 0)
        
        var deviceMatterInfo: [DeviceMatterInfo] = []
        let endpoints = await readEndpoints()
        for endpoint in endpoints {
            let deviceTypes = await getDeviceType(endpoint: endpoint)
            let clientClusters = await readClientClusters(endpoint: endpoint)
            let serverClusters = await readServerClusters(endpoint: endpoint)
            let controller = LocalMatterCustomClusterController()
            let manufacturerSpecificData = try? await controller.getData(deviceId: deviceId, endpoint: Int32(truncating: endpoint))

            let newInfo = DeviceMatterInfo(
                endpoint: endpoint.int32Value,
                types: deviceTypes.map { KotlinLong(value: $0.deviceType.int64Value) },
                serverClusters: serverClusters.map { KotlinLong(value: $0.int64Value) },
                clientClusters: clientClusters.map { KotlinLong(value: $0.int64Value) },
                manufacturerSpecificData: manufacturerSpecificData,
            )
            deviceMatterInfo.append(newInfo)
        }
        
        let deviceType = mapDeviceType(deviceMatterInfo.flatMap { $0.types }.first)

        logger.debug("discoverClusters - finished")
        
        return Device(
            deviceId: deviceId,
            dateCommissioned: KotlinLong(value: Int64(Date().timeIntervalSince1970 * 1000)),
            vendorId: vendorId?.stringValue ?? "unknown",
            productId: productId?.stringValue ?? "unknown",
            deviceType: deviceType,
            name: name,
            productName: productName,
            vendorName: vendorName,
            deviceMatterInfo: deviceMatterInfo,
        )
    }

    func mapDeviceType(_ deviceType: KotlinLong?) -> DeviceType {
        logger.debug("mapDeviceType: \(deviceType)")
        switch deviceType {
        case 10: return .doorLock
        case 260: return .lightSwitch
        case 257: return .lightOnOff
        default: return .manufacturerSpecificDevice
        }
    }
    
    func getDeviceType(endpoint: NSNumber) async -> [MTRDescriptorClusterDeviceTypeStruct] {
        logger.debug("getDeviceType")
        let descriptor = MTRBaseClusterDescriptor(device: baseDevice, endpointID: endpoint, queue: DispatchQueue.global())
        let result = (try? await descriptor?.readAttributeDeviceTypeList())?.map { $0 as! MTRDescriptorClusterDeviceTypeStruct} ?? []
        logger.debug("Supported device types: \(result)")
        return result
    }
    
    func readEndpoints() async -> [NSNumber] {
        logger.debug("readEndpoints")
        let descriptor = MTRBaseClusterDescriptor(device: baseDevice, endpointID: 0, queue: DispatchQueue.global())
        let result = (try? await descriptor?.readAttributePartsList())?.map { $0 as! NSNumber} ?? []
        logger.debug("Supported endpoints: \(result)")
        return result
    }
    
    func readServerClusters(endpoint: NSNumber) async -> [NSNumber] {
        logger.debug("readServerClusters")
        let descriptor = MTRBaseClusterDescriptor(device: baseDevice, endpointID: endpoint, queue: DispatchQueue.global())
        let result = (try? await descriptor?.readAttributeServerList())?.map { $0 as! NSNumber} ?? []
        logger.debug("Supported server clusters: \(result)")
        return result
    }
    
    func readClientClusters(endpoint: NSNumber) async -> [NSNumber] {
        logger.debug("readClientClusters")
        let descriptor = MTRBaseClusterDescriptor(device: baseDevice, endpointID: endpoint, queue: DispatchQueue.global())
        let result = (try? await descriptor?.readAttributeClientList())?.map { $0 as! NSNumber} ?? []
        logger.debug("Supported client clusters: \(result)")
        return result
    }
}
