package dev.slne.surf.teleporter.permissions

import dev.slne.surf.api.paper.permission.PermissionRegistry
import dev.slne.surf.teleporter.core.client.permission.TeleporterPermissions

object Permissions : PermissionRegistry() {
    val COMMAND_TELEPORTER_GENERIC = create(TeleporterPermissions.COMMAND_TELEPORTER_GENERIC)
    val COMMAND_TELEPORTER_RELOAD = create(TeleporterPermissions.COMMAND_TELEPORTER_RELOAD)
}
