package dev.slne.surf.teleporter.commands

import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.locationArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.teleporter.commands.subcommands.teleporterReloadCommand
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterListDialog
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.teleporter.TeleporterService
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location

fun teleporterCommand() = commandAPICommand("teleporter") {
    withPermission(Permissions.COMMAND_TELEPORTER_GENERIC)
    locationArgument("teleporterLocation", LocationType.BLOCK_POSITION, optional = true)
    teleporterReloadCommand()

    playerExecutor { player, arguments ->
        val teleporterLocation = arguments.getUnchecked<Location>("teleporterLocation")
        if (teleporterLocation == null) {
            player.showDialog(TeleporterMainDialog.createDialog())
            return@playerExecutor
        }
        val teleporterAtBlock = TeleporterService.getTeleporterAt(teleporterLocation)
        val teleporterAbove =
            TeleporterService.getTeleporterAt(teleporterLocation.clone().add(0.0, 1.0, 0.0))
        val teleporter = teleporterAtBlock ?: teleporterAbove

        if (teleporter == null) {
            val clickable = buildText {
                text("HIER", Colors.VARIABLE_VALUE, TextDecoration.UNDERLINED)
                hoverEvent(HoverEvent.showText(buildText { info("Klicke hier, um dir die Liste der existierenden Teleporter anzusehen.") }))
                clickEvent(ClickEvent.callback { player.showDialog(TeleporterListDialog.createDialog()) })
            }

            player.sendText {
                appendErrorPrefix()
                error("An dieser Stelle befindet sich kein Teleporter!")
                appendNewErrorPrefixedLine()
                error("Klicke ")
                append(clickable)
                error(" um dir die Liste der existierenden Telepoerter anzusehen.")
            }
            return@playerExecutor
        }
        player.showDialog(TeleporterInfoDialog.createDialog(teleporter))
    }
}