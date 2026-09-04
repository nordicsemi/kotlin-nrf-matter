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
    /// A list, such as the Descriptor cluster's `ServerList`.
    case array
    /// A structure, such as an entry of the Descriptor cluster's `DeviceTypeList`.
    case structure
    /// A type this bridge does not represent.
    case unsupported
}

/// One field of a Matter structure, identified by its context tag.
///
/// Matter structures carry no field names on the wire: a field is addressed by the context tag its
/// cluster's schema assigns to it - 0 for `DeviceType` and 1 for `Revision` in a `DeviceTypeStruct`,
/// for instance.
@objc public final class MatterStructureField: NSObject {

    /// The field's context tag, as defined by the cluster's schema.
    @objc public let contextTag: NSNumber

    /// The field's value.
    @objc public let value: MatterValue

    @objc public init(contextTag: NSNumber, value: MatterValue) {
        self.contextTag = contextTag
        self.value = value
        super.init()
    }
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

    /// The elements, for ``MatterValueType/array``.
    ///
    /// Matter reports a list as an array of `[MTRDataKey: <value dictionary>]` entries; each entry
    /// is decoded into a `MatterValue` of its own, so a list of lists nests the same way. An entry
    /// that carries no decodable value is dropped rather than reported as `null`, because a Matter
    /// list has no holes.
    @objc public var array: [MatterValue]? {
        guard type == .array, let entries = rawValue as? [[String: Any]] else { return nil }

        return entries.compactMap { entry in
            guard let data = entry[MTRDataKey] as? [String: Any] else { return nil }
            return MatterValue.from(dictionary: data)
        }
    }

    /// The fields, for ``MatterValueType/structure``.
    ///
    /// Matter reports a structure as an array of `[MTRContextTagKey: ..., MTRDataKey: ...]` entries.
    /// Fields arrive in schema order but are identified by their context tag, so a reader should
    /// look a field up by tag rather than by position.
    @objc public var structure: [MatterStructureField]? {
        guard type == .structure, let fields = rawValue as? [[String: Any]] else { return nil }

        return fields.compactMap { field in
            guard let contextTag = field[MTRContextTagKey] as? NSNumber,
                  let data = field[MTRDataKey] as? [String: Any],
                  let value = MatterValue.from(dictionary: data) else {
                return nil
            }
            return MatterStructureField(contextTag: contextTag, value: value)
        }
    }

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

    /// Wraps a list, e.g. the Binding cluster's `Binding` attribute.
    ///
    /// Elements that cannot be encoded are dropped, so a list built from values this bridge does
    /// not represent is written shorter than it was given rather than being rejected.
    @objc public static func arrayValue(_ values: [MatterValue]) -> MatterValue {
        let entries = values.compactMap { value -> [String: Any]? in
            guard let data = value.mtrDictionaryOrNil() else { return nil }
            return [MTRDataKey: data]
        }

        return MatterValue(type: .array, rawValue: entries)
    }

    /// Wraps a structure, e.g. an entry of the Binding cluster's `Binding` attribute.
    ///
    /// Fields that cannot be encoded are dropped, as in ``arrayValue(_:)``.
    @objc public static func structureValue(_ fields: [MatterStructureField]) -> MatterValue {
        let entries = fields.compactMap { field -> [String: Any]? in
            guard let data = field.value.mtrDictionaryOrNil() else { return nil }
            return [MTRContextTagKey: field.contextTag, MTRDataKey: data]
        }

        return MatterValue(type: .structure, rawValue: entries)
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

    /// ``mtrDictionary()``, or `nil` for a value this bridge cannot encode.
    ///
    /// Lets the container constructors skip an element without failing the whole list.
    private func mtrDictionaryOrNil() -> [String: Any]? {
        try? mtrDictionary()
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
        case MTRArrayValueType: self = .array
        case MTRStructureValueType: self = .structure
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
        case .array: return MTRArrayValueType
        case .structure: return MTRStructureValueType
        case .unsupported: throw OperationError.wrongType
        }
    }
}
