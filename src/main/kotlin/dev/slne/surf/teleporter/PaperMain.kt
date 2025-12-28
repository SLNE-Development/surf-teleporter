package dev.slne.surf.teleporter

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.teleporter.commands.teleporterCommand
import dev.slne.surf.teleporter.listeners.PlayerMoveListener
import dev.slne.surf.teleporter.storage.storageService
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {

    override fun onEnable() {
        teleporterCommand()

        PlayerMoveListener.register()
        storageService.init()
        storageService.loadTeleporters()
    }

    override fun onDisable() {
        storageService.saveTeleporters()
    }
}

val plugin: PaperMain get() = JavaPlugin.getPlugin(PaperMain::class.java)
