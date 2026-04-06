package dev.slne.surf.teleporter.teleporter.field.fields

import dev.slne.surf.api.paper.extensions.server
import dev.slne.surf.teleporter.teleporter.field.TeleporterField
import dev.slne.surf.teleporter.teleporter.field.TeleporterFieldParseResult
import org.bukkit.Location

class TeleporterLocationField(
    override val fieldName: String,
    override val fieldDisplayName: String,
    override val initialValue: Location
) : TeleporterField<Location> {
    override var currentValue: Location = initialValue
        private set

    override fun parse(value: String): TeleporterFieldParseResult {
        val split = value.split(", ")

        if (split.size != 6) {
            return WrongFormatResult(fieldName)
        }

        val worldName = split[0]
        val x = split[1].toDoubleOrNull()
        val y = split[2].toDoubleOrNull()
        val z = split[3].toDoubleOrNull()
        val yaw = split[4].toFloatOrNull()
        val pitch = split[5].toFloatOrNull()

        if (x == null || y == null || z == null || yaw == null || pitch == null) {
            return WrongFormatResult(fieldName)
        }

        val world = server.getWorld(worldName) ?: return WorldNotFoundResult(worldName)

        currentValue = Location(world, x, y, z, yaw, pitch)

        return TeleporterFieldParseResult.Success(currentValue)
    }

    override fun asString(): String {
        val world = currentValue.world.name
        val x = "%.2f".format(currentValue.x)
        val y = "%.2f".format(currentValue.y)
        val z = "%.2f".format(currentValue.z)
        val yaw = "%.2f".format(currentValue.yaw)
        val pitch = "%.2f".format(currentValue.pitch)

        return "$world, $x, $y, $z, $yaw, $pitch"
    }

    class WorldNotFoundResult(
        private val worldName: String
    ) : TeleporterFieldParseResult({
        error("Die Welt ")
        variableValue(worldName)
        error(" wurde nicht gefunden.")
    })

    class WrongFormatResult(
        private val fieldName: String
    ) : TeleporterFieldParseResult({
        error("Die Position für das Feld ")
        variableValue(fieldName)
        error(" muss im Format ")
        variableValue("Welt, X, Y, Z, Yaw, Pitch")
        error(" angegeben werden.")
    })
}