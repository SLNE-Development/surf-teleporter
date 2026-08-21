package dev.slne.surf.teleporter.minestom.dialog

import dev.slne.surf.api.minestom.dialog.builder.DialogBaseBuilder
import dev.slne.surf.teleporter.core.client.dialog.TeleporterInputTexts
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterField
import net.kyori.adventure.text.Component

/**
 * Adds the input field this teleporter field is typed in to [baseBuilder].
 *
 * @param baseBuilder the dialog the field is shown in
 */
fun TeleporterField<*>.buildInputField(baseBuilder: DialogBaseBuilder) = baseBuilder.input {
    text(fieldName) {
        label(Component.text(fieldDisplayName))
        maxLength(TeleporterInputTexts.INPUT_MAX_LENGTH)
        width(TeleporterInputTexts.INPUT_WIDTH)

        initial(asString())
    }
}
