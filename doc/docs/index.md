# Get started

nRF Matter for Mobile is a [Matter](https://nrfconnectdocs.nordicsemi.com/ncs/latest/nrf/protocols/matter/index.html)
commissioning and control companion app by [Nordic Semiconductor](https://www.nordicsemi.com/),
available for Android and iOS. It is built with Kotlin Multiplatform and Compose Multiplatform, with
the iOS-specific implementation written in Swift.

## Application 
[![Download on the App Store](assets/AppStore.png)](https://apps.apple.com/ng/app/nrf-matter/id6786253679) [![Download on the App Store](assets/PlayStore.png)](https://play.google.com/store/apps/details?id=no.nordicsemi.nrf.matter)

## Overview

The app acts as a Matter **controller and administrator** on its own local fabric. It commissions
accessories, keeps their node IDs and credentials, reads and writes their clusters, and writes
Access Control List and Binding Cluster entries on them.

The application supports the following features:

- **Commissioning** new Matter devices onto your fabric:
    - Android — through the Android Home API and Google Play Services, provisioning the device onto
      both the Google Home fabric and the app's local fabric.
    - iOS — through Apple's `MatterSupport` framework (`MatterAddDeviceRequest`), onto a local fabric
      managed directly by the app itself (using `Matter.framework` and `MTRDeviceController`), with a
      bundled app extension providing the system QR-code scanning UI.
- **Controlling** commissioned devices — door locks, lights, switches, and manufacturer-specific
  clusters.
- **Managing bindings** between devices, for example a switch controlling a light directly.
- **Viewing logs** for diagnosing commissioning and cluster interactions.

For a description of every screen and control, see
[Overview and user interface](overview.md).

## Getting started quickly

1. Check that your phone and accessory meet the [requirements](requirements.md).
2. [Prepare a Matter device](preparing_a_matter_device.md) to commission — either a simulated device
   or a Nordic development kit running a Matter sample.
3. If the accessory uses Thread, make sure
   [Thread network credentials](thread_network_credentials.md) are available on the phone.
4. [Build and run the application](building.md), then [commission the device](commissioning.md).

## Application source code

The code of the application is open source and [available on GitHub](https://github.com/nordicsemi/kotlin-nrf-matter/tree/main).
You can fork the repository and clone it for secondary development or feature contributions.
