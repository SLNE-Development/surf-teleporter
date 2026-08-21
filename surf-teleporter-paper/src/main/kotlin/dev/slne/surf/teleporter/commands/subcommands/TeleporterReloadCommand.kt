package dev.slne.surf.teleporter.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.booleanArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.teleporter.core.client.message.TeleporterMessages
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.permissions.Permissions

fun CommandAPICommand.teleporterReloadCommand() = subcommand("reload") {
    withPermission(Permissions.COMMAND_TELEPORTER_RELOAD)

    booleanArgument("saveBeforeReload", optional = true)

    anyExecutor { sender, arguments ->
        val saveBeforeReload = arguments.getOrDefaultUnchecked("saveBeforeReload", false)

        if (saveBeforeReload) {
            TeleporterService.saveTeleporters()
        }

        TeleporterService.registerTeleporters()

        sender.sendMessage(TeleporterMessages.reloaded(TeleporterService.teleporterCount))
    }
}
