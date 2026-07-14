import ComposeApp
import Observation

/// Owns one tab's native push/pop back-stack.
@MainActor
@Observable
final class TabNavigationCoordinator {
    var path: [RouteWrapper] = []
    
    func push(_ route: any Navigation3_runtimeNavKey) {
        let wrapper = RouteWrapper(route: route)

        guard !path.isEmpty else {
            path.append(wrapper)
            return
        }

        path.removeLast()
        Task { @MainActor [weak self] in
            self?.path.append(wrapper)
        }
    }
    
    func pop() {
        if !path.isEmpty {
            path.removeLast()
        }
    }
    
    func popToRoot() {
        path.removeAll()
    }
}
