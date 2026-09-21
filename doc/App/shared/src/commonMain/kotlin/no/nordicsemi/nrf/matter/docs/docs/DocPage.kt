package no.nordicsemi.nrf.matter.docs.docs

enum class DocGroup(val displayName: String) {
    GETTING_STARTED("Getting started"),
    USER_GUIDE("User guide"),
    DEVELOPER("Developer"),
    ABOUT("About"),
}

enum class DocPage(val resourceFile: String, val displayTitle: String, val group: DocGroup) {
    INDEX("index.md", "Get started", DocGroup.GETTING_STARTED),
    REQUIREMENTS("requirements.md", "Requirements", DocGroup.GETTING_STARTED),
    PREPARING_DEVICE("preparing_a_matter_device.md", "Preparing a Matter device", DocGroup.GETTING_STARTED),
    THREAD_CREDENTIALS("thread_network_credentials.md", "Thread network credentials", DocGroup.GETTING_STARTED),
    BUILDING("building.md", "Building the application", DocGroup.GETTING_STARTED),

    OVERVIEW("overview.md", "Overview and user interface", DocGroup.USER_GUIDE),
    COMMISSIONING("commissioning.md", "Commissioning devices", DocGroup.USER_GUIDE),
    BINDINGS("bindings.md", "Configuring bindings", DocGroup.USER_GUIDE),
    LOGS("logs.md", "Viewing logs", DocGroup.USER_GUIDE),

    PROJECT_STRUCTURE("project_structure.md", "Project structure", DocGroup.DEVELOPER),
    VENDORED_DEPS("vendored_dependencies.md", "Vendored dependencies", DocGroup.DEVELOPER),

    RELEASE_NOTES("release_notes.md", "Release notes", DocGroup.ABOUT),
    REVISION_HISTORY("revision_history.md", "Revision history", DocGroup.ABOUT),
}

data class DocAnchor(val page: DocPage, val sectionId: String? = null)
