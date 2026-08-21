package dev.slne.surf.teleporter.core.client.storage

import dev.slne.surf.teleporter.core.client.platform.TestTeleporterPlatform
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterPosition
import net.kyori.adventure.key.Key
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TeleporterNbtStorageTest {

    @TempDir
    lateinit var dataDirectory: Path

    private val storage get() = TeleporterNbtStorage.inDirectory(dataDirectory)

    @Test
    fun `holds nothing while no file exists`() {
        assertTrue(storage.loadTeleporters().isEmpty())
    }

    @Test
    fun `reads back what it wrote`() {
        val teleporter = teleporter(
            name = "spawn",
            origin = position(1.5, 64.0, -3.25, 90.0f, -12.5f),
            target = position(-100.75, 70.5, 200.0, 180.0f, 45.0f),
            width = 3.0,
            length = 4.5,
            height = 5.25
        )

        storage.saveTeleporters(listOf(teleporter))
        val loaded = storage.loadTeleporters().single()

        assertEquals(teleporter.uuid, loaded.uuid)
        assertEquals(teleporter.name, loaded.name)
        assertEquals(teleporter.originLocation, loaded.originLocation)
        assertEquals(teleporter.targetLocation, loaded.targetLocation)
        assertEquals(teleporter.width, loaded.width)
        assertEquals(teleporter.length, loaded.length)
        assertEquals(teleporter.height, loaded.height)
    }

    @Test
    fun `replaces what was stored before`() {
        storage.saveTeleporters(listOf(teleporter(name = "first"), teleporter(name = "second")))

        val remaining = teleporter(name = "third")
        storage.saveTeleporters(listOf(remaining))

        assertEquals(listOf(remaining.uuid), storage.loadTeleporters().map { it.uuid })
    }

    @Test
    fun `skips teleporters whose world this server does not have`() {
        val foreignPosition = TeleporterPosition(Key.key("minecraft", "the_nether"), 0.0, 64.0, 0.0)
        storage.saveTeleporters(
            listOf(teleporter(origin = foreignPosition, target = foreignPosition))
        )

        assertTrue(storage.loadTeleporters().isEmpty())
    }

    private fun teleporter(
        name: String = "teleporter",
        origin: TeleporterPosition = position(10.0, 64.0, 10.0, 0.0f, 0.0f),
        target: TeleporterPosition = position(20.0, 65.0, 20.0, 0.0f, 0.0f),
        width: Double = 3.0,
        length: Double = 3.0,
        height: Double = 3.0
    ) = Teleporter(
        uuid = UUID.randomUUID(),
        name = name,
        originLocation = origin,
        targetLocation = target,
        width = width,
        length = length,
        height = height
    )

    private fun position(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) =
        TeleporterPosition(TestTeleporterPlatform.WORLD_KEY, x, y, z, yaw, pitch)
}
