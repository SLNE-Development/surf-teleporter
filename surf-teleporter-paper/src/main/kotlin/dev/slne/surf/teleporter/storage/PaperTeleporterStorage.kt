package dev.slne.surf.teleporter.storage

import com.google.auto.service.AutoService
import dev.slne.surf.api.core.util.logger
import dev.slne.surf.api.core.util.mutableObjectListOf
import dev.slne.surf.teleporter.config.TeleporterConfig
import dev.slne.surf.teleporter.core.client.storage.TeleporterNbtStorage
import dev.slne.surf.teleporter.core.client.storage.TeleporterStorage
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.plugin
import it.unimi.dsi.fastutil.objects.ObjectList
import kotlin.io.path.exists

@AutoService(TeleporterStorage::class)
class PaperTeleporterStorage : TeleporterStorage {
    private val log = logger()

    private val teleportersPath by lazy {
        plugin.dataPath.resolve(TeleporterNbtStorage.FILE_NAME)
    }
    private val storage by lazy { TeleporterNbtStorage(teleportersPath) }

    override fun loadTeleporters(): ObjectList<Teleporter> {
        if (!teleportersPath.exists()) {
            migrateLegacyStorage()
        }

        return storage.loadTeleporters()
    }

    override fun saveTeleporters(teleporters: Collection<Teleporter>) {
        storage.saveTeleporters(teleporters)
    }

    @Suppress("DEPRECATION")
    private fun migrateLegacyStorage() {
        if (!plugin.dataPath.resolve(LEGACY_FILE_NAME).exists()) return

        val teleporters = mutableObjectListOf<Teleporter>()
        for (dto in TeleporterConfig.getConfig().teleporters) {
            runCatching { dto.toTeleporter() }
                .onSuccess { teleporters.add(it) }
                .onFailure {
                    log.atWarning()
                        .withCause(it)
                        .log("Could not migrate teleporter ${dto.uuid}. Skipping this teleporter.")
                }
        }

        storage.saveTeleporters(teleporters)
        log.atInfo()
            .log("Migrated ${teleporters.size} legacy teleporters to ${teleportersPath.fileName}.")
    }

    private companion object {
        const val LEGACY_FILE_NAME = "teleporters.yml"
    }
}
