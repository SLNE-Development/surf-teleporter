package dev.slne.surf.teleporter.core.client.teleporter.field.fields

import dev.slne.surf.teleporter.core.client.teleporter.field.TeleporterFieldParseResult
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class TeleporterBoxFieldTest {

    @Test
    fun `renders the area the way it is typed`() {
        assertEquals("3.0x4.0x5.0", TeleporterBoxField(3.0, 4.0, 5.0).asString())
    }

    @Test
    fun `adopts an area written as three numbers around an x`() {
        val field = TeleporterBoxField(3.0, 3.0, 3.0)

        assertIs<TeleporterFieldParseResult.Success<*>>(field.parse("1.5x2x3"))
        assertEquals(TeleporterBox(1.5, 2.0, 3.0), field.currentValue)
    }

    @Test
    fun `keeps the current area when the wrong number of sides is typed`() {
        val field = TeleporterBoxField(3.0, 3.0, 3.0)

        assertSame(TeleporterBoxField.WrongSplitSizeResult, field.parse("3x3"))
        assertEquals(TeleporterBox(3.0, 3.0, 3.0), field.currentValue)
    }

    @Test
    fun `keeps the current area when a side is no number`() {
        val field = TeleporterBoxField(3.0, 3.0, 3.0)

        assertSame(TeleporterBoxField.ArgumentsNotParsableResult, field.parse("3xAx3"))
        assertEquals(TeleporterBox(3.0, 3.0, 3.0), field.currentValue)
    }
}
