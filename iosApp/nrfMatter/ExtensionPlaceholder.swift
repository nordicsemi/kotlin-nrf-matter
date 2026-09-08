//
//  ExtensionPlaceholder.swift
//  nrfMatter
//

// Intentionally empty. Do not delete this file.
//
// This target holds no commissioning code: its principal class is
// `NordicMatterRequestHandler`, which ships inside the `shared` framework and is named in
// Info.plist under NSExtensionPrincipalClass. The Objective-C runtime resolves it at launch, so
// there is nothing here to declare, subclass or import. The framework is linked by
// OTHER_LDFLAGS (`-ObjC -framework shared`), not from here -- Swift only auto-links a module it
// actually uses, so an `import shared` would be dropped and link nothing.
//
// What this file *is* for: Xcode needs at least one compilable source in the target to run the
// link step at all. With no sources it silently skips linking and emits an .appex containing only
// Info.plist and resources -- no executable -- while still reporting BUILD SUCCEEDED. The
// extension then fails at launch, with nothing in the build log pointing at the cause.
