package dev.slne.surf.teleporter.core.client.teleporter.field

/**
 * Reads the values players type into the teleporter dialogs.
 */
object TeleporterInputs {
    private const val LOCATION_SEPARATOR = ", "
    private const val LOCATION_PART_COUNT = 6

    /**
     * Reads the position [raw] describes, in the `world, x, y, z, yaw, pitch` form the dialogs use.
     *
     * @param raw the value typed by the player
     * @return the position, or `null` if the value does not describe one
     */
    fun parseLocation(raw: String): Location? {
        val split = raw.split(LOCATION_SEPARATOR)

        if (split.size != LOCATION_PART_COUNT) {
            return null
        }

        val worldName = split[0]
        val x = split[1].toDoubleOrNull()
        val y = split[2].toDoubleOrNull()
        val z = split[3].toDoubleOrNull()
        val yaw = split[4].toFloatOrNull()
        val pitch = split[5].toFloatOrNull()

        if (x == null || y == null || z == null || yaw == null || pitch == null) {
            return null
        }

        return Location(worldName, x, y, z, yaw, pitch)
    }

    /**
     * A position typed by a player, still naming its world the way the player typed it.
     *
     * @property worldName the name of the world
     * @property x the x coordinate
     * @property y the y coordinate
     * @property z the z coordinate
     * @property yaw the yaw
     * @property pitch the pitch
     */
    data class Location(
        val worldName: String,
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
    )
}
