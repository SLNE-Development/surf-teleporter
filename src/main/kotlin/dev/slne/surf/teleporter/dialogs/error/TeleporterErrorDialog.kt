@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.error

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.teleporter.dialogs.DIALOG_TITLE
import dev.slne.surf.teleporter.dialogs.create.TeleporterCreateDialog
import dev.slne.surf.teleporter.dialogs.edit.TeleporterEditDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

enum class InvalidField(val message: String) {
    NAME_EMPTY("Der Name des Teleporters darf nicht leer sein."),
    START_LOCATION("Die Startposition wurde nicht korrekt angegeben."),
    TARGET_LOCATION("Die Zielposition wurde nicht korrekt angegeben."),
    BOX_SIZE("Die angegebene Box überschreitet die maximale Größe von 10x10."),
    BOX_INVALID("Das Format der Box ist ungültig. Verwende z. B. 3x3."),
    ORIGIN_WORLD("Die angegebene Startwelt existiert nicht."),
    TARGET_WORLD("Die angegebene Zielwelt existiert nicht.")
}

object TeleporterErrorDialog {
    fun createDialog(
        type: TeleporterActionType,
        invalidFields: List<InvalidField>,
        previousValues: Map<String, String>,
        teleporter: Teleporter? = null
    ) = dialog {
        base {
            title(DIALOG_TITLE)

            body {
                plainMessage(400) {
                    error(
                        "Es ist ein Fehler aufgetreten!",
                        TextDecoration.BOLD,
                        TextDecoration.UNDERLINED
                    )
                    appendNewline(2)

                    error("Die folgenden Felder wurden nicht korrekt ausgefüllt:")
                    appendNewline(2)

                    appendCollectionNewLine(
                        collection = invalidFields,
                        linePrefix = Component.empty()
                    ) { field ->
                        buildText {
                            variableValue(field.message)
                        }
                    }

                    appendNewline(2)
                    error("Bitte korrigiere die Eingaben und versuche es erneut.")
                }
            }
        }

        type {
            notice(backButton(type, teleporter, previousValues))
        }
    }

    private fun backButton(
        type: TeleporterActionType,
        teleporter: Teleporter?,
        previousValues: Map<String, String>
    ) = actionButton {
        label { spacer("Zurück") }
        action {
            playerCallback {
                when (type) {
                    TeleporterActionType.CREATE ->
                        it.showDialog(TeleporterCreateDialog.createDialog(it, previousValues))

                    TeleporterActionType.EDIT ->
                        it.showDialog(TeleporterEditDialog.createDialog(teleporter!!))

                    else -> {
                        it.closeDialog()
                        it.sendText {
                            appendErrorPrefix()
                            error("Es ist ein Fehler aufgetreten!")
                        }
                    }
                }
            }
        }
    }
}