@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.error

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.appendBullet
import dev.slne.surf.teleporter.dialogs.DIALOG_TITLE
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterListDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import io.papermc.paper.registry.data.dialog.ActionButton
import net.kyori.adventure.text.format.TextDecoration

object TeleporterSuccessDialog {
    fun createDialog(type: TeleporterActionType, teleporter: Teleporter?) = dialog {
        base {
            title(DIALOG_TITLE)

            body {
                plainMessage(400) {
                    success(
                        "Der Vorgang wurde erfolgreich abgeschlossen!",
                        TextDecoration.BOLD,
                        TextDecoration.UNDERLINED
                    )
                    appendNewline(2)

                    success(
                        when (type) {
                            TeleporterActionType.CREATE -> "Der Teleporter wurde erfolgreich erstellt!"
                            TeleporterActionType.EDIT -> "Die Änderungen wurden erfolgreich gespeichert!"
                            TeleporterActionType.DELETE -> "Der ausgewählte Teleporter wurde erfolgreich gelöscht!"
                        }
                    )
                    appendNewline(2)

                    if (type != TeleporterActionType.DELETE && teleporter != null) {
                        appendBullet()
                        primary("UUID: ")
                        variableValue(teleporter.uuid.toString())
                    }
                }
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
        label { spacer("Teleporter ansehen") }
        tooltip { info("Klicke, um Details des neuen Teleporters zu sehen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun mainMenuButton(): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }
        action {
            playerCallback {
                it.showDialog(TeleporterMainDialog.createDialog())
            }
        }
    }

    private fun backToInfoButton(teleporter: Teleporter): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Zurück zur Teleporter-Ansicht.") }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun backToListButton(): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Zurück zur Teleporter-Übersicht.") }
        action {
            playerCallback {
                it.showDialog(TeleporterListDialog.createDialog())
            }
        }
    }
}