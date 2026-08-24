package dev.slne.surf.teleporter.core.client.teleporter.field.fields

import dev.slne.surf.teleporter.core.client.platform.TeleporterPlatform
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterPosition
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterField
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterInputs

class TeleporterLocationField(
    override val fieldName: String,
    override val fieldDisplayName: String,
    override val initialValue: TeleporterPosition
) : TeleporterField<TeleporterPosition> {

    @Volatile
    override var currentValue: TeleporterPosition = initialValue
        private set

    override fun parse(value: String): TeleporterFieldParseResult {
        val location = TeleporterInputs.parseLocation(value) ?: return WrongFormatResult(fieldName)

        val worldKey = TeleporterPlatform.findWorldKey(location.worldName)
            ?: return WorldNotFoundResult(location.worldName)

        currentValue = TeleporterPosition(
            worldKey,
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )

        return TeleporterFieldParseResult.Success(currentValue)
    }

    override fun asString(): String {
        val world = TeleporterPlatform.worldName(currentValue.worldKey)
            ?: currentValue.worldKey.value()
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
