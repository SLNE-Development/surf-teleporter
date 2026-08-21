package dev.slne.surf.teleporter.core.client.storage

import dev.slne.surf.api.core.util.requiredService
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import it.unimi.dsi.fastutil.objects.ObjectList

private val storage = requiredService<TeleporterStorage>()

/**
 * Keeps the teleporters of a server between restarts.
 *
 * Where the teleporters are kept is up to the server; what is stored is not.
 */
interface TeleporterStorage {
    /**
     * Reads every stored teleporter.
     *
     * @return the stored teleporters
     */
    fun loadTeleporters(): ObjectList<Teleporter>

    /**
     * Writes [teleporters] back, replacing whatever was stored before.
     *
     * @param teleporters the teleporters to store
     */
    fun saveTeleporters(teleporters: Collection<Teleporter>)

    companion object : TeleporterStorage by storage
}
