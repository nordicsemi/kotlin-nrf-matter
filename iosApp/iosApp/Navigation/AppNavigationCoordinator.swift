import ComposeApp
import Observation

/// The three bottom-bar tabs. Mirrors `HomeRoute` / `BindingRoute` / `LoggerRoute` on the
/// Kotlin side, but SwiftUI's `TabView` needs its own plain `Hashable` selection value.
enum AppTab: Hashable {
    case home, bindings, logger

    static func from(_ route: any Navigation3_runtimeNavKey) -> AppTab {
        switch route {
        case is BindingRoute: return .bindings
        case is LoggerRoute: return .logger
        default: return .home
        }
    }
}

/// App-scoped navigation state: which tab is active, each tab's independent back-stack,
/// and the device-availability signal that drives the global Commissioning button and the
/// Home tab's title. Replaces the tab/back-stack bookkeeping that used to live in `App.kt`.
@MainActor
@Observable
final class AppNavigationCoordinator {
    var selectedTab: AppTab = .home

    let homeCoordinator = TabNavigationCoordinator()
    let bindingsCoordinator = TabNavigationCoordinator()
    let loggerCoordinator = TabNavigationCoordinator()

    private var currentTabCoordinator: TabNavigationCoordinator {
        coordinator(for: selectedTab)
    }

    func coordinator(for tab: AppTab) -> TabNavigationCoordinator {
        switch tab {
        case .home: return homeCoordinator
        case .bindings: return bindingsCoordinator
        case .logger: return loggerCoordinator
        }
    }

    /// Pushes Commissioning onto whichever tab is currently active — mirrors the old
    /// Scaffold-level FAB, which was visible (and pushed onto the current back stack)
    /// from any of the three tabs.
    func pushCommissioning() {
        currentTabCoordinator.push(MainViewControllerKt.doNewCommissioningRoute())
    }

    /// Switches to `tab` and resets its stack to root — used when a detail screen
    /// (e.g. Commissioning's "navigate to Logs") jumps to another tab.
    func activate(tab: AppTab) {
        selectedTab = tab
        coordinator(for: tab).popToRoot()
    }
}
