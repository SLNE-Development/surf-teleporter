package dev.slne.surf.teleporter.storage

import com.google.auto.service.AutoService
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.teleporter.plugin
import dev.slne.surf.teleporter.teleporter.Teleporter
import dev.slne.surf.teleporter.teleporter.teleporterService
import org.bukkit.configuration.file.YamlConfiguration
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@AutoService(StorageService::class)
class StorageService {
    private val teleporterFolder: Path get() = plugin.dataPath.resolve("teleporter")

    fun init() {
        if (!Files.exists(teleporterFolder)) {
            Files.createDirectories(teleporterFolder)
        }
    }

    fun loadTeleporters() {
        val files = Files.list(teleporterFolder).filter { it.toString().endsWith(".yml") }.toList()
        var loadedCount = 0

        files.forEach { path ->
            val config = YamlConfiguration.loadConfiguration(path.toFile())

            runCatching {
                val uuid = UUID.fromString(config.getString("teleporter.data.uuid"))
                val originLocation = config.getLocation("teleporter.data.originLocation") ?: error("OriginLocation missing in ${path.fileName}")
                val targetLocation = config.getLocation("teleporter.data.targetLocation")
                    ?: error("TargetLocation missing in ${path.fileName}")
                val width = config.getInt("teleporter.data.width")
                val length = config.getInt("teleporter.data.length")

                val teleporter = Teleporter(uuid, originLocation, targetLocation, width, length)
                teleporterService.registerTeleporter(teleporter)
                loadedCount++
            }.onFailure {
                logger().atWarning().log("Failed to load Teleporter from file ${path.fileName}: ${it.message}")
            }
        }
        logger().atInfo().log("Successfully loaded $loadedCount Teleporter from ${files.size} files!")
    }

    fun saveTeleporters() {
        Files.list(teleporterFolder)
            .filter { it.toString().endsWith(".yml") }
            .forEach(Files::delete)

        val teleporter = teleporterService.getteleporters()

        teleporter.forEach { porter ->
            val file = teleporterFolder.resolve("${porter.uuid}.yml").toFile()
            val config = YamlConfiguration()

            config["teleporter.data.uuid"] = porter.uuid.toString()
            config["teleporter.data.originLocation"] = porter.originLocation
            config["teleporter.data.targetLocation"] = porter.targetLocation
            config["teleporter.data.width"] = porter.width
            config["teleporter.data.length"] = porter.length

            config.save(file)
        }

        logger().atInfo().log("Successfully saved ${teleporter.size} Teleporter to files!")
    }

    companion object {
        val instance = StorageService()
    }
}

val storageService get() = StorageService.instance