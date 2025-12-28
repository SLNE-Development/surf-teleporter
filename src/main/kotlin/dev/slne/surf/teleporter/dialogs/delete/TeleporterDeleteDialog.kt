@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.delete

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterListDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.teleporterService
import io.papermc.paper.dialog.Dialog
import net.kyori.adventure.text.format.TextDecoration

object TeleporterDeleteDialog {
    fun showDialog(teleporter: Teleporter): Dialog = dialog {
        base {
            title {
                primary("TELEPORTER ".toSmallCaps())
                primary("LISTE ".toSmallCaps())
                variableValue("${teleporter.originLocation.blockX} ${teleporter.originLocation.blockY} ${teleporter.originLocation.blockZ} ")
                error("LÖSCHEN".toSmallCaps())

                body {
                    plainMessage(300) {
                        error("Achtung!", TextDecoration.BOLD)
                        appendNewline(2)

                        error("Du bist dabei einen Teleporter unwiderruflich zu löschen!")
                        appendNewline(2)

                        error("Bitte bestätige dein Vorhaben!")
                        appendNewline(2)

                        info("Im Folgenden siehst du die aktuellen Werte des Teleporters.")
                        appendNewline(2)

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
                confirmation(confirmButton(teleporter), backButton(teleporter))
            }
        }
    }

    private fun backButton(teleporter: Teleporter) = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.showDialog(teleporter))
            }
        }
    }

    private fun confirmButton(teleporter: Teleporter) = actionButton {
        label { error("Löschen") }
        tooltip { info("Klicke hier, um den Teleporter zu löschen.") }
        action {
            playerCallback {
                teleporterService.removePadVisualization(teleporter)
                teleporterService.deleteTeleporter(teleporter)
                it.showDialog(TeleporterListDialog.showDialog())
            }
        }
    }
}