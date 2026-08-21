package dev.slne.surf.teleporter.minestom.dialog.crud

import dev.slne.surf.api.minestom.dialog.base
import dev.slne.surf.api.minestom.dialog.builder.actionButton
import dev.slne.surf.api.minestom.dialog.dialog
import dev.slne.surf.api.minestom.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.minestom.dialog.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.minestom.dialog.error.TeleporterSuccessDialog
import net.minestom.server.dialog.Dialog

object TeleporterDeleteDialog {
    fun createDialog(teleporter: Teleporter): Dialog = dialog {
        base {
            title(TeleporterDialogTexts.dialogTitle)

            body {
                plainMessage(
                    TeleporterDialogTexts.deleteBody(teleporter),
                    TeleporterDialogTexts.BODY_WIDTH
                )
            }
        }

        type {
            confirmation(confirmButton(teleporter), backButton(teleporter))
        }
    }

    private fun backButton(teleporter: Teleporter) = actionButton {
        label(TeleporterButtonTexts.backLabel)
        tooltip(TeleporterButtonTexts.cancelTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun confirmButton(teleporter: Teleporter) = actionButton {
        label(TeleporterButtonTexts.deleteLabel)
        tooltip(TeleporterButtonTexts.deleteTooltip)
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
