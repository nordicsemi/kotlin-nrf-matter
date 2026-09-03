# Preparing a Matter device

To work with the application you need a Matter-enabled accessory device. There are two common ways
of getting one:

1. Using a **Matter Virtual Device**. It works over a local network and is easier to set up.
2. Using one of **Nordic's development kits**. This requires a working Thread Border Router
   accessible on the local network.

Both approaches are explained in the following sections.

## Matter Virtual Device

If you do not have a Thread Border Router or a physical accessory at hand, Google's
[Matter Virtual Device](https://developers.home.google.com/matter/tools/virtual-device) (MVD) tool
lets you commission a simulated Matter accessory from a Mac or Linux machine instead.

The nRF Matter implementation currently supports only a subset of the device types available in the
Matter Virtual Device application:

- Dimmable Light
- Door Lock

To explore and test additional device types, a compatible Nordic development kit is required.

### Testing without a hub
!!! note "Prerequisite"

The Mac running MVD and the phone **must be on the same Wi-Fi network**.

1. Download the MVD `.dmg` for your Mac (Apple Silicon or Intel) and drag it into `Applications`.
   You can download the Matter Virtual Device from the [official Google Home developer resources] (https://developers.home.google.com/matter/tools/virtual-device#install_mvd).
2. Launch MVD and configure the simulated accessory: device type, name, discriminator, Matter port,
   and test VID/PID. After launching the application, the initial screen looks as follow:

    <img width="500" alt="Matter Virtual Device initial screen" src="https://github.com/user-attachments/assets/aac2b545-1e16-4ef1-81bc-68d74bbc186b" />

3. Commission it from this app like a real device.

   It shows a QR code and joins over the existing Wi-Fi® connection of the macOS host.

4. Once commissioned, you can control the simulated device from the app.

<div align="center">
  <img src="https://github.com/user-attachments/assets/dfc2d152-9898-459e-a768-b8b793201a94" width="49%" />
  <img src="https://github.com/user-attachments/assets/89b3650e-246e-4217-a727-308c960e9604" width="49%" />
</div>

## Nordic Semiconductor development kits

Another option is to configure a Nordic Semiconductor development kit to act as a Matter device
using one of the available Matter samples. The samples can be installed using the
[Matter Quick Start app](https://docs.nordicsemi.com/r/bundle/nrf-connect-for-desktop/page/matter-quick-start-app),
which is a part of
[nRF Connect for Desktop](https://www.nordicsemi.com/Products/Development-tools/nRF-Connect-for-Desktop/Download).

| Sample                                                  | How to get it                                                                                                                                                                          |
|---------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Door Lock**                                           | Available directly in the Matter Quick Start app, or build the [light bulb sample](https://github.com/nrfconnect/sdk-nrf/tree/v3.3.0/samples/matter/light_bulb) in Visual Studio Code. |
| **Light**                                               | Available directly in the Matter Quick Start app, or build the [door lock sample](https://github.com/nrfconnect/sdk-nrf/tree/v3.3.0/samples/matter/lock) in Visual Studio Code.        |
| **Switch**                                              | Build the [light switch sample](https://github.com/nrfconnect/sdk-nrf/tree/v3.3.0/samples/matter/light_switch) in Visual Studio Code.                                                  |
| **Manufacturer-specific cluster and cluster extension** | Build the [manufacturer-specific sample](https://github.com/nrfconnect/sdk-nrf/tree/v3.3.0/samples/matter/manufacturer_specific) in Visual Studio Code.                                |

!!! tip "Finding the commissioning QR code"

    All samples print a link with the QR code required for commissioning in their logs. The logs from
    a development kit can be viewed using the
    [Serial Terminal app](https://docs.nordicsemi.com/r/bundle/nrf-connect-for-desktop/page/serial-terminal-app).

    To view the logs, open the Serial Terminal app and connect the kit using the appropriate serial
    port. If the device has not yet been commissioned, press the reset button on the kit. The device
    then prints the logs, including the QR code link, in the logs panel.

    <img width="914" alt="QR code link in the serial log" src="https://github.com/user-attachments/assets/844905d9-5701-4426-b049-5d686369b455" />

## Next steps

- If the accessory uses Thread, continue with
  [Thread network credentials](thread_network_credentials.md).
- Otherwise, go straight to [Commissioning devices](commissioning.md).
