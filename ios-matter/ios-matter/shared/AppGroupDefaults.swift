//
//  AppGroupDefaults.swift
//  ios-matter
//

import Foundation

extension UserDefaults {

    /// Returns the `UserDefaults` suite for an app group, failing loudly if it is unavailable.
    ///
    /// `UserDefaults(suiteName:)` returns `nil` when the running process holds no entitlement for
    /// the group, which is the single most likely mistake when wiring this library into a new app:
    /// the group has to be listed in *both* the app's and the commissioning extension's
    /// `com.apple.security.application-groups` entitlement. Trapping here with the group name and
    /// the `Info.plist` key that selected it turns that into a readable crash rather than a
    /// `nil`-unwrap in whichever storage class happened to be touched first.
    ///
    /// - Parameters:
    ///   - identifier: The app group identifier to open.
    ///   - infoKey: The `Info.plist` key the identifier came from, named in the failure message.
    /// - Returns: The suite for `identifier`.
    static func appGroup(_ identifier: String, configuredBy infoKey: String) -> UserDefaults {
        guard let defaults = UserDefaults(suiteName: identifier) else {
            preconditionFailure(
                """
                App group "\(identifier)" is not available to \
                "\(Bundle.main.bundleIdentifier ?? "this process")". Add it to the target's \
                com.apple.security.application-groups entitlement, or name a different group in \
                the target's Info.plist under "\(infoKey)".
                """
            )
        }
        return defaults
    }
}
