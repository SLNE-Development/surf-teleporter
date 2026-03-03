package dev.slne.surf.teleporter.config

import dev.slne.surf.teleporter.teleporter.Teleporter
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class TeleporterConfig(
    val teleporters: MutableList<Teleporter>
)