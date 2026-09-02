//
//  LocalMatterClusterDiscovery.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 10/03/2026.
//

import Matter

/// Reads metadata for a Matter device: basic information from the root endpoint (0), and
/// device type plus client/server clusters from each supported endpoint (1..n).
///
/// Exposed to Kotlin, which reads a device back after the add-device flow has paired it — see
/// `IosDeviceInfoProvider`. The local controller is obtained in ``discoverClusters()`` rather than
/// in `init`, so that the initialiser stays non-failable and the bridged API is a plain
/// constructor plus one asynchronous call.
@objc public final class LocalMatterClusterDiscovery: NSObject {

    private let nodeId: NSNumber

    /// Creates a discovery helper for the device with the given node ID.
    ///
    /// - Parameter nodeId: The Matter node ID of the target device.
    @objc public init(nodeId: NSNumber) {
        self.nodeId = nodeId
        super.init()
    }

    /// Reads everything the app records about a freshly commissioned device.
    ///
    /// Reads the Basic Information cluster, enumerates the endpoints from the root endpoint's
    /// Descriptor cluster, then reads device types and cluster lists for each one — pulling the
    /// manufacturer-specific data too for any endpoint that advertises that cluster.
    ///
    /// - Returns: The assembled ``Device``. `dateCommissioned` is stamped here, so it reflects when
    ///   discovery ran rather than when the fabric actually accepted the device.
    /// - Throws: An `NSError` carrying the ``withMoreUserInfo(deviceId:stage:displayMessage:)``
    ///   payload if reading basic information or the descriptor cluster fails, or
    ///   `OperationError.unknown` if the local controller cannot be obtained or a Descriptor
    ///   cluster cannot be created for an endpoint.
    @objc public func discoverClusters() async throws -> Device {
        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: nodeId, controller: controller)

        let basicInfo = try await readBasicInformation(baseDevice: baseDevice)

        guard let mainDescriptor = MTRBaseClusterDescriptor(device: baseDevice, endpointID: 0, queue: .global()) else {
            throw OperationError.unknown
        }
        
        let endpoints: [NSNumber]
        do {
            endpoints = try await mainDescriptor.readEndpoints()
        } catch {
            throw (error as NSError).withMoreUserInfo(deviceId: nodeId, stage: CommissioningStage.readDescriptorCluster)
        }
        
        var deviceMatterInfo: [DeviceMatterInfo] = []
        for endpoint in endpoints {
            deviceMatterInfo.append(try await self.fetchEndpointInfo(baseDevice: baseDevice, endpoint: endpoint))
        }
        
        let primaryDeviceType = deviceMatterInfo.compactMap { $0.types.first }.first ?? 0
        
        SwiftLogger.debug("discoverClusters - finished")
        
        return Device(
            deviceId: nodeId,
            dateCommissioned: NSNumber(value: Date().timeIntervalSince1970 * 1000),
            vendorId: basicInfo.vendorId.stringValue,
            producId: basicInfo.productId.stringValue,
            deviceType: primaryDeviceType,
            name: "Matter device: \(nodeId)",
            productName: basicInfo.productName,
            vendorName: basicInfo.vendorName,
            uniqueId: basicInfo.uniqueId,
            softwareVersion: basicInfo.swVersion,
            specificationVersion: basicInfo.specVersion,
            serialNumber: basicInfo.serialNumber,
            deviceMatterInfo: deviceMatterInfo
        )
    }

    private struct BasicDeviceDetails {
        let vendorId: NSNumber
        let vendorName: String
        let productId: NSNumber
        let productName: String
        let uniqueId: String?
        let swVersion: String
        let specVersion: NSNumber?
        let serialNumber: String?
    }

    /// Reads the Basic Information cluster on the root endpoint (0).
    ///
    /// The whole cluster is fetched with a single wildcard read instead of one read per
    /// attribute. Attributes the device does not implement are then simply absent from the
    /// report, whereas reading them by ID fails the read with `UNSUPPORTED_ATTRIBUTE` (0x86) —
    /// `UniqueID` is only mandatory from spec 1.4, `SpecificationVersion` from 1.3, and
    /// `SerialNumber` is always optional, so older firmware rejects them. It also keeps the
    /// device from having to answer eight separate exchanges, which is where slow transports
    /// start timing out.
    ///
    /// - Parameter baseDevice: The device to read from.
    /// - Returns: The attributes that the device reported.
    /// - Throws: An error if the read fails, or if the report is missing an attribute that is
    ///   mandatory in every version of the cluster.
    private func readBasicInformation(baseDevice: MTRBaseDevice) async throws -> BasicDeviceDetails {
        SwiftLogger.debug("Basic Information Cluster - reading all attributes")

        let values: [NSNumber: Any]
        do {
            values = try await baseDevice.readAllAttributes(endpoint: 0, cluster: .basicInformationID)
        } catch {
            throw (error as NSError).withMoreUserInfo(deviceId: nodeId, stage: CommissioningStage.readBaseInfo)
        }

        guard let vendorId: NSNumber = values.attribute(.clusterBasicInformationAttributeVendorIDID),
              let vendorName: String = values.attribute(.clusterBasicInformationAttributeVendorNameID),
              let productId: NSNumber = values.attribute(.clusterBasicInformationAttributeProductIDID),
              let productName: String = values.attribute(.clusterBasicInformationAttributeProductNameID),
              let swVersion: String = values.attribute(.clusterBasicInformationAttributeSoftwareVersionStringID)
        else {
            throw (OperationError.missingAttribute as NSError).withMoreUserInfo(
                deviceId: nodeId,
                stage: CommissioningStage.readBaseInfo,
                displayMessage: "The device did not report its basic information.",
            )
        }

        return BasicDeviceDetails(
            vendorId: vendorId,
            vendorName: vendorName,
            productId: productId,
            productName: productName,
            uniqueId: values.attribute(.clusterBasicInformationAttributeUniqueIDID),
            swVersion: swVersion,
            specVersion: values.attribute(.clusterBasicInformationAttributeSpecificationVersionID),
            serialNumber: values.attribute(.clusterBasicInformationAttributeSerialNumberID)
        )
    }

    private func fetchEndpointInfo(baseDevice: MTRBaseDevice, endpoint: NSNumber) async throws -> DeviceMatterInfo {
        guard let descriptor = MTRBaseClusterDescriptor(device: baseDevice, endpointID: endpoint, queue: .global()) else {
            throw OperationError.unknown
        }
        
        let deviceTypes = try await descriptor.getDeviceType(endpoint: endpoint)
        let clientClusters = try await descriptor.readClientClusters(endpoint: endpoint)
        let serverClusters = try await descriptor.readServerClusters(endpoint: endpoint)
        
        let manufacturerData: ManufacturerSpecificData?
        if serverClusters.contains(0xFFF1FC01) {
            let controller = LocalMatterCustomClusterController()
            manufacturerData = try await controller.getData(deviceId: nodeId, endpoint: endpoint)
        } else {
            manufacturerData = nil
        }
        
        return DeviceMatterInfo(
            endpoint: endpoint,
            types: deviceTypes.map { $0.deviceType },
            serverClusters: serverClusters,
            clientClusters: clientClusters,
            manufacturerSpecificData: manufacturerData
        )
    }
}

private extension MTRBaseDevice {

    /// Reads every attribute the device implements for a cluster in a single interaction.
    ///
    /// The read uses a wildcard attribute path, so unsupported attributes are left out of the
    /// report rather than failing the read, and per-path errors are logged and skipped instead
    /// of propagated.
    ///
    /// - Parameters:
    ///   - endpoint: The endpoint ID hosting the cluster.
    ///   - cluster: The cluster to read.
    /// - Returns: The attribute values that could be read, keyed by attribute ID.
    /// - Throws: An error if the read itself fails.
    func readAllAttributes(endpoint: NSNumber, cluster: MTRClusterIDType) async throws -> [NSNumber: Any] {
        let report = try await readAttributes(
            withEndpointID: endpoint,
            clusterID: NSNumber(value: cluster.rawValue),
            attributeID: nil,
            params: nil,
            queue: .global()
        )

        var values: [NSNumber: Any] = [:]
        for entry in report {
            guard let path = entry["attributePath"] as? MTRAttributePath else { continue }

            if let error = entry["error"] as? NSError {
                SwiftLogger.debug("Attribute \(path.attribute) not readable: \(error.localizedDescription)")
                continue
            }
            guard let value = try? entry.readAny() else { continue }

            values[path.attribute] = value
        }

        SwiftLogger.debug("Endpoint \(endpoint), cluster \(cluster.rawValue) - read attributes: \(values)")
        return values
    }
}

private extension [NSNumber: Any] {

    /// Looks up an attribute value in a report keyed by attribute ID.
    ///
    /// - Parameter id: The attribute ID to look up.
    /// - Returns: The value if the device reported it as a `T`, otherwise `nil`.
    func attribute<T>(_ id: MTRAttributeIDType) -> T? {
        self[NSNumber(value: id.rawValue)] as? T
    }
}

private extension MTRBaseClusterDescriptor {

    /// Reads and logs the device type, client clusters, and server clusters for the root
    /// endpoint (0).
    func readEndpoint0() async throws {
        let deviceTypes = try await getDeviceType(endpoint: 0)
        let clientClusters = try await readClientClusters(endpoint: 0)
        let serverClusters = try await readServerClusters(endpoint: 0)
        SwiftLogger.debug("Endpoint 0 - devicetypes: \(deviceTypes)")
        SwiftLogger.debug("Endpoint 0 - clientClusters: \(clientClusters)")
        SwiftLogger.debug("Endpoint 0 - serverClusters: \(serverClusters)")
    }
    
    /// Reads the device type(s) declared for the endpoint.
    ///
    /// The device type defines what kind of device the endpoint represents, and specifies
    /// which clusters are mandatory and which are optional for that device type.
    ///
    /// - Parameter endpoint: The endpoint ID to query.
    /// - Returns: The device types declared for the endpoint.
    /// - Throws: An error if the attribute read fails.
    func getDeviceType(endpoint: NSNumber) async throws -> [MTRDescriptorClusterDeviceTypeStruct] {
        SwiftLogger.debug("Cluster Descriptor - getDeviceType()")
        let result = (try await readAttributeDeviceTypeList()).map { $0 as! MTRDescriptorClusterDeviceTypeStruct}
        SwiftLogger.debug("Supported device types: \(result)")
        return result
    }

    /// Reads the parts list attribute for the endpoint.
    ///
    /// The parts list is the set of endpoints logically connected to this endpoint. For the
    /// root endpoint (0), this should return all endpoints available on the device.
    ///
    /// - Returns: The endpoint IDs that make up the parts list.
    /// - Throws: An error if the attribute read fails.
    func readEndpoints() async throws -> [NSNumber] {
        SwiftLogger.debug("Cluster Descriptor - readEndpoints()")
        let result = (try await readAttributePartsList()).map { $0 as! NSNumber}
        SwiftLogger.debug("Supported endpoints: \(result)")
        return result
    }
    
    /// Reads the server clusters list for the endpoint.
    ///
    /// A server cluster implements the logic of a cluster, holds its state, and accepts
    /// commands — for example, a light bulb that receives on/off commands.
    ///
    /// - Parameter endpoint: The endpoint ID to query.
    /// - Returns: The server cluster IDs supported by the endpoint.
    /// - Throws: An error if the attribute read fails.
    func readServerClusters(endpoint: NSNumber) async throws -> [NSNumber] {
        SwiftLogger.debug("Cluster Descriptor - readServerClusters()")
        let result = (try await readAttributeServerList()).map { $0 as! NSNumber}
        SwiftLogger.debug("Supported server clusters: \(result)")
        return result
    }
    
    /// Reads the client clusters list for the endpoint.
    ///
    /// A client cluster means the device can send commands to the same cluster defined as a
    /// server cluster on another device — for example, a switch that sends on/off commands to
    /// a light bulb.
    ///
    /// - Parameter endpoint: The endpoint ID to query.
    /// - Returns: The client cluster IDs supported by the endpoint.
    /// - Throws: An error if the attribute read fails.
    func readClientClusters(endpoint: NSNumber) async throws -> [NSNumber] {
        SwiftLogger.debug("Cluster Descriptor - readClientClusters()")
        let result = (try await readAttributeClientList()).map { $0 as! NSNumber}
        SwiftLogger.debug("Supported client clusters: \(result)")
        return result
    }
}
