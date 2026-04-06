plugins {
    id("dev.slne.surf.api.gradle.paper-plugin") version "+"
}

version = findProperty("version") as String
group = "dev.slne.surf.teleporter"

surfPaperPluginApi {
    mainClass("dev.slne.surf.teleporter.PaperMain")
    authors.addAll("Jo_field", "Ammo")
}