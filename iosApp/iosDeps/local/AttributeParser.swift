//
//  AttributeParser.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

/// A type that can be parsed from the raw `Any` value returned when reading a Matter attribute.
public protocol AttributeParser {
    
    /// The concrete type produced by parsing; always the conforming type itself.
    associatedtype Parsable = Self where Parsable == Self

    /// Attempts to convert a raw attribute value into this type.
    ///
    /// - Parameter value: The raw value returned by the Matter framework for an attribute read.
    /// - Returns: The parsed value.
    /// - Throws: `OperationError.wrongType` if `value` cannot be converted.
    static func parse(value: Any) throws -> Parsable
}

extension String: AttributeParser {

    /// Parses a `String` attribute value.
    ///
    /// - Parameter value: The raw value returned by the Matter framework.
    /// - Returns: The value cast to `String`.
    /// - Throws: `OperationError.wrongType` if `value` is not a `String`.
    public static func parse(value: Any) throws -> String {
        if let result = value as? String {
            return result
        }

        throw OperationError.wrongType
    }
}

extension Bool: AttributeParser {

    /// Parses a `Bool` attribute value.
    ///
    /// Matter often represents booleans as integers, so an `Int` value of `0` is treated as
    /// `false` and any other `Int` value is treated as `true`.
    ///
    /// - Parameter value: The raw value returned by the Matter framework.
    /// - Returns: The value cast to `Bool`, or converted from `Int`.
    /// - Throws: `OperationError.wrongType` if `value` is neither a `Bool` nor an `Int`.
    public static func parse(value: Any) throws -> Bool {
        if let bool = value as? Bool {
            return bool
        }
        if let int = value as? Int {
            return int != 0
        }

        throw OperationError.wrongType
    }
}

extension Int: AttributeParser {

    /// Parses an `Int` attribute value.
    ///
    /// - Parameter value: The raw value returned by the Matter framework.
    /// - Returns: The value cast to `Int`.
    /// - Throws: `OperationError.wrongType` if `value` is not an `Int`.
    public static func parse(value: Any) throws -> Int {
        if let int = value as? Int {
            return int
        }

        throw OperationError.wrongType
    }
}

extension Int32: AttributeParser {

    /// Parses an `Int32` attribute value.
    ///
    /// - Parameter value: The raw value returned by the Matter framework.
    /// - Returns: The value cast to `Int32`.
    /// - Throws: `OperationError.wrongType` if `value` is not an `Int32`.
    public static func parse(value: Any) throws -> Int32 {
        if let int = value as? Int32 {
            return int
        }

        throw OperationError.wrongType
    }
}
