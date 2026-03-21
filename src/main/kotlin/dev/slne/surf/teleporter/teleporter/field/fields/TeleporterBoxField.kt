package dev.slne.surf.teleporter.teleporter.field.fields

import dev.slne.surf.teleporter.teleporter.field.TeleporterField
import dev.slne.surf.teleporter.teleporter.field.TeleporterFieldParseResult

/**
 * @param initialValue A triple of width, length and height of the teleporter box.
 */
class TeleporterBoxField(
    width: Double,
    length: Double,
    height: Double,
) : TeleporterField<Triple<Double, Double, Double>> {
    override val fieldName: String = "teleporter_box"
    override val fieldDisplayName: String = "Bounding Box"

    override val initialValue: Triple<Double, Double, Double> = Triple(width, length, height)
    override var currentValue: Triple<Double, Double, Double> = initialValue
        private set

    val width: Double get() = currentValue.first
    val length: Double get() = currentValue.second
    val height: Double get() = currentValue.third

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

        currentValue = Triple(width, length, height)

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