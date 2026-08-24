package dev.slne.surf.teleporter.core.client.teleporter.field.fields

import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterField
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult

class TeleporterBoxField(
    width: Double,
    length: Double,
    height: Double,
) : TeleporterField<TeleporterBox> {
    override val fieldName: String = "teleporter_box"
    override val fieldDisplayName: String = "Bounding Box"

    override val initialValue: TeleporterBox = TeleporterBox(width, length, height)

    @Volatile
    override var currentValue: TeleporterBox = initialValue
        private set

    val width: Double get() = currentValue.width
    val length: Double get() = currentValue.length
    val height: Double get() = currentValue.height

    override fun parse(value: String): TeleporterFieldParseResult {
        val split = value.split("x")

        if (split.size != 3) {
            return WrongSplitSizeResult
        }

        val width = split[0].toDoubleOrNull()
        val length = split[1].toDoubleOrNull()
        val height = split[2].toDoubleOrNull()

        if (width == null || length == null || height == null) {
            return ArgumentsNotParsableResult
        }

        currentValue = TeleporterBox(width, length, height)

        return TeleporterFieldParseResult.Success(currentValue)
    }

    override fun asString(): String = "${width}x${length}x${height}"

    object WrongSplitSizeResult : TeleporterFieldParseResult({
        error("Die Größe muss im Format 'Breite x Länge x Höhe' angegeben werden.")
    })

    object ArgumentsNotParsableResult : TeleporterFieldParseResult({
        error("Die angegebenen Werte konnten nicht in Zahlen umgewandelt werden.")
    })
}

data class TeleporterBox(
    val width: Double,
    val length: Double,
    val height: Double,
)
