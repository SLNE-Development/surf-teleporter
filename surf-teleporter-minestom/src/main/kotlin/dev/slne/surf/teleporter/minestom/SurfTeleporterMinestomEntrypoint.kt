package dev.slne.surf.teleporter.minestom

import com.google.inject.Inject
import com.google.inject.Singleton
import dev.slne.minestom.lobby.api.plugin.MinestomPluginEntrypoint
import dev.slne.minestom.lobby.api.plugin.annotation.DataDirectory
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import java.nio.file.Path

@Singleton
class SurfTeleporterMinestomEntrypoint @Inject constructor(
    @DataDirectory path: Path
) : MinestomPluginEntrypoint {

    init {
        dataPath = path
    }

    override suspend fun start() {
        TeleporterService.registerTeleporters()
    }

    companion object {
        lateinit var dataPath: Path
            private set
    }
}
