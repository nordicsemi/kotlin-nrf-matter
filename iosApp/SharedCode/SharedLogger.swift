//
//  SharedLogger.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 04/05/2026.
//

internal import Pulse
import OSLog
import Foundation
import Combine

/// Severity level of a logged message.
public enum Level {
    /// Verbose diagnostic information.
    case debug
    /// General informational message.
    case info
    /// An error condition.
    case error
}

/// A single log entry.
public struct LogItem {
    /// The date and time the entry was created.
    public let createdAt: Date
    /// The severity level of the entry.
    public let level: Level
    /// The tag identifying the source of the entry.
    public let tag: String
    /// The logged message text.
    public let message: String
}

/// Centralized logger that writes to the system log and a persistent store, and publishes
/// entries for live observation.
public class SharedLogger {

    /// Publishes every logged entry as it is recorded, for live observation.
    nonisolated(unsafe) public static let logPublisher = PassthroughSubject<LogItem, Never>()
    
    private static let logger = Logger(subsystem: "nrf.matter", category: "SharedLogger")
    
    private static let store: LoggerStore = {
        let containerURL = FileManager.default.containerURL(
            forSecurityApplicationGroupIdentifier: SharedConsts.sharedStorage
        )!
        let url = containerURL.appendingPathComponent("pulse.sqlite")
        return try! LoggerStore(storeURL: url)
    }()
    
    private static func notify(
          level: Level,
          tag: String,
          message: String
      ) {
          let item = LogItem(
              createdAt: Date(),
              level: level,
              tag: tag,
              message: message
          )
          logPublisher.send(item)
      }

    /// Reads all persisted log entries, most recent first.
    ///
    /// - Returns: The stored log entries ordered by creation date, descending.
    /// - Throws: An error if the underlying log store could not be read.
    public static func logs() throws -> [LogItem] {
        let result = try store.messages(sortDescriptors: [SortDescriptor(\.createdAt, order: .reverse)])
        
        return result.map { item in
            let level: Level = switch item.level {
            case LoggerStore.Level.debug.rawValue: Level.debug
            case LoggerStore.Level.info.rawValue: Level.info
            case LoggerStore.Level.error.rawValue: Level.error
            default: Level.debug
            }
            
            return LogItem(
                createdAt: item.createdAt,
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
    public static func debug(tag: String = "nRF Matter", _ message: String) {
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
    public static func info(tag: String = "nRF Matter", _ message: String) {
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
    public static func error(tag: String = "nRF Matter", _ message: String) {
        store.storeMessage(
            label: tag,
            level: .error,
            message: message,
        )
        logger.error("\(message)")
        notify(level: .error, tag: tag, message: message)
    }
}
