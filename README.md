# nRF Matter for Mobile

[![Download on the App Store](assets/AppStore.png)](https://apps.apple.com/ng/app/nrf-matter/id6786253679) [![Download on the App Store](assets/PlayStore.png)](https://play.google.com/store/apps/details?id=no.nordicsemi.nrf.matter)

A [Matter](https://nrfconnectdocs.nordicsemi.com/ncs/latest/nrf/protocols/matter/index.html)
commissioning and control companion app by
[Nordic Semiconductor](https://www.nordicsemi.com/), built with Kotlin Multiplatform and Compose Multiplatform, with the iOS-specific implementation written in Swift.

The app lets you:

- **Commission** new Matter devices onto your fabric:
    - Android — via the Android Home API / Google Play Services, provisioning the device onto both
      the Google Home fabric and the app’s local fabric.
    - iOS — via Apple's `MatterSupport` framework (`MatterAddDeviceRequest`), onto a local fabric
      managed
      directly by the app itself (using `Matter.framework` / `MTRDeviceController`), with a bundled
      app
      extension providing the system QR-code scanning UI.
- **Control** commissioned devices — door locks, lights, switches, and manufacturer-specific
  clusters.
- **Manage bindings** between devices, e.g. a switch controlling a light.
- **View logs** for diagnosing commissioning and cluster interactions.

## Preparing the work setup

The apps for working need a Matter-enabled device. There are 2 common ways of getting such a device.
1. Using a Matter Virtual Device. It works over a local network, and it's easier to set up.
2. Using one of Nordic's DKs. It will require a working Thread Border Router accessible in a local network.

Those 2 approaches are explained in detail in below section.

### Matter Virtual Device

If you don't have a Thread Border Router or physical accessory handy, Google's
[Matter Virtual Device](https://developers.home.google.com/matter/tools/virtual-device) (MVD) tool
lets you
commission a simulated Matter accessory from a Mac or Linux devices instead.
The nRF Matter implementation currently supports only a subset of the device types available in the Matter Virtual Device application:
1. **Dimmable Light**
2. **Door Lock**

To explore and test additional device types, a compatible Nordic development kit (DK) is required.

#### Testing without a hub: Matter Virtual Device (MVD)


1. Download the MVD `.dmg` for your Mac (Apple Silicon or Intel) and drag it into `Applications`. The Matter Virtual Device (MVD) can be downloaded from the official Google Home developer resources [here](https://developers.home.google.com/matter/tools/virtual-device#install_mvd).
2. Launch MVD and configure the simulated accessory (device type, name, discriminator, Matter port,
   test VID/PID). After launching the application, the initial screen will look like this:
<img  width="500" alt="Screenshot 2026-07-22 at 13 04 52" src="https://github.com/user-attachments/assets/aac2b545-1e16-4ef1-81bc-68d74bbc186b" />

3. Commission it from this app like a real device — it shows a QR code and joins over the macOS
   existing Wi-Fi connection.
4. The Mac running MVD and the phone **must be on the same Wi-Fi network**.
5. Once commissioned, you can control the simulated device from this app.
   
<div align="center">
  <img src="https://github.com/user-attachments/assets/dfc2d152-9898-459e-a768-b8b793201a94" width="49%" />
  <img src="https://github.com/user-attachments/assets/89b3650e-246e-4217-a727-308c960e9604" width="49%" />
</div>

### Nordic Semiconductor DKs

Another option is to configure a Nordic Semiconductor development kit (DK) to act as a Matter device using one of the available Matter samples.
The samples can be installed using the [Matter Quick Start app](https://docs.nordicsemi.com/r/bundle/nrf-connect-for-desktop/page/matter-quick-start-app) 
which is a part of [nRF Connect For Desktop](https://www.nordicsemi.com/Products/Development-tools/nRF-Connect-for-Desktop/Download).

1. **Door Lock** — available directly in the Matter Quick Start App.
2. **Light** — available directly in the Matter Quick Start App.
3. **Switch** - build this [sample](https://github.com/nrfconnect/sdk-nrf/tree/v3.3.0/samples/matter/light_switch) in Visual Studio.
4. **Manufacturer specific cluster + cluster extension** - build this [sample](https://github.com/nrfconnect/sdk-nrf/tree/v3.3.0/samples/matter/manufacturer_specific) in Visual Studio.

> [!TIP]
> All examples display a link with a QR code required for commissioning in the logs. The logs from a DK can be viewed using the [Serial Terminal app](https://docs.nordicsemi.com/r/bundle/nrf-connect-for-desktop/page/serial-terminal-app).
> To view the logs, open the Serial Terminal app and connect the DK using the appropriate serial terminal port. If the device has not yet been commissioned, press the reset button on the DK. The device will then print the logs, including the QR code link, in the logs panel.
> 
> <img width="914" height="21" alt="Screenshot 2026-07-22 at 15 35 56" src="https://github.com/user-attachments/assets/844905d9-5701-4426-b049-5d686369b455" />

## Initial setup: hosting Thread network credentials

Commissioning a **Thread** Matter device requires a Thread Border Router already running on the
local
network, and Thread network credentials available on the phone. Setup code is not part of this
repository — it relies on the OS-provided home hub infrastructure, which is configured once per
network before the app is used.

### Installing Thread Network Credentials on iOS

Matter examples installed on DKs require a **Thread Border Router** connected to the same local network as the app. In
addition, the iPhone must already have the corresponding **Thread Network Credentials** installed.
The credentials are installed via the system API and are available to all apps on the phone.

The process for obtaining these credentials depends on the Thread ecosystem being used. In most
cases, when the Thread network is provided by a device such as a Samsung TV or a dedicated hub such
as **Google TV Streamer 4K**, the manufacturer's companion app must be used to download and install
the Thread Network Credentials on the iPhone.

For example:

- **Samsung**: [SmartThings](https://apps.apple.com/us/app/smartthings/id1222822904)
- **Google**: [Google Home](https://apps.apple.com/us/app/google-home/id680819774)

For detailed instructions, refer to the documentation provided by the device manufacturer. In
general, the required credentials are installed after signing in to the companion app, adding the
Thread-enabled device to the home, and enabling its Thread Border Router functionality.

If the credentials are not immediately available, commissioning a Matter device using the
corresponding companion app may trigger the download and installation of the Thread Network
Credentials.

> **Note**
>
> The app has been tested with **Google TV Streamer 4K**. At the time of writing, Google does not
> provide any alternative method for installing or sharing Thread Network Credentials on iPhone
> other
> than through the **Google Home** app.
>

### Android

Set up a Thread Border Router — such as a Nest Hub (2nd gen) or Google TV Streamer 4K — via the
Google
Home app. Google Play Services (the Home API) then makes the credentials available to this app the
same
way.

- The phone and the hub **must be on the same Wi-Fi network** — credential/device discovery relies
  on
  local-network multicast (mDNS), which doesn't cross subnets or routers.
- The hub needs a **user account signed in** (a Google account added via the Google Home app) before it will share any credentials — a freshly unboxed,
  no-account hub won't work.
- Make sure the router on the network has **IPv6 enabled** — without it, Thread commissioning can
  appear to succeed, but device control might fail afterward.
- Matter standardizes Thread credential sharing across ecosystems, so a single hub can plausibly
  serve
  both platforms — e.g. a Google TV Streamer 4K set up once in Google Home has been observed working
  for both iOS and Android commissioning in this app, without a separate Apple-ecosystem hub.

## Project structure

This is a Kotlin Multiplatform project targeting Android and iOS.

* [`/composeApp`](./composeApp/src) — the Matter layer, published as the `matter-support` library.
  It owns commissioning, cluster access, bindings, persistence, and logging, and carries no UI
  beyond the `CommissioningTask` composable that drives the platform commissioning flow. Contains
  the usual KMP source sets:
    - [`commonMain`](./composeApp/src/commonMain/kotlin) — the platform-agnostic half: domain
      models (`Device`, `DeviceMatterInfo`, `LockDeviceState`, …), cluster definitions,
      repositories/data sources, the decommission and binding use cases, and the `NordicLogger`
      abstraction — backed by Room on Android and, on iOS, by `ios-matter`'s Pulse-based
      `SwiftLogger`.
    - `androidMain` / `iosMain` — platform-specific code, e.g. wiring up Matter commissioning on
      each platform. `androidMain` also holds the wrappers around the native Matter (CHIP) SDK and
      the Google Home API (`ChipClient`, `ClustersHelper`, `BindingControllerImpl`) along with the
      prebuilt binaries they need — see
      [Native Matter (CHIP) SDK binaries](#native-matter-chip-sdk-binaries).
* [`/shared`](./shared) — the Compose Multiplatform UI: screens for home, commissioning, bindings
  and logs, per-device-type controllers for locks, lights and switches, the theme, navigation, and
  the view models and Koin bindings (`uiModule`) behind them. It `api`/`export`s `:composeApp`, so
  it is also the iOS framework the Xcode project consumes — Swift needs a single `import shared` to
  reach the whole Kotlin surface. Both Xcode targets build it through a run-script phase calling
  `./gradlew :shared:embedAndSignAppleFrameworkForXcode`.

* [`/androidApp`](./androidApp) — the Android application entry point.
* [`/iosApp`](./iosApp/iosApp) — the iOS application entry point (SwiftUI host for the shared
  Compose UI), plus the `nrfMatter` target — the `MatterSupport` app extension that provides the
  system commissioning/QR-code UI.
  Even though the UI is shared, this project is required as the entry point for the iOS app, and is
  where
  you'd add any additional SwiftUI code.
* [`/ios-matter`](./ios-matter) — the Swift package that wraps Apple's Matter and MatterSupport
  frameworks, vendored into this repo rather than resolved from git. `:composeApp` cinterops against
  it, so this is where the iOS half of commissioning, cluster access, and the keypair/storage shared
  with the Matter extension lives. See
  [`/ios-matter` — vendored Matter Swift package](#ios-matter--vendored-matter-swift-package).

### Native Matter (CHIP) SDK binaries

[`/composeApp/libs`](./composeApp/libs) contains prebuilt binaries checked directly into git —
they are not built by this Gradle project:

- Jars: `AndroidPlatform.jar`, `CHIPClusterID.jar`, `CHIPClusters.jar`, `CHIPController.jar`,
  `CHIPInteractionModel.jar`, `OnboardingPayload.jar`, `libMatterJson.jar`, `libMatterTlv.jar`.
- Native libraries: [`/composeApp/libs/jniLibs/arm64-v8a`](./composeApp/libs/jniLibs/arm64-v8a) —
  `libCHIPController.so` and `libc++_shared.so` (`arm64-v8a` only — there's no `x86_64` build, so
  these
  libs won't load on an Android emulator, only on a physical arm64 device).

These binaries are built against **Matter 1.5.0**, as provided by Nordic. It comes from Nordic's
fork of Project CHIP,
[`nrfconnect/sdk-connectedhomeip`](https://github.com/nrfconnect/sdk-connectedhomeip) (the NCS
downstream of
[project-chip/connectedhomeip](https://github.com/project-chip/connectedhomeip)) — specifically its
Android
`chip-tool` build target for arm64. To rebuild them from source, follow the instructions provided in the
[nrfconnect/sdk-connectedhomeip](https://github.com/nrfconnect/sdk-connectedhomeip/blob/9895b2bdb4c43b48426930f03e3c05502babd2f0/docs/platforms/android/android_building.md).

> **Note:** if you build  `.jars`/`.so` files yourself against a newer Matter version, this project
> may need some changes to handle the newer version — newer Matter releases can add, rename, or
> change the behavior of
> the APIs these binaries expose.

### `mavenLocal` — vendored Google Home API artifacts (Android only)

This project includes a `./mavenLocal` directory checked directly into git — a pre-built local Maven
repository
with the same directory structure and artifact metadata (`maven-metadata.xml`, checksums) that
Gradle expects.
It is wired up in [`settings.gradle.kts`](./settings.gradle.kts).

When you clone this repo and build, Gradle finds the Home API artifacts from `./mavenLocal`
transparently
— no manual setup required.

#### What `./mavenLocal` contains

The directory vendors the following Android dependencies Google doesn't publish on public Maven
repos:

- **`com.google.android.gms:play-services-home`** at `17.1.0` — the main Google Home Mobile SDK for
  Matter (the Home API). Provides API interfaces, device control, authorization, and commissioning
  services.
- **`com.google.android.gms:play-services-home-types`** at `17.1.0` — a helper library containing
  models
  for device types, traits, command parameters, and other domain types. Its POM declares a
  compile-scope
  dependency on `play-services-home`, so **both artifacts must always be updated together**.

Google's public Maven repo (`google()` / `dl.google.com/android/maven2`) only publishes
`play-services-home`
up to `16.0.0` and doesn't publish `play-services-home-types` at all. Version `17.1.0` introduced
several new
APIs that weren't available in `16.0.0`

#### Gradle setup and availability of Google Home APIs for Android

> **Note:** This is not required just to build the project — `./mavenLocal` folder already ships the
> vendored `17.1.0` artifacts in this repo, so the steps below only matter if you're deliberately
> updating to a newer version.
>
The Google Home APIs are currently in **open beta**, which means they are available to developers,
but they may
change without notice. They are **not** part of the standard Android SDK or the usual Google Play
Services
libraries (`com.google.android.gms.*`), and they are **not yet available** in Maven Central or
Google's
standard Maven repositories (`google()` / `dl.google.com/android/maven2`).
Therefore, getting started requires a few non-standard integration steps.

#### How to get the SDK: manual download

1. Sign in to the [Google Cloud Console](https://console.cloud.google.com/) with your Google
   account.
2. Access the Home APIs early-access program and download the ZIP archive containing the SDK
   artifacts.
3. Extract the SDK into your system's local Maven repository, the `.m2/repository` directory. This is
   the standard path used for local Maven repositories.
    - **Linux:** `~/.m2/repository/`
    - **macOS:** `~/.m2/repository/`
    - **Windows:** `C:\Users\<User_Name>\.m2\repository\`
4. Add `mavenLocal()` to your Gradle `repositories` block so Gradle can find the artifacts —
   [`settings.gradle.kts`](./settings.gradle.kts) already declares it alongside the vendored
   `./mavenLocal` repository.
5. Repeat this process each time the SDK is updated, until Google officially publishes it to a Maven
   repository.

> **Warning:** the Home API is still evolving, so a newer version may introduce breaking changes —
> check `composeApp` and anywhere else the Home API is used (search for `play.services.home` in the
> source), and adjust as needed.
>

### `/ios-matter` — vendored Matter Swift package

[`/ios-matter`](./ios-matter) is a full Swift package — manifest and sources — checked directly into
git, the Apple-side counterpart to the vendoring described above. It used to be resolved from
`git@github.com:sylwester-zielinski/ios-matter.git` at an exact tag; it is now built in place.

**It is not a SwiftPM dependency of the Kotlin build.** It is compiled to a static library and
consumed through plain cinterop, so the Swift object code ends up *inside* the published artifact.
Three Gradle tasks per iOS target do this, in [`build.gradle.kts`](./composeApp/build.gradle.kts):

| Task | Does |
| --- | --- |
| `compileIosMatterSwift<Target>` | runs `xcodebuild` on `/ios-matter`, which also resolves and builds Pulse |
| `iosMatterStaticLib<Target>` | `libtool`s the resulting objects into `libios-matter.a` and copies the Swift-generated ObjC header and module map beside it |
| `cinteropIosMatter<Target>` | translates that module into the `iosMatter` Kotlin package and embeds the archive in the klib |

`./gradlew :composeApp:iosMatterStaticLibs` builds the library for every target. All three tasks run
automatically as part of any iOS compile — there is nothing to invoke by hand.

Only the `@objc public` surface of ios-matter crosses the boundary; the Swift-generated
Objective-C header is the contract, which is why the Kotlin-facing classes are annotated.
Kotlin reaches them through the `iosMatter.*` package (`iosMatter.SwiftLogger`,
`iosMatter.LocalMatterLightController`, …).

**Why not `localSwiftPackage`.** A SwiftPM declaration is published as
`SwiftPMDependency.Local` carrying an **absolute** path — inspect
`matter-support-<version>-swiftpm-metadata.json` in any published artifact to see it. A consumer
resolving `matter-support` from Maven therefore cannot find the Swift code at all, and the Swift
sources are not in the klib either. Only the version-pinned `swiftPackage(url = ...)` form is
publishable, and that means a second source of truth for the Swift code. Archiving the objects into
the cinterop klib avoids both problems: `no.nordicsemi.nrf.matter:matter-support` is now
self-contained, and Xcode needs no package graph — neither `iosApp` nor `nrfMatter` imports
`ios_matter`, both reach it through Kotlin bridges such as `KeychainKt.prepareKeychain()`.

**Editing it.** Change a `.swift` file under `/ios-matter/ios-matter` and build — the task inputs
cover the sources and the manifest, so the library is rebuilt and re-archived automatically. There
is no tag to push, no version to bump, and no lockfile to realign. Its own remote dependency,
[Pulse](https://github.com/kean/Pulse), is still pinned by
[`/ios-matter/Package.resolved`](./ios-matter/Package.resolved) and is linked into the same archive.

One consequence of `/ios-matter` staying a local package: SwiftPM refuses `unsafeFlags` in a package
consumed as a dependency but exempts local ones, which is what lets
[`/ios-matter/Package.swift`](./ios-matter/Package.swift) keep `-enable-library-evolution`. Its
comment explains why that flag is needed.

### Build and run the Android application

Use the run configuration from the run widget in your IDE's toolbar, or build it directly from the
terminal:

- on macOS/Linux
  ```shell
  ./gradlew :androidApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :androidApp:assembleDebug
  ```

### Build and run the iOS application

Use the run configuration from the run widget in your IDE's toolbar, or open the [
`/iosApp`](./iosApp)
directory in Xcode and run it from there.

## Requirements

- Android: minSdk 27+, a device with Google Play Services (Home API is used for commissioning). The
  vendored CHIP native libraries are `arm64-v8a` only, so a physical arm64 device is required — the
  app won't run on an emulator.
- iOS: iOS 26.0 or newer — both [`/ios-matter`](./ios-matter/Package.swift) and the Xcode targets set
  that as their minimum, because Apple's `Matter`/`MatterSupport` APIs the app relies on are only
  available there. Building needs an Xcode recent enough for `swift-tools-version: 6.3`
  (Xcode 26+). Open [`/iosApp`](./iosApp) in Xcode to build/run.

## Firmware supported

The vendored CHIP binaries (see
[Native Matter (CHIP) SDK binaries](#native-matter-chip-sdk-binaries))
are built against **Matter 1.5.0**, first introduced in **nRF Connect SDK v3.2.0**, so below listed
Nordic DK running Matter firmware built with NCS v3.2.0 or
newer should be compatible for testing commissioning/control with this app.

| Development Kit | SoC       |
|-----------------|-----------|
| nRF52840 DK     | nRF52840  |
| nRF5340 DK      | nRF5340   |
| nRF54L15 DK     | nRF54L15  |
| nRF54LM20 DK    | nRF54LM20 |

For the authoritative, up-to-date list of supported hardware, see Nordic's
[Matter hardware and memory requirements](https://nrfconnectdocs.nordicsemi.com/ncs/latest/nrf/protocols/matter/getting_started/hw_requirements.html)
page — new DKs and SoCs are added there as they gain Matter support.


## License

Copyright © Nordic Semiconductor. Licensed under a BSD-3-Clause style license — see the [LICENSE](LICENSE) for full terms.

