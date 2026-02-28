@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.error

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import io.papermc.paper.registry.data.dialog.ActionButton

object TeleporterSuccessDialog {
    fun createDialog(type: TeleporterActionType, teleporter: Teleporter) = dialog {
        base {
            title {
                primary("TELEPORTER ".toSmallCaps())
                success(
                    when (type) {
                        TeleporterActionType.CREATE -> "ERSTELLT".toSmallCaps()
                        TeleporterActionType.EDIT -> "AKTUALISIERT".toSmallCaps()
                    }
                )
            }

            body {
                plainMessage(400) {
                    success(
                        when (type) {
                            TeleporterActionType.CREATE -> "Der Teleporter wurde erfolgreich erstellt!"
                            TeleporterActionType.EDIT -> "Die Änderungen wurden erfolgreich gespeichert!"
                        }
                    )
                    appendNewline(2)

                    primary("UUID: ")
                    variableValue(teleporter.uuid.toString())
                }
            }
        }

        type {
            notice(backButton(teleporter))
        }
    }

    private fun backButton(teleporter: Teleporter): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Zurück zur Teleporter-Ansicht.") }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }
}
