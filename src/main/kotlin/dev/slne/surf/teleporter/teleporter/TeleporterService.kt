package dev.slne.surf.teleporter.teleporter

import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import org.bukkit.Location
import org.jetbrains.annotations.Unmodifiable

private val teleporterService = requiredService<TeleporterService>()

interface TeleporterService {
    val teleporterCount: Int
    val teleporters: @Unmodifiable ObjectList<Teleporter>

    fun registerTeleporters()
    fun registerTeleporter(teleporter: Teleporter)
    fun unregisterTeleporter(teleporter: Teleporter)

    fun getTeleporterAt(location: Location): Teleporter?
    fun saveTeleporters()

    companion object : TeleporterService by teleporterService
}