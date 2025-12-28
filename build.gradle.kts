plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.teleporter.PaperMain")
    authors.add("Jo_field")

    generateLibraryLoader(false)
    foliaSupported(true)
}