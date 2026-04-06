package dev.slne.surf.teleporter.permissions

import dev.slne.surf.api.paper.permission.PermissionRegistry

object Permissions : PermissionRegistry() {
    private const val PREFIX = "surf.teleporter"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_TELEPORTER_GENERIC = create("$COMMAND_PREFIX.teleporter")
    val COMMAND_TELEPORTER_RELOAD = create("$COMMAND_PREFIX.reload")
}