plugins {
    application
    `version-catalog`
    `maven-publish`
    `ivy-publish`
}

project.version = localLib.design.pattern.lib.get().version.toString()
    .takeIf{ it.isNotBlank() }?: "unknown-version"
project.group = localLib.design.pattern.lib.get().group.toString()
    .takeIf{ it.isNotBlank() }?: "unknown-group"