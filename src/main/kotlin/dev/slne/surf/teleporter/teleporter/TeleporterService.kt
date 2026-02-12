package dev.slne.surf.teleporter.teleporter

import org.bukkit.Location

val teleporterService = TeleporterManager

object TeleporterManager {

    private val teleporter = mutableSetOf<Teleporter>()

    fun registerTeleporter(porter: Teleporter) {
        teleporter.removeIf { it.uuid == porter.uuid }
        addTeleporter(porter)
    }

    fun addTeleporter(porter: Teleporter) {
        teleporter.add(porter)
    }

    fun deleteTeleporter(porter: Teleporter) {
        teleporter.removeIf { it.uuid == porter.uuid }
    }

    fun updateTeleporter(porter: Teleporter) {
        deleteTeleporter(porter)
        addTeleporter(porter)
    }

    fun getTeleporterAt(location: Location): Teleporter? {
        return teleporter.firstOrNull { pad ->
            val dx = location.blockX - pad.originLocation.blockX
            val dz = location.blockZ - pad.originLocation.blockZ

            dx in -(pad.width / 2)..(pad.width / 2) &&
                    dz in -(pad.length / 2)..(pad.length / 2) &&
                    location.blockY == pad.originLocation.blockY
        }
    }

    fun getTeleporters(): List<Teleporter> = teleporter.toList()

    val teleporterCount: Int get() = teleporter.size
}