package dev.slne.surf.teleporter.core.client.teleporter.field

import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder

/**
 * A single configurable value of a teleporter, as it is shown and typed in the teleporter dialogs.
 *
 * @param T the kind of value this field holds
 */
interface TeleporterField<T> {
    /**
     * The key this field is declared under, and the key its value is reported back under.
     */
    val fieldName: String

    /**
     * The name this field is shown to players under.
     */
    val fieldDisplayName: String

    /**
     * The value this field started out with.
     */
    val initialValue: T

    /**
     * The value this field currently holds.
     */
    val currentValue: T

    /**
     * Appends this field's name and current value to [builder].
     *
     * @param builder the builder to append to
     */
    fun appendInfo(builder: SurfComponentBuilder) = builder.append {
        variableKey("$fieldDisplayName:")
        appendSpace()
        variableValue(asString())
    }

    /**
     * Reads [value] into this field, adopting it when it could be read.
     *
     * @param value the value typed by the player
     * @return the outcome of reading the value
     */
    fun parse(value: String): TeleporterFieldParseResult

    /**
     * Reads the value [values] holds for this field, adopting it when it could be read.
     *
     * @param values the values a dialog collected, by field name
     * @return the outcome of reading the value
     */
    fun parse(values: (String) -> String?) = parse(values(fieldName) ?: "")

    /**
     * Renders the current value the way it is shown and typed in the dialogs.
     *
     * @return the rendered value
     */
    fun asString(): String
}
