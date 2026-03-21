package dev.slne.surf.teleporter.teleporter.field

import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.ComponentLike

abstract class TeleporterFieldParseResult(
    message: SurfComponentBuilder.() -> Unit
) : ComponentLike {
    val message = SurfComponentBuilder.builder().append(message).build()
    override fun asComponent() = message

    class Success<T>(val value: T) : TeleporterFieldParseResult({})

    val isSuccess: Boolean get() = this is Success<*>
}