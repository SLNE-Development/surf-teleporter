package dev.slne.surf.teleporter.minestom.teleporter

import net.kyori.adventure.key.Key
import net.minestom.server.instance.Instance
import net.minestom.server.tag.Tag

/**
 * The tag the lobby server stores the identity of an instance's world in.
 */
private val WORLD_KEY = Tag.String("surf_lobby_world_key")

/**
 * The key of the world this instance holds, or `null` if the world has no identity.
 */
val Instance.worldKey: Key?
    get() = getTag(WORLD_KEY)?.let { Key.key(it) }
