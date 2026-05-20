//
//  GoogleHomeOnOffController .swift
//  iosApp
//
//  Created by Sylwester Zielinski on 16/03/2026.
//

import ComposeApp
import Matter
import SharedCode
import GoogleHomeSDK
import GoogleHomeTypes

/**
 * A helper class from controlling a light type Matter device using Googel Home hub.
 */
class GoogleHomeOnOffController : MatterOnOffController {

    /**
     * Set the light on/off on a remote Matter device.
     */
    func setDeviceOnOff(deviceId: DeviceId, isOn: Bool, endpoint: Int32) async throws {
        let controller = await GoogleHomeController.instance()
        let structure = await controller.getStructure()
        
        let device = await controller.getDevice(id: deviceId.stringValue)
        
        guard let device else { return }
        
        guard let lightType = await device.parts.get(OnOffLightDeviceType.self) else {
            return
        }
        
        do {
            if let onOffTrait = lightType.matterTraits.onOffTrait {
                if isOn {
                    try await onOffTrait.on()
                } else {
                    try await onOffTrait.off()
                }
            }
        } catch {
            
        }
    }
}
