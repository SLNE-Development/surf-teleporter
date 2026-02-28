@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.view

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.create.CreateTeleporterDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.TeleporterService
import io.papermc.paper.dialog.Dialog

object TeleporterListDialog {
    fun createDialog(): Dialog {
        val teleporters = TeleporterService.teleporters

        val dialogList = buildPadDialogList(teleporters)
        if (dialogList.isEmpty()) {
            return dialog {
                base {
                    title {
                        primary("TELEPORTER LISTE".toSmallCaps())
                    }
                    body {
                        plainMessage(300) {
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
                title {
                    primary("TELEPORTER LISTE".toSmallCaps())
                }

                body {
                    plainMessage(400) {
                        info("Aktuell existieren ")
                        variableValue(teleporters.size)
                        info(" Teleporter.")
                        appendNewline(2)
                    }
                }
            }

            type {
                dialogList {
                    addAll(dialogList)
                    buttonWidth(200)
                    columns(3)
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
