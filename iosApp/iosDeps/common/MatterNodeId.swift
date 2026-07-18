import Foundation

/// Conversion helper for interop with native Matter APIs that expect Foundation numeric
/// types rather than the string-encoded device ID used across the `iosDeps` boundary.
extension String {

    /// - Returns: This device ID string parsed as an `NSNumber`, for use with
    ///   Objective-C-based Matter APIs.
    func toMatterNodeId() -> NSNumber {
        NSNumber(value: UInt64(self)!)
    }
}
