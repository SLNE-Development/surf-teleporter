package dev.slne.surf.teleporter

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.teleporter.commands.teleporterCommand
import dev.slne.surf.teleporter.listeners.PlayerInteractListener
import dev.slne.surf.teleporter.listeners.PlayerMoveListener
import dev.slne.surf.teleporter.teleporter.TeleporterService
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
