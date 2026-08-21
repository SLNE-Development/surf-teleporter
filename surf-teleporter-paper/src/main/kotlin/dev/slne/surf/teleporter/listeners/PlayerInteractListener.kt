package dev.slne.surf.teleporter.listeners

import dev.slne.surf.teleporter.core.client.message.TeleporterMessages
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.teleporter.toTeleporterPosition
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object PlayerInteractListener : Listener {

    @EventHandler
    fun onBlockInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        val player = event.player

        if (!player.hasPermission(Permissions.COMMAND_TELEPORTER_GENERIC)) return

        val position = block.location.toTeleporterPosition() ?: return

        val padAtBlock = TeleporterService.getTeleporterAt(position)
        val padAbove = TeleporterService.getTeleporterAt(position.add(0.0, 1.0, 0.0))

        val porter = padAtBlock ?: padAbove ?: return

        player.sendMessage(
            TeleporterMessages.teleporterAtBlock { TeleporterInfoDialog.createDialog(porter) }
        )

        event.isCancelled = true
    }
}
