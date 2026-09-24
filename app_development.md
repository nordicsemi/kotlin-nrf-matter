# App development

This repository contains complete, open-source code for the application. You can explore the codebase to examine device cluster interactions, node management, and binding workflows. By cloning or forking the public repository, you can build the app locally, integrate core Matter commissioning and control logic into your own multiplatform applications, or contribute fixes and features back to the community.

[![Release notes](assets/ReleaseNotes.png)](https://github.com/nordicsemi/kotlin-nrf-matter/releases) [![App user guide](assets/AppUserGuide.png)](./README.md)

---

**Contents:** [Project structure](#project-structure) · [Vendored dependencies](#vendored-dependencies) · [Building and running the application](#building-and-running-the-application) · [License](#license)

## Project structure

The nRF Matter for Mobile project consists of the following modules:

| Module                          | Contents  |
|---------------------------------|-----------|
| [`/lib`](./lib)                 | The shared Matter layer, published as the `matter-support` library. Contains KMP source sets (`commonMain`, `androidMain`, `iosMain`). |
| [`/shared`](./shared)           | Compose Multiplatform UI: screens for home, commissioning, bindings
  and logs, per-device-type controllers for locks, lights and switches, the theme, navigation, and the view models and Koin bindings (`uiModule`) behind them.              |
| [`/androidApp`](./androidApp)   | The Android application entry point.                                                                                                                      |
| [`/iosApp`](./iosApp)           | The iOS application entry point (SwiftUI host for Compose UI) and the `nrfMatter` target (`MatterSupport` app extension for system commissioning/QR UI).  |
| [`/ios-matter`](./ios-matter)   | The Swift package wrapping Apple's `Matter` and `MatterSupport` frameworks, compiled to a static library and linked via Cinterop into Kotlin.             |

### lib

[`/lib`](./composeApp/src) contains the shared Matter layer, published as the `matter-support` library. It owns commissioning, cluster access, bindings, persistence, and logging, and carries no UI beyond the `CommissioningTask` composable that drives the platform commissioning flow. Contains the usual KMP source sets:

* [`commonMain`](./composeApp/src/commonMain/kotlin) — code shared across all targets: screens for home, commissioning, bindings, and logs; per-device-type controllers for locks, lights, and switches; the theme; navigation; and the view models and Koin bindings (`uiModule`) behind them.
* `iosMain` — iOS-specific code, for example wiring up Matter commissioning on each platform.
* `androidMain` — Android-specific code, for example wiring up Matter commissioning on each platform. It also holds the wrappers around the native Matter (CHIP) SDK and the Google Home API (`ChipClient`, `ClustersHelper`, `BindingControllerImpl`) along with the prebuilt binaries they need — see [Native Matter (CHIP) SDK binaries](#vendored-dependencies-for-android).

### shared

[`/shared`](./shared) is a thin Kotlin Multiplatform module with the Compose Multiplatform UI: screens for home, commissioning, bindings and logs, per-device-type controllers for locks, lights and switches, the theme, navigation, and the view models and Koin bindings (`uiModule`) behind them. It has no application source of its own. It `api`-exports `:composeApp` and builds the static `shared` framework that the Xcode project imports. (Swift needs a single `import shared` to reach the whole Kotlin surface.) Both Xcode targets produce it through a run-script phase that calls `./gradlew :shared:embedAndSignAppleFrameworkForXcode`.

### androidApp

[`/androidApp`](./androidApp) is the Android application entry point.

### iosApp

[`/iosApp`](./iosApp/iosApp) is the iOS application entry point — a SwiftUI host for the shared Compose UI — plus the `nrfMatter` target, which is the `MatterSupport` app extension that provides the system commissioning and QR-code UI.

Even though the UI is shared, this project is required as the entry point for the iOS app, and is where you would add any additional SwiftUI code.

### ios-matter

[`/ios-matter`](./ios-matter) is a full Swift package — manifest and sources — checked directly into git, the Apple-side counterpart to the vendoring described above. It used to be resolved from a git remote at an exact tag; it is now built in place.

**This is not a SwiftPM dependency of the Kotlin build.** It is compiled to a static library and consumed through plain cinterop, so the Swift object code ends up *inside* the published artifact. The following Gradle tasks per iOS target do this, in `lib/build.gradle.kts`:

| Task                            | What it does |
|---------------------------------|--------------|
| `compileIosMatterSwift<Target>` | Runs `xcodebuild` on `/ios-matter`, which also resolves and builds Pulse.                                                                         |
| `iosMatterStaticLib<Target>`    | Runs `libtool` on the resulting objects to produce `libios-matter.a`, and copies the Swift-generated Objective-C header and module map beside it. |
| `cinteropIosMatter<Target>`     | Translates that module into the `iosMatter` Kotlin package and embeds the archive in the klib.                                                    |

`./gradlew :lib:iosMatterStaticLibs` builds the library for every target. All three tasks run automatically as part of any iOS compile — there is nothing to invoke by hand.

Only the `@objc public` surface of `ios-matter` crosses the boundary; the Swift-generated Objective-C header is the contract, which is why the Kotlin-facing classes are annotated. Kotlin reaches them through the `iosMatter.*` package, for example `iosMatter.SwiftLogger` and `iosMatter.LocalMatterLightController`.

<details>
  <summary>Reasons for not using `localSwiftPackage`</summary>

A SwiftPM declaration is published as `SwiftPMDependency.Local` carrying an _absolute_ path (inspect `matter-support-<version>-swiftpm-metadata.json` in any published artifact to see). A consumer resolving `matter-support` from Maven therefore cannot find the Swift code at all, and the Swift sources are not in the `klib` either. Only the version-pinned `swiftPackage(url = ...)` form is publishable, and that means a second source of truth for the Swift code. Archiving the objects into the `cinterop` `klib` avoids both problems: `no.nordicsemi.nrf.matter:matter-support` is now self-contained, and Xcode needs no package graph — neither `iosApp` nor `nrfMatter` imports `ios_matter`, both reach it through Kotlin bridges such as `KeychainKt.prepareKeychain()`.
</details>

### How to edit the Swift package

Change a `.swift` file under `/ios-matter/ios-matter` and build. The task inputs cover the sources and the manifest, so the library is rebuilt and re-archived automatically. There is no tag to push, no version to bump, and no lockfile to realign.

Its own remote dependency, [Pulse](https://github.com/kean/Pulse), is still pinned by `/ios-matter/Package.resolved` and is linked into the same archive.

One consequence of `/ios-matter` staying a local package: SwiftPM refuses `unsafeFlags` in a package consumed as a dependency, but exempts local ones. That is what lets `/ios-matter/Package.swift` keep `-enable-library-evolution`. Its comment explains why that flag is needed.

---

## Vendored dependencies

The Android build of the app uses several vendored dependencies, as described in the following sections.

The iOS build of the app uses Apple's Matter frameworks and does not require any vendored dependencies.

### Vendored dependencies for Android

Two dependencies are checked directly into the repository rather than resolved from a remote repository:

* Native Matter (CHIP) binaries
* Google Home API Maven artifacts

Cloning the repository and building is enough — none of them require manual setup.

#### Native Matter (CHIP) SDK binaries

[`/lib/libs`](./lib/libs) contains prebuilt binaries checked directly into git. They are prebuilt binaries and imported into this project.

| Import type      | Files      |
|------------------|------------|
| Jars             | `AndroidPlatform.jar`, `CHIPClusterID.jar`, `CHIPClusters.jar`, `CHIPController.jar`, `CHIPInteractionModel.jar`, `OnboardingPayload.jar`, `libMatterJson.jar`, `libMatterTlv.jar`      |
| Native libraries | `libCHIPController.so` and `libc++_shared.so`, in [`/lib/libs/jniLibs/arm64-v8a`](./lib/libs/jniLibs/arm64-v8a) |

**Note:** The native libraries are built for `arm64-v8a` only. There is no `x86_64` build, so these libraries will not load on an Android emulator — a physical arm64 device is required.

These binaries are built against Matter v1.5.0, as provided by Nordic Semiconductor (first used in the nRF Connect SDK v3.2.0). The Matter integration comes from Nordic Semiconductor's fork of the Matter project (formerly known as Connected Home over IP, CHIP), [`nrfconnect/sdk-connectedhomeip`](https://github.com/nrfconnect/sdk-connectedhomeip), which is a the downstream of [`project-chip/connectedhomeip`](https://github.com/project-chip/connectedhomeip). Specifically, the Android binaries come from the CHIP Tool's build target for `arm64`.

To rebuild them from source, follow the [Android building instructions](https://github.com/nrfconnect/sdk-connectedhomeip/blob/9895b2bdb4c43b48426930f03e3c05502babd2f0/docs/platforms/android/android_building.md) in that repository.

**Note:** If you build the `.jar` and `.so` files yourself against a newer Matter version, this project may need changes to handle it. Newer Matter releases can add, rename, or change the behavior of the APIs these binaries expose.

#### Google Home API artifacts

The repository includes a [`/mavenLocal`](./mavenLocal) directory checked directly into git — a pre-built local Maven repository with the same directory structure and artifact metadata (`maven-metadata.xml`, checksums) that Gradle expects. It is wired up in `settings.gradle.kts`.

When you clone this repository and build, Gradle finds the Home API artifacts from `./mavenLocal` transparently. No manual setup is required.

##### What the directory contains

The [`/mavenLocal`](./mavenLocal) directory vendors the following Android dependencies, which Google does not publish on public Maven repositories:

| Artifact | Version  | Purpose   |
|----------|----------|-----------|
| `com.google.android.gms:play-services-home`       | `17.1.0` | The main Google Home Mobile SDK for Matter (the Home API). Provides API interfaces, device control, authorization, and commissioning services. |
| `com.google.android.gms:play-services-home-types` | `17.1.0` | A helper library containing models for device types, traits, command parameters, and other domain types.                                       |

**Note:** Both artifacts must always be updated together because the POM of `play-services-home-types` declares a compile-scope dependency on `play-services-home`.

Google's public Maven repository (`google()`, that is `dl.google.com/android/maven2`) only publishes `play-services-home` up to `16.0.0`, and does not publish `play-services-home-types` at all. Version `17.1.0` introduced several new APIs that were not available in `16.0.0`.

##### Updating to a newer version

**Note:** The `./mavenLocal` directory already ships the vendored `17.1.0` artifacts, so the steps below only matter if you are deliberately updating to a newer version.

The Google Home APIs are currently in open beta, which means they are available to developers but may change without notice. They are not part of the standard Android SDK or the usual Google Play Services libraries (`com.google.android.gms.*`), and they are not yet available in Maven Central or Google's standard Maven repositories. Getting started therefore requires a few non-standard integration steps:

1. Sign in to the [Google Cloud Console](https://console.cloud.google.com/) with your Google
   account.
1. Access the Home APIs early-access program and download the ZIP archive containing the SDK
   artifacts.
1. Extract the SDK into your system's local Maven repository, the `.m2/repository` directory:

    * **Linux:** `~/.m2/repository/`
    * **macOS:** `~/.m2/repository/`
    * **Windows:** `C:\Users\<User_Name>\.m2\repository\`

1. Add `mavenLocal()` to your Gradle `repositories` block so Gradle can find the artifacts.
   `settings.gradle.kts` already declares it alongside the vendored `./mavenLocal` repository.
1. Repeat this process each time the SDK is updated, until Google officially publishes it to a Maven repository.

**Caution:** The Home API is still evolving, so a newer version may introduce breaking changes. Check `lib` and anywhere else the Home API is used: search for `play.services.home` in the source and adjust as needed.

---

## Building and running the application

The application is built from source; it is not distributed through the app stores from this repository. Check the [minimum OS requirements](./README.md#minimum-os-requirements) and [other requirements](./README.md#requirements) before you start.

Clone the repository first:

```shell
git clone https://github.com/nordicsemi/kotlin-nrf-matter.git
```

All dependencies, including the native Matter binaries and the Google Home API artifacts, are vendored in the repository, so no additional repository setup is needed. See [Vendored dependencies](#vendored-dependencies) for more information.

### How to build and run the Android application

Use the run configuration from the run widget in your IDE's toolbar, or build it directly from the terminal:

* macOS and Linux:

  ```shell
  ./gradlew :androidApp:assembleDebug
  ```

* Windows:

  ```shell
  .\gradlew.bat :androidApp:assembleDebug
  ```

**Note:** The vendored CHIP native libraries are built for `arm64-v8a` only, so the app does not run on an Android emulator. Deploy to a physical arm64 device with Google Play Services.

### How to build and run the iOS application

Use the run configuration from the run widget in your IDE's toolbar, or open the [`/iosApp`](./iosApp) directory in Xcode and run it from there.

The Xcode targets build the shared Kotlin framework through a run-script phase, and the vendored `ios-matter` Swift package is compiled and archived automatically as part of any iOS compile. There is nothing to invoke by hand.

## License

Copyright © Nordic Semiconductor. Licensed under a BSD-3-Clause style license — see the [LICENSE](LICENSE) for full terms.