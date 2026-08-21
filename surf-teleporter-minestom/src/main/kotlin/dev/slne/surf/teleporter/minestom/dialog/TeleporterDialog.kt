package dev.slne.surf.teleporter.minestom.dialog

import dev.slne.surf.api.minestom.dialog.base
import dev.slne.surf.api.minestom.dialog.builder.DialogTypeBuilder
import dev.slne.surf.api.minestom.dialog.callback.DialogResponseView
import dev.slne.surf.api.minestom.dialog.dialog
import dev.slne.surf.api.minestom.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.platform.TeleporterPlatform
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterConfirmations
import dev.slne.surf.teleporter.minestom.dialog.error.TeleporterErrorDialog
import dev.slne.surf.teleporter.minestom.dialog.error.TeleporterSuccessDialog
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player

object TeleporterDialog {
    fun create(
        teleporter: Teleporter,
        dialogHeader: Component,
        confirmationBuilder: DialogTypeBuilder.DialogConfirmationTypeBuilder.(Teleporter) -> Unit
    ) = dialog {
        base {
            title(TeleporterDialogTexts.dialogTitle)

            body {
                plainMessage(
                    TeleporterDialogTexts.configureBody(
                        dialogHeader,
                        TeleporterPlatform.worldNames(),
                        teleporter
                    ),
                    TeleporterDialogTexts.BODY_WIDTH
                )
            }

            teleporter.teleporterNameField.buildInputField(this)
            teleporter.originLocationField.buildInputField(this)
            teleporter.targetLocationField.buildInputField(this)
            teleporter.boxField.buildInputField(this)
        }

        type {
            confirmation {
                confirmationBuilder(teleporter)
            }
        }
    }

    fun handleConfirmation(
        player: Player,
        content: DialogResponseView,
        actionType: TeleporterActionType,
        teleporter: Teleporter
    ) {
        val results = TeleporterConfirmations.confirm(actionType, teleporter) {
            content.getText(it)
        }

        if (results.isNotEmpty()) {
            player.showDialog(
                TeleporterErrorDialog.createDialog(
                    type = actionType,
                    invalidFields = results,
                    teleporter = teleporter
                )
            )

            return
        }

        player.showDialog(
            TeleporterSuccessDialog.createDialog(
                actionType,
                teleporter
            )
        )
    }
}
