package dev.slne.surf.teleporter.permissions

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object Permissions : PermissionRegistry() {
    const val PREFIX = "surf.teleporter"
    const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_TELEPORTER_GENERIC = create("$COMMAND_PREFIX.teleporter")
}