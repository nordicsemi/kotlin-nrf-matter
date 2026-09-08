// swift-tools-version: 6.3
// The swift-tools-version declares the minimum version of Swift required to build this package.
//
// VENDORED COPY
//
// This is the ios-matter Swift package, kept in-tree rather than resolved from
// git@github.com:sylwester-zielinski/ios-matter.git. It was vendored from tag
// 0.0.20. Edit it here: a change is picked up by the next build with no tag, no
// push and no version bump. See "/ios-matter -- vendored Matter Swift package"
// in the repository README.
//
// This manifest is a *build entry point*, not a distribution format. Nothing
// consumes ios-matter as a Swift package: it is not declared as a SwiftPM
// dependency of the Kotlin build, and iosApp.xcodeproj references this directory
// only as a folder to browse. It exists because `ios-matter/` holds no
// .xcodeproj, so the manifest is what lets composeApp's
// `compileIosMatterSwift*` task build these sources with `xcodebuild -scheme
// ios-matter` -- and what gives Xcode a target to index and autocomplete them
// against while editing.
//
// The package has no dependencies. Keeping it that way is deliberate: the
// compiled objects are archived into the cinterop klib and published inside
// matter-support, so anything linked here has to be redistributable and has to
// build for both iOS targets without a package graph at the consumer's end.

import PackageDescription

let package = Package(
    name: "ios-matter",
    platforms: [
        // Matter and MatterSupport are only available on recent iOS releases.
        .iOS("26.0"),
    ],
    products: [
        .library(
            name: "ios-matter",
            targets: ["ios-matter"]
        ),
    ],
    targets: [
        // The sources live in `ios-matter/`, which is also what the Xcode project references.
        .target(
            name: "ios-matter",
            path: "ios-matter"
        ),
    ],
    swiftLanguageModes: [.v5]
)
