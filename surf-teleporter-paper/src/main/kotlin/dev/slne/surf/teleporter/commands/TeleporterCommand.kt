package dev.slne.surf.teleporter.commands

import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.locationArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.teleporter.commands.subcommands.teleporterReloadCommand
import dev.slne.surf.teleporter.core.client.message.TeleporterMessages
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterListDialog
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.teleporter.toTeleporterPosition
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
        val position = teleporterLocation.toTeleporterPosition() ?: return@playerExecutor
        val teleporterAtBlock = TeleporterService.getTeleporterAt(position)
        val teleporterAbove = TeleporterService.getTeleporterAt(position.add(0.0, 1.0, 0.0))
        val teleporter = teleporterAtBlock ?: teleporterAbove

        if (teleporter == null) {
            player.sendMessage(
                TeleporterMessages.noTeleporterAtBlock { TeleporterListDialog.createDialog() }
            )
            return@playerExecutor
        }
        player.showDialog(TeleporterInfoDialog.createDialog(teleporter))
    }
}
