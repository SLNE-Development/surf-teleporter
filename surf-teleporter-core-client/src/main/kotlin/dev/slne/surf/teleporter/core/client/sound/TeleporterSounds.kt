package dev.slne.surf.teleporter.core.client.sound

import dev.slne.surf.api.core.generated.SoundKeys
import dev.slne.surf.api.core.messages.adventure.playSound
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.Sound

private val TELEPORT_SOUND = SoundKeys.ENTITY_ENDERMAN_TELEPORT

/**
 * Plays the sound a player hears when a teleporter moves them.
 */
fun Audience.playTeleportSound() {
    playSound(true) {
        type(TELEPORT_SOUND)
        source(Sound.Source.NEUTRAL)
        pitch(1.0f)
    }
}
