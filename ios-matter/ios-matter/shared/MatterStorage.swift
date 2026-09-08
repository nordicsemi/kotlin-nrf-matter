//
//  MatterStorage.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 04/08/2026.
//

import Matter

/// `MTRStorage` implementation backing the local fabric.
///
/// Handed to `MTRDeviceControllerFactoryParams`, so the Matter framework persists the fabric,
/// operational certificates and node data through it. ``LocalControllerProvider`` also stores the
/// IPK here.
///
/// Keyed into the ``SharedConsts/localStorage`` app group, which the app and the commissioning
/// extension both reach — the same fabric has to be visible from both processes. Deleting that
/// group's contents drops the fabric and orphans every commissioned device.
final class MatterStorage : NSObject, MTRStorage {

    private let defaults = UserDefaults.appGroup(
        SharedConsts.localStorage,
        configuredBy: SharedConsts.localAppGroupInfoKey
    )
    
    func storageData(forKey key: String) -> Data? {
        return defaults.data(forKey: key)
    }
    
    func setStorageData(_ value: Data, forKey key: String) -> Bool {
        defaults.setValue(value, forKey: key)
        return true
    }
    
    func removeStorageData(forKey key: String) -> Bool {
        defaults.removeObject(forKey: key)
        return true
    }
    
    /// Returns the raw data stored for the given key, if any.
    func getKey(forKey key: String) -> Data? {
        return defaults.data(forKey: key)
    }

    /// Stores raw data for the given key.
    ///
    /// - Returns: `true` once the data has been stored.
    func setKey(_ value: Data, forKey key: String) -> Bool {
        defaults.setValue(value, forKey: key)
        return true
    }
}
