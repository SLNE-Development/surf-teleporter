@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.create

import dev.slne.surf.teleporter.dialogs.TeleporterDialog
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

object TeleporterCreateDialog {
    fun createDialog(
        player: Player,
    ) = TeleporterDialog.create(
        player = player,
        initialValues = mapOf(),
        dialogHeader = {
            primary(
                "Du erstellst gerade einen neuen Teleporter.",
                TextDecoration.BOLD,
                TextDecoration.UNDERLINED
            )
        }
    ) {
        yes {
            label { success("Teleporter erstellen") }
            tooltip { info("Klicke hier, um den Teleporter zu erstellen.") }

            action {
                customPlayerClick { content, player ->
                    TeleporterDialog.handleConfirmation(
                        player,
                        content,
                        TeleporterActionType.CREATE,
                        null
                    )
                }
            }
        }

        no {
            label { spacer("Zurück") }
            tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
            action {
                playerCallback {
                    it.showDialog(TeleporterMainDialog.createDialog())
                }
            }
        }
    }
}
