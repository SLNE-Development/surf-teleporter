@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.crud

import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.dialog.TeleporterButtonTexts
import dev.slne.surf.teleporter.core.client.dialog.TeleporterDialogTexts
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.dialogs.TeleporterDialog
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.teleporter.toTeleporterPosition
import org.bukkit.entity.Player
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
        val position = player.location.toTeleporterPosition()
            ?: error("Cannot create a teleporter in an unloaded world")

        return Teleporter(
            uuid = UUID.randomUUID(),
            name = player.name,
            originLocation = position,
            targetLocation = position,
            width = 3.0,
            length = 3.0,
            height = 3.0
        )
    }
}
