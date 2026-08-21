package dev.slne.surf.teleporter.minestom.platform

import com.google.auto.service.AutoService
import dev.slne.minestom.lobby.api.extension.InstanceManager
import dev.slne.minestom.lobby.api.instance.worldKey
import dev.slne.surf.api.core.util.toObjectList
import dev.slne.surf.teleporter.core.client.platform.TeleporterPlatform
import dev.slne.surf.teleporter.minestom.teleporter.findInstance
import net.kyori.adventure.key.Key

@AutoService(TeleporterPlatform::class)
class MinestomTeleporterPlatform : TeleporterPlatform {
    override fun worldNames() = InstanceManager.instances
        .asSequence()
        .mapNotNull { it.worldKey?.value() }
        .toObjectList()

    override fun findWorldKey(worldName: String): Key? = InstanceManager.instances
        .firstNotNullOfOrNull { instance -> instance.worldKey?.takeIf { it.value() == worldName } }

    override fun worldName(worldKey: Key): String? = findInstance(worldKey)?.worldKey?.value()
}
