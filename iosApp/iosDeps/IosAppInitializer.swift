//
//  IosAppInitializer.swift
//  iosDeps
//
//  Created by Sylwester Zielinski on 24/07/2026.
//

import Foundation
import SharedCode

/// Primitives-only facade exposing app-launch initialization to Kotlin, so the main app can call
/// it via `import shared` instead of compiling `SharedCode` directly.
@objc public class IosAppInitializer: NSObject {

    /// Clears any leftover keychain data on first launch after a fresh install.
    @objc public static func initKeychain() {
        KeypairInitializer.initKeychain()
    }
}
