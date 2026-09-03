# Building the application

The application is built from source; it is not distributed through the app stores from this
repository. Check the [requirements](requirements.md) before you start.

Clone the repository first:

```shell
git clone https://github.com/nordicsemi/kotlin-nrf-matter.git
```

All dependencies, including the native Matter binaries and the Google Home API artifacts, are
vendored in the repository, so no additional setup is needed. See
[Vendored dependencies](vendored_dependencies.md) for what that involves.

## Building and running the Android application

Use the run configuration from the run widget in your IDE's toolbar, or build it directly from the
terminal:

On macOS and Linux:

```shell
./gradlew :androidApp:assembleDebug
```

On Windows:

```shell
.\gradlew.bat :androidApp:assembleDebug
```

!!! note "Note"

    The vendored CHIP native libraries are built for `arm64-v8a` only, so the app does not run on an
    Android emulator. Deploy to a physical arm64 device with Google Play Services.

## Building and running the iOS application

Use the run configuration from the run widget in your IDE's toolbar, or open the
[`/iosApp`](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main/iosApp) directory in Xcode and
run it from there.

The Xcode targets build the shared Kotlin framework through a run-script phase, and the vendored
`ios-matter` Swift package is compiled and archived automatically as part of any iOS compile. There
is nothing to invoke by hand.
