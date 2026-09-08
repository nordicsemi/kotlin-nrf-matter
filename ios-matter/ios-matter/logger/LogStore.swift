//
//  LogStore.swift
//  ios-matter
//

import Foundation

/// Append-only log store shared by the app and the commissioning extension.
///
/// Lives in the shared app group container, because the two run in separate processes and the
/// app's log viewer has to show what the extension recorded.
///
/// One JSON object per line. JSON escapes newlines, so a multi-line message can never be mistaken
/// for two entries, and a line that fails to parse is skipped rather than failing the whole read -
/// a log store is not worth losing history over.
///
/// Every operation opens the file, takes an exclusive `flock`, acts, and closes. That is more
/// syscalls than holding a descriptor open, but it is safe across both processes at log volumes
/// where the writes are rare and small, and it means a crash can never leave the lock held.
final class LogStore {

    /// Entry as stored on disk. Keys are single characters because every line carries them.
    private enum Key {
        static let date = "d"
        static let level = "l"
        static let tag = "t"
        static let message = "m"
    }

    private let fileURL: URL
    private let maxBytes: Int

    /// Creates a store backed by `fileURL`.
    ///
    /// - Parameters:
    ///   - fileURL: File to append to. Created on first write.
    ///   - maxBytes: Size at which the oldest half of the entries is dropped.
    init(fileURL: URL, maxBytes: Int = 4 * 1024 * 1024) {
        self.fileURL = fileURL
        self.maxBytes = maxBytes
    }

    /// Appends one entry, dropping the oldest half of the file if it has grown past `maxBytes`.
    ///
    /// Failures are swallowed: logging must never take down a commissioning flow, and there is
    /// nowhere useful to report a failure to write a log line to.
    func append(_ entry: LogEntity) {
        guard let line = encode(entry) else { return }

        withLockedFile(create: true) { handle in
            handle.seekToEndOfFile()
            handle.write(line)

            guard handle.offsetInFile > UInt64(maxBytes) else { return }
            trim(handle)
        }
    }

    /// Reads every stored entry, most recent first.
    ///
    /// - Returns: The stored entries in reverse order of writing.
    func read() -> [LogEntity] {
        var entries: [LogEntity] = []

        withLockedFile(create: false) { handle in
            handle.seek(toFileOffset: 0)
            let data = handle.readDataToEndOfFile()
            entries = data
                .split(separator: UInt8(ascii: "\n"))
                .compactMap { decode(Data($0)) }
        }

        return entries.reversed()
    }

    /// Rewrites the file with only its newer half, keeping whole lines.
    ///
    /// Called with the lock already held, so no other process can append into the gap between the
    /// read and the truncate.
    private func trim(_ handle: FileHandle) {
        handle.seek(toFileOffset: 0)
        let data = handle.readDataToEndOfFile()
        let lines = data.split(separator: UInt8(ascii: "\n"))
        let kept = lines.suffix(lines.count / 2)

        handle.seek(toFileOffset: 0)
        handle.truncateFile(atOffset: 0)
        for line in kept {
            handle.write(Data(line))
            handle.write(Data([UInt8(ascii: "\n")]))
        }
    }

    /// Opens the file, takes an exclusive lock across processes, runs `body`, then unlocks.
    ///
    /// - Parameters:
    ///   - create: Whether to create the file if it does not exist. `false` for reads, so that
    ///     reading before anything has been logged is a no-op rather than creating an empty file.
    ///   - body: Work to run while holding the lock.
    private func withLockedFile(create: Bool, _ body: (FileHandle) -> Void) {
        if create, !FileManager.default.fileExists(atPath: fileURL.path) {
            FileManager.default.createFile(atPath: fileURL.path, contents: nil)
        }

        guard let handle = try? FileHandle(forUpdating: fileURL) else { return }
        defer { try? handle.close() }

        guard flock(handle.fileDescriptor, LOCK_EX) == 0 else { return }
        defer { flock(handle.fileDescriptor, LOCK_UN) }

        body(handle)
    }

    private func encode(_ entry: LogEntity) -> Data? {
        let object: [String: Any] = [
            Key.date: entry.date,
            Key.level: entry.level.rawValue,
            Key.tag: entry.tag,
            Key.message: entry.message,
        ]
        guard var data = try? JSONSerialization.data(withJSONObject: object) else { return nil }
        data.append(UInt8(ascii: "\n"))
        return data
    }

    private func decode(_ line: Data) -> LogEntity? {
        guard !line.isEmpty,
              let object = try? JSONSerialization.jsonObject(with: line) as? [String: Any],
              let date = object[Key.date] as? Int64,
              let level = object[Key.level] as? Int,
              let tag = object[Key.tag] as? String,
              let message = object[Key.message] as? String else {
            return nil
        }

        return LogEntity(
            date: date,
            level: LogLevel(rawValue: level) ?? .debug,
            tag: tag,
            message: message
        )
    }
}
