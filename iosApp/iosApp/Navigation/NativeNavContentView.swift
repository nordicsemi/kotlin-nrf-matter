import ComposeApp
import SwiftUI

/// One tab's `NavigationStack`: a Compose-rendered root plus any pushed detail screens.
struct TabContentView: View {
    let rootRoute: any Navigation3_runtimeNavKey
    let coordinator: TabNavigationCoordinator
    let appCoordinator: AppNavigationCoordinator
    let title: String

    var body: some View {
        NavigationStack(path: Binding(
            get: { coordinator.path },
            set: { coordinator.path = $0 }
        )) {
            TabRootComposeView(route: rootRoute, coordinator: coordinator, appCoordinator: appCoordinator)
                .ignoresSafeArea(.container, edges: .bottom)
                .navigationTitle(title)
                .navigationDestination(for: RouteWrapper.self) { wrapper in
                    DetailComposeView(route: wrapper.route, coordinator: coordinator, appCoordinator: appCoordinator)
                        .ignoresSafeArea(.container, edges: .bottom)
                        .navigationTitle(AppRouteKt.title(wrapper.route))
                        .toolbarTitleDisplayMode(.inline)
                }
        }
    }
}

/// Root navigation surface: a native `TabView` (Home / Bindings / Logger) so the system
/// applies Liquid Glass to the tab bar and toolbars automatically, plus a floating
/// Commissioning button that mirrors the old cross-tab FAB.
struct NativeNavContentView: View {
    @State private var appCoordinator = AppNavigationCoordinator()

    var body: some View {
        TabView(selection: Binding(
            get: { appCoordinator.selectedTab },
            set: { appCoordinator.selectedTab = $0 }
        )) {
            Tab("Dashboard", systemImage: "house", value: AppTab.home) {
                TabContentView(
                    rootRoute: HomeRoute.shared,
                    coordinator: appCoordinator.homeCoordinator,
                    appCoordinator: appCoordinator,
                    title: "nRF Matter"
                )
                .overlay(alignment: .bottomTrailing) {
                    Button {
                        appCoordinator.pushCommissioning()
                    } label: {
                        Image(systemName: "plus")
                            .font(.title2.weight(.semibold))
                            .foregroundStyle(.white)
                            .frame(width: 56, height: 56)
                            .background(
                                RoundedRectangle(cornerRadius: 16, style: .continuous)
                                    .fill(Color(red: 0.0, green: 0.663, blue: 0.808))
                            )
                    }            .padding(24)
                    .padding(.vertical, 48)
                }
            }
            Tab("Bindings", systemImage: "cable.connector", value: AppTab.bindings) {
                TabContentView(
                    rootRoute: BindingRoute.shared,
                    coordinator: appCoordinator.bindingsCoordinator,
                    appCoordinator: appCoordinator,
                    title: "Bindings"
                )
            }
            Tab("Logs Panel", systemImage: "terminal", value: AppTab.logger) {
                TabContentView(
                    rootRoute: LoggerRoute.shared,
                    coordinator: appCoordinator.loggerCoordinator,
                    appCoordinator: appCoordinator,
                    title: "Logs"
                )
            }
        }
        .tabBarMinimizeBehavior(.automatic)
    }
}
