# Overview and user interface

The app is build in Compose Multiplatform, the user interface provides identical screens, labels,
and controls across both Android and iOS. The app automatically adapts to the system theme on both
platforms. The only platform-specific behavior occurs during commissioning, where execution is
handed off to the native operating system — `Google Play Services` on Android and Apple’s
`MatterSupport`
on iOS.

Upon launch, the app opens to the Dashboard. If no accessories have been commissioned, a
getting-started screen appears with options to begin setup, access Matter documentation, and view
the app version. Once a device is commissioned, the Dashboard dynamically updates to display the
list of commissioned devices.

<div align="center">
  <img src="./screenshots/dashboard_empty_android.png" alt="Dashboard with no devices on Android" />
  <img src="./screenshots/dashboard_empty_ios.png" alt="Dashboard with no devices on iOS" />
</div>

## Common interface

The following elements are present on every screen.

| UI element            | Description                                                                                                                                                                           |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Top app bar           | Displays the title of the current screen.                                                                                                                                             |
| Bottom navigation bar | The bottom navigation bar allows to switch between the three main screens: **Dashboard**, **Bindings**, and **Logs Panel**.                                                           |
| Add device button     | A floating **+** button in the bottom-right corner allows the user to commission other matter devices. This button appears once at least one device has been commissioned to the app. |
| Back navigation       | The system back gesture or button closes the current screen. From **Bindings** or **Logs Panel** it returns to the Dashboard; from the Dashboard it leaves the app.                   |

The top app bar title depends on the current screen.

| Screen                                      | Bottom navigation label          | Top app bar title |
|---------------------------------------------|----------------------------------|-------------------|
| Dashboard, no devices commissioned          | Dashboard                        | `nRF Matter`      |
| Dashboard, at least one device commissioned | Dashboard                        | `Dashboard`       |
| Bindings                                    | Bindings                         | `Bindings`        |
| Logs                                        | Logs Panel                       | `Logs`            |
| Commissioning                               | *(not in the bottom navigation)* | `Commissioning`   |

## Dashboard

The Dashboard is the home screen and lists every device commissioned onto the app's fabric.

### Getting-started screen

When you open the nRF Matter app and no devices have been commissioned yet, the main dashboard
displays the Getting Started screen. It features an Add Device button to initiate the commissioning
process via QR code or manual setup payload. Once your first accessory is successfully provisioned
onto the network, this screen automatically updates to showcase your active device cards on the main
dashboard.

| UI element           | Description                                                                                       |
|----------------------|---------------------------------------------------------------------------------------------------|
| **Add New Device**   | The user can begin by commissiong the matter device by adding a new device. Starts commissioning. |
| **What is Matter?**  | Opens Nordic's Matter documentation in the system browser.                                        |
| `Version: <version>` | The application version.                                                                          |

### Dashboard Device Cards

Once commissioned, your accessory is added to the home dashboard as a dedicated Device Card. Each
card provides quick access to essential device details and controls:

* Quick Controls: Directly operate primary actions such as toggling an On/Off light, controlling a
  door lock, or triggering a custom manufacturer command and Remove/Decommission device.
* Device Status: View the device name, type, and current connection state.
* Detailed View: Tap the card to open the complete device view, where you can inspect device
  metadata read directly from the accessory's Basic Information cluster (`0x0028`).

<div align="center">
  <img src="./screenshots/device_card_light.png" alt="Expanded light device card" />
  <img src="./screenshots/device_information.png" alt="Matter Device Information sheet" />
</div>

| UI element                     | Description                                                                                                                                                                                                                        |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Card header                    | Device icon, title, subtitle, and the primary control for the device type, for example the on/off switch of a light. Tap to expand or collapse the card.                                                                           |
| Device-specific controls       | Shown when the card is expanded. The available controls depend on the Matter device type — see [Supported device types](#supported-device-types).                                                                                  |
| **Matter Device information**  | An expandable preview row displays the Vendor and Firmware version. Tapping this row opens the [Matter Device Information](#matter-device-information) sheet, where you can view the complete set of Basic Information attributes. |
| **Remove/Decommission Device** | Decommissions / Removes the matter accessory from the app's fabric — see [Removing a device](#removing-a-device).                                                                                                                  |

## Supported device types

The app includes dedicated control interfaces for standard Matter device types, including
Dimmable Lights, Light Switches, and Door Locks. Additionally, the app supports Nordic
Manufacturer-Specific Clusters, allowing developers to interact with custom attributes and commands
on proprietary accessories. Any device type not explicitly implemented by the app is categorized as
an Unsupported Device Type, where essential metadata (such as Product Name, Vendor ID, and Product
ID) can still be inspected and the accessory can be decommissioned as needed.

| Device type                  | Matter device type ID | Controls available in the app                                                   |
|------------------------------|-----------------------|---------------------------------------------------------------------------------|
| On/off light                 | `0x0100`              | On/off switch, **Brightness Control** slider                                    |
| Dimmable light               | `0x0101`              | On/off switch, **Brightness Control** slider                                    |
| Door lock                    | `0x000A`              | Lock/unlock control                                                             |
| Light switch                 | `0x0103`              | None — the switch is a client node and is configured on the **Bindings** screen | 
| Manufacturer-specific device | `0xFFF10001`          | **Generate number** button, **LED** switch, button state indicator              | 
| Any other device type        | —                     | None — reported as unsupported                                                  |

Regardless of the device type, every card provides the **Matter Device information** sheet and the
**Remove/Decommission Device** button.

### Lights

Once a Light Bulb is commissioned, you can control it directly through the app—with support for both
standard On/Off and Dimmable lights. The interface includes a power switch and a brightness slider
that updates its percentage in real time as you drag. The brightness command sends as soon as you
release the slider. Because the app subscribes to level attribute updates, the control stays in sync
if the light is adjusted externally.

### Light bulb controls

The following table describes the controls available for On/Off and Dimmable lights.

* On/Off - The app writes the On/Off cluster (`0x0006`) on the accessory. It continuously
  subscribes to this attribute, ensuring the toggle switch updates in real time if the light is
  turned on or off externally.
* Dimmable - The app writes the Level Control cluster (`0x0008`). The percentage updates dynamically
  while dragging, and the command sends upon release. The app subscribes to the level attribute,
  ensuring the updates in real time if brightness changes externally.
* Binding capability - The light bulb can serve as a binding target device. For more information,
  see [Configuring bindings](bindings.md).

| UI element                    | Description                                                                                                                                                                                           |
|-------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| On/off switch                 | Writes the On/Off cluster (`0x0006`) on the accessory. The app subscribes to the attribute, so the switch also follows changes made from outside the app.                                             |
| **Brightness Control** slider | Writes the Level Control cluster (`0x0008`). The percentage next to the label updates while dragging, and the command is sent when the slider is released. The app subscribes to the level attribute. |
| **Binding capability**        | An informational label indicating that the light bulb can be used as a binding target.                                                                                                                |

### Door lock

Once a Door Lock is commissioned, you can control it directly through the app. The interface
features a lock/unlock toggle that responds to your taps. The app subscribes to
lock state attribute updates, the control stays continuously in sync if the lock is manually or
externally operated.

### Door lock controls

* Lock/unlock - The app writes the Door Lock cluster (`0x0101`) on the accessory. It continuously
  subscribes to this attribute, ensuring the toggle switch updates in real time if the lock is
  operated externally.

| UI element         | Description                                                                                                                     |
|--------------------|---------------------------------------------------------------------------------------------------------------------------------|
| Lock state control | A status indicator showing **Locked** or **Unlocked**. Tapping it sends the corresponding Door Lock cluster (`0x0101`) command. |

### Light switches

The Light Switch device is a Matter *client* node, this light switch binds with target lighting
devices to control their light states. Because switches operate as clients, they do not expose
controllable states within the app.

| UI element                               | Description                                                                                                              |
|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| Title and subtitle                       | **Light Switch** and **Bind the switch with other devices**.                                                             |
| `Cluster 0x001D (Descriptor Device Map)` | Explains that the node operates as a Matter client whose Binding Table must be configured to link it with target lights. |
| Binding hint                             | Directs you to manage the switch's targets on the **Bindings** screen.                                                   |

### Manufacturer-specific device

A Manufacturer-Specific Cluster (also called a Vendor-Specific Cluster) is a non-standardized
cluster used in Matter devices to support proprietary or custom features. While standard Matter
clusters (like `0x0006` for On/Off or `0x0101` for Door Lock) ensure interoperability across
different
smart home ecosystems (Apple, Google, Amazon, etc.), vendor-specific clusters allow manufacturers to
implement features unique to their hardware.

The app allows you to commission and interact with devices running Nordic Semiconductor's
Manufacturer Specific Cluster sample. The app reads the device’s data model directly during
discovery. Any custom endpoints, attributes, or commands defined via the nRF Connect Matter
Manufacturer Cluster Editor are automatically exposed these custom features directly in the mobile
UI without needing extra client-side development

### Manufacturer-specific device controls

* Generate random number — The app invokes a custom command added to the Basic Information cluster (
  `0x28`) via a cluster extension. The generated result is displayed in the UI under Random number
 
* On/Off — LED switch — The app writes to the manufacturer-specific cluster (`0xFFF1FC01`) to turn
  the
  LED on the development kit on or off. Continuous subscription ensures the switch toggle reflects
  the physical LED state if changed externally.
* Button state — The app subscribes to custom button state attributes or events on the accessory.
  The UI dynamically updates to show real-time state changes (such as pressed, released, or
  long-pressed) whenever a physical button (button 1 in the Nordic kit) on the device is operated.

The app's support for manufacturer-specific clusters is demonstrated in the
[manufacturer-specific sample](https://github.com/nrfconnect/sdk-nrf/tree/v3.3.0/samples/matter/manufacturer_specific).

| UI element          | Description                                                                                                                                                                                                         |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Generate number** | Invokes a command added to the Basic Information cluster (`0x28`) by a cluster extension. The returned value is shown below as **Random number**; a placeholder is displayed until the first value arrives.         |
| **LED** switch      | Writes the manufacturer-specific cluster (`0xFFF1FC01`) to turn the LED on the development kit on or off.                                                                                                           |
| Button state        | A read-only indicator that follows a subscription to the same manufacturer-specific cluster. It reads **Press button 01** until the physical button on the kit is pressed, and **Button pressed** while it is held. |

### Unsupported device types

Devices not fully supported by the app are categorized as Unsupported Device Type. However, you can
still access **Matter Device Information** to inspect the accessory or use **Remove/Decommission
Device** to unpair or remove the accessory.

## Matter Device Information

The Matter Device Information screen provides essential device metadata read directly from the
accessory's Basic Information cluster (`0x0028`). It displays key operational details, including:

* Vendor & Identity: Product Name, Vendor Name, Vendor ID (VID), and Product ID (PID).

* Firmware & Build: Software Version, Software Version String, and Serial Number.

This information is available for all commissioned accessories, including Unsupported Device Types.

| Field                 | Attribute |
|-----------------------|-----------|
| Product Name          | `0x0003`  |
| Vendor ID             | `0x0002`  |
| Product ID            | `0x0004`  |
| Vendor Name           | `0x0001`  |
| Software Version      | `0x0009`  |
| Serial Number         | `0x000F`  |
| Unique ID             | `0x0012`  |
| Specification Version | `0x0013`  |

## Removing a device

**Remove / Decommission Device** removes the accessory from the app's fabric and clears all
associated bindings.

!!! note "Note"

    Applicable to Android only- Since the device is commissioned through Android's Google Play services and Home API, the device is linked across all integrated fabrics. Decommissioning disassociates the device across these APIs, returning it to a factory-ready state.

Once decommissioned, the device is ready to be re-commissioned at any time by scanning its QR code
or entering the setup code.

### Force Remove

If removing the fabric from the device fails (e.g., if the device is offline), a prompt will give
you the option to Force Remove it. Force removing deletes the device from the app’s repository
immediately without waiting to unlink the fabric directly on the device.
!!! note "Note"

    Force-removing a device only clears the app's own records. The accessory keeps the fabric
    credentials it was given, so it may need to be factory reset before it can be commissioned again.
