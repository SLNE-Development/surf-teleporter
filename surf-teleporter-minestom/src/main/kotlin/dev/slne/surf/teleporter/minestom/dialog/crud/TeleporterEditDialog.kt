package dev.slne.surf.teleporter.minestom.dialog.crud

import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.minestom.dialog.TeleporterDialog
import dev.slne.surf.teleporter.minestom.dialog.crud.view.TeleporterInfoDialog

object TeleporterEditDialog {
    fun createDialog(
        teleporter: Teleporter
    ) = TeleporterDialog.create(
        teleporter = teleporter,
        dialogHeader = TeleporterDialogTexts.editHeader
    ) {
        yes {
            label(TeleporterButtonTexts.saveLabel)
            tooltip(TeleporterButtonTexts.saveTooltip)
            action {
                customPlayerClick { content, player ->
                    TeleporterDialog.handleConfirmation(
                        player = player,
                        content = content,
                        actionType = TeleporterActionType.EDIT,
                        teleporter = teleporter
                    )
                }
            }
        }

        no {
            label(TeleporterButtonTexts.backLabel)
            tooltip(TeleporterButtonTexts.cancelTooltip)
            action {
                playerCallback {
                    it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
                }
            }
        }
    }
}
