package dev.slne.surf.teleporter.teleporter

import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterPosition
import org.bukkit.Bukkit
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.util.*

/**
 * The YAML shape teleporters were stored in before they moved into a binary tag file, kept so that
 * existing files can still be migrated.
 */
@ConfigSerializable
data class TeleporterDto(
    val name: String,
    val uuid: UUID,

    @Setting("origin_world")
    var originWorldId: UUID,
    var originX: Double,
    var originY: Double,
    var originZ: Double,
    var originYaw: Float,
    var originPitch: Float,

    @Setting("target_world")
    var targetWorldId: UUID,
    var targetX: Double,
    var targetY: Double,
    var targetZ: Double,
    var targetYaw: Float,
    var targetPitch: Float,

    var width: Double,
    var length: Double,
    val height: Double
) {
    fun toTeleporter() = Teleporter(
        uuid = uuid,
        name = name,
        originLocation = TeleporterPosition(
            worldKey(originWorldId),
            originX,
            originY,
            originZ,
            originYaw,
            originPitch
        ),
        targetLocation = TeleporterPosition(
            worldKey(targetWorldId),
            targetX,
            targetY,
            targetZ,
            targetYaw,
            targetPitch
        ),
        width = width,
        length = length,
        height = height,
    )

    companion object {
        private fun worldKey(worldId: UUID) = Bukkit.getWorld(worldId)!!.key()
    }
}
