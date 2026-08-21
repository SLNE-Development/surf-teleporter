package dev.slne.surf.teleporter.core.client.message

import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.buildText
import net.kyori.adventure.dialog.DialogLike
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

/**
 * The chat messages teleporters send to players.
 *
 * Dialogs are handed in as suppliers, so that a dialog only takes shape once a player actually asks
 * for it and therefore always shows the current state.
 */
object TeleporterMessages {
    /**
     * Tells a player that the block they looked at holds no teleporter and offers the list of all of
     * them.
     *
     * @param listDialog the dialog listing every teleporter
     * @return the message to send
     */
    fun noTeleporterAtBlock(listDialog: () -> DialogLike): Component {
        val clickable = buildText {
            text("HIER", Colors.VARIABLE_VALUE, TextDecoration.UNDERLINED)
            hoverEvent(HoverEvent.showText(buildText { info("Klicke hier, um dir die Liste der existierenden Teleporter anzusehen.") }))
            clickEvent(ClickEvent.callback { it.showDialog(listDialog()) })
        }

        return buildText {
            appendErrorPrefix()
            error("An dieser Stelle befindet sich kein Teleporter!")
            appendNewErrorPrefixedLine()
            error("Klicke ")
            append(clickable)
            error(" um dir die Liste der existierenden Telepoerter anzusehen.")
        }
    }

    /**
     * Tells a player that the block they touched belongs to a teleporter and offers to show it.
     *
     * @param infoDialog the dialog describing the teleporter
     * @return the message to send
     */
    fun teleporterAtBlock(infoDialog: () -> DialogLike): Component {
        val clickable = buildText {
            text("HIER", Colors.VARIABLE_VALUE, TextDecoration.UNDERLINED)
            hoverEvent(HoverEvent.showText(buildText { info("Klicke hier, um dir das JumpPad anzusehen.") }))
            clickEvent(ClickEvent.callback { it.showDialog(infoDialog()) })
        }

        return buildText {
            appendErrorPrefix()
            error("An dieser Stelle befindet sich ein Teleporter!")
            appendNewErrorPrefixedLine()
            error("Klicke ")
            append(clickable)
            error(" um dir den Teleporter anzusehen!")
        }
    }

    /**
     * Tells a player that they were moved to a teleporter and offers to show it.
     *
     * @param teleporterName the name of the teleporter the player was moved to
     * @param infoDialog the dialog describing the teleporter
     * @return the message to send
     */
    fun teleported(teleporterName: String, infoDialog: () -> DialogLike): Component = buildText {
        appendSuccessPrefix()
        success("Du wurdest zum Teleporter ")
        appendSpace()
        variableValue(teleporterName)
        appendSpace()
        success("teleportiert!")
        hoverEvent(buildText {
            error("Klicke, um dir den Teleporter anzusehen.")
        })
        clickCallback { it.showDialog(infoDialog()) }
    }

    /**
     * Reports how many teleporters were loaded after the configuration was read again.
     *
     * @param teleporterCount how many teleporters were loaded
     * @return the message to send
     */
    fun reloaded(teleporterCount: Int): Component = buildText {
        appendSuccessPrefix()
        success("Successfully reloaded Teleporter config!")

        appendNewSuccessPrefixedLine()
        success("Sucesfully loaded")
        appendSpace()
        variableValue(teleporterCount)
        appendSpace()
        success("teleporters!")
    }

    /**
     * Tells a player that something went wrong.
     */
    val genericError: Component = buildText {
        appendErrorPrefix()
        error("Es ist ein Fehler aufgetreten!")
    }
}
