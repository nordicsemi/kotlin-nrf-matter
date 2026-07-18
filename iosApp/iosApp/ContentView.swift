import UIKit
import SwiftUI
import ComposeApp

/// Bridges the shared Compose Multiplatform UI into SwiftUI.
///
/// `composeApp` wires its own native Matter implementations internally (via `iosDeps`,
/// consumed through cinterop), so this app target never needs to touch that directly.
struct ContentView: UIViewControllerRepresentable {

    /// Creates the Compose Multiplatform view controller.
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {

    }
}
