// swift-tools-version:6.0
import PackageDescription

let package = Package(
    name: "iosDeps",
    platforms: [.iOS(.v18), .macOS(.v13)],
    products: [
        .library(name: "iosDeps", type: .static, targets: ["iosDeps"]),
    ],
    dependencies: [
        .package(url: "https://github.com/kean/Pulse", from: "5.2.1"),
    ],
    targets: [
        .target(
            name: "SharedCode",
            dependencies: [
                .product(name: "Pulse", package: "Pulse"),
            ],
            path: "SharedCode"
        ),
        .target(
            name: "iosDeps",
            dependencies: ["SharedCode"],
            path: ".",
            exclude: ["Package.swift", "SharedCode"],
            linkerSettings: [
                .linkedFramework("Matter"),
                .linkedFramework("MatterSupport"),
            ]
        ),
    ]
)
