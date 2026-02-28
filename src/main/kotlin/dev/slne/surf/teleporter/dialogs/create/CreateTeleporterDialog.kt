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
import dev.slne.surf.teleporter.dialogs.error.InvalidField
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.dialogs.error.TeleporterErrorDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterSuccessDialog
import dev.slne.surf.teleporter.teleporter.TeleporterService
import dev.slne.surf.teleporter.teleporter.teleporter
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

    private val locationRegex by lazy {
        Regex("""^-?\d+(\.\d+)?\s-?\d+(\.\d+)?\s-?\d+(\.\d+)?\s-?\d+\s-?\d+$""")
    }
    private val boxRegex by lazy { Regex("^\\d+x\\d+$") }

    fun createDialog(
        player: Player,
        initialValues: Map<String, String>? = null
    ) = dialog {
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
                    label { text("Startposition") }
                    maxLength(Int.MAX_VALUE)
                    initial(
                        initialValues?.get(LOCATION_KEY)
                            ?: "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ} ${player.yaw.toInt()} ${player.pitch.toInt()}"
                    )
                    width(400)
                }
            }

            input {
                text(LOCATION_WORLD_KEY) {
                    label { text("Startwelt") }
                    initial(initialValues?.get(LOCATION_WORLD_KEY) ?: player.world.name)
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_KEY) {
                    label { text("Zielposition") }
                    maxLength(Int.MAX_VALUE)
                    initial(initialValues?.get(TARGET_LOCATION_KEY) ?: "0 100 0 90 90")
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_WORLD_KEY) {
                    label { text("Zielwelt") }
                    initial(initialValues?.get(TARGET_LOCATION_WORLD_KEY) ?: player.world.name)
                    width(400)
                }
            }

            input {
                text(BOX_KEY) {
                    label { text("Box (max. 10x10)") }
                    initial(initialValues?.get(BOX_KEY) ?: "3x3")
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
        tooltip { info("Klicke hier, um den Teleporter zu erstellen.") }

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
                            TeleporterActionType.CREATE,
                            invalidFields,
                            previousValues
                        )
                    )
                    return@customPlayerClick
                }

                val origin = parseLocation(locationString, originWorld!!)
                val targetLocation = parseLocation(targetLocationString, targetWorld!!)

                val teleporter = teleporter(
                    uuid = uuid,
                    originLocation = origin,
                    targetLocation = targetLocation,
                    width = width,
                    length = length
                )

                TeleporterService.registerTeleporter(teleporter)

                player.showDialog(
                    TeleporterSuccessDialog.createDialog(
                        TeleporterActionType.CREATE,
                        teleporter
                    )
                )
            }
        }
    }

    private fun backButton(): ActionButton = actionButton {
        label { spacer("Zurück") }
        tooltip { info("Klicke hier, um den Vorgang abzubrechen.") }
        action {
            playerCallback {
                it.showDialog(TeleporterMainDialog.createDialog())
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

        val x = parts[0].toDoubleOrNull() ?: error("Ungültige X-Koordinate: ${parts[0]}")
        val y = parts[1].toDoubleOrNull() ?: error("Ungültige Y-Koordinate: ${parts[1]}")
        val z = parts[2].toDoubleOrNull() ?: error("Ungültige Z-Koordinate: ${parts[2]}")

        val yaw = parts.getOrNull(3)?.toFloatOrNull() ?: 0f
        val pitch = parts.getOrNull(4)?.toFloatOrNull() ?: 0f

        return Location(world, x, y, z, yaw, pitch)
    }
}
