//
//  SharedStorage.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 20/03/2026.
//

import Matter

/// A  storage class used for storing key-value entires.
///
/// It is required by the Matter framework for storing the fabric (for the
/// local fabric) and all related data.
///
/// Also used for sharing data between the main app and the app extension.
/// It uses ``UserDefaults`` with app groups under the hood.
public class SharedStorage : NSObject, MTRStorage {

    private let defaults: UserDefaults

    /// Creates a storage instance backed by the shared ``UserDefaults`` for the given app group.
    ///
    /// - Parameter suitName: The app group identifier to use as the ``UserDefaults`` suite name.
    public init(suitName: String) {
        defaults = UserDefaults(suiteName: suitName)!
    }

    /// Stores a string value for the given key.
    public func storeString(key: String, value: String) {
        defaults.set(value, forKey: key)
    }

    /// Returns the string value stored for the given key, if any.
    public func getString(key: String) -> String? {
        defaults.string(forKey: key)
    }

    /// Stores a number value for the given key.
    public func storeNumber(key: String, value: NSNumber) {
        defaults.set(value, forKey: key)
    }

    /// Returns the number value stored for the given key, if any.
    public func getNumber(key: String) -> NSNumber? {
        defaults.object(forKey: key) as? NSNumber
    }

    /// Stores a boolean value for the given key.
    public func storeBool(key: String, value: Bool) {
        defaults.set(value, forKey: key)
    }

    /// Returns the boolean value stored for the given key.
    public func getBool(key: String) -> Bool? {
        defaults.bool(forKey: key)
    }

    /// Returns the raw data stored for the given key, if any.
    public func storageData(forKey key: String) -> Data? {
        return defaults.data(forKey: key)
    }

    /// Stores raw data for the given key.
    ///
    /// - Returns: `true` once the data has been stored.
    public func setStorageData(_ value: Data, forKey key: String) -> Bool {
        defaults.setValue(value, forKey: key)
        return true
    }

    /// Removes the value stored for the given key.
    ///
    /// - Returns: `true` once the value has been removed.
    public func removeStorageData(forKey key: String) -> Bool {
        defaults.removeObject(forKey: key)
        return true
    }

    /// Returns the raw data stored for the given key, if any.
    public func getKey(forKey key: String) -> Data? {
        return defaults.data(forKey: key)
    }

    /// Stores raw data for the given key.
    ///
    /// - Returns: `true` once the data has been stored.
    public func setKey(_ value: Data, forKey key: String) -> Bool {
        defaults.setValue(value, forKey: key)
        return true
    }
}
