@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.crud.view

import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.clickCallback
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.dialogs.DIALOG_TITLE
import dev.slne.surf.teleporter.dialogs.TeleporterDialog
import dev.slne.surf.teleporter.dialogs.crud.TeleporterDeleteDialog
import dev.slne.surf.teleporter.dialogs.crud.TeleporterEditDialog
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
                variableValue(teleporter.name)
            }
            body {
                plainMessage(400) {
                    primary(
                        "Du siehst dir gerade einen Teleporter an.",
                        TextDecoration.BOLD,
                        TextDecoration.UNDERLINED
                    )
                    appendNewline(2)
                    
                    TeleporterDialog.run {
                        appendTeleporterInformation(teleporter)
                    }
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
                    success("Du wurdest zum Teleporter ")
                    appendSpace()
                    variableValue(teleporter.name)
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