import Foundation

/// Plain-data mirror of `no.nordicsemi.nrf.matter.domain.ManufacturerSpecificData`.
///
/// `iosDeps` never imports `Core` (a Kotlin/Native framework cannot be linked into two
/// independently-compiled Kotlin/Native processes without crashing), so every `:core` model type
/// that crosses this boundary has a plain, `@objc`-visible mirror here. `composeApp` converts
/// between the two on its own side, where the real `:core` types are already compiled in.
@objc public class SwiftManufacturerSpecificData: NSObject {
    @objc public let name: String
    @objc public let led: Bool
    @objc public let button: Bool

    @objc public init(name: String, led: Bool, button: Bool) {
        self.name = name
        self.led = led
        self.button = button
    }
}

/// Plain-data mirror of `no.nordicsemi.nrf.matter.model.DeviceMatterInfo`.
@objc public class SwiftDeviceMatterInfo: NSObject {
    @objc public let endpoint: Int32
    @objc public let types: [NSNumber]
    @objc public let serverClusters: [NSNumber]
    @objc public let clientClusters: [NSNumber]
    @objc public let manufacturerSpecificData: SwiftManufacturerSpecificData?

    @objc public init(
        endpoint: Int32,
        types: [NSNumber],
        serverClusters: [NSNumber],
        clientClusters: [NSNumber],
        manufacturerSpecificData: SwiftManufacturerSpecificData?
    ) {
        self.endpoint = endpoint
        self.types = types
        self.serverClusters = serverClusters
        self.clientClusters = clientClusters
        self.manufacturerSpecificData = manufacturerSpecificData
    }
}

/// Plain-data mirror of `no.nordicsemi.nrf.matter.model.Device`.
@objc public class SwiftDevice: NSObject {
    @objc public let deviceId: String
    @objc public let dateCommissioned: NSNumber?
    @objc public let vendorId: String?
    @objc public let productId: String?
    @objc public let deviceType: Int32
    @objc public let name: String?
    @objc public let productName: String?
    @objc public let vendorName: String?
    @objc public let uniqueId: String?
    @objc public let softwareVersion: String?
    @objc public let specificationVersion: NSNumber?
    @objc public let serialNumber: String?
    @objc public let deviceMatterInfo: [SwiftDeviceMatterInfo]

    @objc public init(
        deviceId: String,
        dateCommissioned: NSNumber?,
        vendorId: String?,
        productId: String?,
        deviceType: Int32,
        name: String?,
        productName: String?,
        vendorName: String?,
        uniqueId: String?,
        softwareVersion: String?,
        specificationVersion: NSNumber?,
        serialNumber: String?,
        deviceMatterInfo: [SwiftDeviceMatterInfo]
    ) {
        self.deviceId = deviceId
        self.dateCommissioned = dateCommissioned
        self.vendorId = vendorId
        self.productId = productId
        self.deviceType = deviceType
        self.name = name
        self.productName = productName
        self.vendorName = vendorName
        self.uniqueId = uniqueId
        self.softwareVersion = softwareVersion
        self.specificationVersion = specificationVersion
        self.serialNumber = serialNumber
        self.deviceMatterInfo = deviceMatterInfo
    }
}

/// Thrown by `LocalMatterCommissioner` on commissioning failure; mirrors
/// `no.nordicsemi.nrf.matter.commission.CommissioningException`'s fields.
@objc public class SwiftCommissioningError: NSError {
    @objc public let stage: Int32
    @objc public let errorCode: NSNumber?
    @objc public let displayMessage: String
    @objc public let fabricId: Int32

    @objc public init(stage: Int32, errorCode: NSNumber?, displayMessage: String, fabricId: Int32) {
        self.stage = stage
        self.errorCode = errorCode
        self.displayMessage = displayMessage
        self.fabricId = fabricId
        super.init(domain: "SwiftCommissioningError", code: 0, userInfo: [NSLocalizedDescriptionKey: displayMessage])
    }

    @objc public required init?(coder: NSCoder) {
        fatalError("init(coder:) is not supported")
    }
}

/// A single log line, plain-data mirror of `no.nordicsemi.nrf.matter.logger.LogEntity`.
@objc public class SwiftLogEntity: NSObject {
    @objc public let date: Int64
    @objc public let level: Int32
    @objc public let tag: String
    @objc public let message: String

    @objc public init(date: Int64, level: Int32, tag: String, message: String) {
        self.date = date
        self.level = level
        self.tag = tag
        self.message = message
    }
}
