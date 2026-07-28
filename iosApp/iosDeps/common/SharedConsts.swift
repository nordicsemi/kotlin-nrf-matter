//
//  SharedConst.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 16/03/2026.
//

/// Shared constants for app group storage identifiers and storage keys.
public class SharedConsts {
    /// App group identifier for storage used by the local fabric.
    public static let localStorage = "group.nordicsemi.nrf.matter.local"
    /// App group identifier for storage shared between the main app and the extension.
    public static let sharedStorage = "group.nordicsemi.nrf.matter.shared"
    /// Storage key for the currently configured ``MatterEnv``.
    public static let matterEnvStorageKey = "MatterEnvironment"
    /// Storage key for the node ID.
    public static let nodeIdKey = "nodeIdKey"
    /// Storage key for a stored result value.
    public static let resultKey = "resultKey"
}
