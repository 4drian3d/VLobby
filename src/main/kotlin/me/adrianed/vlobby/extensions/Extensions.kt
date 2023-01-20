package me.adrianed.vlobby.extensions

import com.velocitypowered.api.command.CommandSource
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.Optional

fun CommandSource.sendMiniMessage(message: String, resolver: TagResolver = TagResolver.empty()) {
    if (message.isBlank()) return
    this.sendMessage(MiniMessage.miniMessage().deserialize(message, resolver))
}

val <T: Any> Optional<T>.nil: T?
    get() = orElse(null)
