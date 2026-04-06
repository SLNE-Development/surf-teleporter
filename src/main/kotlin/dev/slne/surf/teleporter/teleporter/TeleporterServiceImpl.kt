package dev.slne.surf.teleporter.teleporter

import com.google.auto.service.AutoService
import dev.slne.surf.api.core.util.freeze
import dev.slne.surf.api.core.util.mutableObjectListOf
import dev.slne.surf.teleporter.config.TeleporterConfig
import net.kyori.adventure.util.Services
import org.bukkit.Location

@AutoService(TeleporterService::class)
class TeleporterServiceImpl : TeleporterService, Services.Fallback {
    private val _teleporters = mutableObjectListOf<Teleporter>()
    override val teleporters get() = _teleporters.freeze()

    override val teleporterCount get() = teleporters.size

    override fun registerTeleporters() {
        _teleporters.clear()
        _teleporters.addAll(TeleporterConfig.getConfig().teleporters.map { it.toApi() })
    }

    override fun registerTeleporter(teleporter: Teleporter) {
        _teleporters.add(teleporter)

        saveTeleporters()
    }

    override fun unregisterTeleporter(teleporter: Teleporter) {
        _teleporters.remove(teleporter)

        saveTeleporters()
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
        val config = TeleporterConfig.getConfig()

        config.teleporters.clear()
        config.teleporters.addAll(teleporters.map { TeleporterDto.fromApi(it) })

        TeleporterConfig.save()
    }
}