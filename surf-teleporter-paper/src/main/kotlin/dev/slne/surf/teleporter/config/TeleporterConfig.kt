package dev.slne.surf.teleporter.config

import dev.slne.surf.api.core.config.SpongeYmlConfigClass
import dev.slne.surf.teleporter.plugin
import dev.slne.surf.teleporter.teleporter.TeleporterDto
import org.spongepowered.configurate.objectmapping.ConfigSerializable

/**
 * The YAML file teleporters were stored in before they moved into a binary tag file, kept so that
 * existing files can still be migrated.
 */
@Deprecated("Teleporters are stored in a binary tag file now; the YAML file is only read to migrate it")
@ConfigSerializable
data class TeleporterConfig(
    val teleporters: MutableList<TeleporterDto>
) {
    @Suppress("DEPRECATION")
    companion object : SpongeYmlConfigClass<TeleporterConfig>(
        configClass = TeleporterConfig::class.java,
        plugin.dataPath,
        "teleporters.yml"
    )
}
