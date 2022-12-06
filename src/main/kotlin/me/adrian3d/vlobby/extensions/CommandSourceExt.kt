package me.adrian3d.vlobby.extensions

import com.velocitypowered.api.command.CommandSource
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

fun CommandSource.sendMiniMessage(message: String, resolver: TagResolver = TagResolver.empty()) {
    if (message.isBlank()) return
    this.sendMessage(MiniMessage.miniMessage().deserialize(message, resolver))
}