package dev.slne.surf.teleporter.core.client.teleporter.field.fields

import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class TeleporterNameFieldTest {

    @Test
    fun `adopts a name without the whitespace around it`() {
        val field = TeleporterNameField("old")

        assertIs<TeleporterFieldParseResult.Success<*>>(field.parse("  new  "))
        assertEquals("new", field.currentValue)
    }

    @Test
    fun `keeps the current name when a blank one is typed`() {
        val field = TeleporterNameField("old")

        assertSame(TeleporterNameField.NameBlankResult, field.parse("   "))
        assertSame(TeleporterNameField.NameBlankResult, field.parse(""))
        assertEquals("old", field.currentValue)
    }
}
