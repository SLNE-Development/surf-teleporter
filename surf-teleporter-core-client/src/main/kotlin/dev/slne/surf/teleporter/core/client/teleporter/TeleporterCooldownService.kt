package dev.slne.surf.teleporter.core.client.teleporter

import com.github.benmanes.caffeine.cache.Caffeine
import java.util.UUID
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Keeps players from being moved again while a teleport is in progress or while its cooldown is
 * still active.
 */
object TeleporterCooldownService {
    private val cooldowns = Caffeine.newBuilder()
        .expireAfterWrite(3.seconds.toJavaDuration())
        .build<UUID, Use>()

    /**
     * Attempts to reserve a teleporter use for the given player.
     *
     * The reservation is created atomically, preventing multiple concurrent teleports for the same
     * player.
     *
     * @param playerId the unique id of the player
     * @return the reserved use, or `null` if the player is already teleporting or on cooldown
     */
    fun startUse(playerId: UUID): Use? {
        val use = Use()
        return if (cooldowns.asMap().putIfAbsent(playerId, use) == null) use else null
    }

    /**
     * Marks the given use as successfully completed and starts the cooldown from this moment.
     */
    fun finishUse(playerId: UUID, use: Use) {
        cooldowns.asMap().replace(playerId, use, Use())
    }

    /**
     * Cancels the given use without applying a cooldown.
     *
     * The entry is only removed if it still belongs to this exact use.
     */
    fun cancelUse(playerId: UUID, use: Use) {
        cooldowns.asMap().remove(playerId, use)
    }

    class Use internal constructor()
}