//
//  SwiftLogger.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 04/05/2026.
//

internal import Pulse
import OSLog
import Foundation
import Combine

/// Centralized logger that writes to the system log and a persistent store, and publishes
/// entries for live observation.
@objc public final class SwiftLogger : NSObject {

    /// Publishes every logged entry as it is recorded, for live observation.
    @objc public static var callback: ((LogEntity) -> Void)? = nil
    
    private static let logger = Logger(subsystem: "nrf.matter", category: "SharedLogger")
    
    private static let store: LoggerStore = {
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
        let url = containerURL.appendingPathComponent("pulse.sqlite")
        return try! LoggerStore(storeURL: url)
    }()
    
    private static func notify(
          level: LogLevel,
          tag: String,
          message: String
      ) {
          let item = LogEntity(
              date: Int64(Date().timeIntervalSince1970 * 1000),
              level: level,
              tag: tag,
              message: message
          )
          callback?(item)
      }

    /// Reads all persisted log entries, most recent first.
    ///
    /// The store is in the shared app group, so this includes entries written by the commissioning
    /// extension's process as well as the app's.
    ///
    /// - Returns: The stored log entries, ordered by creation date descending. Each entry's
    ///   ``LogEntity/date`` is stamped with the current time rather than the stored `createdAt`, so
    ///   only the ordering conveys when things happened.
    /// - Throws: An error if the underlying log store could not be read.
    @objc public static func logs() throws -> [LogEntity] {
        let result = try store.messages(sortDescriptors: [SortDescriptor(\.createdAt, order: .reverse)])
        
        return result.map { item in
            let level: LogLevel = switch item.level {
            case LoggerStore.Level.debug.rawValue: LogLevel.debug
            case LoggerStore.Level.info.rawValue: LogLevel.info
            case LoggerStore.Level.error.rawValue: LogLevel.error
            default: LogLevel.debug
            }
            
            return LogEntity(
                date: Int64(Date().timeIntervalSince1970 * 1000),
                level: level,
                tag: item.label,
                message: item.text,
            )
        }
    }
    
    /// Logs a debug-level message.
    ///
    /// - Parameters:
    ///   - tag: Tag identifying the source of the message. Defaults to `"nRF Matter"`.
    ///   - message: The message to log.
    @objc public static func debug(tag: String = "nRF Matter", _ message: String) {
        store.storeMessage(
            label: tag,
            level: .debug,
            message: message,
        )
        logger.debug("\(message)")
        notify(level: .debug, tag: tag, message: message)
    }
    
    /// Logs an info-level message.
    ///
    /// - Parameters:
    ///   - tag: Tag identifying the source of the message. Defaults to `"nRF Matter"`.
    ///   - message: The message to log.
    @objc public static func info(tag: String = "nRF Matter", _ message: String) {
        store.storeMessage(
            label: tag,
            level: .info,
            message: message,
        )
        logger.info("\(message)")
        notify(level: .info, tag: tag, message: message)
    }
    
    /// Logs an error-level message.
    ///
    /// - Parameters:
    ///   - tag: Tag identifying the source of the message. Defaults to `"nRF Matter"`.
    ///   - message: The message to log.
    @objc public static func error(tag: String = "nRF Matter", _ message: String) {
        store.storeMessage(
            label: tag,
            level: .error,
            message: message,
        )
        logger.error("\(message)")
        notify(level: .error, tag: tag, message: message)
    }
}
