package dev.slne.surf.teleporter.dialogs

import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.DialogTypeBuilder
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.teleporter.appendBullet
import dev.slne.surf.teleporter.dialogs.error.InvalidField
import dev.slne.surf.teleporter.dialogs.error.TeleporterActionType
import dev.slne.surf.teleporter.dialogs.error.TeleporterErrorDialog
import dev.slne.surf.teleporter.dialogs.error.TeleporterSuccessDialog
import dev.slne.surf.teleporter.formatToCoordString
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.TeleporterService
import dev.slne.surf.teleporter.teleporter.teleporter
import io.papermc.paper.dialog.DialogResponseView
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

@Suppress("UnstableApiUsage")
object TeleporterDialog {
    const val UUID_KEY = "teleporter_uuid"
    const val NAME_KEY = "teleporter_name"
    const val LOCATION_KEY = "teleporter_location"
    const val LOCATION_WORLD_KEY = "teleporter_location_world"
    const val TARGET_LOCATION_KEY = "teleporter_target_location"
    const val TARGET_LOCATION_WORLD_KEY = "teleporter_target_location_world"
    const val BOX_KEY = "teleporter_box"

    private val locationRegex by lazy {
        Regex("^-?\\d+(\\.\\d{1,2})?(?:\\s+-?\\d+(\\.\\d{1,2})?){4}$")
    }

    private val boxRegex by lazy { Regex("^\\d+x\\d+$") }

    fun create(
        player: Player,
        initialValues: Map<String, String>,
        dialogHeader: SurfComponentBuilder.() -> Unit,
        confirmationBuilder: DialogTypeBuilder.DialogConfirmationTypeBuilder.() -> Unit
    ) = dialog {
        base {
            title(DIALOG_TITLE)

            body {
                plainMessage(400) {
                    dialogHeader()
                    appendNewline(2)

                    info("Folgende Welten können zur Konfiguration verwendet werden:")
                    appendNewline()
                    server.worlds.forEachIndexed { index, world ->
                        appendBullet()
                        variableValue(world.name)

                        if (index < server.worlds.size - 1) {
                            appendNewline()
                        }
                    }

                    if (initialValues.isNotEmpty()) {
                        appendNewline(2)
                        appendTeleporterInformation(initialValues)
                    }
                }
            }

            input {
                text(NAME_KEY) {
                    label { text("Name") }
                    initial(initialValues.getOrDefault(NAME_KEY, ""))
                    width(400)
                }
            }

            input {
                text(LOCATION_KEY) {
                    label { text("Location") }
                    maxLength(Int.MAX_VALUE)
                    initial(
                        initialValues.getOrDefault(
                            LOCATION_KEY,
                            player.location.formatToCoordString()
                        )
                    )
                    width(400)
                }
            }

            input {
                text(LOCATION_WORLD_KEY) {
                    label { text("Location World") }
                    initial(
                        initialValues.getOrDefault(
                            LOCATION_WORLD_KEY,
                            player.location.world.name
                        )
                    )
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_KEY) {
                    label { text("TargetLocation") }
                    maxLength(Int.MAX_VALUE)
                    initial(
                        initialValues.getOrDefault(
                            TARGET_LOCATION_KEY,
                            player.location.formatToCoordString()
                        )
                    )
                    width(400)
                }
            }

            input {
                text(TARGET_LOCATION_WORLD_KEY) {
                    label { text("TargetLocation World") }
                    initial(
                        initialValues.getOrDefault(
                            TARGET_LOCATION_WORLD_KEY,
                            player.location.world.name
                        )
                    )
                    width(400)
                }
            }

            input {
                text(BOX_KEY) {
                    label { text("Boundingbox (max. 10x10)") }
                    initial(
                        initialValues.getOrDefault(
                            BOX_KEY,
                            "3x3"
                        )
                    )
                    width(400)
                }
            }
        }

        type {
            confirmation(confirmationBuilder)
        }
    }

    fun handleConfirmation(
        player: Player,
        content: DialogResponseView,
        actionType: TeleporterActionType,
        teleporter: Teleporter?
    ) {
        var teleporter = teleporter

        val name = (content.getText(NAME_KEY) ?: "").trim()
        val locationString = content.getText(LOCATION_KEY) ?: ""
        val targetLocationString = content.getText(TARGET_LOCATION_KEY) ?: ""
        val locationWorldName = content.getText(LOCATION_WORLD_KEY) ?: ""
        val targetWorldName = content.getText(TARGET_LOCATION_WORLD_KEY) ?: ""
        val boxString = content.getText(BOX_KEY) ?: ""

        val validName = name.isNotBlank()
        val validLocation = locationRegex.matches(locationString)
        val validTargetLocation = locationRegex.matches(targetLocationString)
        val validBox = boxRegex.matches(boxString)
        val (width, length) = parseBox(boxString)
        val boxTooLarge = width > 10 || length > 10

        val originWorld = server.getWorld(locationWorldName)
        val targetWorld = server.getWorld(targetWorldName)

        val invalidFields = mutableListOf<InvalidField>()

        if (!validName) invalidFields.add(InvalidField.NAME_EMPTY)
        if (!validLocation) invalidFields.add(InvalidField.START_LOCATION)
        if (!validTargetLocation) invalidFields.add(InvalidField.TARGET_LOCATION)
        if (!validBox) invalidFields.add(InvalidField.BOX_INVALID)
        if (boxTooLarge) invalidFields.add(InvalidField.BOX_SIZE)
        if (originWorld == null) invalidFields.add(InvalidField.ORIGIN_WORLD)
        if (targetWorld == null) invalidFields.add(InvalidField.TARGET_WORLD)

        if (invalidFields.isNotEmpty()) {
            val previousValues = mapOf(
                NAME_KEY to name,
                LOCATION_KEY to locationString,
                LOCATION_WORLD_KEY to locationWorldName,
                TARGET_LOCATION_KEY to targetLocationString,
                TARGET_LOCATION_WORLD_KEY to targetWorldName,
                BOX_KEY to boxString
            )

            player.showDialog(
                TeleporterErrorDialog.createDialog(
                    actionType,
                    invalidFields,
                    previousValues,
                    teleporter
                )
            )

            return
        }

        val origin = parseLocation(locationString, originWorld!!)
        val targetLocation = parseLocation(targetLocationString, targetWorld!!)

        if (actionType == TeleporterActionType.CREATE) {
            teleporter = teleporter(
                name = name,
                uuid = UUID.randomUUID(),
                originLocation = origin,
                targetLocation = targetLocation,
                width = width,
                length = length
            )

            TeleporterService.registerTeleporter(teleporter)
        } else if (actionType == TeleporterActionType.EDIT) {
            teleporter?.apply {
                this.originLocation = origin
                this.targetLocation = targetLocation
                this.width = width
                this.length = length
            }

            TeleporterService.saveTeleporters()
        }

        player.showDialog(
            TeleporterSuccessDialog.createDialog(
                actionType,
                teleporter
            )
        )
    }

    fun parseBox(box: String): Pair<Int, Int> {
        return box.split("x").mapNotNull { it.toIntOrNull() }.let {
            if (it.size == 2) it[0] to it[1] else 3 to 3
        }
    }

    fun parseLocation(raw: String, world: World): Location {
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

    fun SurfComponentBuilder.appendTeleporterInformation(initialValues: Map<String, String>) {
        info("Im Folgenden siehst du die aktuellen Werte des Teleporters:")
        appendNewline(2)

        appendBullet()
        primary("Name:")
        appendSpace()
        variableValue(initialValues.getOrDefault(NAME_KEY, "Unbekannt"))
        appendNewline(2)

        appendBullet()
        primary("UUID:")
        appendSpace()
        variableValue(initialValues.getOrDefault(UUID_KEY, "Unbekannt"))
        appendNewline(2)

        appendBullet()
        primary("Startposition (X Y Z Pitch Yaw):")
        appendSpace()
        variableValue(initialValues.getOrDefault(LOCATION_KEY, "Unbekannt"))
        appendSpace()
        primary("in Welt")
        appendSpace()
        variableValue(initialValues.getOrDefault(LOCATION_WORLD_KEY, "Unbekannt"))
        appendNewline(2)

        appendBullet()
        primary("Zielposition (X Y Z Pitch Yaw):")
        appendSpace()
        variableValue(initialValues.getOrDefault(TARGET_LOCATION_KEY, "Unbekannt"))
        primary(" in Welt ")
        variableValue(initialValues.getOrDefault(TARGET_LOCATION_WORLD_KEY, "Unbekannt"))
        appendNewline(2)

        appendBullet()
        primary("Box:")
        appendSpace()
        variableValue(initialValues.getOrDefault(BOX_KEY, "Unbekannt"))
    }
}