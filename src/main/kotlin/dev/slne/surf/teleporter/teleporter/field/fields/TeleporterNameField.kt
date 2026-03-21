package dev.slne.surf.teleporter.teleporter.field.fields

import dev.slne.surf.teleporter.teleporter.field.TeleporterField
import dev.slne.surf.teleporter.teleporter.field.TeleporterFieldParseResult

class TeleporterNameField(
    override val initialValue: String
) : TeleporterField<String> {
    override val fieldName: String = "teleporter_name"
    override val fieldDisplayName: String = "Name"

    override var currentValue: String = initialValue
        private set

    override fun parse(value: String): TeleporterFieldParseResult {
        val value = value.trim()

        if (value.isBlank()) {
            return NameBlankResult
        }

        currentValue = value

        return TeleporterFieldParseResult.Success(value)
    }

    override fun asString(): String = currentValue

    object NameBlankResult : TeleporterFieldParseResult({
        error("Der Name des Teleporters darf nicht leer sein.")
    })
}