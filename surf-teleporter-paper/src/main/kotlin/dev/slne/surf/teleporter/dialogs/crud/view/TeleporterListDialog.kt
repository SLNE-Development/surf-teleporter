@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.crud.view

import dev.slne.surf.api.core.util.toObjectSet
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterService
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.crud.TeleporterCreateDialog
import io.papermc.paper.dialog.Dialog

object TeleporterListDialog {
    fun createDialog(): Dialog {
        val teleporters = TeleporterService.teleporters

        val dialogList = buildPadDialogList(teleporters)

        if (dialogList.isEmpty()) {
            return dialog {
                base {
                    title(TeleporterDialogTexts.dialogTitle)

                    body {
                        plainMessage(
                            TeleporterDialogTexts.emptyListBody,
                            TeleporterDialogTexts.BODY_WIDTH
                        )
                    }
                }

                type {
                    confirmation(createButton(), backButton())
                }
            }
        }

        return dialog {
            base {
                title(TeleporterDialogTexts.dialogTitle)

                body {
                    plainMessage(
                        TeleporterDialogTexts.listBody(teleporters.size),
                        TeleporterDialogTexts.BODY_WIDTH
                    )
                }
            }

            type {
                dialogList {
                    addAll(dialogList)
                    buttonWidth(TeleporterDialogTexts.LIST_BUTTON_WIDTH)
                    columns(TeleporterDialogTexts.LIST_COLUMNS)
                    exitAction(backButton())
                }
            }
        }
    }

    private fun buildPadDialogList(teleporters: Collection<Teleporter>) =
        teleporters.map { TeleporterInfoDialog.createDialog(it) }.toObjectSet()

    private fun backButton() = actionButton {
        label(TeleporterButtonTexts.backLabel)
        tooltip(TeleporterButtonTexts.backToMainTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterMainDialog.createDialog())
            }
        }
    }

    private fun createButton() = actionButton {
        label(TeleporterButtonTexts.createFromListLabel)
        tooltip(TeleporterButtonTexts.createFromListTooltip)
        action {
            playerCallback {
                it.showDialog(TeleporterCreateDialog.createDialog(it))
            }
        }
    }
}
