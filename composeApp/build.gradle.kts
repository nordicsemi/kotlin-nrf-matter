@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nordic.android.kmp.library)
    alias(libs.plugins.nordic.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nordic.publish.kmp)
}

group = "no.nordicsemi.nrf.matter"

nordicPublishing {
    POM_ARTIFACT_ID = "matter-support"
    POM_NAME = "Nordic library for Matter connectivity."

    POM_DESCRIPTION = "Nordic Android Matter Library"
    POM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_URL = "https://github.com/nordicsemi/kotlin-nrf-matter"
    POM_SCM_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:nordicsemi/kotlin-nrf-matter.git"
}

// ─────────────────────────────────────────────────────────────────────────────
// ios-matter as a static library
//
// The vendored ios-matter Swift package is compiled to a static library and
// consumed through plain cinterop, rather than being declared as a SwiftPM
// dependency. The reason is publication: a `localSwiftPackage` declaration is
// published as an absolute filesystem path (see the swiftpm-metadata.json inside
// any matter-support artifact), so a consumer resolving matter-support from Maven
// cannot find the Swift code. Archiving the objects into the cinterop klib puts
// the compiled Swift *inside* the published artifact instead.
//
// Only the `@objc public` surface of ios-matter crosses this boundary, which is
// what Kotlin already used -- the Swift-generated Objective-C header is the
// contract.
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Archives the relocatable objects that xcodebuild produced for ios-matter and its
 * dependencies (currently Pulse and PulseObjCHelpers) into a single static library,
 * and copies the Swift-generated Objective-C header next to it.
 *
 * xcodebuild emits one partially-linked `.o` per module rather than an archive, so
 * there is nothing for cinterop's `-staticLibrary` to consume until libtool has run.
 */
abstract class PackageIosMatterStaticLib : DefaultTask() {

    /** DerivedData root that the matching `compileIosMatterSwift*` task wrote. */
    @get:InputDirectory
    abstract val derivedDataDir: DirectoryProperty

    /** Platform SDK name as it appears in DerivedData: `iphoneos` or `iphonesimulator`. */
    @get:Input
    abstract val sdkName: Property<String>

    @get:OutputFile
    abstract val staticLibrary: RegularFileProperty

    @get:OutputDirectory
    abstract val headerDir: DirectoryProperty

    @get:Inject
    abstract val execOps: ExecOperations

    @get:Inject
    abstract val fsOps: FileSystemOperations

    @TaskAction
    fun archive() {
        val dd = derivedDataDir.get().asFile
        val sdk = sdkName.get()

        val products = File(dd, "Build/Products/Release-$sdk")
        val objects = products.listFiles { file -> file.isFile && file.extension == "o" }
            ?.sortedBy { it.name }
            .orEmpty()
        check(objects.isNotEmpty()) {
            "No relocatable objects in $products. Did the xcodebuild step succeed?"
        }

        val library = staticLibrary.get().asFile
        library.parentFile.mkdirs()
        library.delete()
        execOps.exec {
            commandLine(
                listOf("libtool", "-static", "-o", library.absolutePath) +
                    objects.map { it.absolutePath }
            )
        }
        logger.lifecycle("Archived ${objects.size} object(s) into ${library.name}: " +
            objects.joinToString { it.name })

        // Swift writes the ObjC header and its module map here, next to each other
        // -- not into Build/Products. The module map refers to the header by a bare
        // relative name, so the two have to stay in the same directory.
        val generated = File(dd, "Build/Intermediates.noindex/GeneratedModuleMaps-$sdk")
        val header = File(generated, "ios-matter-Swift.h")
        val moduleMap = File(generated, "ios-matter.modulemap")
        check(header.isFile) { "Swift-generated header missing: $header" }
        check(moduleMap.isFile) { "Swift-generated module map missing: $moduleMap" }
        fsOps.copy {
            from(header, moduleMap)
            into(headerDir)
        }
    }
}

/** xcodebuild coordinates for one Kotlin/Native target. */
data class IosMatterPlatform(val destination: String, val sdk: String)

/**
 * Where the packaged artefacts for one Kotlin/Native target end up.
 *
 * [packageTask] is null off macOS, where the Apple toolchain that produces those
 * artefacts does not exist -- see [isMacOs].
 */
data class IosMatterArtifacts(
    val packageTask: TaskProvider<PackageIosMatterStaticLib>?,
    val libraryDir: File,
    val headerDir: File,
)

/**
 * Whether the Apple toolchain (`xcodebuild`, `libtool`) is available.
 *
 * Kotlin/Native cannot build Apple targets off macOS either, so it disables the
 * iOS compile and cinterop tasks on other hosts -- but Gradle still executes a
 * skipped task's *dependencies*, so a plain `xcodebuild` task in the graph would
 * fail the build on Windows and Linux even though nothing consumes its output.
 * The tasks are therefore only registered on macOS, and the iOS targets below
 * are left without their ios-matter dependency on other hosts. Android builds
 * and runs everywhere.
 */
val isMacOs = HostManager.hostIsMac

val iosMatterRoot = rootProject.layout.projectDirectory.dir("ios-matter")

val iosMatterPlatforms = mapOf(
    "IosArm64" to IosMatterPlatform("generic/platform=iOS", "iphoneos"),
    "IosSimulatorArm64" to IosMatterPlatform("generic/platform=iOS Simulator", "iphonesimulator"),
)

val iosMatterArtifacts: Map<String, IosMatterArtifacts> =
    iosMatterPlatforms.mapValues { (suffix, platform) ->
        val outputRoot = layout.buildDirectory.dir("ios-matter/$suffix").get().asFile
        val derivedData = File(outputRoot, "derivedData")
        val libraryDir = File(outputRoot, "lib")
        val headerDir = File(outputRoot, "include")

        if (!isMacOs) {
            return@mapValues IosMatterArtifacts(null, libraryDir, headerDir)
        }

        val compile = tasks.register<Exec>("compileIosMatterSwift$suffix") {
            group = "ios-matter"
            description = "Compiles the vendored ios-matter Swift package for $suffix."

            workingDir = iosMatterRoot.asFile
            inputs.file(iosMatterRoot.file("Package.swift"))
            inputs.dir(iosMatterRoot.dir("ios-matter"))
            outputs.dir(derivedData)

            // This xcodebuild usually runs *inside* another one: the Kotlin framework
            // is built from an Xcode run-script phase. The Xcode build system exports
            // TARGET_BUILD_DIR, CONFIGURATION_BUILD_DIR, SDKROOT, ARCHS and dozens
            // more, and a nested invocation that inherits them builds the outer
            // project's targets into the outer project's product directory:
            //   error: Multiple commands produce '.../nrfMatter.appex/nrfMatter.debug.dylib'
            // Passing only what xcodebuild genuinely needs keeps the two independent.
            environment = listOf(
                "PATH", "HOME", "USER", "LOGNAME", "SHELL", "TMPDIR",
                "LANG", "LC_ALL", "DEVELOPER_DIR",
            ).mapNotNull { key ->
                providers.environmentVariable(key).orNull?.let { key to it }
            }.toMap()
            // DerivedData embeds absolute paths, so it must never be shared between
            // machines through the build cache.
            outputs.cacheIf { false }

            commandLine(
                "xcodebuild",
                "-scheme", "ios-matter",
                "-destination", platform.destination,
                "-configuration", "Release",
                "-derivedDataPath", derivedData.absolutePath,
                // Kotlin/Native has no x86_64 simulator target here, and building it
                // would only be archived and discarded.
                "ARCHS=arm64",
                "ONLY_ACTIVE_ARCH=NO",
                // ios-matter's manifest already passes -enable-library-evolution;
                // keeping the two consistent avoids a rebuild of every dependency.
                "BUILD_LIBRARY_FOR_DISTRIBUTION=YES",
                "build",
            )
        }

        val packageTask = tasks.register<PackageIosMatterStaticLib>("iosMatterStaticLib$suffix") {
            group = "ios-matter"
            description = "Archives ios-matter and its Swift dependencies into a static library for $suffix."

            dependsOn(compile)
            derivedDataDir.set(derivedData)
            sdkName.set(platform.sdk)
            staticLibrary.set(File(libraryDir, "libios-matter.a"))
            this.headerDir.set(headerDir)
        }

        IosMatterArtifacts(packageTask, libraryDir, headerDir)
    }

/** Convenience aggregate so `./gradlew :composeApp:iosMatterStaticLibs` builds every target. */
tasks.register("iosMatterStaticLibs") {
    group = "ios-matter"
    description = "Builds the ios-matter static library for every iOS target."
    if (isMacOs) {
        dependsOn(iosMatterArtifacts.values.mapNotNull { it.packageTask })
    } else {
        doFirst {
            error("ios-matter needs xcodebuild, which is only available on macOS.")
        }
    }
}

kotlin {
    android {
        namespace = "no.nordicsemi.nrf.matter.lib"

        minSdk = 27

        androidResources {
            enable = true
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }

        // iosArm64 -> IosArm64, matching the task-name suffixes above and the
        // suffix Kotlin gives the generated cinterop task.
        val suffix = iosTarget.name.replaceFirstChar { it.uppercaseChar() }
        val artifacts = iosMatterArtifacts.getValue(suffix)

        iosTarget.compilations.getByName("main").cinterops.create("iosMatter") {
            definitionFile.set(
                layout.projectDirectory.file("src/nativeInterop/cinterop/iosMatter.def")
            )
            // All per-target, so none of it can live in the .def.
            includeDirs(artifacts.headerDir)
            extraOpts(
                "-compiler-option",
                "-fmodule-map-file=${File(artifacts.headerDir, "ios-matter.modulemap")}",
                "-libraryPath", artifacts.libraryDir.absolutePath,
                "-staticLibrary", "libios-matter.a",
            )
        }

        // The .def references a header and a library that only exist once the Swift
        // package has been compiled and archived. `matching` keeps this lazy: the
        // cinterop task is registered by the block above, not yet realised here.
        val packageTask = artifacts.packageTask
        if (packageTask != null) {
            tasks.matching { it.name == "cinteropIosMatter$suffix" }.configureEach {
                dependsOn(packageTask)
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
            // Home API SDK, resolved from the flat `mavenLocal` directory in this repo.
            implementation(libs.play.services.home)
            implementation(libs.play.services.types)
            implementation(libs.androidx.activity.compose)
            implementation(libs.jetbrains.compose.viewmodel)
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.viewmodel)

            // Data time
            implementation(libs.kotlinx.datetime)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            // serialization
            implementation(libs.kotlinx.serialization.json)
            // data store
            implementation(libs.androidx.dataStore.preferences)
            implementation(libs.androidx.dataStore.core)
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

androidComponents {
    onVariants { variant ->
        val jniLibs = requireNotNull(variant.sources.jniLibs)
        jniLibs.addStaticSourceDirectory("libs/jniLibs")
    }
}
