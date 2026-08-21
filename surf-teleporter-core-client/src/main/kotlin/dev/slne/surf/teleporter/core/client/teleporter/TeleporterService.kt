package dev.slne.surf.teleporter.core.client.teleporter

import dev.slne.surf.teleporter.core.client.storage.TeleporterStorage
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Manages all registered teleporters.
 */
object TeleporterService {

    /**
     * Every currently registered teleporter.
     */
    val teleporters: List<Teleporter>
        field = CopyOnWriteArrayList<Teleporter>()

    /**
     * How many teleporters are currently registered.
     */
    val teleporterCount get() = teleporters.size

    /**
     * Replaces every registered teleporter with the stored ones.
     */
    fun registerTeleporters() {
        teleporters.clear()
        teleporters.addAll(TeleporterStorage.loadTeleporters())
    }

    /**
     * Registers [teleporter] and stores it.
     *
     * @param teleporter the teleporter to register
     */
    fun registerTeleporter(teleporter: Teleporter) {
        teleporters.add(teleporter)

        saveTeleporters()
    }

    /**
     * Unregisters [teleporter] and stores the remaining ones.
     *
     * @param teleporter the teleporter to unregister
     */
    fun unregisterTeleporter(teleporter: Teleporter) {
        teleporters.remove(teleporter)

        saveTeleporters()
    }

    /**
     * Finds the teleporter [position] lies inside of.
     *
     * @param position the position to check
     * @return the teleporter at the given position, or `null` if there is none
     */
    fun getTeleporterAt(position: TeleporterPosition): Teleporter? {
        return teleporters.find { it.contains(position) }
    }

    /**
     * Stores every registered teleporter.
     */
    fun saveTeleporters() {
        TeleporterStorage.saveTeleporters(teleporters)
    }
}
