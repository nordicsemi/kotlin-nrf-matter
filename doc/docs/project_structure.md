# Project structure

This is a Kotlin Multiplatform project targeting Android and iOS.

| Module                                                                                                                      | Contents                                                                                                                                                  |
|-----------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| [`/composeApp`](https://www.google.com/search?q=https://github.com/nordicsemi/nordic-matter-app-kmp/tree/main/composeApp)   | Shared Compose Multiplatform UI, screens, navigation, and dependency injection (Koin). Contains KMP source sets (`commonMain`, `androidMain`, `iosMain`). |
| [`/shared`](https://www.google.com/search?q=https://github.com/nordicsemi/nordic-matter-app-kmp/tree/main/shared)           | A thin KMP module that exports `:composeApp` and produces the iOS framework consumed by the Xcode project. Carries no source code of its own.             |
| [`/androidDeps`](https://www.google.com/search?q=https://github.com/nordicsemi/nordic-matter-app-kmp/tree/main/androidDeps) | Android library wrapping the native Matter (CHIP) SDK binaries and the Google Home API (`play-services-home`).                                            |
| [`/core`](https://www.google.com/search?q=https://github.com/nordicsemi/nordic-matter-app-kmp/tree/main/core)               | Shared domain models (`Device`, `DeviceMatterInfo`, etc.) and the logging abstraction (`NordicLogger`) backed by Room on Android and SwiftLogger on iOS.  |
| [`/androidApp`](https://www.google.com/search?q=https://github.com/nordicsemi/nordic-matter-app-kmp/tree/main/androidApp)   | The Android application entry point.                                                                                                                      |
| [`/iosApp`](https://www.google.com/search?q=https://github.com/nordicsemi/nordic-matter-app-kmp/tree/main/iosApp)           | The iOS application entry point (SwiftUI host for Compose UI) and the `nrfMatter` target (`MatterSupport` app extension for system commissioning/QR UI).  |
| [`/ios-matter`](https://www.google.com/search?q=https://github.com/nordicsemi/nordic-matter-app-kmp/tree/main/ios-matter)   | The Swift package wrapping Apple's `Matter` and `MatterSupport` frameworks, compiled to a static library and linked via Cinterop into Kotlin.             |

## composeApp

[`/composeApp`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/composeApp/src) contains
the shared Compose Multiplatform UI, screens, navigation, and dependency injection (Koin), in the
usual Kotlin Multiplatform source sets:

- [
  `commonMain`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/composeApp/src/commonMain/kotlin)
  — code shared across all targets: screens for home, commissioning, bindings, and logs, plus the
  per-device-type controllers for locks, lights, and switches.
- `androidMain` and `iosMain` — platform-specific code, for example wiring up Matter commissioning
  on each platform.

## shared

[`/shared`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/shared) — the Compose
Multiplatform UI: screens for home, commissioning, bindings and logs, per-device-type controllers
for locks, lights and switches, the theme, navigation, and the view models and Koin bindings (
uiModule) behind them. It `api/exports` `:composeApp`, so it is also the iOS framework the Xcode project
consumes — Swift needs a single import shared to reach the whole Kotlin surface. Both Xcode targets
build it through a run-script phase calling `./gradlew :shared:embedAndSignAppleFrameworkForXcode`.

## androidDeps

[`/androidDeps`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/androidDeps) is an
Android library wrapping the native Matter (CHIP) SDK and the Google Home API, exposing helpers such as
`ChipClient`, `ClustersHelper`, and `BindingControllerImpl`.

It depends on prebuilt binaries and vendored Maven artifacts — see
[Vendored dependencies](vendored_dependencies.md).

## core

[`/core`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/core) holds the shared domain
models (`Device`, `DeviceMatterInfo`, `LockDeviceState`, and others) and the `NordicLogger`
abstraction used across platforms — backed by Room on Android and, on iOS, by the Pulse-based
`SwiftLogger` from `ios-matter`.

## androidApp

[`/androidApp`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/androidApp) is the Android
application entry point.

## iosApp

[`/iosApp`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/iosApp/iosApp) is the iOS
application entry point — a SwiftUI host for the shared Compose UI — plus the `nrfMatter` target,
which is the `MatterSupport` app extension that provides the system commissioning and QR-code UI.

Even though the UI is shared, this project is required as the entry point for the iOS app, and is
where you would add any additional SwiftUI code.

## ios-matter

[`/ios-matter`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/ios-matter) is a full
Swift
package — manifest and sources — checked directly into git, the Apple-side counterpart to the
vendoring described above. It used to be resolved from a git remote at an exact tag; it is now built
in place.

**It is not a SwiftPM dependency of the Kotlin build.** It is compiled to a static library and
consumed through plain cinterop, so the Swift object code ends up *inside* the published artifact.
Three Gradle tasks per iOS target do this, in `composeApp/build.gradle.kts`:

| Task                            | What it does                                                                                                                                      |
|---------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| `compileIosMatterSwift<Target>` | Runs `xcodebuild` on `/ios-matter`, which also resolves and builds Pulse.                                                                         |
| `iosMatterStaticLib<Target>`    | Runs `libtool` on the resulting objects to produce `libios-matter.a`, and copies the Swift-generated Objective-C header and module map beside it. |
| `cinteropIosMatter<Target>`     | Translates that module into the `iosMatter` Kotlin package and embeds the archive in the klib.                                                    |

`./gradlew :composeApp:iosMatterStaticLibs` builds the library for every target. All three tasks run
automatically as part of any iOS compile — there is nothing to invoke by hand.

Only the `@objc public` surface of `ios-matter` crosses the boundary; the Swift-generated
Objective-C header is the contract, which is why the Kotlin-facing classes are annotated. Kotlin
reaches them through the `iosMatter.*` package, for example `iosMatter.SwiftLogger` and
`iosMatter.LocalMatterLightController`.

### Editing it

Change a `.swift` file under `/ios-matter/ios-matter` and build. The task inputs cover the sources
and
the manifest, so the library is rebuilt and re-archived automatically. There is no tag to push, no
version to bump, and no lockfile to realign.

Its own remote dependency, [Pulse](https://github.com/kean/Pulse), is still pinned by
`/ios-matter/Package.resolved` and is linked into the same archive.

One consequence of `/ios-matter` staying a local package: SwiftPM refuses `unsafeFlags` in a package
consumed as a dependency, but exempts local ones. That is what lets `/ios-matter/Package.swift` keep
`-enable-library-evolution`. Its comment explains why that flag is needed.
 