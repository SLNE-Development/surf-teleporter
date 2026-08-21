package dev.slne.surf.teleporter.minestom

import com.google.auto.service.AutoService
import dev.slne.minestom.lobby.api.plugin.MinestomPlugin
import dev.slne.minestom.lobby.api.plugin.annotation.MinestomPluginMeta
import dev.slne.surf.teleporter.minestom.command.TeleporterCommandRegistrar
import dev.slne.surf.teleporter.minestom.listener.TeleporterListener

@AutoService(MinestomPlugin::class)
@MinestomPluginMeta(
    "surf-teleporter-minestom",
    dependsOn = ["surf-api-minestom"]
)
class SurfTeleporterMinestomPlugin :
    MinestomPlugin(SurfTeleporterMinestomEntrypoint::class.java) {
    override fun configurePlugin() {
        bindEventRegistrar<TeleporterListener>()
        bindCommandRegistrar<TeleporterCommandRegistrar>()
    }
}
