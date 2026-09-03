# Commissioning devices

Commissioning adds a Matter accessory to the app's fabric so it can be controlled. Before you start,
make sure you have a [prepared Matter device](preparing_a_matter_device.md) and, for Thread
accessories, [Thread network credentials](thread_network_credentials.md) on the phone.

## Starting commissioning

To commission a new accessory, tap **Add Device** (or the floating **+** button) on the main screen
to start
the onboarding process. Scan the device’s QR code or manually enter the setup payload (discriminator
and PIN).

The app scans for the accessory's Bluetooth Low Energy (BLE) advertising signal to establish an
initial secure connection. Once connected over BLE, the app authenticates the accessory, provisions
your local network credentials (Thread or Wi-Fi), and adds the device to your Matter fabric.

Once network credentials are provisioned and the fabric is bound, the app reads the accessory's
Descriptor (`0x001D`) and Basic Information (`0x0028`) clusters. This allows the app to determine
the device type, render the correct UI controls, and extract hardware metadata like the Product
Name and Vendor ID. The accessory is then added to the dashboard for further control or inspection
as a functional Device Card.

!!! note "The pairing UI belongs to the operating system"

    Scanning the QR code, choosing the network, and naming the device are all handled by the
    operating system, not by this app.

## Commissioning on Android

Commissioning goes through Google Play Services and the Android Home API:

1. **Initiate Onboarding Flow:** Prerequisite: App has camera & Bluetooth permissions.
   Tap **Add Device** on the Getting Started screen (or the floating **+** button if devices are
   already present) to launch the Android system commissioning interface.
   !!! note "Prerequisite"

   App has camera & Bluetooth permissions.

2. **Scan Payload or Enter Code:**
   Scan the accessory's Matter **QR Code** using the on-screen camera viewfinder, or tap **Setup
   with Code** to manually enter the 11-digit or 21-digit setup payload (Discriminator and PIN
   Code).

3. **Establish BLE Rendezvous (PASE):** Bluetooth LE Discovery & Authentication.
   Google Play Services initiates a **Bluetooth Low Energy (BLE)** scan for the accessory's
   advertisement. Once discovered, the app establishes a Password-Authenticated Session
   Establishment (PASE) using the setup PIN to secure the channel.

4. **Provision Network Credentials:**
   The app securely transfers operational network credentials (Wi-Fi SSID/Password or Thread Network
   Credentials via the local Border Router) over the encrypted BLE channel.

5. **Fabric Binding & Certificate Exchange:**
   The device connects directly to the local Wi-Fi or Thread network. The app exchanges operational
   certificates (Node Operational Certificate / NOC) and binds the device to both the Google Home
   ecosystem fabric and the nRF Matter app's local fabric.

6. **Descriptor & Metadata Reading:** Endpoint mapping & identity parsing.
   After successful fabric join, the app queries Endpoint 0 clusters over IP:


* Reads **Descriptor Cluster (`0x001D`)** to identify endpoints and Device Type IDs.
* Reads **Basic Information Cluster (`0x0028`)** for basic matter device metadata such as Vendor ID,
  Product ID, Product Name, and Serial
  Number.

7. **Dashboard Rendering:**
   The onboarding window closes and redirects to the main view, replacing or appending a new
   **Device Card** with custom controls, online status, and primary actions.

## Commissioning on iOS

Commissioning goes through Apple's `MatterSupport` framework and the app extension bundled with the
app:

1. The app issues a `MatterAddDeviceRequest`, and the system commissioning sheet appears, showing
   the
   ecosystem as *Nordic Ecosystem* and the home as *Nordic Home*.
2. Scan the accessory's QR code in the system sheet and pick a room from the list offered by the
   extension.
3. Network selection is automatic: Wi-Fi accessories join the current system network, and Thread
   accessories join the first network found during scanning.
4. The extension commissions the accessory onto the app's local fabric, and the app reads its
   clusters and adds it to the Dashboard.

Cancelling the system sheet ends commissioning with a *Cancelled* message on the error screen.

## If commissioning fails

If the onboarding process encounters an error such as an invalid setup payload,
timeout, or failed network authentication, the app halts the setup and displays a **Connection
Failed** screen. An error message details the specific issue, along with options to navigate
directly to the Logs panel to inspect detailed diagnostic logs or return to the main view.

| Field            | Description                                                                                                                 |
|------------------|-----------------------------------------------------------------------------------------------------------------------------|
| Commissioning id | Identifier of the commissioning attempt, useful for correlating with the log.                                               |
| Error Code       | The error reported by the platform or the Matter stack.                                                                     |
| Stage            | Where the failure happened: during commissioning, while reading Basic Information, or while reading the Descriptor cluster. |
| Message          | The underlying error message.                                                                                               |

The **Troubleshooting** section serves as a quick hardware sanity check before diving into the logs
panel. It instructs you to verify that the accessory is in commissioning mode: confirmed by
its LED flashing rapidly and that the correct Fabric ID is configured. Catching these physical
hardware or setup mismatches early saves time before you navigate to the logs panel for deeper
technical investigation.

!!! tip "Commissioning an accessory that was paired before"

    An accessory only accepts commissioning while it is in commissioning mode, and it keeps the
    credentials of fabrics it has already joined. If a device was previously paired — including a
    device that was force-removed from this app — factory reset it before commissioning it again.
