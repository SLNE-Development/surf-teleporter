@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.error

import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.message.TeleporterMessages
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult
import dev.slne.surf.teleporter.dialogs.crud.TeleporterCreateDialog
import dev.slne.surf.teleporter.dialogs.crud.TeleporterEditDialog

object TeleporterErrorDialog {
    fun createDialog(
        type: TeleporterActionType,
        invalidFields: Collection<TeleporterFieldParseResult>,
        teleporter: Teleporter,
    ) = dialog {
        base {
            title(TeleporterDialogTexts.dialogTitle)

            body {
                plainMessage(
                    TeleporterDialogTexts.errorBody(invalidFields),
                    TeleporterDialogTexts.BODY_WIDTH
                )
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
        label(TeleporterButtonTexts.backLabel)
        action {
            playerCallback {
                when (type) {
                    TeleporterActionType.CREATE ->
                        it.showDialog(TeleporterCreateDialog.createDialog(it, teleporter))

                    TeleporterActionType.EDIT ->
                        it.showDialog(TeleporterEditDialog.createDialog(teleporter))

                    else -> {
                        it.closeDialog()
                        it.sendMessage(TeleporterMessages.genericError)
                    }
                }
            }
        }
    }
}
