package dev.slne.surf.teleporter.core.client.teleporter

import net.kyori.adventure.key.Key
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TeleporterTest {

    @Test
    fun `covers the area around its origin block`() {
        val teleporter = teleporter(width = 3.0, length = 3.0, height = 3.0)

        assertTrue(teleporter.contains(position(10.0, 64.0, 10.0)))
        assertTrue(teleporter.contains(position(8.5, 66.9, 11.4)))
        assertTrue(teleporter.contains(position(11.4, 64.0, 8.5)))
    }

    @Test
    fun `leaves out what lies beyond its area`() {
        val teleporter = teleporter(width = 3.0, length = 3.0, height = 3.0)

        assertFalse(teleporter.contains(position(8.4, 64.0, 10.0)))
        assertFalse(teleporter.contains(position(11.5, 64.0, 10.0)))
        assertFalse(teleporter.contains(position(10.0, 63.9, 10.0)))
        assertFalse(teleporter.contains(position(10.0, 67.0, 10.0)))
        assertFalse(teleporter.contains(position(10.0, 64.0, 11.5)))
    }

    @Test
    fun `starts at the floor of its origin block`() {
        val teleporter = teleporter(width = 1.0, length = 1.0, height = 1.0)

        assertTrue(teleporter.contains(position(10.0, 64.0, 10.0)))
        assertTrue(teleporter.contains(position(10.0, 64.99, 10.0)))
        assertFalse(teleporter.contains(position(10.0, 65.0, 10.0)))
    }

    @Test
    fun `covers no position in another world`() {
        val teleporter = teleporter(width = 3.0, length = 3.0, height = 3.0)

        assertFalse(
            teleporter.contains(
                TeleporterPosition(Key.key("minecraft:the_nether"), 10.0, 64.0, 10.0)
            )
        )
    }

    @Test
    fun `covers nothing while a side is zero`() {
        val teleporter = teleporter(width = 0.0, length = 3.0, height = 3.0)

        assertFalse(teleporter.contains(position(10.0, 64.0, 10.0)))
    }

    private fun teleporter(width: Double, length: Double, height: Double): Teleporter {
        val origin = position(10.0, 64.0, 10.0)

        return Teleporter(
            uuid = UUID.randomUUID(),
            name = "teleporter",
            originLocation = origin,
            targetLocation = origin,
            width = width,
            length = length,
            height = height
        )
    }

    private fun position(x: Double, y: Double, z: Double) =
        TeleporterPosition(WORLD_KEY, x, y, z)

    private companion object {
        val WORLD_KEY: Key = Key.key("minecraft:overworld")
    }
}
