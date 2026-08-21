package dev.slne.surf.teleporter.minestom.dialog.crud

import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.minestom.dialog.TeleporterDialog
import dev.slne.surf.teleporter.minestom.dialog.TeleporterMainDialog
import dev.slne.surf.teleporter.minestom.teleporter.toTeleporterPosition
import net.minestom.server.entity.Player
import java.util.*

object TeleporterCreateDialog {
    fun createDialog(
        player: Player,
        teleporter: Teleporter? = null
    ) = TeleporterDialog.create(
        teleporter = teleporter ?: newTeleporter(player),
        dialogHeader = TeleporterDialogTexts.createHeader
    ) { teleporter ->
        yes {
            label(TeleporterButtonTexts.createTeleporterLabel)
            tooltip(TeleporterButtonTexts.createConfirmTooltip)

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
            label(TeleporterButtonTexts.backLabel)
            tooltip(TeleporterButtonTexts.cancelTooltip)
            action {
                playerCallback {
                    it.showDialog(TeleporterMainDialog.createDialog())
                }
            }
        }
    }

    private fun newTeleporter(player: Player): Teleporter {
        val position = player.toTeleporterPosition()
            ?: error("Cannot create a teleporter in a world without an identity")

        return Teleporter(
            uuid = UUID.randomUUID(),
            name = player.username,
            originLocation = position,
            targetLocation = position,
            width = 3.0,
            length = 3.0,
            height = 3.0
        )
    }
}
