package dev.slne.surf.teleporter.listeners

import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.teleporter.teleporterService
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object PlayerInteractListener : Listener {

    @EventHandler
    fun onBlockInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        val location = block.location
        val player = event.player

        if (!player.hasPermission(Permissions.COMMAND_TELEPORTER_GENERIC)) return

        val padAtBlock = teleporterService.getTeleporterAt(location)

        val blockAboveLocation = location.clone().add(0.0, 1.0, 0.0)
        val padAbove = teleporterService.getTeleporterAt(blockAboveLocation)

        val porter = padAtBlock ?: padAbove ?: return

        val clickable = buildText {
            text("HIER", Colors.VARIABLE_VALUE, TextDecoration.UNDERLINED)
            hoverEvent(HoverEvent.showText(buildText { info("Klicke hier, um dir das JumpPad anzusehen.") }))
            clickEvent(ClickEvent.callback { player.showDialog(TeleporterInfoDialog.showDialog(porter)) })
        }

        player.sendText {
            appendErrorPrefix()
            error("An dieser Stelle befindet sich ein Teleporter!")
            appendNewErrorPrefixedLine()
            error("Klicke ")
            append(clickable)
            error(" um dir den Teleporter anzusehen!")
        }

        event.isCancelled = true
    }
}