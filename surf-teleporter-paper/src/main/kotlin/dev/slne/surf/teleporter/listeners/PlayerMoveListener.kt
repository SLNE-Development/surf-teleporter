package dev.slne.surf.teleporter.listeners

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.teleporter.core.client.sound.playTeleportSound
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterCooldownService
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.plugin
import dev.slne.surf.teleporter.teleporter.toLocation
import dev.slne.surf.teleporter.teleporter.toTeleporterPosition
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object PlayerMoveListener : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasExplicitlyChangedBlock()) return
        val player = event.player

        if (player.gameMode == GameMode.SPECTATOR) return
        if (TeleporterService.teleporterCount == 0) return

        val position = event.to.toTeleporterPosition() ?: return
        val teleporter = TeleporterService.getTeleporterAt(position) ?: return

        val playerUuid = player.uniqueId
        val use = TeleporterCooldownService.startUse(playerUuid) ?: return
        val target = teleporter.targetLocation.toLocation()
        if (target == null) {
            TeleporterCooldownService.cancelUse(playerUuid, use)
            return
        }

        plugin.launch {
            try {
                player.teleportAsync(target).await()
                withContext(plugin.regionDispatcher(target)) {
                    player.playTeleportSound()
                }

                TeleporterCooldownService.finishUse(playerUuid, use)
            } catch (throwable: Throwable) {
                TeleporterCooldownService.cancelUse(playerUuid, use)
                throw throwable
            }
        }
    }
}
