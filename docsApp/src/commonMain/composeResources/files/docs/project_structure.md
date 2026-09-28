# Project structure

This is a Kotlin Multiplatform project targeting Android and iOS, plus a small Wasm/JS web app that
hosts this documentation. It consists of the following Gradle modules, declared in
`settings.gradle.kts`:

| Module                                                                                             | Contents                                                                                                                                                 |
|------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| [`/lib`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/lib)                              | Shared domain models, Matter cluster and commissioning logic, the `NordicLogger` abstraction, and the platform bridges to CHIP (Android) and Apple's Matter frameworks (iOS). KMP source sets for `commonMain`, `androidMain`, `iosMain`, and `wasmJsMain`. |
| [`/shared`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/shared)                        | The Compose Multiplatform UI: screens, navigation, device-type controllers, and the Koin bindings that wire it to `:lib`. Exports `:lib` and produces the iOS framework consumed by the Xcode project.                                                     |
| [`/androidApp`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/androidApp)                | The Android application entry point.                                                                                                                      |
| [`/iosApp`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/iosApp)                        | The iOS application entry point (SwiftUI host for the shared Compose UI) and the `nrfMatter` target (`MatterSupport` app extension for system commissioning/QR UI).                                                                                        |
| [`/ios-matter`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/ios-matter)                | The Swift package wrapping Apple's `Matter` and `MatterSupport` frameworks, compiled to a static library and linked via Cinterop into Kotlin. Not a Gradle module — see [below](#ios-matter).                                                             |
| [`/doc/App/shared`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/doc/App/shared)        | The Markdown content and rendering for this documentation (including this page), packaged as Compose resources.                                                                                                                                          |
| [`/doc/App/webApp`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/doc/App/webApp)        | The Wasm/JS browser app that hosts the documentation and, for the onboarding walkthrough, embeds the real `:shared` UI for the side-by-side live preview.                                                                                                  |

## lib

[`/lib`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/lib/src) is a Kotlin Multiplatform
library, published as `no.nordicsemi.nrf.matter:matter-support`, in the usual source sets:

- `commonMain` — domain models (`Device`, `BasicInformation`, `LockDeviceState`, and others),
  cluster definitions (`OnOffCluster`, `DoorLockCluster`, `LevelControlCluster`,
  `BasicInformationCluster`, the RVC clusters, and more), the commissioning use cases, and the
  `NordicLogger` abstraction.
- `androidMain` — the Android implementation: `ChipClient`, `BindingController`, the Room-backed
  logger, and the wiring to the native Matter (CHIP) SDK and the Google Home API.
- `iosMain` — the iOS implementation: `MatterCommissionerImpl`, `IosMatterClient`, and the adapters
  that reach the vendored `ios-matter` Swift package through the `iosMatter.*` Cinterop package (for
  example `iosMatter.SwiftLogger`).
- `wasmJsMain` — the browser-facing implementation used by the documentation web app's live preview.

## shared

[`/shared`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/shared) is the Compose
Multiplatform UI: screens for the dashboard, commissioning, bindings, and logs, per-device-type
controllers (locks, lights, switches, contact and temperature sensors, the robotic vacuum cleaner
controls), the theme, navigation, and the view models and Koin bindings behind them. It
`api`-exports `:lib`, so it is also the iOS framework the Xcode project consumes — Swift needs a
single `import shared` to reach the whole Kotlin surface. Both Xcode targets build it through a
run-script phase calling `./gradlew :shared:embedAndSignAppleFrameworkForXcode`.

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

[`/ios-matter`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/ios-matter) is a full Swift
package — manifest and sources — checked directly into git, the Apple-side counterpart to the
vendoring described in [Vendored dependencies](vendored_dependencies.md). It used to be resolved
from a git remote at an exact tag; it is now built in place.

**It is not a SwiftPM dependency of the Kotlin build.** It is compiled to a static library and
consumed through plain cinterop, so the Swift object code ends up *inside* the published artifact.
Three Gradle tasks per iOS target do this, in `shared/build.gradle.kts`:

| Task                            | What it does                                                                                                                                      |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `compileIosMatterSwift<Target>` | Runs `xcodebuild` on `/ios-matter`, which also resolves and builds Pulse.                                                                        |
| `iosMatterStaticLib<Target>`    | Runs `libtool` on the resulting objects to produce `libios-matter.a`, and copies the Swift-generated Objective-C header and module map beside it. |
| `cinteropIosMatter<Target>`     | Translates that module into the `iosMatter` Kotlin package and embeds the archive in the klib.                                                   |

`./gradlew :shared:iosMatterStaticLibs` builds the library for every target. All three tasks run
automatically as part of any iOS compile — there is nothing to invoke by hand.

Only the `@objc public` surface of `ios-matter` crosses the boundary; the Swift-generated
Objective-C header is the contract, which is why the Kotlin-facing classes are annotated. Kotlin
reaches them through the `iosMatter.*` package, for example `iosMatter.SwiftLogger` and
`iosMatter.MatterCommissionerImpl`.

### Editing it

Change a `.swift` file under `/ios-matter/ios-matter` and build. The task inputs cover the sources
and the manifest, so the library is rebuilt and re-archived automatically. There is no tag to push,
no version to bump, and no lockfile to realign.

Its own remote dependency, [Pulse](https://github.com/kean/Pulse), is still pinned by
`/ios-matter/Package.resolved` and is linked into the same archive.

One consequence of `/ios-matter` staying a local package: SwiftPM refuses `unsafeFlags` in a package
consumed as a dependency, but exempts local ones. That is what lets `/ios-matter/Package.swift` keep
`-enable-library-evolution`. Its comment explains why that flag is needed.

## doc/App

[`/doc/App`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/doc/App) is itself a small
Kotlin Multiplatform Compose project — the documentation you are reading now:

- `doc/App/shared` packages the Markdown pages (under
  `composeResources/files/docs`) and the screens that render them.
- `doc/App/webApp` is the Wasm/JS entry point that runs in the browser. For the onboarding
  walkthrough, it depends directly on `:shared` so the documentation can embed the real app UI as a
  live, interactive preview alongside the written steps, instead of static screenshots.

Neither module ships as part of the mobile app; they exist purely to author and serve this
documentation site.
