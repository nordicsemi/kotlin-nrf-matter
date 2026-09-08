//
//  SwiftLogger.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 04/05/2026.
//

import OSLog
import Foundation

/// Centralized logger that writes to the system log and a persistent store, and publishes
/// entries for live observation.
@objc public final class SwiftLogger : NSObject {

    /// Publishes every logged entry as it is recorded, for live observation.
    @objc public static var callback: ((LogEntity) -> Void)? = nil

    private static let logger = Logger(subsystem: "nrf.matter", category: "SharedLogger")

    private static let store: LogStore = {
        guard let containerURL = FileManager.default.containerURL(
            forSecurityApplicationGroupIdentifier: SharedConsts.sharedStorage
        ) else {
            // Same failure and same fix as `UserDefaults.appGroup(_:configuredBy:)`, spelled out
            // here too because the logger is usually the first thing any process touches.
            preconditionFailure(
                """
                App group "\(SharedConsts.sharedStorage)" is not available to \
                "\(Bundle.main.bundleIdentifier ?? "this process")". Add it to the target's \
                com.apple.security.application-groups entitlement, or name a different group in \
                the target's Info.plist under "\(SharedConsts.sharedAppGroupInfoKey)".
                """
            )
        }

        return LogStore(fileURL: containerURL.appendingPathComponent("matter-log.jsonl"))
    }()

    /// Records one entry: to the store, to the system log, and to ``callback``.
    private static func log(level: LogLevel, tag: String, message: String) {
        let entry = LogEntity(
            date: Int64(Date().timeIntervalSince1970 * 1000),
            level: level,
            tag: tag,
            message: message
        )

        store.append(entry)

        switch level {
        case .debug: logger.debug("\(message)")
        case .info: logger.info("\(message)")
        case .error: logger.error("\(message)")
        }

        callback?(entry)
    }

    /// Reads all persisted log entries, most recent first.
    ///
    /// The store is in the shared app group, so this includes entries written by the commissioning
    /// extension's process as well as the app's.
    ///
    /// - Returns: The stored log entries, newest first, each carrying the time it was logged.
    /// - Throws: Nothing. Declared `throws` because Kotlin already calls this through the
    ///   generated `logsAndReturnError(_:)`; a read failure yields an empty list instead, since a
    ///   missing or partly corrupt log file is not worth surfacing as an error.
    @objc public static func logs() throws -> [LogEntity] {
        store.read()
    }

    /// Logs a debug-level message.
    ///
    /// - Parameters:
    ///   - tag: Tag identifying the source of the message. Defaults to `"nRF Matter"`.
    ///   - message: The message to log.
    @objc public static func debug(tag: String = "nRF Matter", _ message: String) {
        log(level: .debug, tag: tag, message: message)
    }

    /// Logs an info-level message.
    ///
    /// - Parameters:
    ///   - tag: Tag identifying the source of the message. Defaults to `"nRF Matter"`.
    ///   - message: The message to log.
    @objc public static func info(tag: String = "nRF Matter", _ message: String) {
        log(level: .info, tag: tag, message: message)
    }

    /// Logs an error-level message.
    ///
    /// - Parameters:
    ///   - tag: Tag identifying the source of the message. Defaults to `"nRF Matter"`.
    ///   - message: The message to log.
    @objc public static func error(tag: String = "nRF Matter", _ message: String) {
        log(level: .error, tag: tag, message: message)
    }
}
