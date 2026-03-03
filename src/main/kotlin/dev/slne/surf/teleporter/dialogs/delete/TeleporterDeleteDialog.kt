@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.delete

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.appendBullet
import dev.slne.surf.teleporter.dialogs.DIALOG_TITLE
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.dialogs.error.TeleporterSuccessDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.formatToCoordString
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.TeleporterService
import io.papermc.paper.dialog.Dialog
import net.kyori.adventure.text.format.TextDecoration

object TeleporterDeleteDialog {
    fun createDialog(teleporter: Teleporter): Dialog = dialog {
        base {
            title(DIALOG_TITLE)

            body {
                plainMessage(400) {
                    error(
                        "Du bist dabei einen Teleporter unwiderruflich zu löschen!",
                        TextDecoration.BOLD,
                        TextDecoration.UNDERLINED
                    )
                    appendNewline(2)

                    error("Bitte bestätige dein Vorhaben!")
                    appendNewline(2)

                    info("Im Folgenden siehst du die aktuellen Werte des Teleporters:")
                    appendNewline(2)

                    appendBullet()
                    primary("UUID:")
                    appendSpace()
                    variableValue(teleporter.uuid.toString())
                    appendNewline(2)

                    appendBullet()
                    primary("Startposition:")
                    appendSpace()
                    variableValue(teleporter.originLocation.formatToCoordString())
                    appendNewline(2)

                    appendBullet()
                    primary("Startwelt:")
                    appendSpace()
                    variableValue(teleporter.originLocation.world?.name ?: "Unbekannt")
                    appendNewline(2)

                    appendBullet()
                    primary("Zielposition:")
                    appendSpace()
                    variableValue(teleporter.targetLocation.formatToCoordString())
                    appendNewline(2)

                    appendBullet()
                    primary("Zielwelt:")
                    appendSpace()
                    variableValue(teleporter.targetLocation.world?.name ?: "Unbekannt")
                    appendNewline(2)

                    appendBullet()
                    primary("Box:")
                    appendSpace()
                    variableValue("${teleporter.width}x${teleporter.length}")
                }

            }
            type {
                confirmation(confirmButton(teleporter), backButton(teleporter))
            }
        }
    }

    private fun backButton(teleporter: Teleporter) = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun confirmButton(teleporter: Teleporter) = actionButton {
        label { error("Löschen") }
        tooltip { info("Klicke hier, um den Teleporter zu löschen.") }
        action {
            playerCallback {
                TeleporterService.unregisterTeleporter(teleporter)

                it.showDialog(TeleporterSuccessDialog.createDialog(TeleporterActionType.DELETE, null))
            }
        }
    }
}