package dev.slne.surf.teleporter.core.client.teleporter

import dev.slne.surf.teleporter.core.client.storage.TeleporterStorage
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectImmutableList

/**
 * Manages all registered teleporters.
 */
object TeleporterService {

    private val lock = Any()

    @Volatile
    private var registered: ObjectImmutableList<Teleporter> = ObjectImmutableList.of()

    /**
     * Every currently registered teleporter.
     */
    val teleporters: List<Teleporter> get() = registered

    /**
     * How many teleporters are currently registered.
     */
    val teleporterCount get() = registered.size

    /**
     * Replaces every registered teleporter with the stored ones.
     */
    fun registerTeleporters() {
        synchronized(lock) {
            registered = ObjectImmutableList(TeleporterStorage.loadTeleporters())
        }
    }

    /**
     * Registers [teleporter] and stores it.
     *
     * @param teleporter the teleporter to register
     */
    fun registerTeleporter(teleporter: Teleporter) {
        synchronized(lock) {
            val current = registered
            val next = ObjectArrayList<Teleporter>(current.size + 1)
            next.addAll(current)
            next.add(teleporter)
            registered = ObjectImmutableList(next)

            saveTeleporters()
        }
    }

    /**
     * Unregisters [teleporter] and stores the remaining ones.
     *
     * @param teleporter the teleporter to unregister
     */
    fun unregisterTeleporter(teleporter: Teleporter) {
        synchronized(lock) {
            val next = ObjectArrayList(registered)
            next.remove(teleporter)
            registered = ObjectImmutableList(next)

            saveTeleporters()
        }
    }

    /**
     * Finds the teleporter [position] lies inside of.
     *
     * @param position the position to check
     * @return the teleporter at the given position, or `null` if there is none
     */
    @Suppress("ReplaceManualRangeWithIndicesCalls")
    fun getTeleporterAt(position: TeleporterPosition): Teleporter? {
        val teleporters = registered

        for (index in 0 until teleporters.size) {
            val teleporter = teleporters[index]

            if (teleporter.contains(position)) {
                return teleporter
            }
        }

        return null
    }

    /**
     * Stores every registered teleporter.
     */
    fun saveTeleporters() {
        synchronized(lock) {
            TeleporterStorage.saveTeleporters(registered)
        }
    }
}
