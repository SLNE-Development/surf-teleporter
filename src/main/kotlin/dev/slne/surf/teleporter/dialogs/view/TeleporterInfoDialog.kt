@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.view

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.delete.TeleporterDeleteDialog
import dev.slne.surf.teleporter.dialogs.edit.TeleporterEditDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import io.papermc.paper.dialog.Dialog

object TeleporterInfoDialog {
    fun createDialog(teleporter: Teleporter): Dialog = dialog {
        base {
            title {
                variableValue("${teleporter.originLocation.blockX} ${teleporter.originLocation.blockY} ${teleporter.originLocation.blockZ}")
            }
            body {
                plainMessage(300) {
                    info("Im Folgenden siehst du alle aktuellen Werte des Teleporters.")
                    appendNewline(2)

                    primary("UUID: ")
                    variableValue(teleporter.uuid.toString())
                    appendNewline(2)

                    spacer("- ")
                    primary("Position: ")
                    variableValue("${teleporter.originLocation.blockX} ${teleporter.originLocation.blockY} ${teleporter.originLocation.blockZ}")
                    primary(" in Welt ")
                    variableValue(teleporter.originLocation.world?.name ?: "Unbekannt")
                    appendNewline(2)

                    spacer("- ")
                    primary("TargetPosition: ")
                    variableValue("${teleporter.targetLocation.blockX} ${teleporter.targetLocation.blockY} ${teleporter.targetLocation.blockZ}")
                    primary(" in Welt ")
                    variableValue(teleporter.targetLocation.world?.name ?: "Unbekannt")
                    appendNewline(2)

                    spacer("- ")
                    primary("Box: ")
                    variableValue("${teleporter.width}x${teleporter.length}")
                    appendNewline(2)
                }
            }
        }
        type {
            multiAction {
                action(teleportButton(teleporter))
                action(editButton(teleporter))
                action(deleteButton(teleporter))

                columns(1)
                exitAction(backButton())
            }
        }
    }

    private fun backButton() = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterListDialog.createDialog())
            }
        }
    }

    private fun deleteButton(teleporter: Teleporter) = actionButton {
        label { error("Löschen") }
        tooltip { info("Klicke hier, um den Teleporter zu löschen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterDeleteDialog.createDialog(teleporter))
            }
        }
    }

    internal fun teleportButton(teleporter: Teleporter) = actionButton {
        label { primary("Teleportieren") }
        tooltip { info("Klicke hier, um dich zum Teleporter zu teleportieren.") }
        action {
            playerCallback {
                it.teleportAsync(teleporter.originLocation)
                it.closeDialog()
            }
        }
    }

    private fun editButton(teleporter: Teleporter) = actionButton {
        label { primary("Konfigurieren") }
        tooltip { info("Klicke hier, um die Einstellungen des Teleporters zu konfigurieren.") }
        action {
            playerCallback {
                it.showDialog(TeleporterEditDialog.createDialog(teleporter))
            }
        }
    }
}