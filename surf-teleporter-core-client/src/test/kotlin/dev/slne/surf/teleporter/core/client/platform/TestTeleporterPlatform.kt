package dev.slne.surf.teleporter.core.client.platform

import dev.slne.surf.api.core.util.objectListOf
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.key.Key

class TestTeleporterPlatform : TeleporterPlatform {
    override fun worldNames(): ObjectList<String> = objectListOf(WORLD_NAME)

    override fun findWorldKey(worldName: String): Key? =
        WORLD_KEY.takeIf { worldName == WORLD_NAME }

    override fun worldName(worldKey: Key): String? = WORLD_NAME.takeIf { worldKey == WORLD_KEY }

    companion object {
        const val WORLD_NAME = "world"
        val WORLD_KEY: Key = Key.key("minecraft", "world")
    }
}
