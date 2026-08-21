package dev.slne.surf.teleporter.core.client.teleporter.field

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TeleporterInputsTest {

    @Test
    fun `reads a position written as world and six comma separated values`() {
        assertEquals(
            TeleporterInputs.Location("world", 1.0, 2.0, 3.0, 4.0f, 5.0f),
            TeleporterInputs.parseLocation("world, 1, 2, 3, 4, 5")
        )
    }

    @Test
    fun `reads fractional and negative coordinates`() {
        assertEquals(
            TeleporterInputs.Location("world_nether", -10.25, 64.5, -300.75, -90.0f, 12.5f),
            TeleporterInputs.parseLocation("world_nether, -10.25, 64.5, -300.75, -90.0, 12.5")
        )
    }

    @Test
    fun `keeps world names that contain a comma free of interpretation`() {
        assertEquals(
            "my world",
            TeleporterInputs.parseLocation("my world, 1, 2, 3, 4, 5")?.worldName
        )
    }

    @Test
    fun `rejects positions that do not carry six values`() {
        assertNull(TeleporterInputs.parseLocation("world, 1, 2, 3, 4"))
        assertNull(TeleporterInputs.parseLocation("world, 1, 2, 3, 4, 5, 6"))
        assertNull(TeleporterInputs.parseLocation(""))
    }

    @Test
    fun `rejects positions whose values are no numbers`() {
        assertNull(TeleporterInputs.parseLocation("world, X, Y, Z, Yaw, Pitch"))
        assertNull(TeleporterInputs.parseLocation("world, 1, 2, 3, 4, pitch"))
    }

    @Test
    fun `rejects positions that are not separated by a comma and a space`() {
        assertNull(TeleporterInputs.parseLocation("world,1,2,3,4,5"))
        assertNull(TeleporterInputs.parseLocation("world 1 2 3 4 5"))
    }
}
