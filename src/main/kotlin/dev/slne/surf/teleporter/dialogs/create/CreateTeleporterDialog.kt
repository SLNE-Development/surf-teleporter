@file:Suppress("UnstableApiUsage")

package dev.slne.surf.teleporter.dialogs.create

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.teleporter.dialogs.TeleporterMainDialog
import dev.slne.surf.teleporter.dialogs.create.results.TeleportCreateSuccessDialog
import dev.slne.surf.teleporter.dialogs.create.results.TeleportCreationFailResultDialog
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.teleporterService
import io.papermc.paper.registry.data.dialog.ActionButton
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

object CreateTeleporterDialog {
    private const val LOCATION_KEY = "teleporter_location"
    private const val LOCATION_WORLD_KEY = "teleporter_location_world"
    private const val TARGET_LOCATION_KEY = "teleporter_target_location"
    private const val TARGET_LOCATION_WORLD_KEY = "teleporter_target_location_world"
    private const val BOX_KEY = "teleporter_box"

    private val locationRegex by lazy { Regex("^-?\\d+\\s-?\\d+\\s-?\\d+$") }
    private val boxRegex by lazy { Regex("^\\d+x\\d+$") }

    fun showDialog(player: Player) = dialog {
        val uuid = UUID.randomUUID()
        base {
            title {
                primary("TELEPORTER ".toSmallCaps())
                success("ERSTELLEN".toSmallCaps())
            }

            body {
                plainMessage(400) {
                    primary("Du bist dabei einen neuen Teleporter zu erstellen.")
                    appendNewline(2)

                    info("Folgende Welten können zur Erstellung verwendet werden:")
                    appendNewline()
                    server.worlds.forEach { world ->
                        spacer("- ")
                        variableValue(world.name)
                        appendNewline()
                    }
                    appendNewline()

                    primary("UUID: ")
                    variableValue(uuid.toString())
                }
            }

            input {
                text(LOCATION_KEY) {
                    label { text("Location") }
                    initial("${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    width(400)
                }
            }

            input {
                text(LOCATION_WORLD_KEY) {
                    label { text("Location World") }
                    initial(player.world.name)
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_KEY) {
                    label { text("TaregetLocation") }
                    initial("0 100 0")
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_WORLD_KEY) {
                    label { text("TargetLocation World") }
                    initial(player.world.name)
                    width(400)
                }
            }

            input {
                text(BOX_KEY) {
                    label { text("Box (max. 10x10)") }
                    initial("3x3")
                    width(400)
                }
            }
        }

        type {
            confirmation(createButton(uuid), backButton())
        }
    }

    private fun createButton(uuid: UUID): ActionButton = actionButton {
        label { success("Teleporter erstellen") }
        tooltip {
            info("Klicke hier, um den Teleporter zu erstellen.")
        }
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

                if (!validLocation || !validTargetLocation || !validBox || originWorld == null || targetWorld == null || boxTooLarge) {
                    player.showDialog(TeleportCreationFailResultDialog.showDialog())
                    return@customPlayerClick
                }

                val origin = parseLocation(locationString, originWorld)
                val targetLocation = parseLocation(targetLocationString, targetWorld)


                val teleporter = Teleporter(
                    uuid = uuid,
                    originLocation = origin,
                    targetLocation = targetLocation,
                    width = width,
                    length = length
                )
                teleporterService.addTeleporter(teleporter)
                player.showDialog(TeleportCreateSuccessDialog.showDialog(teleporter))
            }
        }
    }

    private fun backButton(): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip {
            info("Klicke hier, um den Vorgang abzubrechen.")
        }
        action {
            playerCallback {
                it.showDialog(TeleporterMainDialog.showDialog())
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
        val x = parts[0].toDoubleOrNull() ?: 0.0
        val y = parts[1].toDoubleOrNull() ?: 0.0
        val z = parts[2].toDoubleOrNull() ?: 0.0
        return Location(world, x, y, z)
    }
}