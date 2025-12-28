package dev.slne.surf.teleporter.sound

import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import net.kyori.adventure.sound.Sound as AdventureSound
import org.bukkit.Sound as BukkitSound
import org.bukkit.entity.Player

class SoundService {

    fun playTeleportSound(player: Player) {
        player.playSound(true) {
            type(BukkitSound.ENTITY_ENDERMAN_TELEPORT)
            source(AdventureSound.Source.NEUTRAL)
            pitch(1.0f)
        }
    }

    companion object {
        val INSTANCE = SoundService()
    }
}

val soundService get() = SoundService.INSTANCE