package dev.slne.surf.teleporter.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.booleanArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.teleporter.config.TeleporterConfig
import dev.slne.surf.teleporter.permissions.Permissions
import dev.slne.surf.teleporter.teleporter.TeleporterService

fun CommandAPICommand.teleporterReloadCommand() = subcommand("reload") {
    withPermission(Permissions.COMMAND_TELEPORTER_RELOAD)

    booleanArgument("saveBeforeReload", optional = true)

    anyExecutor { sender, arguments ->
        val saveBeforeReload = arguments.getOrDefaultUnchecked("saveBeforeReload", false)

        if (saveBeforeReload) {
            TeleporterService.saveTeleporters()
        }

        TeleporterConfig.reloadFromFile()
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