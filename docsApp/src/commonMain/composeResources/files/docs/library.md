# Library

This repository also contains an alpha version of a Compose Multiplatform Matter library.

Add the following line to the `commonMain` dependencies to include it:

```kotlin
api("no.nordicsemi.nrf.matter:matter-support:0.1.1")
```

Use `api` rather than `implementation`. The iOS setup described below exports the library from the
shared framework, and only `api` dependencies can be exported.

On Android the library initializes itself through `androidx.startup.InitializationProvider`, so no setup code is needed.
On iOS you must call `NordicMatters.shared.initializePlatform()` before using any other API, in
both the main app and the Matter extension.

## Commissioning
Commissioning can be started from common code, as shown below. Some additional setup is required per
platform.
```kotlin
val fabric = NordicMatters.defaultFabric

val commissioningTask = rememberCommissioningTask(
    fabric = fabric,
    onSuccess = { deviceId ->
        fabric.commissionDevice(deviceId)
    },
    onError = {
        // handle error
    },
)

Button(onClick = { commissioningTask.startCommissioning() }) {
    Text("Commission a new device")
}
```
### Android

#### Home API
The Google Home SDK must be available to your build. Either copy the `mavenLocal` directory from this
repository into your project, or obtain the SDK from the official Google sources.

If you copy the `mavenLocal` directory, declare it as a repository in `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("$rootDir/mavenLocal")
        }
        // ...other repositories
    }
}
```

#### Permissions
The library declares the required permissions in its manifest, but the runtime permissions below must
be granted before a commissioning task can be started:

- `android.permission.ACCESS_FINE_LOCATION`
- `android.permission.CAMERA`
- `android.permission.BLUETOOTH_SCAN`
- `android.permission.BLUETOOTH_CONNECT`
- `android.permission.ACCESS_LOCAL_NETWORK`

### iOS
On iOS, commissioning runs inside an app extension. The extension cannot be part of the library and
has to be configured separately in your own project.

This repository's extension lives in the `iosApp/nrfMatter` directory and can serve as a reference.

#### Main app
1. In the `build.gradle.kts` of the ":shared" module, export the library from the framework.
   ```kotlin
       listOf(
           iosArm64(),
           iosSimulatorArm64()
       ).forEach { iosTarget ->
           iosTarget.binaries.framework {
               baseName = "shared"
               isStatic = true

               export("no.nordicsemi.nrf.matter:matter-support:<version>")
           }
       }
   ```
2. Call `NordicMatters.shared.initializePlatform()` before using any other library API, for example in
   the `App` initializer:
   ```swift
   @main
   struct iOSApp: App {

       init() {
           NordicMatters.shared.initializePlatform()
       }

       // ...
   }
   ```
3. In the main app's `Info.plist`, add "Privacy - Local Network Usage Description"
   (`NSLocalNetworkUsageDescription`) to work with Matter devices.
4. Complete the [shared configuration](#shared-configuration) for the main app target.

#### Matter extension
1. In Xcode, select *File -> New -> Target...* and choose "Matter extension" from the list.
2. Add the following entries to the extension's `Info.plist`:
   ```xml
   <key>NSExtension</key>
   <dict>
   <key>NSExtensionPointIdentifier</key>
   <string>com.apple.matter.support.extension.device-setup</string>
   <key>NSExtensionPrincipalClass</key>
   <string>$(PRODUCT_MODULE_NAME).RequestHandler</string>
   </dict>
   ```
3. Declare a single `RequestHandler` class. It can be copied from this project as is. The part most
   likely to need changing is the Thread network selection in `selectThreadNetwork`.
   ```swift
   final class RequestHandler: MatterAddDeviceExtensionRequestHandler {

       private let fabric: Fabric

       override init() {
           NordicMatters.shared.initializePlatform()
           fabric = NordicMatters.shared.defaultFabric
       }

       override func rooms(in home: MatterAddDeviceRequest.Home?) async -> [MatterAddDeviceRequest.Room] {
           return NordicMatters.shared.rooms()
               .map { MatterAddDeviceRequest.Room(displayName: $0) }
       }

       override func commissionDevice(in home: MatterAddDeviceRequest.Home?, onboardingPayload: String, commissioningID: UUID) async throws {
           try await fabric.commissionDevice(payload: onboardingPayload)
       }

       override func configureDevice(named name: String, in room: MatterAddDeviceRequest.Room?) async {
           fabric.finalizeDevice(name: name)
       }

       override func validateDeviceCredential(_ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential) async throws {
       }

       override func selectWiFiNetwork(from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {
           return .defaultSystemNetwork
       }

       override func selectThreadNetwork(from threadScanResults: [MatterAddDeviceExtensionRequestHandler.ThreadScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation {
           NordicMatters.shared.logThreadNetworks(
               names: threadScanResults.map { $0.networkName }
           )

           return .network(extendedPANID: threadScanResults[0].extendedPANID)
       }
   }
   ```
4. The extension links against the shared framework, so it must build it. Select the extension target,
   open *Build Phases*, and add a *Run Script Phase* named "Compile Kotlin Framework" (the same script
   as in the main app target), placed before "Compile Sources".
5. Complete the [shared configuration](#shared-configuration) for the extension target.

#### Shared configuration
The following must be configured in **both** the main app and the extension.

1. Declare the Bonjour services required to discover Matter devices in `Info.plist`:
   ```xml
   <key>NSBonjourServices</key>
   <array>
   <string>_matter._tcp</string>
   <string>_matterc._udp</string>
   <string>_matterd._udp</string>
   </array>
   ```
2. Declare two app groups in the target's *Signing & Capabilities* and set their names in `Info.plist`
   using the keys below. Using the same app group for both is not recommended, as the two could
   overwrite each other's values under the same keys.
   - `NordicMatterLocalAppGroup`
   - `NordicMatterSharedAppGroup`
3. Declare a shared keychain in the target's *Signing & Capabilities* and set its name in `Info.plist`
   using the key below.
   - `NordicMatterKeychainGroup`

## Controlling a Matter device

### Clusters

A Matter device is controlled through its clusters. A cluster declares attributes that can be read or
observed, and commands that modify them.

After commissioning, the library reads the Basic Information cluster, which contains, unsurprisingly,
basic information about the device, such as its name and firmware version. It then reads the
Descriptor cluster of every endpoint. The Descriptor cluster lists the server clusters (functionality
the device exposes), the client clusters (functionality the device can consume), and the device types,
which define which clusters are mandatory or optional for that particular device type.

Every device belongs to a fabric. For now the library supports a single fabric, accessible through
`NordicMatters.defaultFabric`. After successful commissioning, the device appears in the fabric's
device flow:
```kotlin
val devices: Flow<List<Device>> = NordicMatters.defaultFabric.devices
```

Given a device, its clusters are obtained with `device.toClusters()`. `Cluster` wraps a single Matter
cluster, identified by its unique id, and exposes the following operations:
```kotlin
// reading attributes 
suspend fun <T> readAttribute(attributeId: Long): T
// observing attributes
fun <T> observeAttribute(attributeId: Long): Flow<T>
// executing commands
suspend fun executeCommand(
    commandId: Long,
    value: Any? = null,
    timedInvokeTimeoutMs: Int? = null,
)
```

The library ships implementations of several standard Matter clusters, for example the
On/Off cluster, which wrap `Cluster` and expose the same functionality in a friendlier form, such as
`setOn()` and `observeOnOff()`. Under the hood they still only read attributes, observe attributes and
send commands. Custom clusters can be registered with `NordicMatters.registerCustomCluster(...)`.

### Decommissioning

Decommissioning is triggered with the function below. It clears the fabric credentials on the Matter
device and then removes the device from the local fabric.
```kotlin
NordicMatters.defaultFabric.decommissionDevice(deviceId)
```

If the device is unreachable, it can be removed from the local fabric only. The device keeps its
credentials and may need a factory reset before it can be commissioned again.
```kotlin
NordicMatters.defaultFabric.forceRemoveDevice(deviceId)
```

### Binding

Binding requires two devices: a source, such as a switch, and a target, such as a bulb. Binding
currently supports only the On/Off cluster. Support for binding of any cluster type is planned for a future
release.

To get the devices that can act as a source:
```kotlin
val sources: List<Device> = NordicMatters.defaultFabric.getBindingSourceDevices()
```

To observe the devices that are eligible targets for a given source:
```kotlin
val targets: Flow<List<Device>> = NordicMatters.defaultFabric.getEligibleTargetDevices(sourceDeviceId)
```

To create the binding:
```kotlin
val binding: DeviceBinding = NordicMatters.defaultFabric.bindDevices(sourceDeviceId, targetDeviceId)
```

Existing bindings are available through `NordicMatters.defaultFabric.bindings`.
