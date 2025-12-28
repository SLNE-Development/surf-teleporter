@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.create.CreateTeleporterDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterListDialog
import dev.slne.surf.teleporter.teleporter.teleporterService
import io.papermc.paper.registry.data.dialog.ActionButton

object TeleporterMainDialog {
    fun showDialog() = dialog {
        base {
            val teleporters = teleporterService.getteleporters()
            title { primary("TELEPORTER".toSmallCaps()) }

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
            multiAction {
                action(createTeleporterButton())
                action(showPadsButton())
                exitAction(exitButton())
            }
        }
    }

    private fun createTeleporterButton(): ActionButton = actionButton {
        label { success("Teleporter erstellen") }
        tooltip {
            info("Klicke hier, einen Teleporter zu erstellen.")
        }
        action {
            playerCallback {
                it.showDialog(CreateTeleporterDialog.showDialog(it))
            }
        }
    }

    internal fun showPadsButton(): ActionButton = actionButton {
        label { primary("Teleporter ansehen") }
        tooltip {
            info("Klicke hier, die existierenden Teleporter anzusehen.")
        }
        action {
            playerCallback {
                it.showDialog(TeleporterListDialog.showDialog())
            }
        }
    }

    private fun exitButton(): ActionButton = actionButton {
        label { spacer("Schließen") }
        tooltip {
            info("Klicke hier, um den Vorgang abzubrechen.")
        }
        action {
            playerCallback {
                it.closeDialog()
            }
        }
    }
}
