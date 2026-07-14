import ComposeApp
import SwiftUI

/// Hosts a tab's root screen content (Home / Bindings / Logger), rendered by Compose.
struct TabRootComposeView: UIViewControllerRepresentable {
    let route: any Navigation3_runtimeNavKey
    let coordinator: TabNavigationCoordinator
    let appCoordinator: AppNavigationCoordinator

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.TabRootViewController(
            route: route,
            onNavigate: { newRoute in coordinator.push(newRoute) },
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// Hosts a screen pushed on top of a tab's native navigation stack (e.g. Commissioning),
/// rendered by Compose.
struct DetailComposeView: UIViewControllerRepresentable {
    let route: any Navigation3_runtimeNavKey
    let coordinator: TabNavigationCoordinator
    let appCoordinator: AppNavigationCoordinator

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.DetailViewController(
            route: route,
            onBack: { coordinator.pop() },
            onNavigateToTab: { tabRoute in appCoordinator.activate(tab: .from(tabRoute)) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
