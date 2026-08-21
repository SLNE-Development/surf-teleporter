package dev.slne.surf.teleporter.minestom.storage

import com.google.auto.service.AutoService
import dev.slne.surf.teleporter.core.client.storage.TeleporterNbtStorage
import dev.slne.surf.teleporter.core.client.storage.TeleporterStorage
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.minestom.SurfTeleporterMinestomEntrypoint
import it.unimi.dsi.fastutil.objects.ObjectList

@AutoService(TeleporterStorage::class)
class MinestomTeleporterStorage : TeleporterStorage {
    private val storage by lazy {
        TeleporterNbtStorage.inDirectory(SurfTeleporterMinestomEntrypoint.dataPath)
    }

    override fun loadTeleporters(): ObjectList<Teleporter> = storage.loadTeleporters()

    override fun saveTeleporters(teleporters: Collection<Teleporter>) =
        storage.saveTeleporters(teleporters)
}
