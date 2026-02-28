package dev.slne.surf.teleporter.teleporter

import com.google.auto.service.AutoService
import dev.slne.surf.home.config.homes.TeleporterConfigHolder
import dev.slne.surf.home.config.homes.teleporterConfig
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import net.kyori.adventure.util.Services
import org.bukkit.Location

@AutoService(TeleporterService::class)
class TeleporterServiceImpl : TeleporterService, Services.Fallback {
    private val _teleporters = mutableObjectListOf<Teleporter>()
    override val teleporters get() = _teleporters.freeze()

    override val teleporterCount get() = teleporters.size

    override fun registerTeleporters() {
        _teleporters.clear()
        _teleporters.addAll(teleporterConfig.teleporters)
    }

    override fun registerTeleporter(teleporter: Teleporter) {
        _teleporters.add(teleporter)

        teleporterConfig.apply {
            teleporters.add(teleporter)
        }
        TeleporterConfigHolder.save()
    }

    override fun unregisterTeleporter(teleporter: Teleporter) {
        _teleporters.remove(teleporter)

        teleporterConfig.apply {
            teleporters.remove(teleporter)
        }
        TeleporterConfigHolder.save()
    }

    override fun getTeleporterAt(location: Location) = teleporters.firstOrNull { teleporter ->
        val boundingBox = teleporter.boundingBox

        val originLoaded = teleporter.originLocation.isWorldLoaded
        val locationLoaded = location.isWorldLoaded
        if (!originLoaded || !locationLoaded) return@firstOrNull false

        val worldMatches = teleporter.originLocation.world.uid == location.world.uid
        val boundingBoxContains = boundingBox.contains(location.toVector())

        worldMatches && boundingBoxContains
    }

    override fun saveTeleporters() {
        TeleporterConfigHolder.save()
    }
}