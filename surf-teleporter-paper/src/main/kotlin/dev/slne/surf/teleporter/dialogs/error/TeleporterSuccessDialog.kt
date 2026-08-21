@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.error

import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterListDialog
import io.papermc.paper.registry.data.dialog.ActionButton

object TeleporterSuccessDialog {
    fun createDialog(type: TeleporterActionType, teleporter: Teleporter?) = dialog {
        base {
            title(TeleporterDialogTexts.dialogTitle)

            body {
                plainMessage(
                    TeleporterDialogTexts.successBody(type, teleporter),
                    TeleporterDialogTexts.BODY_WIDTH
                )
            }
        }

        type {
            when (type) {
                TeleporterActionType.CREATE -> {
                    confirmation(viewTeleporterButton(teleporter!!), mainMenuButton())
                }

                TeleporterActionType.EDIT -> {
                    notice(backToInfoButton(teleporter!!))
                }

                TeleporterActionType.DELETE -> {
                    notice(backToListButton())
                }
            }
        }
    }

    private fun viewTeleporterButton(teleporter: Teleporter): ActionButton = actionButton {
        label(TeleporterButtonTexts.viewTeleporterLabel)
        tooltip(TeleporterButtonTexts.viewTeleporterTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun mainMenuButton(): ActionButton = actionButton {
        label(TeleporterButtonTexts.backLabel)
        tooltip(TeleporterButtonTexts.mainMenuTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterMainDialog.createDialog())
            }
        }
    }

    private fun backToInfoButton(teleporter: Teleporter): ActionButton = actionButton {
        label(TeleporterButtonTexts.backLabel)
        tooltip(TeleporterButtonTexts.backToInfoTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun backToListButton(): ActionButton = actionButton {
        label(TeleporterButtonTexts.backLabel)
        tooltip(TeleporterButtonTexts.backToListTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterListDialog.createDialog())
            }
        }
    }
}
