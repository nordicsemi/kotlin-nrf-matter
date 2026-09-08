//
//  SharedConsts.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 16/03/2026.
//

import Foundation

/// Shared constants for app group storage identifiers and storage keys.
///
/// The two app group identifiers are read from the running target's `Info.plist`, because an app
/// group is provisioned per developer team and cannot be shared across them: an app embedding this
/// library has to own its groups, so they cannot be compiled in. Both keys are optional and fall
/// back to the groups used by Nordic's own nRF Matter app.
@objc public final class SharedConsts: NSObject {

    /// `Info.plist` key naming the app group backing ``localStorage``.
    @objc public static let localAppGroupInfoKey = "NordicMatterLocalAppGroup"
    /// `Info.plist` key naming the app group backing ``sharedStorage``.
    @objc public static let sharedAppGroupInfoKey = "NordicMatterSharedAppGroup"

    /// App group identifier for storage used by the local fabric.
    @objc public static let localStorage = appGroup(
        infoKey: localAppGroupInfoKey,
        fallback: "group.nordicsemi.nrf.matter.local"
    )
    /// App group identifier for storage shared between the main app and the extension.
    @objc public static let sharedStorage = appGroup(
        infoKey: sharedAppGroupInfoKey,
        fallback: "group.nordicsemi.nrf.matter.shared"
    )

    /// Storage key for the currently configured ``MatterEnv``.
    @objc public static let matterEnvStorageKey = "MatterEnvironment"
    /// Storage key for the node ID.
    @objc public static let nodeIdKey = "nodeIdKey"
    /// Storage key for a stored result value.
    @objc public static let resultKey = "resultKey"
    /// Storage key for the room names the extension offers the user.
    @objc public static let roomsKey = "roomsKey"
    /// Storage key for the device name the user chose in the extension's UI.
    @objc public static let deviceNameKey = "deviceNameKey"

    /// Reads an app group identifier from the running target's `Info.plist`.
    ///
    /// - Parameters:
    ///   - infoKey: The `Info.plist` key to read.
    ///   - fallback: Identifier to use when the key is absent or blank.
    /// - Returns: The configured identifier, or `fallback`.
    private static func appGroup(infoKey: String, fallback: String) -> String {
        guard let configured = Bundle.main.object(forInfoDictionaryKey: infoKey) as? String,
              !configured.isEmpty else {
            return fallback
        }
        return configured
    }
}
