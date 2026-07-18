//
//  SwiftCodeProvider.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/02/2026.
//

import ComposeApp
import SharedCode

/// Native iOS implementation of the Kotlin `SwiftCodeProvider` protocol.
///
/// Supplies the shared Compose Multiplatform code with the two pieces of Matter functionality
/// that can't be implemented in Kotlin: commissioning (built on Apple's Swift-only
/// `MatterSupport.MatterAddDeviceRequest`) and logging (built on Combine and a third-party
/// logging library). Every other Matter controller is implemented directly in Kotlin via
/// cinterop against `Matter.framework` — see `composeApp/.../controller/Ios*Controller.kt`.
@MainActor
class SwiftCodeProviderImpl : @MainActor SwiftCodeProvider {

    /// - Returns: The native Matter commissioner used to commission new devices.
    func getMatterCommissioner() -> any MatterCommissioner {
        return LocalMatterCommissioner()
    }

    /// - Returns: The native logger implementation used to bridge logs to the shared Kotlin logger.
    func getLogger() -> IOSLogger {
        return IOSLoggerImpl()
    }
}
