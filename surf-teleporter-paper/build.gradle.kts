plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.teleporter.PaperMain")
    authors.addAll("Jo_field", "Ammo")
}

paper {
    name = "surf-teleporter" // No data loss after module split
}

dependencies {
    api(projects.surfTeleporterCoreClient)
}
