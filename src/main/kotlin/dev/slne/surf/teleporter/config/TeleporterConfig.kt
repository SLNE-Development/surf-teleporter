package dev.slne.surf.teleporter.config

import dev.slne.surf.api.core.config.SpongeYmlConfigClass
import dev.slne.surf.teleporter.plugin
import dev.slne.surf.teleporter.teleporter.TeleporterDto
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class TeleporterConfig(
    val teleporters: MutableList<TeleporterDto>
) {
    companion object : SpongeYmlConfigClass<TeleporterConfig>(
        configClass = TeleporterConfig::class.java,
        plugin.dataPath,
        "teleporters.yml"
    )
}