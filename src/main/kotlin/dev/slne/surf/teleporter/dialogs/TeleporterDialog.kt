package dev.slne.surf.teleporter.dialogs

import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.DialogTypeBuilder
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.api.paper.extensions.server
import dev.slne.surf.teleporter.appendBullet
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.dialogs.error.TeleporterErrorDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterSuccessDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.TeleporterService
import io.papermc.paper.dialog.DialogResponseView
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
object TeleporterDialog {
    fun create(
        teleporter: Teleporter,
        dialogHeader: SurfComponentBuilder.() -> Unit,
        confirmationBuilder: DialogTypeBuilder.DialogConfirmationTypeBuilder.(Teleporter) -> Unit
    ) = dialog {
        base {
            title(DIALOG_TITLE)

            body {
                plainMessage(400) {
                    dialogHeader()
                    appendNewline(2)

                    info("Folgende Welten können zur Konfiguration verwendet werden:")
                    appendNewline()
                    server.worlds.forEachIndexed { index, world ->
                        appendBullet()
                        variableValue(world.name)

                        if (index < server.worlds.size - 1) {
                            appendNewline()
                        }
                    }

                    appendNewline(2)
                    appendTeleporterInformation(teleporter)
                }
            }

            run {
                teleporter.teleporterNameField.buildInputField(this)
                teleporter.originLocationField.buildInputField(this)
                teleporter.targetLocationField.buildInputField(this)
                teleporter.boxField.buildInputField(this)
            }
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
        val results = teleporter.parse(content)

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

        if (actionType == TeleporterActionType.CREATE) {
            TeleporterService.registerTeleporter(teleporter)
        } else if (actionType == TeleporterActionType.EDIT) {
            TeleporterService.saveTeleporters()
        }

        player.showDialog(
            TeleporterSuccessDialog.createDialog(
                actionType,
                teleporter
            )
        )
    }

    fun SurfComponentBuilder.appendTeleporterInformation(teleporter: Teleporter) {
        info("Im Folgenden siehst du die aktuellen Werte des Teleporters:")
        appendNewline(2)

        appendBullet()
        teleporter.teleporterNameField.appendInfo(this)
        appendNewline(2)

        appendBullet()
        primary("UUID:")
        appendSpace()
        variableValue(teleporter.uuid.toString())
        appendNewline(2)

        appendBullet()
        teleporter.originLocationField.appendInfo(this)
        appendNewline(2)

        appendBullet()
        teleporter.targetLocationField.appendInfo(this)
        appendNewline(2)

        appendBullet()
        teleporter.boxField.appendInfo(this)
    }
}