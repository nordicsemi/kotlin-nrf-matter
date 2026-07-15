import SwiftUI

/// Root view: a native `TabView`/`NavigationStack` hierarchy so iOS 26 applies Liquid
/// Glass to the tab bar and toolbars automatically. Each tab and detail screen renders
/// its content via Compose (see `Navigation/ComposeViews.swift`).
struct ContentView: View {
    var body: some View {
        NativeNavContentView()
    }
}
