//
//  SharedStorage.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 20/03/2026.
//

import Matter

/// A storage class used for storing key-value entries.
///
/// Used for sharing data between the main app and the app extension, which run in separate
/// processes: `UserDefaults` over the ``SharedConsts/sharedStorage`` app group under the hood.
///
/// The fabric that the Matter framework itself persists lives in the separate
/// ``SharedConsts/localStorage`` group, written through ``MatterStorage`` — not through this class.
///
/// ``storageData(forKey:)``/``setStorageData(_:forKey:)`` and ``getKey(forKey:)``/``setKey(_:forKey:)``
/// are two names for the same behaviour, kept because both spellings are already called.
@objc public final class SharedStorage : NSObject {

    private let defaults = UserDefaults.appGroup(
        SharedConsts.sharedStorage,
        configuredBy: SharedConsts.sharedAppGroupInfoKey
    )

    /// Stores a string value for the given key.
    @objc public func storeString(key: String, value: String) {
        defaults.set(value, forKey: key)
    }

    /// Returns the string value stored for the given key, if any.
    @objc public func getString(key: String) -> String? {
        defaults.string(forKey: key)
    }

    /// Stores a list of strings for the given key.
    @objc public func storeStringArray(key: String, value: [String]) {
        defaults.set(value, forKey: key)
    }

    /// Returns the list of strings stored for the given key, if any.
    ///
    /// - Returns: The stored list, or `nil` if the key is absent or holds something other than a
    ///   list of strings.
    @objc public func getStringArray(key: String) -> [String]? {
        defaults.stringArray(forKey: key)
    }

    /// Stores a number value for the given key.
    @objc public func storeNumber(key: String, value: NSNumber) {
        defaults.set(value, forKey: key)
    }

    /// Returns the number value stored for the given key, if any.
    @objc public func getNumber(key: String) -> NSNumber? {
        defaults.object(forKey: key) as? NSNumber
    }

    /// Stores a boolean value for the given key.
    @objc public func storeBool(key: String, value: Bool) {
        defaults.set(value, forKey: key)
    }

    /// Returns the boolean value stored for the given key, or `false` if the key is absent.
    ///
    /// Never returns `nil` despite the optional return type — `UserDefaults.bool(forKey:)` cannot
    /// distinguish "absent" from "false", so callers cannot use this to test for presence.
    ///
    /// Not exposed to Objective-C, so this one is unreachable from Kotlin.
    public func getBool(key: String) -> Bool? {
        defaults.bool(forKey: key)
    }

    /// Returns the raw data stored for the given key, if any.
    @objc public func storageData(forKey key: String) -> Data? {
        return defaults.data(forKey: key)
    }

    /// Stores raw data for the given key.
    ///
    /// - Returns: `true` once the data has been stored.
    @objc public func setStorageData(_ value: Data, forKey key: String) -> Bool {
        defaults.setValue(value, forKey: key)
        return true
    }

    /// Removes the value stored for the given key.
    ///
    /// - Returns: `true` once the value has been removed.
    @objc public func removeStorageData(forKey key: String) -> Bool {
        defaults.removeObject(forKey: key)
        return true
    }

    /// Returns the raw data stored for the given key, if any.
    @objc public func getKey(forKey key: String) -> Data? {
        return defaults.data(forKey: key)
    }

    /// Stores raw data for the given key.
    ///
    /// - Returns: `true` once the data has been stored.
    @objc public func setKey(_ value: Data, forKey key: String) -> Bool {
        defaults.setValue(value, forKey: key)
        return true
    }
}
