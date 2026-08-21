package dev.slne.surf.teleporter.minestom.teleporter

import dev.slne.minestom.lobby.api.extension.InstanceManager
import dev.slne.minestom.lobby.api.instance.worldKey
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterPosition
import net.kyori.adventure.key.Key
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import java.util.concurrent.CompletableFuture

/**
 * Converts this position into a platform-neutral position in [instance].
 *
 * @param instance the instance the position belongs to
 * @return the position, or `null` if the instance holds a world without an identity
 */
fun Point.toTeleporterPosition(instance: Instance): TeleporterPosition? {
    val worldKey = instance.worldKey ?: return null
    val yaw = (this as? Pos)?.yaw() ?: 0f
    val pitch = (this as? Pos)?.pitch() ?: 0f

    return TeleporterPosition(worldKey, x(), y(), z(), yaw, pitch)
}

/**
 * Converts the position this entity stands at into a platform-neutral position.
 *
 * @return the position, or `null` if the entity is in no instance, or in one whose world has no
 * identity
 */
fun Entity.toTeleporterPosition(): TeleporterPosition? =
    position.toTeleporterPosition(instance ?: return null)

/**
 * Converts this position into a Minestom position.
 *
 * @return the position
 */
fun TeleporterPosition.toPos(): Pos = Pos(x, y, z, yaw, pitch)

/**
 * Returns the instance holding the world this position belongs to.
 *
 * @return the instance, or `null` if no loaded instance holds that world
 */
fun TeleporterPosition.instance(): Instance? = findInstance(worldKey)

/**
 * Returns the instance holding the world [worldKey] names.
 *
 * @param worldKey the key of the world
 * @return the instance, or `null` if no loaded instance holds that world
 */
fun findInstance(worldKey: Key): Instance? =
    InstanceManager.instances.firstOrNull { it.worldKey == worldKey }

/**
 * Moves this player to [position], switching instances when the position lies in another world.
 *
 * @param position the position to move to
 * @return the move being carried out, or `null` if no loaded instance holds the world of that
 * position
 */
fun Player.moveTo(position: TeleporterPosition): CompletableFuture<Void>? {
    val target = position.instance() ?: return null
    val pos = position.toPos()

    return if (instance == target) teleport(pos) else setInstance(target, pos)
}
