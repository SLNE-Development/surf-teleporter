package dev.slne.surf.teleporter.dialogs.crud

import dev.slne.surf.teleporter.dialogs.TeleporterDialog
import dev.slne.surf.teleporter.dialogs.crud.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.teleporter.Teleporter
import net.kyori.adventure.text.format.TextDecoration

object TeleporterEditDialog {
    fun createDialog(
        teleporter: Teleporter
    ) = TeleporterDialog.create(
        teleporter = teleporter,
        dialogHeader = {
            primary(
                "Du konfigurierst gerade einen Teleporter.",
                TextDecoration.BOLD,
                TextDecoration.UNDERLINED
            )
        }
    ) {
        yes {
            label { success("Änderungen speichern") }
            tooltip { info("Klicke hier, um die Änderungen zu übernehmen.") }
            action {
                customPlayerClick { content, player ->
                    TeleporterDialog.handleConfirmation(
                        player = player,
                        content = content,
                        actionType = TeleporterActionType.EDIT,
                        teleporter = teleporter
                    )
                }
            }
        }

        no {
            label { spacer("Zurück") }
            tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
            action {
                playerCallback {
                    it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
                }
            }
        }
    }
}