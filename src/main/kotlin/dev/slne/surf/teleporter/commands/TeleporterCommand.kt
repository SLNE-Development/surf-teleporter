package dev.slne.surf.teleporter.commands

import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.locationArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterListDialog
import dev.slne.surf.teleporter.teleporter.teleporterService
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location

fun teleporterCommand() = commandAPICommand("teleporter") {

    withPermission(Permissions.COMMAND_TELEPORTER_GENERIC)
    locationArgument("location", LocationType.BLOCK_POSITION, optional = true)

    playerExecutor { player, arguments ->
        val location = arguments.getUnchecked<Location>("location")
        if (location == null) {
            player.showDialog(TeleporterMainDialog.showDialog())
            return@playerExecutor
        }
        val teleporterAtBlock = teleporterService.getTeleporterAt(location)
        val teleporterAbove = teleporterService.getTeleporterAt(location.clone().add(0.0, 1.0, 0.0))
        val teleporter = teleporterAtBlock ?: teleporterAbove

        if (teleporter == null) {
            val clickable = buildText {
                text("HIER", Colors.VARIABLE_VALUE, TextDecoration.UNDERLINED)
                hoverEvent(HoverEvent.showText(buildText { info("Klicke hier, um ir die Liste existierenden Teleporter anzusehen.") }))
                clickEvent(ClickEvent.callback { player.showDialog(TeleporterListDialog.showDialog()) })
            }

            player.sendText {
                appendPrefix()
                error("An dieser Stelle befindet sich kein Teleporter!")
                appendNewPrefixedLine()
                error("Klicke ")
                append(clickable)
                error(" um dir die Liste der existierenden Telepoerter anzusehen.")
            }
            return@playerExecutor
        }
        player.showDialog(TeleporterInfoDialog.showDialog(teleporter))
    }
}