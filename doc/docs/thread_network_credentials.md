# Thread network credentials

Commissioning a **Thread** Matter device requires a Thread Border Router already running on the
local network, and Thread network credentials available on the phone.

This setup is not part of the application. It relies on the home hub infrastructure provided by the
operating system, which is configured once per network before the app is used.

## Installing Thread network credentials on iOS

Matter samples installed on development kits require a **Thread Border Router** connected to the
same local network as the app. In addition, the iPhone must already have the corresponding **Thread
network credentials** installed. The credentials are installed through the system API and are
available to all apps on the phone.

The process for obtaining these credentials depends on the Thread ecosystem being used. In most
cases, when the Thread network is provided by a device such as a Samsung TV or a dedicated hub such
as the **Google TV Streamer 4K**, the manufacturer's companion app must be used to download and
install the Thread network credentials on the iPhone.

For example:

- **Samsung**: [SmartThings](https://apps.apple.com/us/app/smartthings/id1222822904)
- **Google**: [Google Home](https://apps.apple.com/us/app/google-home/id680819774)

For detailed instructions, refer to the documentation provided by the device manufacturer. In
general, the required credentials are installed after signing in to the companion app, adding the
Thread-enabled device to the home, and enabling its Thread Border Router functionality.

If the credentials are not immediately available, commissioning a Matter device using the
corresponding companion app may trigger the download and installation of the Thread network
credentials.

!!! note "Note"

    The app has been tested with the **Google TV Streamer 4K**. At the time of writing, Google does
    not provide any alternative method for installing or sharing Thread network credentials on iPhone
    other than through the **Google Home** app.

## Installing Thread network credentials on Android

Set up a Thread Border Router, such as a Nest Hub (2nd generation) or a Google TV Streamer 4K, through the Google Home app. Google Play Services, by way of the Home API, then makes the credentials
available to this app in the same way.

Keep the following in mind:

- The phone and the hub **must be on the same Wi-Fi network**. Credential and device discovery
  relies on local-network multicast (mDNS), which does not cross subnets or routers.
- The hub needs a **user account signed in** — A Google account added through the Google Home app —
  before it shares any credentials. A freshly unboxed hub with no account will not work.
- Make sure the router on the network has **IPv6 enabled**. Without it, Thread commissioning can
  appear to succeed, but device control might fail afterwards.

!!! tip "Sharing one hub between platforms"

    Matter standardizes Thread credential sharing across ecosystems, so a single hub can plausibly
    serve both platforms. For example, a Google TV Streamer 4K set up once in Google Home has been
    observed working for both iOS and Android commissioning in this app, without a separate
    Apple-ecosystem hub.
