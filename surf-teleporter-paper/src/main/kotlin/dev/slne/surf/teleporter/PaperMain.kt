package dev.slne.surf.teleporter

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.api.paper.event.register
import dev.slne.surf.teleporter.commands.teleporterCommand
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.listeners.PlayerInteractListener
import dev.slne.surf.teleporter.listeners.PlayerMoveListener
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {
    override suspend fun onEnableAsync() {
        teleporterCommand()

        TeleporterService.registerTeleporters()

        PlayerMoveListener.register()
        PlayerInteractListener.register()
    }
}

val plugin: PaperMain get() = JavaPlugin.getPlugin(PaperMain::class.java)
