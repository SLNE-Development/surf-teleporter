@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.edit

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.error.InvalidField
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.dialogs.error.TeleporterErrorDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterSuccessDialog
import dev.slne.surf.teleporter.dialogs.view.TeleporterInfoDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.TeleporterService
import io.papermc.paper.registry.data.dialog.ActionButton
import org.bukkit.Location
import org.bukkit.World

object TeleporterEditDialog {
    private const val LOCATION_KEY = "teleporter_location"
    private const val LOCATION_WORLD_KEY = "teleporter_location_world"
    private const val TARGET_LOCATION_KEY = "teleporter_target_location"
    private const val TARGET_LOCATION_WORLD_KEY = "teleporter_target_location_world"
    private const val BOX_KEY = "teleporter_box"

    private val locationRegex by lazy {
        Regex("""^-?\d+(\.\d+)?\s-?\d+(\.\d+)?\s-?\d+(\.\d+)?(\s-?\d+(\.\d+)?){0,2}$""")
    }
    private val boxRegex by lazy { Regex("^\\d+x\\d+$") }

    fun createDialog(teleporter: Teleporter) = dialog {
        base {
            title {
                primary("TELEPORTER ".toSmallCaps())
                primary("LISTE ".toSmallCaps())
                success("KONFIGURIEREN ".toSmallCaps())
                variableValue("${teleporter.originLocation.blockX} ${teleporter.originLocation.blockY} ${teleporter.originLocation.blockZ} ")
            }

            body {
                plainMessage(400) {
                    info("Du konfigurierst gerade einen Teleporter.")
                    appendNewline(2)

                    info("Folgende Welten können zur Konfiguration verwendet werden:")
                    appendNewline()
                    server.worlds.forEach { world ->
                        spacer("- ")
                        variableValue(world.name)
                        appendNewline()
                    }
                    appendNewline()

                    info("Im Folgenden siehst du die aktuellen Werte des Teleporters.")
                    appendNewline(2)

                    primary("UUID: ")
                    variableValue(teleporter.uuid.toString())
                    appendNewline(2)

                    spacer("- ")
                    primary("Position: ")
                    variableValue("${teleporter.originLocation.blockX} ${teleporter.originLocation.blockY} ${teleporter.originLocation.blockZ}")
                    primary(" in Welt ")
                    variableValue(teleporter.originLocation.world?.name ?: "Unbekannt")
                    appendNewline(2)

                    spacer("- ")
                    primary("TargetPosition: ")
                    variableValue("${teleporter.targetLocation.blockX} ${teleporter.targetLocation.blockY} ${teleporter.targetLocation.blockZ}")
                    primary(" in Welt ")
                    variableValue(teleporter.targetLocation.world?.name ?: "Unbekannt")
                    appendNewline(2)

                    spacer("- ")
                    primary("Box: ")
                    variableValue("${teleporter.width}x${teleporter.length}")
                    appendNewline(2)
                }
            }

            input {
                text(LOCATION_KEY) {
                    label { text("Location") }
                    initial("${teleporter.originLocation.blockX} ${teleporter.originLocation.blockY} ${teleporter.originLocation.blockZ}")
                    width(400)
                }
            }

            input {
                text(LOCATION_WORLD_KEY) {
                    label { text("Location World") }
                    initial(teleporter.originLocation.world?.name ?: "Unbekannt")
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_KEY) {
                    label { text("TargetLocation") }
                    initial("${teleporter.targetLocation.blockX} ${teleporter.targetLocation.blockY} ${teleporter.targetLocation.blockZ}")
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_WORLD_KEY) {
                    label { text("TargetLocation World") }
                    initial(teleporter.targetLocation.world?.name ?: "Unbekannt")
                    width(400)
                }
            }

            input {
                text(BOX_KEY) {
                    label { text("Box (max. 10x10)") }
                    initial("${teleporter.width}x${teleporter.length}")
                    width(400)
                }
            }
        }

        type {
            confirmation(saveButton(teleporter), backButton(teleporter))
        }
    }

    private fun saveButton(teleporter: Teleporter): ActionButton = actionButton {
        label { success("Änderungen speichern") }
        tooltip { info("Klicke hier, um die Änderungen zu übernehmen.") }
        action {
            customPlayerClick { content, player ->
                val locationString = content.getText(LOCATION_KEY) ?: ""
                val targetLocationString = content.getText(TARGET_LOCATION_KEY) ?: ""
                val locationWorldName = content.getText(LOCATION_WORLD_KEY) ?: ""
                val targetWorldName = content.getText(TARGET_LOCATION_WORLD_KEY) ?: ""
                val boxString = content.getText(BOX_KEY) ?: ""

                val validLocation = locationRegex.matches(locationString)
                val validTargetLocation = locationRegex.matches(targetLocationString)
                val validBox = boxRegex.matches(boxString)
                val (width, length) = parseBox(boxString)
                val boxTooLarge = width > 10 || length > 10

                val originWorld = server.getWorld(locationWorldName)
                val targetWorld = server.getWorld(targetWorldName)

                val invalidFields = mutableListOf<InvalidField>()

                if (!validLocation) invalidFields.add(InvalidField.START_LOCATION)
                if (!validTargetLocation) invalidFields.add(InvalidField.TARGET_LOCATION)
                if (!validBox) invalidFields.add(InvalidField.BOX_INVALID)
                if (boxTooLarge) invalidFields.add(InvalidField.BOX_SIZE)
                if (originWorld == null) invalidFields.add(InvalidField.ORIGIN_WORLD)
                if (targetWorld == null) invalidFields.add(InvalidField.TARGET_WORLD)

                if (invalidFields.isNotEmpty()) {
                    val previousValues = mapOf(
                        LOCATION_KEY to locationString,
                        LOCATION_WORLD_KEY to locationWorldName,
                        TARGET_LOCATION_KEY to targetLocationString,
                        TARGET_LOCATION_WORLD_KEY to targetWorldName,
                        BOX_KEY to boxString
                    )

                    player.showDialog(
                        TeleporterErrorDialog.createDialog(
                            TeleporterActionType.EDIT,
                            invalidFields,
                            previousValues,
                            teleporter
                        )
                    )
                    return@customPlayerClick
                }

                val origin = parseLocation(locationString, originWorld!!)
                val targetLocation = parseLocation(targetLocationString, targetWorld!!)

                teleporter.apply {
                    this.originLocation = origin
                    this.targetLocation = targetLocation
                    this.width = width
                    this.length = length
                }

                TeleporterService.saveTeleporters()

                player.showDialog(TeleporterSuccessDialog.createDialog(TeleporterActionType.CREATE, teleporter))
            }
        }
    }

    private fun backButton(teleporter: Teleporter): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterInfoDialog.createDialog(teleporter))
            }
        }
    }

    private fun parseBox(box: String): Pair<Int, Int> {
        return box.split("x").mapNotNull { it.toIntOrNull() }.let {
            if (it.size == 2) it[0] to it[1] else 3 to 3
        }
    }

    private fun parseLocation(raw: String, world: World): Location {
        val parts = raw.trim().split(" ")

        if (parts.size < 3) {
            throw IllegalArgumentException("Ungültiges Location-Format: $raw")
        }

        val x = parts[0].toDoubleOrNull() ?: error("Ungültige X-Koordinate: ${parts[0]}")
        val y = parts[1].toDoubleOrNull() ?: error("Ungültige Y-Koordinate: ${parts[1]}")
        val z = parts[2].toDoubleOrNull() ?: error("Ungültige Z-Koordinate: ${parts[2]}")

        val yaw = parts.getOrNull(3)?.toFloatOrNull() ?: 0f
        val pitch = parts.getOrNull(4)?.toFloatOrNull() ?: 0f

        return Location(world, x, y, z, yaw, pitch)
    }
}