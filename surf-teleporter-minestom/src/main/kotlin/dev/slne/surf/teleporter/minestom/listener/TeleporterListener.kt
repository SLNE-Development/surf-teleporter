package dev.slne.surf.teleporter.minestom.listener

import com.google.inject.Singleton
import dev.slne.minestom.lobby.api.coroutine.minestomScope
import dev.slne.minestom.lobby.api.event.EventRegistrar
import dev.slne.minestom.lobby.api.extension.addListener
import dev.slne.minestom.lobby.api.player.requireLobbyPlayer
import dev.slne.surf.teleporter.core.client.message.TeleporterMessages
import dev.slne.surf.teleporter.core.client.permission.TeleporterPermissions
import dev.slne.surf.teleporter.core.client.sound.playTeleportSound
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterCooldownService
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.minestom.dialog.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.minestom.teleporter.moveTo
import dev.slne.surf.teleporter.minestom.teleporter.toTeleporterPosition
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import net.minestom.server.coordinate.Point
import net.minestom.server.entity.GameMode
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.event.player.PlayerMoveEvent

/**
 * Handles everything players do to teleporters: stepping onto them and touching them.
 */
@Singleton
class TeleporterListener : EventRegistrar {

    override fun register(node: EventNode<Event>) {
        node.addListener(::onPlayerMove)
        node.addListener(::onBlockInteract)
    }

    private fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        if (!hasChangedBlock(player.position, event.newPosition)) return

        if (player.gameMode == GameMode.SPECTATOR) return

        val instance = player.instance ?: return
        val position = event.newPosition.toTeleporterPosition(instance) ?: return
        val teleporter = TeleporterService.getTeleporterAt(position) ?: return

        val use = TeleporterCooldownService.startUse(player.uuid) ?: return

        val move = player.moveTo(teleporter.targetLocation)
        if (move == null) {
            TeleporterCooldownService.cancelUse(player.uuid, use)
            return
        }

        minestomScope.launch {
            try {
                move.await()
                TeleporterCooldownService.finishUse(player.uuid, use)
                player.playTeleportSound()
            } catch (throwable: Throwable) {
                TeleporterCooldownService.cancelUse(player.uuid, use)
                throw throwable
            }
        }
    }

    private fun onBlockInteract(event: PlayerBlockInteractEvent) {
        val player = event.player

        if (!player.requireLobbyPlayer()
                .hasPermission(TeleporterPermissions.COMMAND_TELEPORTER_GENERIC)
        ) {
            return
        }

        val position = event.blockPosition.toTeleporterPosition(event.instance) ?: return

        val padAtBlock = TeleporterService.getTeleporterAt(position)
        val padAbove = TeleporterService.getTeleporterAt(position.add(0.0, 1.0, 0.0))

        val porter = padAtBlock ?: padAbove ?: return

        player.sendMessage(
            TeleporterMessages.teleporterAtBlock { TeleporterInfoDialog.createDialog(porter) }
        )

        event.isCancelled = true
    }

    private fun hasChangedBlock(from: Point, to: Point) =
        from.blockX() != to.blockX() || from.blockY() != to.blockY() || from.blockZ() != to.blockZ()
}
