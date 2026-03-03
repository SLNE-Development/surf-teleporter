package dev.slne.surf.teleporter.teleporter

import kotlinx.serialization.Transient
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.util.BoundingBox
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.util.*

fun teleporter(
    uuid: UUID,
    originLocation: Location,
    targetLocation: Location,
    width: Int,
    length: Int
) = Teleporter(
    uuid = uuid,
    originWorldId = originLocation.world.uid,
    originX = originLocation.x,
    originY = originLocation.y,
    originZ = originLocation.z,
    originYaw = originLocation.yaw,
    originPitch = originLocation.pitch,
    targetWorldId = targetLocation.world.uid,
    targetX = targetLocation.x,
    targetY = targetLocation.y,
    targetZ = targetLocation.z,
    targetYaw = targetLocation.yaw,
    targetPitch = targetLocation.pitch,
    width = width,
    length = length
)

@ConfigSerializable
data class Teleporter(
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

    var width: Int,
    var length: Int
) {
    @Transient
    var originLocation: Location
        get() = Location(
            Bukkit.getWorld(originWorldId)!!,
            originX,
            originY,
            originZ,
            originYaw,
            originPitch
        )
        set(value) {
            originWorldId = value.world.uid
            originX = value.x
            originY = value.y
            originZ = value.z
            originYaw = value.yaw
            originPitch = value.pitch
        }

    @Transient
    var targetLocation: Location
        get() = Location(
            Bukkit.getWorld(targetWorldId)!!,
            targetX,
            targetY,
            targetZ,
            targetYaw,
            targetPitch
        )
        set(value) {
            targetWorldId = value.world.uid
            targetX = value.x
            targetY = value.y
            targetZ = value.z
            targetYaw = value.yaw
            targetPitch = value.pitch
        }

    @Transient
    val boundingBox
        get() = BoundingBox(
            originLocation.blockX - width / 2.0,
            originLocation.blockY.toDouble(),
            originLocation.blockZ - length / 2.0,
            originLocation.blockX + width / 2.0,
            originLocation.blockY.toDouble() + 1,
            originLocation.blockZ + length / 2.0
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Teleporter

        return uuid == other.uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    override fun toString(): String {
        return "Teleporter(uuid=$uuid, originLocation=$originLocation, targetLocation=$targetLocation, boundingBox=$boundingBox)"
    }
}