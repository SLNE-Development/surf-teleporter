package dev.slne.surf.teleporter.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.home.config.homes.TeleporterConfigHolder
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.teleporter.TeleporterService

fun CommandAPICommand.teleporterReloadCommand() = subcommand("reload") {
    withPermission(Permissions.COMMAND_TELEPORTER_RELOAD)

    anyExecutor { sender, _ ->
        TeleporterConfigHolder.reload()
        TeleporterService.registerTeleporters()

        sender.sendText {
            appendSuccessPrefix()
            success("Successfully reloaded Teleporter config!")

            appendNewSuccessPrefixedLine()
            success("Sucesfully loaded")
            appendSpace()
            variableValue(TeleporterService.teleporterCount)
            appendSpace()
            success("teleporters!")
        }
    }
}