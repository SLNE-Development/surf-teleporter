@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.crud

import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.dialogs.DIALOG_TITLE
import dev.slne.surf.teleporter.dialogs.TeleporterDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.dialogs.error.TeleporterSuccessDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.TeleporterService
import io.papermc.paper.dialog.Dialog
import net.kyori.adventure.text.format.TextDecoration

object TeleporterDeleteDialog {
    fun createDialog(teleporter: Teleporter): Dialog = dialog {
        base {
            title(DIALOG_TITLE)

            body {
                plainMessage(400) {
                    error(
                        "Du bist dabei einen Teleporter unwiderruflich zu löschen!",
                        TextDecoration.BOLD,
                        TextDecoration.UNDERLINED
                    )
                    appendNewline(2)

                    error("Bitte bestätige dein Vorhaben!")
                    appendNewline(2)

                    TeleporterDialog.run {
                        appendTeleporterInformation(teleporter)
                    }
                }

            }

            type {
                confirmation(confirmButton(teleporter), backButton(teleporter))
            }
        }
    }

    private fun backButton(teleporter: Teleporter) = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun confirmButton(teleporter: Teleporter) = actionButton {
        label { error("Löschen") }
        tooltip { info("Klicke hier, um den Teleporter zu löschen.") }
        action {
            playerCallback {
                TeleporterService.unregisterTeleporter(teleporter)

                it.showDialog(
                    TeleporterSuccessDialog.createDialog(
                        TeleporterActionType.DELETE,
                        null
                    )
                )
            }
        }
    }
}