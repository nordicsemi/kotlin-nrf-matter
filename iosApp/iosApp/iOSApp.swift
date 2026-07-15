import ComposeApp
import SharedCode
import SwiftUI

/// The app's entry point, hosting the shared Compose Multiplatform UI inside native
/// SwiftUI navigation chrome (see `ContentView.swift`).
@main
struct iOSApp: App {

    /// One-time bootstrap: the keychain-backed Matter keypair, and Koin/the shared code's
    /// native Swift bridge. Must run exactly once, before any Compose view controller
    /// (tab root or detail) is created — `init()` is guaranteed to run only once per process.
    init() {
        KeypairInitializer.initKeychain()
        MainViewControllerKt.InitApp(swiftCodeProvider: SwiftCodeProviderImpl())
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
