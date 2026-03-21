package dev.slne.surf.teleporter.teleporter

import kotlinx.serialization.Transient
import org.bukkit.Bukkit
import org.bukkit.Location
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.util.*

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

    fun toApi() = Teleporter(
        uuid = uuid,
        name = name,
        originLocation = originLocation,
        targetLocation = targetLocation,
        width = width,
        length = length,
        height = height,
    )

    companion object {
        fun fromApi(teleporter: Teleporter) = TeleporterDto(
            name = teleporter.name,
            uuid = teleporter.uuid,
            originWorldId = teleporter.originLocation.world.uid,
            originX = teleporter.originLocation.x,
            originY = teleporter.originLocation.y,
            originZ = teleporter.originLocation.z,
            originYaw = teleporter.originLocation.yaw,
            originPitch = teleporter.originLocation.pitch,
            targetWorldId = teleporter.targetLocation.world.uid,
            targetX = teleporter.targetLocation.x,
            targetY = teleporter.targetLocation.y,
            targetZ = teleporter.targetLocation.z,
            targetYaw = teleporter.targetLocation.yaw,
            targetPitch = teleporter.targetLocation.pitch,
            width = teleporter.width,
            length = teleporter.length,
            height = teleporter.height,
        )
    }
}