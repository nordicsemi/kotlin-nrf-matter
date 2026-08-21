//
//  MatterValue.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 13/08/2026.
//

import Matter

/// The Matter data type of an attribute value or a command field.
@objc public enum MatterValueType: Int {
    case boolean
    case signedInteger
    case unsignedInteger
    case float
    case double
    case string
    case bytes
    case null
    /// A type this bridge does not represent, such as a structure or a list.
    case unsupported
}

/// A Matter attribute value or command field, tagged with its Matter data type.
///
/// The Matter framework represents values as `[MTRTypeKey: ..., MTRValueKey: ...]` dictionaries and
/// `MatterValue` is the Objective-C representable form of one such entry. Cluster agnostic reads,
/// writes, and command invocations move values across the bridge without knowing the cluster's
/// schema, and the type tag is what makes that possible: a `Boolean` and a `uint8` attribute both
/// arrive from Matter as an `NSNumber`.
@objc public final class MatterValue: NSObject {

    /// The Matter data type of this value.
    @objc public let type: MatterValueType

    /// The value as represented by the Matter framework: an `NSNumber`, a `String`, `Data`, or
    /// `nil` for ``MatterValueType/null``.
    let rawValue: Any?

    /// The value as a number, for boolean and numeric types.
    @objc public var number: NSNumber? { rawValue as? NSNumber }

    /// The value as a string, for ``MatterValueType/string``.
    @objc public var string: String? { rawValue as? String }

    /// The value as bytes, for ``MatterValueType/bytes``.
    @objc public var bytes: Data? { rawValue as? Data }

    init(type: MatterValueType, rawValue: Any?) {
        self.type = type
        self.rawValue = rawValue
        super.init()
    }

    /// Wraps a boolean, e.g. the On/Off cluster's `OnOff` attribute.
    @objc public static func boolean(_ value: Bool) -> MatterValue {
        MatterValue(type: .boolean, rawValue: NSNumber(value: value))
    }

    /// Wraps a signed integer, for Matter `intX` attributes and fields.
    @objc public static func signedInteger(_ value: NSNumber) -> MatterValue {
        MatterValue(type: .signedInteger, rawValue: value)
    }

    /// Wraps an unsigned integer, for Matter `uintX` attributes and fields.
    @objc public static func unsignedInteger(_ value: NSNumber) -> MatterValue {
        MatterValue(type: .unsignedInteger, rawValue: value)
    }

    /// Wraps a single precision float.
    @objc public static func float(_ value: Float) -> MatterValue {
        MatterValue(type: .float, rawValue: NSNumber(value: value))
    }

    /// Wraps a double precision float.
    @objc public static func double(_ value: Double) -> MatterValue {
        MatterValue(type: .double, rawValue: NSNumber(value: value))
    }

    /// Wraps a UTF-8 string.
    @objc public static func string(_ value: String) -> MatterValue {
        MatterValue(type: .string, rawValue: value)
    }

    /// Wraps an octet string.
    @objc public static func bytes(_ value: Data) -> MatterValue {
        MatterValue(type: .bytes, rawValue: value)
    }

    /// The `null` value, accepted only by nullable attributes and fields.
    @objc public static func nullValue() -> MatterValue {
        MatterValue(type: .null, rawValue: nil)
    }

    /// Creates a value from a Matter framework value dictionary, such as the `data` entry of an
    /// attribute report.
    ///
    /// - Parameter dictionary: A `[MTRTypeKey: ..., MTRValueKey: ...]` dictionary.
    /// - Returns: The value together with its type, or `nil` if the dictionary carries no type.
    static func from(dictionary: [String: Any]) -> MatterValue? {
        guard let type = dictionary[MTRTypeKey] as? String else {
            return nil
        }
        return MatterValue(
            type: MatterValueType(mtrType: type),
            rawValue: dictionary[MTRValueKey]
        )
    }

    /// The Matter framework representation of this value, ready to be sent as an attribute write
    /// payload or as a command field.
    ///
    /// - Throws: `OperationError.wrongType` if the value's type cannot be encoded.
    func mtrDictionary() throws -> [String: Any] {
        let mtrType = try type.mtrType()
        guard let rawValue else {
            return [MTRTypeKey: mtrType]
        }
        return [MTRTypeKey: mtrType, MTRValueKey: rawValue]
    }
}

extension MatterValueType {

    /// Maps a Matter framework type constant onto this enumeration, falling back to
    /// ``MatterValueType/unsupported`` for types that do not cross the bridge.
    init(mtrType: String) {
        switch mtrType {
        case MTRBooleanValueType: self = .boolean
        case MTRSignedIntegerValueType: self = .signedInteger
        case MTRUnsignedIntegerValueType: self = .unsignedInteger
        case MTRFloatValueType: self = .float
        case MTRDoubleValueType: self = .double
        case MTRUTF8StringValueType: self = .string
        case MTROctetStringValueType: self = .bytes
        case MTRNullValueType: self = .null
        default: self = .unsupported
        }
    }

    /// The Matter framework type constant for this type.
    ///
    /// - Throws: `OperationError.wrongType` for ``MatterValueType/unsupported``.
    func mtrType() throws -> String {
        switch self {
        case .boolean: return MTRBooleanValueType
        case .signedInteger: return MTRSignedIntegerValueType
        case .unsignedInteger: return MTRUnsignedIntegerValueType
        case .float: return MTRFloatValueType
        case .double: return MTRDoubleValueType
        case .string: return MTRUTF8StringValueType
        case .bytes: return MTROctetStringValueType
        case .null: return MTRNullValueType
        case .unsupported: throw OperationError.wrongType
        }
    }
}
