package dev.slne.surf.teleporter.core.client.storage

import dev.slne.surf.api.core.util.logger
import dev.slne.surf.api.core.util.mutableObjectListOf
import dev.slne.surf.teleporter.core.client.platform.TeleporterPlatform
import dev.slne.surf.teleporter.core.client.teleporter.Teleporter
import dev.slne.surf.teleporter.core.client.teleporter.TeleporterPosition
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists

/**
 * Reads and writes the teleporters of a server from and to a single binary tag file.
 *
 * @property teleportersPath the file the teleporters are stored in
 */
class TeleporterNbtStorage(private val teleportersPath: Path) {
    private val log = logger()

    /**
     * Reads every stored teleporter.
     *
     * Teleporters that cannot be read, or that belong to a world this server does not have, are
     * skipped.
     *
     * @return the stored teleporters
     */
    fun loadTeleporters(): ObjectList<Teleporter> {
        val teleporters = mutableObjectListOf<Teleporter>()
        if (!teleportersPath.exists()) return teleporters

        val compoundTag = BinaryTagIO.reader().read(teleportersPath)

        for ((key, tag) in compoundTag) {
            if (tag !is CompoundBinaryTag) {
                log.atWarning().log("Invalid tag for key: $key. Skipping this teleporter.")
                continue
            }

            val uuid = try {
                UUID.fromString(key)
            } catch (e: IllegalArgumentException) {
                log.atWarning()
                    .withCause(e)
                    .log("Invalid UUID: $key. Skipping this teleporter.")
                continue
            }

            val teleporter = loadTeleporter(uuid, tag) ?: continue
            teleporters.add(teleporter)
        }

        return teleporters
    }

    /**
     * Writes [teleporters] back, replacing whatever was stored before.
     *
     * @param teleporters the teleporters to store
     */
    fun saveTeleporters(teleporters: Collection<Teleporter>) {
        val compoundTag = CompoundBinaryTag.builder(teleporters.size)
            .apply {
                for (teleporter in teleporters) {
                    put(teleporter.uuid.toString(), saveTeleporter(teleporter))
                }
            }
            .build()

        teleportersPath.createParentDirectories()
        BinaryTagIO.writer().write(compoundTag, teleportersPath)
    }

    private fun saveTeleporter(teleporter: Teleporter): CompoundBinaryTag =
        CompoundBinaryTag.builder(6)
            .putString("name", teleporter.name)
            .put("origin", savePosition(teleporter.originLocation))
            .put("target", savePosition(teleporter.targetLocation))
            .putDouble("width", teleporter.width)
            .putDouble("length", teleporter.length)
            .putDouble("height", teleporter.height)
            .build()

    private fun savePosition(position: TeleporterPosition) = CompoundBinaryTag.builder(6)
        .putString("world_key", position.worldKey.asString())
        .putDouble("x", position.x)
        .putDouble("y", position.y)
        .putDouble("z", position.z)
        .putFloat("yaw", position.yaw)
        .putFloat("pitch", position.pitch)
        .build()

    private fun loadTeleporter(uuid: UUID, tag: CompoundBinaryTag): Teleporter? {
        val origin = loadPosition(tag.getCompound("origin")) ?: return null
        val target = loadPosition(tag.getCompound("target")) ?: return null

        return Teleporter(
            uuid = uuid,
            name = tag.getString("name"),
            originLocation = origin,
            targetLocation = target,
            width = tag.getDouble("width"),
            length = tag.getDouble("length"),
            height = tag.getDouble("height")
        )
    }

    private fun loadPosition(tag: CompoundBinaryTag): TeleporterPosition? {
        val worldKeyString = tag.getString("world_key")

        val key = try {
            Key.key(worldKeyString)
        } catch (e: InvalidKeyException) {
            log.atWarning()
                .withCause(e)
                .log("Invalid key: $worldKeyString. Skipping this teleporter.")
            return null
        }

        if (TeleporterPlatform.worldName(key) == null) {
            log.atWarning()
                .log("Could not find world with key: $worldKeyString. Skipping this teleporter.")
            return null
        }

        return TeleporterPosition(
            key,
            tag.getDouble("x"),
            tag.getDouble("y"),
            tag.getDouble("z"),
            tag.getFloat("yaw"),
            tag.getFloat("pitch")
        )
    }

    companion object {
        /**
         * The name of the file the teleporters are stored in.
         */
        const val FILE_NAME = "teleporters.dat"

        /**
         * Builds a storage keeping its file in [dataDirectory].
         *
         * @param dataDirectory the directory the teleporters are stored in
         * @return the storage
         */
        fun inDirectory(dataDirectory: Path) =
            TeleporterNbtStorage(dataDirectory.resolve(FILE_NAME))
    }
}
