# Requirements

This page lists the phones, development kits, and firmware versions the application works with.

## Phone requirements

### Android

To run the nRF Matter application on an Android smartphone, your device must
meet the following hardware and software requirements:

#### Minimum Android Operating System & APIs

- Android OS: Android 8.1 (API level 27) or higher.
- Google Play Services: Required, specifically with access to Google's Home API (used for local
  fabric and ecosystem device commissioning).

#### Hardware requirements

- Physical Device Required: The nRF matter app requires a physical device to perform commissioning. It
  will not run on Android Emulators.
- 64-bit Architecture Only (`arm64-v8a`): The app relies on compiled native CHIP/Matter libraries (
  `libCHIPController.so`) built specifically for 64-bit ARM processors.

#### Thread & Network Prerequisites

- Thread Border Router (for Thread devices): If you are commissioning Matter-over-Thread
  hardware using the app, a Thread Border Router (such as a Nest Hub or Google TV
  Streamer 4K) must be configured on the same Wi-Fi subnet. For more information, see [Thread network credentials](thread_network_credentials.md).
- Wi-Fi Subnet: The Android device must be connected to a Wi-Fi network that supports **IPv6** and
  allows mDNS traffic without client isolation.
- Bluetooth Low Energy (BLE): Required for initial Matter device discovery and Bluetooth LE
  commissioning.

#### Account and Companion pre-requisites

- Google Account: An active Google account signed in on the phone (required for Google Home API/Play
  Services authentication during commissioning).

### iOS

To run the **nRF Matter** application on an Apple device, the hardware and
software must meet the following requirements:

#### Minimum Operating System & Frameworks

- iOS / iPadOS: **iOS 26.0** or higher. Both the vendored `ios-matter` package and the Xcode targets
  set that as their minimum, because the Apple `Matter` and `MatterSupport` APIs the app relies on
  are only available there.

#### Hardware & Architecture

- 64-bit iOS Device: Requires an iPhone or iPad powered by a 64-bit Apple Silicon chip (
  A-series) with physical **Bluetooth LE (BLE)** capabilities.
- Simulator vs. Physical Device: While app UI can run in the Xcode simulator, device
  commissioning requires a **physical iPhone or iPad** to handle Bluetooth Low Energy scanning and
  local network multicast discovery.

#### Permissions & Device Profiles

- Local Network: The app requires explicit user permission for *Local Network* so it can communicate
  with Matter device over Wi-Fi.

#### Thread & Network Prerequisites

- Thread Border Router (for Thread devices): If you are commissioning Matter-over-Thread
  hardware using the app, a Thread Border Router (such as a Nest Hub or Google TV
  Streamer 4K) must be configured on the same Wi-Fi subnet. For more information, see [Thread network credentials](thread_network_credentials.md).
- Wi-Fi Subnet: The iOS device must be connected to a Wi-Fi network that supports **IPv6** and
  allows mDNS traffic without client isolation.

## Nordic Semiconductor development kits

You can configure a Nordic Semiconductor development kit to act as a Matter device using one of the available Matter samples.
Check the list of [supported device types](overview.md#supported-device-types).

For the authoritative, up-to-date list of supported hardware, see Nordic's [Matter hardware and memory requirements](https://nrfconnectdocs.nordicsemi.com/addons/ncs-matter/latest/matter/getting_started/hw_requirements.html) page - new development kits and SoCs are added there as they gain Matter support.
You can also check the [sample documentation](https://nrfconnectdocs.nordicsemi.com/addons/ncs-matter/latest/samples/index.html) for the list of development kits supported by each sample.

These samples can be installed using the [Matter Quick Start app](https://docs.nordicsemi.com/r/bundle/nrf-connect-for-desktop/page/matter-quick-start-app), which is a part of [nRF Connect for Desktop](https://www.nordicsemi.com/Products/Development-tools/nRF-Connect-for-Desktop/Download). Alternatively, you can install them from the [Matter add-on to the nRF Connect SDK](https://nrfconnectdocs.nordicsemi.com/addons/ncs-matter/latest/index.html).

<details>
  <summary>Finding the commissioning QR code</summary>

All samples print a link with the QR code required for commissioning in their logs. The logs from a development kit can be viewed using the [Serial Terminal app](https://docs.nordicsemi.com/r/bundle/nrf-connect-for-desktop/page/serial-terminal-app).

To view the logs, open the Serial Terminal app and connect the kit using the appropriate serial port. If the device has not yet been commissioned, press the reset button on the kit. The device then prints the logs, including the QR code link, in the logs panel.

![QR code link in the serial log](assets/qr_code_serial_log.png "QR code link in the serial log")

</details>

## Supported device types

The application implements controls for the following Matter device types. Accessories reporting any
other device type can still be commissioned and inspected, but not controlled.

| Device type                  | Matter device type ID |
|------------------------------|-----------------------|
| On/off light                 | `0x0100`              |
| Dimmable light               | `0x0101`              |
| Door lock                    | `0x000A`              |
| Light switch                 | `0x0103`              |
| Contact sensor               | `0x0015`              |
| Temperature sensor           | `0x0302`              |
| Robotic vacuum cleaner       | `0x0074`              |
| Manufacturer-specific device | `0xFFF10001`          |

See [Overview and user interface](overview.md#supported-device-types) for the controls offered for
each type.


