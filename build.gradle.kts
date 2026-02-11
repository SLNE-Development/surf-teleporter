plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin") version "1.21.11+"
}

version = findProperty("version") as String
group = "dev.slne.surf.teleporter"

surfPaperPluginApi {
    mainClass("dev.slne.surf.teleporter.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(true)

    authors.add("Jo_field")
}