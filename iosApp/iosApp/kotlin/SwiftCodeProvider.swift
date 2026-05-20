//
//  SwiftCodeProvider.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/02/2026.
//

import Matter
import ComposeApp
import SharedCode

@MainActor
class SwiftCodeProviderImpl : @MainActor SwiftCodeProvider {
    
    func getMatterCommissioner() -> any MatterCommissioner {
        return LocalMatterCommissioner()
//        return HomeKitCommissioner()
//        return GoogleHomeCommissioner()
    }
    
    func getMatterOnOffController() -> any MatterOnOffController {
        return LocalMatterOnOffController()
//        return HomeKitMatterOnOffController()
//        return GoogleHomeOnOffController()
    }
    
    func getDecommissioner() -> any MatterDecommissioner {
        return LocalMatterDecommissioner()
//        return HomeKitDecommissioner()
//        return GoogleHomeDecommissioner()
    }
    
    func getMatterBinder() -> any MatterBinder {
        return LocalMatterBinder()
    }
    
    func getMatterDoorController() -> any MatterDoorController {
        return LocalMatterDoorController()
    }
    
    func getMatterOutletController() -> any MatterOutletController {
        return LocalMatterOutletController()
    }
    
    func getMatterManufacturerCustomDataController() -> any MatterManufacturerCustomDataController {
        return LocalMatterCustomClusterController()
//        return GoogleHomeCustomClusterController()
    }
    
    func getMatterClusterExtensionController() -> any MatterClusterExtensionController {
        return LocalMatterClusterExtController()
    }
    
    func getLogger() -> PlatformLogger {
        return PlatformLogger(logger: NativePlatformLoggerImpl())
    }
    
    func getHubController() -> any GoogleHubController {
        return GoogleHubControllerImpl()
    }
}
