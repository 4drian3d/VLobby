package io.github._4drian3d.vlobby.extensions

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.permission.Tristate
import io.github.miniplaceholders.api.MiniPlaceholders
import io.github.miniplaceholders.kotlin.applyIfNotEmpty
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.Optional

val hasMiniPlaceholders by lazy {
    try {
        Class.forName("io.github.miniplaceholders.api.MiniPlaceholders")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

fun CommandSource.sendMiniMessage(message: String, resolver: TagResolver = TagResolver.empty()) {
    if (message.isBlank()) return
    if (hasMiniPlaceholders) {
        val miniResolver = TagResolver.builder()
                .resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(this))
        miniResolver.applyIfNotEmpty(resolver)
        this.sendMessage(miniMessage().deserialize(message, miniResolver.build()))
    } else {
        this.sendMessage(miniMessage().deserialize(message, resolver))
    }
}

val <T: Any> Optional<T>.nil: T?
    get() = orElse(null)

fun CommandSource.notNegatePermission(permission: String) = getPermissionValue(permission) != Tristate.FALSE
