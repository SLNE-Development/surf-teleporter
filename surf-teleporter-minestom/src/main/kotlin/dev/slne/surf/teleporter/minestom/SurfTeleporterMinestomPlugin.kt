package dev.slne.surf.teleporter.minestom

import dev.slne.surf.api.minestom.plugin.SurfMinestomPlugin
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.minestom.command.teleporterCommand
import dev.slne.surf.teleporter.minestom.listener.TeleporterListener

object SurfTeleporterMinestomPlugin : SurfMinestomPlugin() {
    override suspend fun onLoad() {
        TeleporterService.registerTeleporters()
    }

    override suspend fun onEnable() {
        teleporterCommand()
        TeleporterListener.register(eventNode)
    }
}
