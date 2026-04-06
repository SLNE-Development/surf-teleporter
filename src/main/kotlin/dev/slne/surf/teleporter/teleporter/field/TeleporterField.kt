@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.teleporter.field

import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.paper.dialog.builder.DialogBaseBuilder
import io.papermc.paper.dialog.DialogResponseView
import net.kyori.adventure.text.Component.text

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