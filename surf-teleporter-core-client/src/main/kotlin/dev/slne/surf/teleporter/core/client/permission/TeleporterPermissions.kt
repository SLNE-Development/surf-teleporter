package dev.slne.surf.teleporter.core.client.permission

/**
 * The permissions guarding teleporter functionality.
 */
object TeleporterPermissions {
    const val PREFIX = "surf.teleporter"
    const val COMMAND_PREFIX = "$PREFIX.command"

    const val COMMAND_TELEPORTER_GENERIC = "$COMMAND_PREFIX.teleporter"
    const val COMMAND_TELEPORTER_RELOAD = "$COMMAND_PREFIX.reload"
}
