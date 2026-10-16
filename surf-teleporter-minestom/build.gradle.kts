import dev.slne.surf.api.gradle.util.slneReleases

plugins {
    id("dev.slne.surf.api.gradle.minestom")
}

minestomPluginFile {
    main = "dev.slne.surf.teleporter.minestom.SurfTeleporterMinestomPlugin"
    authors = listOf("Jo_field", "Ammo", "twisti")
}

dependencies {
    api(projects.surfTeleporterCoreClient)
}

publishing {
    repositories {
        slneReleases()
    }
}
