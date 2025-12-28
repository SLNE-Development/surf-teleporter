@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.edit.result

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.edit.TeleporterEditDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import io.papermc.paper.registry.data.dialog.ActionButton
import net.kyori.adventure.text.format.TextDecoration

object TeleporterEditFailResultDialog {
    fun showDialog(teleporter: Teleporter) = dialog {
        base {
            title {
                primary("TELEPORTER ".toSmallCaps())
                primary("LISTE ".toSmallCaps())
                variableValue("${teleporter.originLocation.blockX} ${teleporter.originLocation.blockY} ${teleporter.originLocation.blockZ} ")
                primary("KONFIGURIEREN ".toSmallCaps())
                error("FEHLER".toSmallCaps())
            }

            body {
                plainMessage(400) {
                    error("Fehler!", TextDecoration.BOLD)
                    appendNewline(2)
                    error("Die angegebenen Felder wurden nicht korrekt ausgefüllt.")
                    appendNewline(2)
                    error("Bitte versuche es erneut.")
                }
            }
        }

        type {
            notice(backButton(teleporter))
        }
    }

    private fun backButton(teleporter: Teleporter): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip {
            info("Klicke hier, um zurück zur Erstellung zu gelangen.")
        }
        action {
            playerCallback {
                it.showDialog(TeleporterEditDialog.showDialog(teleporter))
            }
        }
    }
}