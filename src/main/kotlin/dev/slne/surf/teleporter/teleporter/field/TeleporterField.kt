@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.teleporter.field

import dev.slne.surf.surfapi.bukkit.api.dialog.builder.DialogBaseBuilder
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import io.papermc.paper.dialog.DialogResponseView

interface TeleporterField<T> {
    val fieldName: String
    val fieldDisplayName: String

    val initialValue: T
    val currentValue: T

    fun buildInputField(baseBuilder: DialogBaseBuilder) = baseBuilder.input {
        text(fieldName) {
            label(text(fieldDisplayName))
            maxLength(Int.MAX_VALUE)
            width(400)

            initial(asString())
        }
    }

    fun appendInfo(builder: SurfComponentBuilder) = builder.append {
        variableKey("$fieldDisplayName:")
        appendSpace()
        variableValue(asString())
    }

    fun parse(value: String): TeleporterFieldParseResult
    fun parse(response: DialogResponseView) = parse(response.getText(fieldName) ?: "")

    fun asString(): String
}