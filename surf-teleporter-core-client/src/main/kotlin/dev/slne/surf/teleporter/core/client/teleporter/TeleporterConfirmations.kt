package dev.slne.surf.teleporter.core.client.teleporter

import dev.slne.surf.teleporter.core.client.dialog.TeleporterActionType
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult
import it.unimi.dsi.fastutil.objects.ObjectList

/**
 * Carries out what a player confirmed in a teleporter dialog.
 */
object TeleporterConfirmations {
    /**
     * Reads [values] into [teleporter] and carries out [type] once every value could be read.
     *
     * Nothing is carried out while a single value could not be read, so a teleporter is never left
     * half configured.
     *
     * @param type the action the player confirmed
     * @param teleporter the teleporter the action was confirmed for
     * @param values the values the dialog collected, by field name
     * @return the outcomes of the fields that could not be read, empty if the action was carried out
     */
    fun confirm(
        type: TeleporterActionType,
        teleporter: Teleporter,
        values: (String) -> String?
    ): ObjectList<TeleporterFieldParseResult> {
        val results = teleporter.parse(values)

        if (results.isNotEmpty()) {
            return results
        }

        when (type) {
            TeleporterActionType.CREATE -> TeleporterService.registerTeleporter(teleporter)
            TeleporterActionType.EDIT -> TeleporterService.saveTeleporters()
            TeleporterActionType.DELETE -> Unit
        }

        return results
    }
}
