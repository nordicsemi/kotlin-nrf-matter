//
//  IOSLoggerImpl.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 04/05/2026.
//

import Foundation
import Combine

/// Native iOS implementation backing composeApp's `IOSLogger`.
///
/// Forwards log calls to the shared `SharedLogger` and republishes its log stream through
/// `onLogLine` so native and shared code observe the same log entries.
@objc public class IOSLoggerImpl: NSObject {

    private var cancellables = Set<AnyCancellable>()
    private var onLogLine: ((String) -> Void)?

    /// Subscribes to `SharedLogger`'s publisher so logs emitted from shared code
    /// are also forwarded through `onLogLine`.
    @objc public override init() {
        super.init()
        SharedLogger.logPublisher
            .sink { [weak self] log in
                self?.onLogLine?(log.message)
            }
            .store(in: &cancellables)
    }

    /// Registers the callback invoked for every log line as it happens (own calls below, and
    /// anything logged elsewhere in shared/native code through `SharedLogger`).
    @objc public func setOnLogLine(_ onLogLine: @escaping (String) -> Void) {
        self.onLogLine = onLogLine
    }

    /// Logs an info-level message through the shared logger.
    ///
    /// - Parameters:
    ///   - tag: The subsystem or component identifier the message is associated with.
    ///   - message: The message to log.
    @objc public func info(tag: String, message: String) {
        SharedLogger.info(tag: tag, message)
        onLogLine?(message)
    }

    /// Logs a debug-level message through the shared logger.
    ///
    /// - Parameters:
    ///   - tag: The subsystem or component identifier the message is associated with.
    ///   - message: The message to log.
    @objc public func debug(tag: String, message: String) {
        SharedLogger.debug(tag: tag, message)
        onLogLine?(message)
    }

    /// Logs an error-level message through the shared logger.
    ///
    /// - Parameters:
    ///   - tag: The subsystem or component identifier the message is associated with.
    ///   - message: The message to log.
    @objc public func error(tag: String, message: String) {
        SharedLogger.error(tag: tag, message)
        onLogLine?(message)
    }

    /// Retrieves the persisted log history from the shared logger and delivers it asynchronously.
    ///
    /// - Parameter onReady: Called with the list of log entries once they have been loaded and mapped.
    @objc public func getLogs(onReady: @escaping ([SwiftLogEntity]) -> Void) {
        let logs = try? SharedLogger.logs()

        let result: [SwiftLogEntity] = logs?.compactMap { item in
            let level: Int32 = switch item.level {
            case Level.debug: 1
            case Level.info: 0
            case Level.error: 2
            @unknown default: 0
            }

            return SwiftLogEntity(
                date: Int64(item.createdAt.timeIntervalSince1970 * 1000),
                level: level,
                tag: item.tag,
                message: item.message
            )
        } ?? []

        onReady(result)
    }
}
