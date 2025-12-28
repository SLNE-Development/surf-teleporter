package dev.slne.surf.teleporter.teleporter

import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.bukkit.api.glow.glowingApi
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.Player

class TeleporterManager {

    private val teleporter = mutableSetOf<Teleporter>()

    fun registerTeleporter(porter: Teleporter) {
        teleporter.removeIf { it.uuid == porter.uuid }
        addTeleporter(porter)
    }

    fun addTeleporter(porter: Teleporter) {
        teleporter.add(porter)
        visualizeTeleporterForAll(porter)
    }

    fun deleteTeleporter(porter: Teleporter) {
        removeTeleportVisualization(porter)
        teleporter.removeIf { it.uuid == porter.uuid }
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

    fun visualizeTeleporterForAll(porter: Teleporter) {
        for (player in server.onlinePlayers) {
            visualizeTeleporter(player, porter)
        }
    }

    fun visualizeTeleportersForSpecific(player: Player) {
        getteleporters().forEach { porter ->
            visualizeTeleporter(player, porter)
        }
    }

    private fun visualizeTeleporter(player: Player, porter: Teleporter) {
        val origin = porter.originLocation
        val world = origin.world

        val halfWidth = porter.width / 2
        val halfLength = porter.length / 2


        for (dx in -halfWidth..halfWidth) {
            for (dz in -halfLength..halfLength) {
                val x = origin.blockX + dx
                val y = origin.blockY - 1
                val z = origin.blockZ + dz

                val location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

                glowingApi.makeGlowing(location, player, NamedTextColor.DARK_PURPLE)
            }
        }
    }

    fun removeTeleportVisualization(porter: Teleporter) {
        val origin = porter.originLocation
        val world = origin.world

        val halfWidth = porter.width / 2
        val halfLength = porter.length / 2

        for (dx in -halfWidth..halfWidth) {
            for (dz in -halfLength..halfLength) {
                val x = origin.blockX + dx
                val y = origin.blockY - 1
                val z = origin.blockZ + dz

                val location = world.getBlockAt(x, y, z).location

                for (player in server.onlinePlayers) {
                    glowingApi.removeGlowing(location, player)
                }
            }
        }
    }


    fun getteleporters(): List<Teleporter> = teleporter.toList()

    companion object {
        val INSTANCE = TeleporterManager()
    }
}

val teleporterService get() = TeleporterManager.INSTANCE