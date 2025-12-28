package dev.slne.surf.teleporter.teleporter

import org.bukkit.Location
import java.util.*

data class Teleporter(
    val uuid: UUID,
    val originLocation: Location,
    val targetLocation: Location,
    val width: Int,
    val length: Int
)