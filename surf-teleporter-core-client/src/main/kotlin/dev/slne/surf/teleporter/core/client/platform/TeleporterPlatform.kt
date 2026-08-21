package dev.slne.surf.teleporter.core.client.platform

import dev.slne.surf.api.core.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.key.Key

private val platform = requiredService<TeleporterPlatform>()

/**
 * The few things teleporters need from the server they run on.
 */
interface TeleporterPlatform {
    /**
     * The names of every world a teleporter can be configured for, in the order the server holds
     * them.
     *
     * @return the world names
     */
    fun worldNames(): ObjectList<String>

    /**
     * Returns the key of the world known as [worldName].
     *
     * @param worldName the name of the world
     * @return the key of the world, or `null` if this server has no such world
     */
    fun findWorldKey(worldName: String): Key?

    /**
     * Returns the name the world [worldKey] names is known by.
     *
     * @param worldKey the key of the world
     * @return the name of the world, or `null` if this server has no such world
     */
    fun worldName(worldKey: Key): String?

    companion object : TeleporterPlatform by platform
}
