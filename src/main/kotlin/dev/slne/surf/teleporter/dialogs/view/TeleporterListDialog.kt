@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.view

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import dev.slne.surf.teleporter.dialogs.DIALOG_TITLE
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.create.CreateTeleporterDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.TeleporterService
import io.papermc.paper.dialog.Dialog
import net.kyori.adventure.text.format.TextDecoration

object TeleporterListDialog {
    fun createDialog(): Dialog {
        val teleporters = TeleporterService.teleporters

        val dialogList = buildPadDialogList(teleporters)
        if (dialogList.isEmpty()) {
            return dialog {
                base {
                    title(DIALOG_TITLE)
                    body {
                        plainMessage(400) {
                            primary(
                                "Du befindest dich in der Teleporter-Übersicht.",
                                TextDecoration.BOLD,
                                TextDecoration.UNDERLINED
                            )
                            appendNewline(2)

                            error("Es existieren aktuell keine Teleporter.")
                        }
                    }
                }
                type {
                    confirmation(createButton(), backButton())
                }
            }
        }

        return dialog {
            base {
                title(DIALOG_TITLE)
                body {
                    plainMessage(400) {
                        primary(
                            "Du befindest dich in der Teleporter-Übersicht.",
                            TextDecoration.BOLD,
                            TextDecoration.UNDERLINED
                        )
                        appendNewline(2)

                        info("Aktuell existieren ")
                        variableValue(teleporters.size)
                        info(" Teleporter.")
                    }
                }
            }

            type {
                dialogList {
                    addAll(dialogList)
                    buttonWidth(400)
                    columns(1)
                    exitAction(backButton())
                }
            }
        }
    }

    private fun buildPadDialogList(teleporters: Collection<Teleporter>) =
        teleporters.map { TeleporterInfoDialog.createDialog(it) }.toObjectSet()

    private fun backButton() = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke hier, um zurück zum Hauptmenü zu gelangen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterMainDialog.createDialog())
            }
        }
    }

    private fun createButton() = actionButton {
        label { success("Teleporter erstellen") }
        tooltip { info("Klicke hier, um einen Teleporter zu erstellen.") }
        action {
            playerCallback {
                it.showDialog(CreateTeleporterDialog.createDialog(it))
            }
        }
    }
}
