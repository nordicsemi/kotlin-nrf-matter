<div align="center">

# Onboarding

### From nothing to a commissioned Matter device

A guided first run of **nRF Matter**, using a simulated accessory instead of real hardware —
so no development kit and no Thread Border Router are required.

<a href="https://apps.apple.com/ng/app/nrf-matter/id6786253679"><img src="assets/AppStore.png" alt="Download on the App Store" height="40"></a>

</div>

---

The onboarding flow can be a little tricky, especially for those who have never worked with Matter
devices before. This document walks through it end to end, screen by screen.

To keep the first contact as simple as possible, Google's
[Matter Virtual Device](https://developers.home.google.com/matter/tools/virtual-device) (MVD) is used
as the accessory. It runs on a computer and speaks Matter over Wi-Fi, which removes the need for a
Thread Border Router entirely.

## What is needed

|  | Requirement | Why |
| :-: | --- | --- |
| 💻 | A **Mac or Linux computer** running [MVD](https://developers.home.google.com/matter/tools/virtual-device#install_mvd) | Hosts the simulated Matter accessory |
| 📱 | An **iPhone on iOS 26 or newer** with [nRF Matter](https://apps.apple.com/ng/app/nrf-matter/id6786253679) installed | Commissions and controls the accessory |
| 📶 | A **single Wi-Fi network** | Carries all communication between the two |

> [!IMPORTANT]
> The phone and the computer running MVD must be on the **same Wi-Fi network**. Discovery relies on
> local-network multicast (mDNS), which does not cross subnets or routers.

> [!NOTE]
> Only a subset of the MVD device types is fully supported — **Dimmable Light** and **Door Lock**.
> Other types may still be commissioned and offer partial functionality. For the full range of device
> types, a Nordic development kit is required; see
> [Nordic Semiconductor DKs](README.md#nordic-semiconductor-dks) in the README.

## Contents

1. [Creating a Matter Virtual Device](#creating-a-matter-virtual-device)
2. [Commissioning the device on iOS](#commissioning-the-device-on-ios)
3. [Exploring a commissioned device](#exploring-a-commissioned-device)
4. [Troubleshooting](#troubleshooting)
5. [Where to go next](#where-to-go-next)

---

## Creating a Matter Virtual Device

The accessory is created first, on the computer. The result is a QR code that the phone will scan in
the next section.

<table>
<tr>
<td width="460" align="center"><img src="doc/assets/1.png" alt="The MVD app on first launch" width="440"></td>
<td valign="top">

**Step 1 · Open MVD**

This is the initial screen displayed after opening the MVD app.

</td>
</tr>
<tr>
<td width="460" align="center"><img src="doc/assets/2_highlight.png" alt="Selecting a device type in MVD" width="440"></td>
<td valign="top">

**Step 2 · Choose a device type**

Expand the list of available devices and select one of the supported types.

</td>
</tr>
<tr>
<td width="460" align="center"><img src="doc/assets/3_highlight.png" alt="The Create device button in MVD" width="440"></td>
<td valign="top">

**Step 3 · Create the device**

Press the **Create device** button.

</td>
</tr>
<tr>
<td width="460" align="center"><img src="doc/assets/4_highlight.png" alt="The QR code button in MVD" width="440"></td>
<td valign="top">

**Step 4 · Show the QR code**

The device is now created, but not yet commissioned. This screen can later be used to change the
state of the device. Press **QR code** to display the code required for commissioning.

</td>
</tr>
<tr>
<td width="460" align="center"><img src="doc/assets/5.png" alt="The commissioning QR code displayed by MVD" width="440"></td>
<td valign="top">

**Step 5 · Keep the code on screen**

The displayed QR code is what the phone scans. Once the device has been successfully commissioned,
the **Device commissioned** label appears and this screen can be closed.

</td>
</tr>
</table>

> [!TIP]
> Leave MVD running for the rest of this guide. Closing it takes the accessory off the network, and
> the phone will no longer be able to reach it.

---

## Commissioning the device on iOS

With the QR code on screen, the flow moves to the phone. Steps 2 to 9 run inside a system-managed
extension rather than in nRF Matter itself, which is why the screens look like part of iOS.

<table>
<tr>
<td width="200" align="center"><img src="doc/assets/6.png" alt="The nRF Matter home screen" width="170"></td>
<td valign="top">

**Step 1 · Open nRF Matter**

This is the initial screen displayed after opening the app. No devices are listed yet.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/6_highlight.png" alt="The Add New Device button" width="170"></td>
<td valign="top">

**Step 2 · Start the flow**

Press **Add New Device**. Control is then delegated from the main app to an extension managed by the
iOS system.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/7_highlight.png" alt="The local network permission prompt" width="170"></td>
<td valign="top">

**Step 3 · Allow local network access**

Access to discovering devices on the local network has to be granted first. This is what allows the
phone to detect the MVD accessory on the same Wi-Fi network.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/8.png" alt="The QR code scanner" width="170"></td>
<td valign="top">

**Step 4 · Scan the QR code**

The camera is now active, and the QR code displayed in MVD can be scanned.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/9_highlight.png" alt="The device found confirmation screen" width="170"></td>
<td valign="top">

**Step 5 · Confirm the device**

Once the QR code has been scanned and the accessory detected, a confirmation screen is displayed.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/10_highlight.png" alt="The uncertified accessory warning" width="170"></td>
<td valign="top">

**Step 6 · Accept the uncertified accessory**

Because the accessory is under development and lacks proper certification, iOS asks for
confirmation. Press **Add Anyway** to proceed.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/11_highlight.png" alt="The location picker" width="170"></td>
<td valign="top">

**Step 7 · Pick a location**

The next screen asks for the location to which the device should be added. The selected location has
no meaning in this app, so the first option is sufficient. Press **Continue**.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/12_highlight.png" alt="The device naming screen" width="170"></td>
<td valign="top">

**Step 8 · Name the device**

A custom name can be assigned, although it is not used in the current version of the app. Press
**Continue**.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/13_highlight.png" alt="The commissioning success screen" width="170"></td>
<td valign="top">

**Step 9 · Finish**

Press **Done** on the confirmation screen. Control is then returned to nRF Matter, and the MVD
window on the computer shows the **Device commissioned** label.

</td>
</tr>
</table>

---

## Exploring a commissioned device

<table>
<tr>
<td width="200" align="center"><img src="doc/assets/14.png" alt="The home screen listing the commissioned device" width="170"></td>
<td valign="top">

**The device list**

All commissioned devices are listed on the main screen, together with the components related to
their specific device type and functionality.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/14_highlight.png" alt="A device entry in the list" width="170"></td>
<td valign="top">

**Expanding a device**

Pressing a device expands its content, revealing the controls for that device type.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/15.png" alt="The expanded device controls" width="170"></td>
<td valign="top">

**Controlling it**

Changes made here are sent to the accessory over Wi-Fi, and are reflected live in the MVD window on
the computer.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/15_highlight.png" alt="The Matter Device information entry" width="170"></td>
<td valign="top">

**Matter Device information**

Pressing **Matter Device information** opens a new section with details about the device.

</td>
</tr>
<tr>
<td width="200" align="center"><img src="doc/assets/16.png" alt="The Matter device information section" width="170"></td>
<td valign="top">

**The details**

Endpoints, device types, and the clusters exposed by the accessory are listed here — useful for
confirming that the accessory presents what the app expects.

</td>
</tr>
</table>

---

## Troubleshooting

| Symptom | What to check |
| --- | --- |
| The accessory is never found after scanning | Both the phone and the computer running MVD are on the **same Wi-Fi network**, and local network access has been granted to the app |
| The scanner opens but nothing happens | The QR code screen is still open in MVD, and the accessory has not already been commissioned |
| The device appears, but controls do nothing | MVD is still running, and the accessory has not been recreated since commissioning |
| Only some controls are shown | The chosen MVD device type is only partially supported — see the note in [What is needed](#what-is-needed) |

> [!TIP]
> The in-app log view records commissioning and cluster interactions, and is the fastest place to
> look when a step fails silently.

---

## Where to go next

At this point the Matter Virtual Device has been created, commissioned, and is controllable from the
nRF Matter app. Commissioning is persistent: the device stays available across app restarts and does
not need to be added again unless it is removed from the app.

The same flow applies to physical Matter accessories. The only practical difference is the source of
the QR code and the fact that a Thread accessory additionally requires a Thread Border Router on the
local network:

- [Nordic Semiconductor DKs](README.md#nordic-semiconductor-dks) — flashing a development kit with a
  Matter sample
- [Hosting Thread network credentials](README.md#initial-setup-hosting-thread-network-credentials) —
  the one-time network setup a Thread accessory needs
- [Project structure](README.md#project-structure) — where the commissioning code lives, for anyone
  moving on from using the app to working on it

Feedback and issue reports are welcome in the project's issue tracker.
