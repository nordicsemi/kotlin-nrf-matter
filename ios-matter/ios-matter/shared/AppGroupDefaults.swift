//
//  AppGroupDefaults.swift
//  ios-matter
//

import Foundation

extension UserDefaults {

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
