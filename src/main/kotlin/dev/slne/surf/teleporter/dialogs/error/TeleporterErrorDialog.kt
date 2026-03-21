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
import dev.slne.surf.teleporter.dialogs.crud.TeleporterCreateDialog
import dev.slne.surf.teleporter.dialogs.crud.TeleporterEditDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.field.TeleporterFieldParseResult
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

object TeleporterErrorDialog {
    fun createDialog(
        type: TeleporterActionType,
        invalidFields: List<TeleporterFieldParseResult>,
        teleporter: Teleporter,
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

                    error("Es sind folgende Fehler aufgetreten:")
                    appendNewline(2)

                    appendCollectionNewLine(
                        collection = invalidFields,
                        linePrefix = Component.empty()
                    ) { field ->
                        buildText {
                            append(field)
                        }
                    }

                    appendNewline(2)
                    error("Bitte korrigiere die Eingaben und versuche es erneut.")
                }
            }
        }

        type {
            notice(backButton(type, teleporter))
        }
    }

    private fun backButton(
        type: TeleporterActionType,
        teleporter: Teleporter,
    ) = actionButton {
        label { spacer("Zurück") }
        action {
            playerCallback {
                when (type) {
                    TeleporterActionType.CREATE ->
                        it.showDialog(TeleporterCreateDialog.createDialog(it, teleporter))

                    TeleporterActionType.EDIT ->
                        it.showDialog(TeleporterEditDialog.createDialog(teleporter))

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