import ComposeApp
import Foundation

/// Wraps a Kotlin `NavKey` for use with `NavigationStack`'s `navigationDestination(for:)`.
///
/// Identity is a fresh `UUID` per push rather than the wrapped route's own equality, so
/// pushing the same route twice (e.g. re-opening Commissioning) always yields a distinct
/// stack entry.
struct RouteWrapper: Hashable, Identifiable {
    let id = UUID()
    let route: any Navigation3_runtimeNavKey

    static func == (lhs: RouteWrapper, rhs: RouteWrapper) -> Bool {
        lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
}
