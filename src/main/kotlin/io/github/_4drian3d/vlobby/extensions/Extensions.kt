package io.github._4drian3d.vlobby.extensions

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.permission.Tristate
import io.github.miniplaceholders.api.MiniPlaceholders
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

val hasMiniPlaceholders by lazy {
    try {
        Class.forName("io.github.miniplaceholders.api.MiniPlaceholders")
        true
    } catch (_: ClassNotFoundException) {
        false
    }
}

fun CommandSource.sendMiniMessage(message: String, vararg resolvers: TagResolver) {
    if (message.isBlank()) return
    if (hasMiniPlaceholders) {
        val miniResolver = TagResolver.builder()
                .resolver(MiniPlaceholders.audienceGlobalPlaceholders())
                .resolvers(*resolvers)
        this.sendMessage(miniMessage().deserialize(message, this, miniResolver.build()))
    } else {
        this.sendMessage(miniMessage().deserialize(message, this, *resolvers))
    }
}

fun CommandSource.notNegatePermission(permission: String) = getPermissionValue(permission) != Tristate.FALSE

fun ComponentLogger.miniInfo(string: String) = info(miniMessage().deserialize(string))
