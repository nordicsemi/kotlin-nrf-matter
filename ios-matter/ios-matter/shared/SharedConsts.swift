//
//  SharedConsts.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 16/03/2026.
//

import Foundation

/// Shared constants for app group storage identifiers and storage keys.
@objc public final class SharedConsts: NSObject {

    @objc public static let localAppGroupInfoKey = "NordicMatterLocalAppGroup"
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
    @objc public static let roomsKey = "roomsKey"
    @objc public static let deviceNameKey = "deviceNameKey"

    private static func appGroup(infoKey: String, fallback: String) -> String {
        guard let configured = Bundle.main.object(forInfoDictionaryKey: infoKey) as? String,
              !configured.isEmpty else {
            return fallback
        }
        return configured
    }
}
