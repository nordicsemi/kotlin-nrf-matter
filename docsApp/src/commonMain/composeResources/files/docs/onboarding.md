# Onboarding

A guided first run of nRF Matter, using a simulated accessory instead of real hardware, so no
development kit and no Thread Border Router are required.

[Download nRF Matter on the App Store](https://apps.apple.com/ng/app/nrf-matter/id6786253679)

The onboarding flow can be a little tricky, especially for those who have never worked with Matter
devices before. This walkthrough goes through it end to end, screen by screen.

To keep the first contact as simple as possible, Google's
[Matter Virtual Device](https://developers.home.google.com/matter/tools/virtual-device) (MVD) is used
as the accessory. It runs on a computer and speaks Matter over Wi-Fi, which removes the need for a
Thread Border Router entirely.

## What is needed

| | Requirement | Why |
|-----|-------------|-----|
| 💻 | A Mac or Linux computer running [MVD](https://developers.home.google.com/matter/tools/virtual-device#install_mvd) | Hosts the simulated Matter accessory |
| 📱 | An iPhone on iOS 26 or newer with nRF Matter installed | Commissions and controls the accessory |
| 📶 | A single Wi-Fi network | Carries all communication between the two |

!!! important "Same network required"

    The phone and the computer running MVD must be on the same Wi-Fi network. Discovery relies on
    local-network multicast (mDNS), which does not cross subnets or routers.

!!! note "Limited device support"

    Only a subset of the MVD device types is fully supported. Other types may still be commissioned
    and offer partial functionality. For the full range of device types, a Nordic development kit
    is required.

## Creating a Matter Virtual Device

The accessory is created first, on the computer. The result is a QR code that the phone will scan in
the next section.

**Step 1 · Open MVD**

This is the initial screen displayed after opening the MVD app.

<img src="./screenshots/onboarding_mvd_launch.png" alt="The MVD app on first launch" />

**Step 2 · Choose a device type**

Expand the list of available devices and select one of the supported types.

<img src="./screenshots/onboarding_mvd_select_device.png" alt="Selecting a device type in MVD" />

**Step 3 · Create the device**

Press the **Create device** button.

<img src="./screenshots/onboarding_mvd_create.png" alt="The Create device button in MVD" />

**Step 4 · Show the QR code**

The device is now created, but not yet commissioned. This screen can later be used to change the
state of the device. Press **QR code** to display the code required for commissioning.

<img src="./screenshots/onboarding_mvd_qr_button.png" alt="The QR code button in MVD" />

**Step 5 · Keep the code on screen**

The displayed QR code is what the phone scans. Once the device has been successfully commissioned,
the **Device commissioned** label appears and this screen can be closed.

<img src="./screenshots/onboarding_mvd_qr_code.png" alt="The commissioning QR code displayed by MVD" />

!!! tip "Keep MVD running"

    Leave MVD running for the rest of this guide. Closing it takes the accessory off the network,
    and the phone will no longer be able to reach it.

## Commissioning the device on iOS

With the QR code on screen, the flow moves to the phone. Steps 2 to 9 run inside a system-managed
extension rather than in nRF Matter itself, which is why the screens look like part of iOS.

**Step 1 · Open nRF Matter**

This is the initial screen displayed after opening the app. No devices are listed yet.

<img src="./screenshots/onboarding_ios_home.png" alt="The nRF Matter home screen" />

**Step 2 · Start the flow**

Press **Add New Device**. Control is then delegated from the main app to an extension managed by the
iOS system.

<img src="./screenshots/onboarding_ios_add_device.png" alt="The Add New Device button" />

**Step 3 · Allow local network access**

Access to discovering devices on the local network has to be granted first. This is what allows the
phone to detect the MVD accessory on the same Wi-Fi network.

<img src="./screenshots/onboarding_ios_network_permission.png" alt="The local network permission prompt" />

**Step 4 · Scan the QR code**

The camera is now active, and the QR code displayed in MVD can be scanned.

<img src="./screenshots/onboarding_ios_qr_scanner.png" alt="The QR code scanner" />

**Step 5 · Confirm the device**

Once the QR code has been scanned and the accessory detected, a confirmation screen is displayed.

<img src="./screenshots/onboarding_ios_device_found.png" alt="The device found confirmation screen" />

**Step 6 · Accept the uncertified accessory**

Because the accessory is under development and lacks proper certification, iOS asks for
confirmation. Press **Add Anyway** to proceed.

<img src="./screenshots/onboarding_ios_uncertified_warning.png" alt="The uncertified accessory warning" />

**Step 7 · Pick a location**

The next screen asks for the location to which the device should be added. The selected location has
no meaning in this app, so the first option is sufficient. Press **Continue**.

<img src="./screenshots/onboarding_ios_location.png" alt="The location picker" />

**Step 8 · Name the device**

A custom name can be assigned, although it is not used in the current version of the app. Press
**Continue**.

<img src="./screenshots/onboarding_ios_name_device.png" alt="The device naming screen" />

**Step 9 · Finish**

Press **Done** on the confirmation screen. Control is then returned to nRF Matter, and the MVD window
on the computer shows the **Device commissioned** label.

<img src="./screenshots/onboarding_ios_finish.png" alt="The commissioning success screen" />

## Exploring a commissioned device

**The device list**

All commissioned devices are listed on the main screen, together with the components related to
their specific device type and functionality.

<img src="./screenshots/onboarding_device_list.png" alt="The home screen listing the commissioned device" />

**Expanding a device**

Pressing a device expands its content, revealing the controls for that device type.

<img src="./screenshots/onboarding_device_expand.png" alt="A device entry in the list" />

**Controlling it**

Changes made here are sent to the accessory over Wi-Fi, and are reflected live in the MVD window on
the computer.

<img src="./screenshots/onboarding_device_controls.png" alt="The expanded device controls" />

**Matter Device information**

Pressing **Matter Device information** opens a new section with details about the device.

<img src="./screenshots/onboarding_device_info_entry.png" alt="The Matter Device information entry" />

**The details**

Data exposed by the Basic Information Cluster is listed here.

<img src="./screenshots/onboarding_device_info_details.png" alt="The Matter device information section" />

## Where to go next

At this point the Matter Virtual Device has been created, commissioned, and is controllable from the
nRF Matter app. Commissioning is persistent: the device stays available across app restarts and does
not need to be added again unless it is removed from the app.

The same flow applies to physical Matter accessories, with a
[prepared Matter device](preparing_a_matter_device.md) and, for Thread accessories,
[Thread network credentials](thread_network_credentials.md) taking the place of MVD. Anyone moving
from using the app to working on it can start at [Project structure](project_structure.md).

Feedback and issue reports are welcome.
