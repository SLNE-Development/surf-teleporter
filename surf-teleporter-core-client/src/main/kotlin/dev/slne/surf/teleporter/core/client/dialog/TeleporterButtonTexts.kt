package dev.slne.surf.teleporter.core.client.dialog

import dev.slne.surf.api.core.messages.adventure.buildText
import net.kyori.adventure.text.Component

/**
 * The labels and tooltips of the buttons the teleporter dialogs offer.
 */
object TeleporterButtonTexts {
    val createTeleporterLabel: Component = buildText { success("Teleporter erstellen") }
    val createTeleporterTooltip: Component =
        buildText { info("Klicke hier, einen Teleporter zu erstellen.") }

    val showTeleportersLabel: Component = buildText { primary("Teleporter ansehen") }
    val showTeleportersTooltip: Component =
        buildText { info("Klicke hier, die existierenden Teleporter anzusehen.") }

    val closeLabel: Component = buildText { spacer("Schließen") }
    val cancelTooltip: Component = buildText { info("Klicke hier, um den Vorgang abzubrechen.") }

    val backLabel: Component = buildText { spacer("Zurück") }
    val backToMainTooltip: Component =
        buildText { info("Klicke hier, um zurück zum Hauptmenü zu gelangen.") }

    val createFromListLabel: Component = buildText { success("Teleporter erstellen") }
    val createFromListTooltip: Component =
        buildText { info("Klicke hier, um einen Teleporter zu erstellen.") }
    val createConfirmTooltip: Component =
        buildText { info("Klicke hier, um den Teleporter zu erstellen.") }

    val saveLabel: Component = buildText { success("Änderungen speichern") }
    val saveTooltip: Component = buildText { info("Klicke hier, um die Änderungen zu übernehmen.") }

    val teleportLabel: Component = buildText { primary("Teleportieren") }
    val teleportTooltip: Component =
        buildText { info("Klicke hier, um dich zum Teleporter zu teleportieren.") }

    val editLabel: Component = buildText { primary("Konfigurieren") }
    val editTooltip: Component =
        buildText { info("Klicke hier, um die Einstellungen des Teleporters zu konfigurieren.") }

    val deleteLabel: Component = buildText { error("Löschen") }
    val deleteTooltip: Component = buildText { info("Klicke hier, um den Teleporter zu löschen.") }

    val viewTeleporterLabel: Component = buildText { spacer("Teleporter ansehen") }
    val viewTeleporterTooltip: Component =
        buildText { info("Klicke, um Details des neuen Teleporters zu sehen.") }

    val mainMenuTooltip: Component = buildText { info("Klicke, um zum Hauptmenü zurückzukehren.") }
    val backToInfoTooltip: Component = buildText { info("Zurück zur Teleporter-Ansicht.") }
    val backToListTooltip: Component = buildText { info("Zurück zur Teleporter-Übersicht.") }
}
