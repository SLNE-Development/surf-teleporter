package dev.slne.surf.teleporter.minestom.dialog

import dev.slne.surf.api.minestom.dialog.base
import dev.slne.surf.api.minestom.dialog.builder.actionButton
import dev.slne.surf.api.minestom.dialog.dialog
import dev.slne.surf.api.minestom.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.minestom.dialog.crud.TeleporterCreateDialog
import dev.slne.surf.teleporter.minestom.dialog.crud.view.TeleporterListDialog
import net.minestom.server.dialog.DialogActionButton

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

    private fun createTeleporterButton(): DialogActionButton = actionButton {
        label(TeleporterButtonTexts.createTeleporterLabel)
        tooltip(TeleporterButtonTexts.createTeleporterTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterCreateDialog.createDialog(it))
            }
        }
    }

    internal fun showPadsButton(): DialogActionButton = actionButton {
        label(TeleporterButtonTexts.showTeleportersLabel)
        tooltip(TeleporterButtonTexts.showTeleportersTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterListDialog.createDialog())
            }
        }
    }

    private fun exitButton(): DialogActionButton = actionButton {
        label(TeleporterButtonTexts.closeLabel)
        tooltip(TeleporterButtonTexts.cancelTooltip)
        action {
            playerCallback {
                it.closeDialog()
            }
        }
    }
}
