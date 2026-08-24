package dev.slne.surf.teleporter.core.client.teleporter

import dev.slne.surf.api.core.util.mutableObjectListOf
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult
import dev.slne.surf.teleporter.core.client.teleporter.field.fields.TeleporterBoxField
import dev.slne.surf.teleporter.core.client.teleporter.field.fields.TeleporterLocationField
import dev.slne.surf.teleporter.core.client.teleporter.field.fields.TeleporterNameField
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * A teleporter placed in a world.
 *
 * Every configurable value is held by a field of its own, so that it can be shown, typed and read
 * back the same way everywhere.
 *
 * @property uuid the unique identifier of this teleporter
 */
class Teleporter(
    val uuid: UUID,
    name: String,

    originLocation: TeleporterPosition,
    targetLocation: TeleporterPosition,

    width: Double,
    length: Double,
    height: Double
) {
    val teleporterNameField = TeleporterNameField(
        initialValue = name
    )

    val originLocationField = TeleporterLocationField(
        fieldName = "teleporter_origin_location",
        fieldDisplayName = "Startort",
        initialValue = originLocation
    )

    val targetLocationField = TeleporterLocationField(
        fieldName = "teleporter_target_location",
        fieldDisplayName = "Zielort",
        initialValue = targetLocation
    )

    val boxField = TeleporterBoxField(
        width = width,
        length = length,
        height = height
    )

    val name get() = teleporterNameField.currentValue

    val originLocation get() = originLocationField.currentValue
    val targetLocation get() = targetLocationField.currentValue

    val width get() = boxField.width
    val length get() = boxField.length
    val height get() = boxField.height

    /**
     * Returns whether [position] lies inside the area this teleporter triggers in.
     *
     * @param position the position to check
     * @return `true` if the position is inside this teleporter
     */
    fun contains(position: TeleporterPosition): Boolean {
        val origin = originLocation
        if (position.worldKey != origin.worldKey) return false

        val (width, length, height) = boxField.currentValue

        val originX = origin.blockX.toDouble()
        val originY = origin.blockY.toDouble()
        val originZ = origin.blockZ.toDouble()

        val halfWidth = width / 2.0
        val halfLength = length / 2.0

        return between(position.x, originX - halfWidth, originX + halfWidth) &&
                between(position.y, originY, originY + height) &&
                between(position.z, originZ - halfLength, originZ + halfLength)
    }

    /**
     * Reads every field from the values a dialog collected, adopting the ones that could be read.
     *
     * @param values the values a dialog collected, by field name
     * @return the outcomes of the fields that could not be read
     */
    fun parse(values: (String) -> String?): ObjectList<TeleporterFieldParseResult> {
        val results = mutableObjectListOf<TeleporterFieldParseResult>()

        for (field in listOf(
            teleporterNameField,
            originLocationField,
            targetLocationField,
            boxField
        )) {
            val result = field.parse(values)

            if (!result.isSuccess) {
                results.add(result)
            }
        }

        return results
    }

    @Suppress("ConvertTwoComparisonsToRangeCheck")
    private fun between(value: Double, first: Double, second: Double): Boolean {
        val lower = min(first, second)
        val upper = max(first, second)

        return value >= lower && value < upper
    }
}
