package dev.slne.surf.teleporter.dialogs.crud

import dev.slne.surf.teleporter.dialogs.TeleporterDialog
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.teleporter.Teleporter
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import java.util.*

object TeleporterCreateDialog {
    fun createDialog(
        player: Player,
        teleporter: Teleporter? = null
    ) = TeleporterDialog.create(
        teleporter = teleporter ?: Teleporter(
            uuid = UUID.randomUUID(),
            name = player.name,
            originLocation = player.location,
            targetLocation = player.location,
            width = 3.0,
            length = 3.0,
            height = 3.0
        ),
        dialogHeader = {
            primary(
                "Du erstellst gerade einen neuen Teleporter.",
                TextDecoration.BOLD,
                TextDecoration.UNDERLINED
            )
        }
    ) { teleporter ->
        yes {
            label { success("Teleporter erstellen") }
            tooltip { info("Klicke hier, um den Teleporter zu erstellen.") }

            action {
                customPlayerClick { content, player ->
                    TeleporterDialog.handleConfirmation(
                        player,
                        content,
                        TeleporterActionType.CREATE,
                        teleporter
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