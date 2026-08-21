//
//  Extension.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

import Matter

/// Helpers for reading Matter attribute report dictionaries.
extension [String: Any] {

    /// Extracts the nested `data` entry of a Matter attribute report as a typed value.
    ///
    /// - Returns: The attribute's value together with its Matter type.
    /// - Throws: `OperationError.missingAttribute` if the `data` entry is missing or carries no
    ///   Matter type.
    func readMatterValue() throws -> MatterValue {
        guard let data = self[MTRDataKey] as? [String: Any],
              let value = MatterValue.from(dictionary: data) else {
            throw OperationError.missingAttribute
        }
        return value
    }

    /// Extracts the first field of a Matter command response.
    ///
    /// - Returns: The value of the response's first field, or `nil` if the command was answered
    ///   with a status and no data.
    func readCommandResponseValue() -> MatterValue? {
        guard let data = self[MTRDataKey] as? [String: Any],
              data[MTRTypeKey] as? String == MTRStructureValueType,
              let fields = data[MTRValueKey] as? [[String: Any]],
              let field = fields.first,
              let fieldData = field[MTRDataKey] as? [String: Any] else {
            return nil
        }
        return MatterValue.from(dictionary: fieldData)
    }

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
