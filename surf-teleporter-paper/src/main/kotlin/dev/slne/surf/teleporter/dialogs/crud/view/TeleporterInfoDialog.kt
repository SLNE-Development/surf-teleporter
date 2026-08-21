@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.crud.view

import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.message.TeleporterMessages
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.dialogs.crud.TeleporterDeleteDialog
import dev.slne.surf.teleporter.dialogs.crud.TeleporterEditDialog
import dev.slne.surf.teleporter.teleporter.toLocation
import io.papermc.paper.dialog.Dialog

object TeleporterInfoDialog {
    fun createDialog(teleporter: Teleporter): Dialog = dialog {
        base {
            title(TeleporterDialogTexts.infoTitle(teleporter))

            body {
                plainMessage(
                    TeleporterDialogTexts.infoBody(teleporter),
                    TeleporterDialogTexts.BODY_WIDTH
                )
            }
        }
        type {
            multiAction {
                action(teleportButton(teleporter))
                action(editButton(teleporter))
                action(deleteButton(teleporter))

                columns(TeleporterDialogTexts.INFO_COLUMNS)
                exitAction(backButton())
            }
        }
    }

    private fun backButton() = actionButton {
        label(TeleporterButtonTexts.backLabel)
        tooltip(TeleporterButtonTexts.cancelTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterListDialog.createDialog())
            }
        }
    }

    private fun deleteButton(teleporter: Teleporter) = actionButton {
        label(TeleporterButtonTexts.deleteLabel)
        tooltip(TeleporterButtonTexts.deleteTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterDeleteDialog.createDialog(teleporter))
            }
        }
    }

    internal fun teleportButton(teleporter: Teleporter) = actionButton {
        label(TeleporterButtonTexts.teleportLabel)
        tooltip(TeleporterButtonTexts.teleportTooltip)
        action {
            playerCallback { player ->
                val origin = teleporter.originLocation.toLocation() ?: return@playerCallback

                player.teleportAsync(origin)
                player.sendMessage(
                    TeleporterMessages.teleported(teleporter.name) { createDialog(teleporter) }
                )
                player.closeDialog()
            }
        }
    }

    private fun editButton(teleporter: Teleporter) = actionButton {
        label(TeleporterButtonTexts.editLabel)
        tooltip(TeleporterButtonTexts.editTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterEditDialog.createDialog(teleporter))
            }
        }
    }
}
