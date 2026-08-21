package dev.slne.surf.teleporter.dialogs

import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.DialogTypeBuilder
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.platform.TeleporterPlatform
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterConfirmations
import dev.slne.surf.teleporter.dialogs.error.TeleporterErrorDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterSuccessDialog
import io.papermc.paper.dialog.DialogResponseView
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
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
