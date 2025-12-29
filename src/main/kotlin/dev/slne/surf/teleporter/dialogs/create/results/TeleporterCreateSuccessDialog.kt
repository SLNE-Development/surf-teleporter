@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.create.results

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import io.papermc.paper.registry.data.dialog.ActionButton
import net.kyori.adventure.text.format.TextDecoration

object TeleportCreateSuccessDialog {
    fun showDialog(teleporter: Teleporter) = dialog {
        base {
            title {
                primary("Teleporter ".toSmallCaps())
                success("ERSTELLEN ".toSmallCaps())
                success("ERFOLG".toSmallCaps())
            }

            body {
                plainMessage(400) {
                    success("Erfolg!", TextDecoration.BOLD)
                    appendNewline(2)

                    success("Der teleporter wurde erfolgreich erstellt!")
                }
            }
        }

        type {
            confirmation(backButton(), teleportButton(teleporter))
        }
    }

    private fun backButton(): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip {
            info("Klicke hier, um zurück zum Hauptmenü zu gelangen.")
        }
        action {
            playerCallback {
                it.showDialog(TeleporterMainDialog.showDialog())
            }
        }
    }

    private fun teleportButton(teleporter: Teleporter): ActionButton = actionButton {
        label { info("Ansehen") }
        tooltip {
            info("Klicke hier, um dir den Teleporter anzusehen.")
        }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.showDialog(teleporter))
            }
        }
    }
}