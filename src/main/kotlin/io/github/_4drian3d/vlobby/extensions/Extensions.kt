package io.github._4drian3d.vlobby.extensions

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.permission.Tristate
import io.github.miniplaceholders.api.MiniPlaceholders
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

val hasMiniPlaceholders by lazy {
    try {
        Class.forName("io.github.miniplaceholders.api.MiniPlaceholders")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

fun CommandSource.sendMiniMessage(message: String, vararg resolvers: TagResolver) {
    if (message.isBlank()) return
    if (hasMiniPlaceholders) {
        val miniResolver = TagResolver.builder()
                .resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(this))
                .resolvers(*resolvers)
        this.sendMessage(miniMessage().deserialize(message, miniResolver.build()))
    } else {
        this.sendMessage(miniMessage().deserialize(message, *resolvers))
    }
}

fun CommandSource.notNegatePermission(permission: String) = getPermissionValue(permission) != Tristate.FALSE
