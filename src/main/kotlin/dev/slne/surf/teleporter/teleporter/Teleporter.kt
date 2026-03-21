@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.teleporter

import dev.slne.surf.teleporter.teleporter.field.TeleporterFieldParseResult
import dev.slne.surf.teleporter.teleporter.field.fields.TeleporterBoxField
import dev.slne.surf.teleporter.teleporter.field.fields.TeleporterLocationField
import dev.slne.surf.teleporter.teleporter.field.fields.TeleporterNameField
import io.papermc.paper.dialog.DialogResponseView
import org.bukkit.Location
import org.bukkit.util.BoundingBox
import java.util.*

class Teleporter(
    val uuid: UUID,
    name: String,

    originLocation: Location,
    targetLocation: Location,

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
    val originLocationString get() = originLocationField.asString()
    val targetLocation get() = targetLocationField.currentValue
    val targetLocationString get() = targetLocationField.asString()

    val width get() = boxField.width
    val length get() = boxField.length
    val height get() = boxField.height
    val boxString get() = boxField.asString()

    val boundingBox
        get() = BoundingBox(
            originLocation.blockX - width / 2.0,
            originLocation.blockY.toDouble(),
            originLocation.blockZ - length / 2.0,
            originLocation.blockX + width / 2.0,
            originLocation.blockY.toDouble() + height,
            originLocation.blockZ + length / 2.0
        )

    fun parse(response: DialogResponseView): List<TeleporterFieldParseResult> {
        val results = mutableListOf<TeleporterFieldParseResult>()

        for (field in listOf(
            teleporterNameField,
            originLocationField,
            targetLocationField,
            boxField
        )) {
            val result = field.parse(response)

            results.add(result)
        }

        return results.filterNot { it.isSuccess }
    }
}