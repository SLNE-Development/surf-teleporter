package dev.slne.surf.teleporter.minestom.command

import com.google.inject.Singleton
import dev.slne.minestom.lobby.api.command.CommandRegistrar

@Singleton
class TeleporterCommandRegistrar : CommandRegistrar {
    override fun register() {
        teleporterCommand()
    }
}
