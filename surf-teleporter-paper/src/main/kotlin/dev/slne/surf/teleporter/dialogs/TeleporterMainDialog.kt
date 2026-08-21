@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs

import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.dialogs.crud.TeleporterCreateDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterListDialog
import io.papermc.paper.registry.data.dialog.ActionButton

object TeleporterMainDialog {
    fun createDialog() = dialog {
        base {
            title(TeleporterDialogTexts.dialogTitle)

            body {
                plainMessage(
                    TeleporterDialogTexts.mainBody(TeleporterService.teleporterCount),
                    TeleporterDialogTexts.BODY_WIDTH
                )
            }
        }

        type {
            multiAction {
                action(createTeleporterButton())
                action(showPadsButton())
                exitAction(exitButton())
            }
        }
    }

    private fun createTeleporterButton(): ActionButton = actionButton {
        label(TeleporterButtonTexts.createTeleporterLabel)
        tooltip(TeleporterButtonTexts.createTeleporterTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterCreateDialog.createDialog(it))
            }
        }
    }

    internal fun showPadsButton(): ActionButton = actionButton {
        label(TeleporterButtonTexts.showTeleportersLabel)
        tooltip(TeleporterButtonTexts.showTeleportersTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterListDialog.createDialog())
            }
        }
    }

    private fun exitButton(): ActionButton = actionButton {
        label(TeleporterButtonTexts.closeLabel)
        tooltip(TeleporterButtonTexts.cancelTooltip)
        action {
            playerCallback {
                it.closeDialog()
            }
        }
    }
}
