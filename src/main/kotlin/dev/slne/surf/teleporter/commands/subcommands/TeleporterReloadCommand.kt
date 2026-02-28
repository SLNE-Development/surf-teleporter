package dev.slne.surf.teleporter.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.teleporter.TeleporterConfig
import dev.slne.surf.teleporter.teleporter.TeleporterService

fun CommandAPICommand.teleporterReloadCommand() = subcommand("reload") {
    withPermission(Permissions.COMMAND_TELEPORTER_RELOAD)

    anyExecutor { sender, _ ->
        TeleporterConfig.reloadFromFile()
        TeleporterService.registerTeleporters()

        sender.sendText {
            appendSuccessPrefix()
            success("Successfully reloaded Teleporter config!")
        }
    }
}