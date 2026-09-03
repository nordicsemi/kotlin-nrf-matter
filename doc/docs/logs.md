# Viewing logs

The Logs Panel shows a single combined log for the whole app, covering commissioning, cluster reads
and writes, and binding operations. It is the first place to look when an accessory does not
respond.

Log entries are persisted locally, ensuring they remain available across application restarts. On
Android, logs are saved to a local database. On iOS, logging is managed via the [Pulse](https://github.com/kean/Pulse) library, which
stores logs in a file configured using App Groups, making them available to both the main app and
its app extension.

<div align="center">
  <img src="./screenshots/logs_panel.png" alt="Logs Panel" />
</div>

## User interface

| UI element         | Description                                                                                                                                                                                      |
|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Search field       | Perform a case-insensitive text search across all log messages. The clear icon at the end of the field resets the search.                                                                        |
| Level filter chips | **ALL**, **INFO**, **DEBUG**, and **ERROR** filters the logs to focus on the selected log levels.                                                                                                |
| Log view           | Filtered entries are displayed in chronological order, with the newest logs at the top of logs view. If no entries meet the search, it shows a message: **No logs match current search/filter.** |

Each log is formatted across two lines. The top line displays the timestamp on the left, with the
log level and optional tag on the right. The second line contains the main log message. For better
visibility and readability, each log level is assigned a distinct color.

| Level   | Color |
|---------|-------|
| `INFO`  | Green |
| `DEBUG` | Blue  |
| `ERROR` | Red   |

## Exporting logs

!!! note "Note"

    The log cannot be cleared or exported from within the app at the moment. To share a trace, we recommend capturing it from the
    device using the platform tooling, such as `adb logcat` on Android or the Console app on macOS
    for iOS.
