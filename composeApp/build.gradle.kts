plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nordic.android.kmp.library)
    alias(libs.plugins.nordic.kotlin)
    alias(libs.plugins.ksp)
}

group = "no.nordicsemi.nrf.matter.shared"

// iosDeps is a pure-Swift package (iosApp/iosDeps, a SwiftPM package — not an Xcode target) that
// implements the native Matter operations against Apple's Matter framework. It's built via
// `swift build` and consumed here through cinterop. Its public API is deliberately
// primitives-only (String/Bool/Int32/closures) and never references a :core-compiled type:
// Kotlin/Native cannot link two independently-compiled Kotlin/Native frameworks into one process
// ("runtime injected twice"), so iosDeps must have zero dependency on Core's compiled runtime.
// composeApp adapts the resulting cinterop bindings to :core's own interfaces (see IosModule.kt).
//
// iosDeps is deliberately NOT an Xcode target: iosApp/nrfMatter carry no Xcode-level dependency
// on it and consume it only through this Gradle module, exactly as Kotlin/Native's cinterop
// mechanism already implies. Building it via `swift build` rather than `xcodebuild -scheme
// iosDeps` is also what makes that possible in practice — invoking xcodebuild for it from inside
// iosApp's own "Compile Kotlin Framework" run script would be a *nested* xcodebuild call, which
// crashes Xcode's shared build daemon (confirmed: "unexpected service error: The Xcode build
// system has crashed" — deterministic, not a fluke). swift build never touches that daemon, so
// it's safe to invoke here regardless of whether this Gradle build itself runs standalone or
// nested inside an Xcode build.
val iosDepsBuildDir = layout.buildDirectory.dir("iosDepsBuild")
val iosDepsConfiguration = "Debug"

data class IosDepsSdk(val sdkName: String, val productsDirName: String, val triple: String)

fun iosDepsSdkFor(targetName: String): IosDepsSdk = when (targetName) {
    "iosArm64" -> IosDepsSdk("iphoneos", "$iosDepsConfiguration-iphoneos", "arm64-apple-ios26.0")
    "iosSimulatorArm64" -> IosDepsSdk("iphonesimulator", "$iosDepsConfiguration-iphonesimulator", "arm64-apple-ios26.0-simulator")
    else -> error("Unsupported iOS target for iosDeps: $targetName")
}

kotlin {
    android {
        namespace = "no.nordicsemi.nrf.matter.shared"

        androidResources {
            enable = true
        }
    }

    jvm()
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        val sdk = iosDepsSdkFor(iosTarget.name)

        val iosDepsProductsDir = iosDepsBuildDir.map { it.dir("Products/${sdk.productsDirName}") }
        val iosDepsScratchDir = iosDepsBuildDir.map { it.dir("Scratch/${sdk.productsDirName}") }

        val buildIosDepsFramework = tasks.register<Exec>("buildIosDepsFrameworkFor${iosTarget.name}") {
            group = "ios interop"
            description = "Builds iosDeps.framework via swift build for ${iosTarget.name}"
            commandLine(
                rootDir.resolve("iosApp/iosDeps/build-framework.sh").absolutePath,
                sdk.sdkName,
                sdk.triple,
                iosDepsScratchDir.get().asFile.absolutePath,
                iosDepsProductsDir.get().asFile.absolutePath,
            )
            inputs.dir(rootDir.resolve("iosApp/iosDeps"))
            inputs.dir(rootDir.resolve("iosApp/SharedCode"))
            outputs.dir(iosDepsProductsDir)
        }

        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(project(":core"))
        }

        // iosDeps/SharedCode were built with this project's Xcode deployment target (26.0);
        // Kotlin/Native's linker (ld directly, not the clang driver) otherwise defaults to a
        // much older platform version, which makes ld64 look for Swift back-deployment shims
        // that don't exist in this SDK.
        val (platformVersionArgs, swiftCompatLibDir) = when (iosTarget.name) {
            "iosArm64" -> listOf("-platform_version", "ios", "26.0", "26.0") to "iphoneos"
            "iosSimulatorArm64" -> listOf("-platform_version", "ios-simulator", "26.0", "26.0") to "iphonesimulator"
            else -> error("Unsupported iOS target for iosDeps: ${iosTarget.name}")
        }
        // Xcode's clang driver auto-adds this search path when it auto-links Swift's
        // back-deployment compatibility libraries; Kotlin/Native's raw `ld` invocation doesn't.
        val swiftCompatLibPath =
            "/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift/$swiftCompatLibDir"

        iosTarget.binaries.all {
            // Note: Core.framework must never be linked here. iosDeps's public API is
            // primitives-only precisely so its object code never needs Core's compiled
            // Kotlin/Native runtime — linking it crashes with "runtime injected twice"
            // since composeApp already has its own (it compiles :core directly).
            linkerOpts(
                "-F${iosDepsProductsDir.get().asFile.absolutePath}",
                "-framework", "iosDeps",
                "-L$swiftCompatLibPath",
                *platformVersionArgs.toTypedArray()
            )
        }

        iosTarget.compilations.getByName("main") {
            cinterops {
                create("iosDeps") {
                    definitionFile.set(project.file("src/nativeInterop/cinterop/iosDeps.def"))
                    packageName("no.nordicsemi.nrf.matter.iosdeps")
                    compilerOpts(
                        "-F${iosDepsProductsDir.get().asFile.absolutePath}",
                        "-fmodules"
                    )
                }
            }
        }

        val iosDepsFrameworkBundle = iosDepsProductsDir.map { it.dir("iosDeps.framework") }

        val targetNameCapitalized = iosTarget.name.replaceFirstChar(Char::uppercase)
        tasks.matching { it.name.startsWith("cinteropIosDeps") && it.name.endsWith(targetNameCapitalized) }
            .configureEach {
                dependsOn(buildIosDepsFramework)
                inputs.dir(iosDepsFrameworkBundle).withPropertyName("iosDepsFrameworkBundle")
            }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(project(":androidDeps"))
//            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.accompanist.permissions)
            implementation(libs.jetbrains.compose.viewmodel)
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
        }
        commonMain.dependencies {
            api(project(":core"))

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.foundation)
            implementation(libs.jetbrains.icons.extended)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.resources)

            // Preview
            implementation(libs.jetbrains.ui.tooling.preview)
            // Data time
            implementation(libs.kotlinx.datetime)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // collections
            implementation(libs.kotlinx.collections.immutable)
            // Nav 3
            implementation(libs.jetbrains.navigation)
            implementation(libs.jetbrains.adaptive.navigation)
            implementation(libs.jetbrains.lifecycle.navigation)
            // serialization
            implementation(libs.kotlinx.serialization.json)
            // data store
            implementation(libs.androidx.dataStore.preferences)
            implementation(libs.androidx.dataStore.core)

            // Cloudy to have blur effect.
            implementation(libs.skydoves.cloudy)
            // CMPToast: Toasts for Compose Multiplatform
            implementation(libs.cmptoast)
            implementation(libs.compottie)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}
