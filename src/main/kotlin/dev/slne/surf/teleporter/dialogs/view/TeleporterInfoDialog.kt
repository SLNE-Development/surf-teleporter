@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.view

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.clickCallback
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.teleporter.appendBullet
import dev.slne.surf.teleporter.dialogs.DIALOG_TITLE
import dev.slne.surf.teleporter.dialogs.delete.TeleporterDeleteDialog
import dev.slne.surf.teleporter.dialogs.edit.TeleporterEditDialog
import dev.slne.surf.teleporter.formatToCoordString
import dev.slne.surf.teleporter.teleporter.Teleporter
import io.papermc.paper.dialog.Dialog
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

object TeleporterInfoDialog {
    fun createDialog(teleporter: Teleporter): Dialog = dialog {
        base {
            title {
                append(DIALOG_TITLE)
                appendSpace()
                variableValue(teleporter.originLocation.formatToCoordString())
            }
            body {
                plainMessage(400) {
                    primary("Du siehst dir gerade einen Teleporter an.", TextDecoration.BOLD, TextDecoration.UNDERLINED)
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
            playerCallback { player ->
                player.teleportAsync(teleporter.originLocation)
                player.sendText {
                    appendSuccessPrefix()
                    success("Du wurdest zum Teleporter mit der UUID")
                    appendSpace()
                    variableValue(teleporter.uuid.toString())
                    appendSpace()
                    success("teleportiert!")
                    hoverEvent(buildText {
                        error("Klicke, um dir den Teleporter anzusehen.")
                    })
                    clickCallback {
                        val clickPlayer = it as? Player ?: return@clickCallback
                        clickPlayer.showDialog(createDialog(teleporter))
                    }
                }
                player.closeDialog()
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