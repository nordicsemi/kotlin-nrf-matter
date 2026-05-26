//
//  AttributeParser.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

public protocol AttributeParser {
    associatedtype Parsable = Self where Parsable == Self
    
    static func parse(value: Any) throws -> Parsable
}

extension String: AttributeParser {
    
    public static func parse(value: Any) throws -> String {
        if let result = value as? String {
            return result
        }
        
        throw OperationError.wrongType
    }
}

extension Bool: AttributeParser {
    
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
    
    public static func parse(value: Any) throws -> Int {
        if let int = value as? Int {
            return int
        }
        
        throw OperationError.wrongType
    }
}

extension Int32: AttributeParser {
    
    public static func parse(value: Any) throws -> Int32 {
        if let int = value as? Int32 {
            return int
        }
        
        throw OperationError.wrongType
    }
}

extension UInt32: AttributeParser {
    
    public static func parse(value: Any) throws -> UInt32 {
        if let int = value as? UInt32 {
            return int
        }
        
        throw OperationError.wrongType
    }
}
