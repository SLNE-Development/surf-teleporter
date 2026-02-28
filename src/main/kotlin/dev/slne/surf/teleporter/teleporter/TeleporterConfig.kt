package dev.slne.surf.teleporter.teleporter

import dev.slne.surf.surfapi.core.api.config.SpongeYmlConfigClass
import dev.slne.surf.teleporter.plugin
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class TeleporterConfig(
    val teleporters: MutableList<Teleporter>
) {
    companion object : SpongeYmlConfigClass<TeleporterConfig>(
        TeleporterConfig::class.java,
        plugin.dataPath,
        "teleporters.yml"
    )
}