import dev.slne.surf.api.gradle.util.slneReleases

plugins {
    id("dev.slne.surf.api.gradle.minestom")
}

dependencies {
    api(projects.surfTeleporterCoreClient)
}

publishing {
    repositories {
        slneReleases()
    }
}
