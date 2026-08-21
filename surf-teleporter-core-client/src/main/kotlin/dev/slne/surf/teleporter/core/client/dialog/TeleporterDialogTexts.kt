package dev.slne.surf.teleporter.core.client.dialog

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.teleporter.core.client.appendBullet
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

/**
 * Every piece of text the teleporter dialogs are built from.
 *
 * Dialogs themselves are platform specific, their wording is not.
 */
object TeleporterDialogTexts {

    /**
     * The width every teleporter dialog renders its body with.
     */
    const val BODY_WIDTH = 400

    /**
     * The number of columns the dialog describing a teleporter renders its buttons in.
     */
    const val INFO_COLUMNS = 1

    /**
     * The number of columns the dialog listing every teleporter renders its entries in.
     */
    const val LIST_COLUMNS = 1

    /**
     * The width of a single entry in the dialog listing every teleporter.
     */
    const val LIST_BUTTON_WIDTH = 400

    /**
     * The title every teleporter dialog carries.
     */
    val dialogTitle: Component = buildText { primary("TELEPORTER".toSmallCaps()) }

    /**
     * The header of the dialog creating a teleporter.
     */
    val createHeader: Component = buildText {
        primary(
            "Du erstellst gerade einen neuen Teleporter.",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
    }

    /**
     * The header of the dialog configuring a teleporter.
     */
    val editHeader: Component = buildText {
        primary(
            "Du konfigurierst gerade einen Teleporter.",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
    }

    /**
     * The body shown when no teleporter exists yet.
     */
    val emptyListBody: Component = buildText {
        primary(
            "Du befindest dich in der Teleporter-Übersicht.",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
        appendNewline(2)

        error("Es existieren aktuell keine Teleporter.")
    }

    /**
     * The body of the teleporter management dialog.
     *
     * @param teleporterCount how many teleporters currently exist
     * @return the body text
     */
    fun mainBody(teleporterCount: Int): Component = buildText {
        primary("Hauptmenü", TextDecoration.BOLD, TextDecoration.UNDERLINED)
        appendNewline(2)

        info("Aktuell existieren ")
        variableValue(teleporterCount)
        info(" Teleporter.")
        appendNewline(2)
    }

    /**
     * The body of the dialog listing every teleporter.
     *
     * @param teleporterCount how many teleporters currently exist
     * @return the body text
     */
    fun listBody(teleporterCount: Int): Component = buildText {
        primary(
            "Du befindest dich in der Teleporter-Übersicht.",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
        appendNewline(2)

        info("Aktuell existieren ")
        variableValue(teleporterCount)
        info(" Teleporter.")
    }

    /**
     * The body of the dialogs creating and configuring a teleporter.
     *
     * @param header the header telling the player what they are doing
     * @param worldNames the names of every world a teleporter can be configured for
     * @param teleporter the teleporter being created or configured
     * @return the body text
     */
    fun configureBody(
        header: Component,
        worldNames: Collection<String>,
        teleporter: Teleporter
    ): Component = buildText {
        append(header)
        appendNewline(2)

        info("Folgende Welten können zur Konfiguration verwendet werden:")
        appendNewline()
        worldNames.forEachIndexed { index, worldName ->
            appendBullet()
            variableValue(worldName)

            if (index < worldNames.size - 1) {
                appendNewline()
            }
        }

        appendNewline(2)
        appendTeleporterInformation(teleporter)
    }

    /**
     * The title of the dialog describing a single teleporter.
     *
     * @param teleporter the teleporter being described
     * @return the title text
     */
    fun infoTitle(teleporter: Teleporter): Component = buildText {
        append(dialogTitle)
        appendSpace()
        variableValue(teleporter.name)
    }

    /**
     * The body of the dialog describing a single teleporter.
     *
     * @param teleporter the teleporter being described
     * @return the body text
     */
    fun infoBody(teleporter: Teleporter): Component = buildText {
        primary(
            "Du siehst dir gerade einen Teleporter an.",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
        appendNewline(2)

        appendTeleporterInformation(teleporter)
    }

    /**
     * The body of the dialog deleting a teleporter.
     *
     * @param teleporter the teleporter being deleted
     * @return the body text
     */
    fun deleteBody(teleporter: Teleporter): Component = buildText {
        error(
            "Du bist dabei einen Teleporter unwiderruflich zu löschen!",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
        appendNewline(2)

        error("Bitte bestätige dein Vorhaben!")
        appendNewline(2)

        appendTeleporterInformation(teleporter)
    }

    /**
     * The body of the dialog reporting values that could not be read.
     *
     * @param invalidFields the fields that could not be read
     * @return the body text
     */
    fun errorBody(invalidFields: Collection<TeleporterFieldParseResult>): Component = buildText {
        error(
            "Es ist ein Fehler aufgetreten!",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
        appendNewline(2)

        error("Es sind folgende Fehler aufgetreten:")
        appendNewline(2)

        appendCollectionNewLine(
            collection = invalidFields,
            linePrefix = Component.empty()
        ) { field ->
            buildText {
                append(field)
            }
        }

        appendNewline(2)
        error("Bitte korrigiere die Eingaben und versuche es erneut.")
    }

    /**
     * The body of the dialog reporting a finished action.
     *
     * @param type the action that was carried out
     * @param teleporter the teleporter the action was carried out on, if it still exists
     * @return the body text
     */
    fun successBody(type: TeleporterActionType, teleporter: Teleporter?): Component = buildText {
        success(
            "Der Vorgang wurde erfolgreich abgeschlossen!",
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        )
        appendNewline(2)

        success(
            when (type) {
                TeleporterActionType.CREATE -> "Der Teleporter wurde erfolgreich erstellt!"
                TeleporterActionType.EDIT -> "Die Änderungen wurden erfolgreich gespeichert!"
                TeleporterActionType.DELETE -> "Der ausgewählte Teleporter wurde erfolgreich gelöscht!"
            }
        )
        appendNewline(2)

        if (type != TeleporterActionType.DELETE && teleporter != null) {
            appendBullet()
            primary("UUID: ")
            variableValue(teleporter.uuid.toString())
        }
    }

    private fun SurfComponentBuilder.appendTeleporterInformation(teleporter: Teleporter) {
        info("Im Folgenden siehst du die aktuellen Werte des Teleporters:")
        appendNewline(2)

        appendBullet()
        teleporter.teleporterNameField.appendInfo(this)
        appendNewline(2)

        appendBullet()
        primary("UUID:")
        appendSpace()
        variableValue(teleporter.uuid.toString())
        appendNewline(2)

        appendBullet()
        teleporter.originLocationField.appendInfo(this)
        appendNewline(2)

        appendBullet()
        teleporter.targetLocationField.appendInfo(this)
        appendNewline(2)

        appendBullet()
        teleporter.boxField.appendInfo(this)
    }
}
