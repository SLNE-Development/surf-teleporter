package dev.slne.surf.teleporter.listeners

import dev.slne.surf.teleporter.teleporter.teleporterService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener : Listener {
    @EventHandler()
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        teleporterService.visualizeTeleportersForSpecific(player)
    }
}