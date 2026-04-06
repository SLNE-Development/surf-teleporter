@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.builder.actionButton
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.teleporter.dialogs.crud.TeleporterCreateDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterListDialog
import dev.slne.surf.teleporter.teleporter.TeleporterService
import io.papermc.paper.registry.data.dialog.ActionButton
import net.kyori.adventure.text.format.TextDecoration

val DIALOG_TITLE = buildText { primary("TELEPORTER".toSmallCaps()) }

object TeleporterMainDialog {
    fun createDialog() = dialog {
        base {
            val teleporters = TeleporterService.teleporterCount
            title(DIALOG_TITLE)

            body {
                plainMessage(400) {
                    primary("Hauptmenü", TextDecoration.BOLD, TextDecoration.UNDERLINED)
                    appendNewline(2)

                    info("Aktuell existieren ")
                    variableValue(teleporters)
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
                it.showDialog(TeleporterCreateDialog.createDialog(it))
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
                it.showDialog(TeleporterListDialog.createDialog())
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
