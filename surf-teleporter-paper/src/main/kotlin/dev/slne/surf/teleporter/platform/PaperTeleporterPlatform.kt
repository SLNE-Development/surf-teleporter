package dev.slne.surf.teleporter.platform

import com.google.auto.service.AutoService
import dev.slne.surf.api.core.util.toObjectList
import dev.slne.surf.api.paper.extensions.server
import dev.slne.surf.teleporter.core.client.platform.TeleporterPlatform
import net.kyori.adventure.key.Key

@AutoService(TeleporterPlatform::class)
class PaperTeleporterPlatform : TeleporterPlatform {
    override fun worldNames() = server.worlds.asSequence().map { it.name }.toObjectList()

    override fun findWorldKey(worldName: String): Key? = server.getWorld(worldName)?.key()

    override fun worldName(worldKey: Key): String? = server.getWorld(worldKey)?.name
}
