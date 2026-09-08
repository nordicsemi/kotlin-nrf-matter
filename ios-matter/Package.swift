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
// This manifest exists so xcodebuild can build the package; it is NOT declared as
// a SwiftPM dependency of the Kotlin build. composeApp's
// `compileIosMatterSwift*`/`iosMatterStaticLib*` tasks archive the resulting
// objects into a static library that plain cinterop embeds in the klib, which is
// what lets the compiled Swift ship inside the published matter-support artifact.

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
