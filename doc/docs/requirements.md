# Requirements

This page lists the phones, development kits, and firmware versions the application works with.

## Phone requirements

### Android

To run the nRF Matter application on an Android smartphone, your device must
meet the following hardware and software requirements:

#### Minimum Android Operating System & APIs

- Android OS: Android 8.0 (API level 27) or higher.
- Google Play Services: Required, specifically with access to Google's Home API (used for local
  fabric and ecosystem device commissioning).

#### Hardware requirements

- 64-bit Architecture Only (`arm64-v8a`): The app relies on compiled native CHIP/Matter libraries (
  `libCHIPController.so`) built specifically for 64-bit ARM processors.
- Physical Device Required: It
  will not run on Android Emulators (which run on x86/x86_64 architectures) because the bundled
  native libraries do not include x86 builds.

#### Network and Connectivity requirements

- Wi-Fi: A physical Wi-Fi adapter on the phone connected to the same local network subnet as your
  Thread Border Router or Matter Virtual Device. Discovery relies on local multicast DNS (`mDNS`),
  which does not cross different subnets or VLANs.
- Bluetooth Low Energy (BLE): Required for initial Matter device discovery and Bluetooth LE
  commissioning.

#### Account and Companion pre-requisites

- Google Account: An active Google account signed in on the phone (required for Google Home API/Play
  Services authentication during commissioning).
- Thread Border Router (if using Thread devices): A border router (such as a Nest Hub or Google TV
  Streamer 4K) set up on the same network via the Google Home app so local Thread network
  credentials can be shared with the nRF Matter app.

### iOS

To run the **nRF Matter** application on an Apple device, the hardware and
software must meet the following requirements:

#### Minimum Operating System & Frameworks

- iOS / iPadOS: **iOS 26.0** or higher. Both the vendored `ios-matter` package and the Xcode targets
  set that as their minimum, because the Apple `Matter` and `MatterSupport` APIs the app relies on
  are only available
  there.

#### Hardware & Architecture

- 64-bit iOS Device: Requires an iPhone or iPad powered by a 64-bit Apple Silicon chip (
  A-series) with physical **Bluetooth LE (BLE)** capabilities.
- Simulator vs. Physical Device: While app UI can run in the Xcode simulator, device
  commissioning requires a **physical iPhone or iPad** to handle Bluetooth Low Energy scanning and
  local network multicast discovery.

#### Permissions & Device Profiles

- Local Network & Bluetooth Permissions: The app requires explicit user permission for *Local
  Network* (to discover mDNS nodes) and *Bluetooth* (for initial BLE commissioning).
- iCloud Account: An active Apple ID/iCloud account signed in to the iPhone is required to sync
  and manage local Matter fabric keys securely.

#### Thread & Network Prerequisites

- Thread Border Router (for Thread devices): If you are commissioning Matter-over-Thread
  hardware using the app, a Thread Border Router (such as a Nest Hub or Google TV
  Streamer 4K) must be configured on the same Wi-Fi subnet.
- Wi-Fi Subnet: The iOS device must be connected to a Wi-Fi network that supports **IPv6** and
  allows mDNS traffic without client isolation.

## Supported firmware

The vendored CHIP binaries are built against **Matter 1.5.0**, which was first introduced in
**nRF Connect SDK v3.2.0**. Nordic development kits running Matter firmware built with nRF Connect
SDK v3.2.0 or newer are therefore compatible for testing commissioning and control with this app.

| Development kit | SoC       |
|-----------------|-----------|
| nRF52840 DK     | nRF52840  |
| nRF5340 DK      | nRF5340   |
| nRF54L15 DK     | nRF54L15  |
| nRF54LM20 DK    | nRF54LM20 |

For the authoritative, up-to-date list of supported hardware, see Nordic's
[Matter hardware and memory requirements](https://nrfconnectdocs.nordicsemi.com/addons/ncs-matter/latest/matter/getting_started/hw_requirements.html)
page — new development kits and SoCs are added there as they gain Matter support.

## Supported device types

The application implements controls for the following Matter device types. Accessories reporting any
other device type can still be commissioned and inspected, but not controlled.

| Device type                  | Matter device type ID |
|------------------------------|-----------------------|
| On/off light                 | `0x0100`              |
| Dimmable light               | `0x0101`              |
| Door lock                    | `0x000A`              |
| Light switch                 | `0x0103`              |
| Manufacturer-specific device | `0xFFF10001`          |

See [Overview and user interface](overview.md#supported-device-types) for the controls offered for
each type.

## Network requirements

Both the Android and iOS versions of the **nRF Matter** app rely on the exact same underlying Matter
networking standards.

### Local Network and Addressing

- Active Wi-Fi Connection: The smartphone must be connected to the local Wi-Fi access point
  where the Matter infrastructure resides.
- Full IPv6 Support: Matter operates strictly over IPv6. The local network router/access point
  must have IPv6 enabled and support local IPv6 packet routing between the phone and other IP
  devices.
- Single Subnet / L2 Domain: The phone and all target devices (or border routers) must sit on
  the same broadcast domain/VLAN.

### Device Discovery and Transport

- mDNS (Multicast DNS) Unblocked: The network must allow local UDP multicast traffic (
  specifically port `5353`). Routers with features like "AP Isolation," "Client Isolation," or
  blocked multicast will prevent the app from discovering devices.
- Bluetooth Low Energy (BLE): The smartphone must have physical Bluetooth LE enabled. BLE is
  required for initial "out-of-band" commissioning (scanning the QR code and sending network
  credentials to the device).

### Infrastructure Prerequisites (Matter Core)

- Thread Border Router (For Thread End-Devices): If commissioning Matter-over-Thread hardware (
  rather than Matter-over-Wi-Fi), the local network must contain a Thread Border Router (such as
  Google TV Streamer 4K, Google Nest Hub, or OpenThread Border Router) connected to the same Wi-Fi
  subnet. For more information, see [Thread network credentials](thread_network_credentials.md).
- Active Internet Access: Internet access is required on the local network for ecosystem API
  authentication (Google Play Services / Apple Home Framework) during the initial pairing workflow.

