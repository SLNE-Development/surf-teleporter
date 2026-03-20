package dev.slne.surf.teleporter.listeners

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.teleporter.plugin
import dev.slne.surf.teleporter.sound.soundService
import dev.slne.surf.teleporter.teleporter.TeleporterService
import kotlinx.coroutines.future.await
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

object PlayerMoveListener : Listener {
    private val cooldowns: ConcurrentHashMap<UUID, Long> = ConcurrentHashMap()
    private val cooldown: Long = 3.seconds.inWholeMilliseconds

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasExplicitlyChangedBlock()) return
        val player = event.player

        if (player.gameMode == GameMode.SPECTATOR) return
        val teleporter = TeleporterService.getTeleporterAt(event.to) ?: return

        val now = System.currentTimeMillis()
        val lastUse = cooldowns[player.uniqueId] ?: 0L
        if (now - lastUse < cooldown) return

        plugin.launch(plugin.regionDispatcher(teleporter.targetLocation)) {
            player.teleportAsync(teleporter.targetLocation).await()
            soundService.playTeleportSound(player)
            
            cooldowns[player.uniqueId] = now
        }
    }
}