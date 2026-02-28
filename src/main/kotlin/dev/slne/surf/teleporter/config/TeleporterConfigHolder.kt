package dev.slne.surf.home.config.homes

import dev.slne.surf.surfapi.core.api.config.createSpongeYmlConfig
import dev.slne.surf.surfapi.core.api.config.manager.SpongeConfigManager
import dev.slne.surf.surfapi.core.api.config.surfConfigApi
import dev.slne.surf.teleporter.config.TeleporterConfig
import dev.slne.surf.teleporter.plugin

object TeleporterConfigHolder {
    private val manager: SpongeConfigManager<TeleporterConfig>

    init {
        surfConfigApi.createSpongeYmlConfig<TeleporterConfig>(plugin.dataPath, "teleporters.yml")
        manager = surfConfigApi.getSpongeConfigManagerForConfig(TeleporterConfig::class.java)
    }

    val config: TeleporterConfig get() = manager.config

    fun save() {
        manager.save()
    }

    fun reload() {
        manager.reloadFromFile()
    }
}

val teleporterConfig get() = TeleporterConfigHolder.config