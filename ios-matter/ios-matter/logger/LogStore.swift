//
//  LogStore.swift
//  ios-matter
//

import Foundation

final class LogStore {

    private enum Key {
        static let date = "d"
        static let level = "l"
        static let tag = "t"
        static let message = "m"
    }

    private let fileURL: URL
    private let maxBytes: Int

    init(fileURL: URL, maxBytes: Int = 4 * 1024 * 1024) {
        self.fileURL = fileURL
        self.maxBytes = maxBytes
    }

    func append(_ entry: LogEntity) {
        guard let line = encode(entry) else { return }

        withLockedFile(create: true) { handle in
            handle.seekToEndOfFile()
            handle.write(line)

            guard handle.offsetInFile > UInt64(maxBytes) else { return }
            trim(handle)
        }
    }

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
