#!/bin/sh
# Builds the iosDeps Swift package via `swift build` (not xcodebuild) and assembles a static
# .framework bundle from its output, so that Kotlin/Native's cinterop can consume it as an
# Objective-C module. `swift build` is used deliberately instead of `xcodebuild -scheme iosDeps`:
# invoking xcodebuild for this package while an outer xcodebuild (iosApp's own build) is in
# progress crashes Xcode's shared build system daemon. swift build does not go through that
# daemon at all, so it is safe to invoke from Gradle regardless of whether this script itself
# runs standalone or nested inside an Xcode build.
#
# Usage: build-framework.sh <sdkName> <targetTriple> <scratchDir> <outputProductsDir>
set -eu

SDK_NAME="$1"
TARGET_TRIPLE="$2"
SCRATCH_DIR="$3"
OUTPUT_DIR="$4"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SDK_PATH="$(xcrun --sdk "$SDK_NAME" --show-sdk-path)"

# swift build runs its compiler subprocesses with cwd set to the package directory
# ($SCRIPT_DIR), not the caller's cwd — so relative paths passed in must be resolved to
# absolute paths here, before they're handed to swiftc as -emit-objc-header-path/output dirs.
mkdir -p "$SCRATCH_DIR" "$OUTPUT_DIR"
SCRATCH_DIR="$(cd "$SCRATCH_DIR" && pwd)"
OUTPUT_DIR="$(cd "$OUTPUT_DIR" && pwd)"
OBJC_HEADER_PATH="$SCRATCH_DIR/iosDeps-Swift.h"

# SDKROOT is inherited from an outer Xcode build when this script runs nested inside iosApp's
# "Compile Kotlin Framework" run script phase; it points at the app's own SDK (which may not
# even be for this same platform/arch) and confuses SwiftPM's manifest compilation (a macOS host
# tool). The actual target platform is controlled explicitly below via --sdk/-target instead.
unset SDKROOT

swift build \
    --package-path "$SCRIPT_DIR" \
    --scratch-path "$SCRATCH_DIR" \
    --configuration debug \
    --sdk "$SDK_PATH" \
    -Xswiftc -target -Xswiftc "$TARGET_TRIPLE" \
    -Xcc -target -Xcc "$TARGET_TRIPLE" \
    -Xswiftc -emit-objc-header-path -Xswiftc "$OBJC_HEADER_PATH"

# SwiftPM names its per-triple output directory after the *host* triple it would use without
# our -Xswiftc/-Xcc target overrides (e.g. "arm64-apple-macosx"), not the actual target triple
# we built for — so locate the archive by name rather than assuming a directory layout.
ARCHIVE_PATH="$(find "$SCRATCH_DIR" -name "libiosDeps.a" -not -path "*/checkouts/*" -print -quit)"
if [ -z "$ARCHIVE_PATH" ]; then
    echo "error: libiosDeps.a not found under $SCRATCH_DIR after swift build" >&2
    exit 1
fi

FRAMEWORK_DIR="$OUTPUT_DIR/iosDeps.framework"
rm -rf "$FRAMEWORK_DIR"
mkdir -p "$FRAMEWORK_DIR/Headers" "$FRAMEWORK_DIR/Modules"

cp "$ARCHIVE_PATH" "$FRAMEWORK_DIR/iosDeps"
cp "$OBJC_HEADER_PATH" "$FRAMEWORK_DIR/Headers/iosDeps-Swift.h"
cat > "$FRAMEWORK_DIR/Modules/module.modulemap" <<EOF
framework module iosDeps {
    umbrella header "iosDeps-Swift.h"
    export *
    module * { export * }
}
EOF

# Emit artifacts for the Kotlin/Native cinterop so iosDeps's compiled Swift object code travels
# INSIDE the published .m2 klib and consumers need no local iosDeps.framework at link time:
#   * libiosDeps.a       — the static archive, bundled into the klib via cinterop's -staticLibrary.
#   * iosDepsInterop/     — a *non-framework* clang module (no `link` directive), so `@import
#                           iosDeps` during cinterop resolves the headers WITHOUT emitting a
#                           `-framework iosDeps` autolink into consumers (which have no such
#                           framework). System deps (Foundation/Matter/...) still autolink normally.
cp "$ARCHIVE_PATH" "$OUTPUT_DIR/libiosDeps.a"

INTEROP_DIR="$OUTPUT_DIR/iosDepsInterop"
rm -rf "$INTEROP_DIR"
mkdir -p "$INTEROP_DIR"
cp "$OBJC_HEADER_PATH" "$INTEROP_DIR/iosDeps-Swift.h"
cat > "$INTEROP_DIR/module.modulemap" <<EOF
module iosDeps {
    header "iosDeps-Swift.h"
    export *
}
EOF
