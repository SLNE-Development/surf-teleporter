package dev.slne.surf.teleporter.minestom.command

import dev.slne.minestom.lobby.api.command.commandapi.dsl.anyExecutor
import dev.slne.minestom.lobby.api.command.commandapi.dsl.blockPositionArgument
import dev.slne.minestom.lobby.api.command.commandapi.dsl.booleanArgument
import dev.slne.minestom.lobby.api.command.commandapi.dsl.commandTree
import dev.slne.minestom.lobby.api.command.commandapi.dsl.literalArgument
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutor
import dev.slne.surf.teleporter.core.client.message.TeleporterMessages
import dev.slne.surf.teleporter.core.client.permission.TeleporterPermissions
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.minestom.dialog.TeleporterMainDialog
import dev.slne.surf.teleporter.minestom.dialog.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.minestom.dialog.crud.view.TeleporterListDialog
import dev.slne.surf.teleporter.minestom.teleporter.toTeleporterPosition
import net.minestom.server.coordinate.Vec

private const val TELEPORTER_LOCATION_ARGUMENT = "teleporterLocation"
private const val SAVE_BEFORE_RELOAD_ARGUMENT = "saveBeforeReload"

/**
 * The reload branch and the position branch hang off the root side by side, because a subcommand
 * would otherwise be reachable only behind the position.
 */
fun teleporterCommand() = commandTree("teleporter") {
    withPermission(TeleporterPermissions.COMMAND_TELEPORTER_GENERIC)

    literalArgument("reload") {
        withPermission(TeleporterPermissions.COMMAND_TELEPORTER_RELOAD)

        booleanArgument(SAVE_BEFORE_RELOAD_ARGUMENT, optional = true) {
            anyExecutor { sender, arguments ->
                val saveBeforeReload =
                    arguments.getOptional<Boolean>(SAVE_BEFORE_RELOAD_ARGUMENT) == true

                if (saveBeforeReload) {
                    TeleporterService.saveTeleporters()
                }

                TeleporterService.registerTeleporters()

                sender.sendMessage(
                    TeleporterMessages.reloaded(TeleporterService.teleporterCount)
                )
            }
        }
    }

    blockPositionArgument(TELEPORTER_LOCATION_ARGUMENT, optional = true) {
        playerExecutor { player, arguments ->
            val teleporterLocation = arguments.getOptional<Vec>(TELEPORTER_LOCATION_ARGUMENT)
            if (teleporterLocation == null) {
                player.showDialog(TeleporterMainDialog.createDialog())
                return@playerExecutor
            }

            val instance = player.instance ?: return@playerExecutor
            val position =
                teleporterLocation.toTeleporterPosition(instance) ?: return@playerExecutor
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
}
