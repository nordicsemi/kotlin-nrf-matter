//
//  Extension.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

/// Helpers for reading Matter attribute report dictionaries.
extension [String: Any] {

    /// Extracts the nested `data.value` entry from a Matter attribute report.
    ///
    /// - Returns: The attribute's raw value.
    /// - Throws: `OperationError.missingAttribute` if the `data` or `value` entry is missing.
    func readAny() throws -> Any {
        guard let data = self["data"] as? [String: Any],
              let value = data["value"] else {
            throw OperationError.missingAttribute
        }
        return value
    }
}
