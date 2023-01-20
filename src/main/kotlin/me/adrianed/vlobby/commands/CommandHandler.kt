package me.adrianed.vlobby.commands

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import me.adrianed.vlobby.VLobby
import me.adrianed.vlobby.extensions.sendMiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

abstract class CommandHandler(protected val plugin: VLobby) {
    abstract fun register()

    abstract fun unregister()

    abstract val servers: List<String>

    fun connectionRequest(player: Player, server: RegisteredServer) {
        player.createConnectionRequest(server).connect()
            .thenAccept { result ->
                val attemptedConnection = result.attemptedConnection
                if (result.isSuccessful) {
                    player.sendMiniMessage(
                        plugin.messages.successfullyMessage,
                        Placeholder.unparsed(
                            "server",
                            attemptedConnection.serverInfo.name
                        )
                    )
                } else {
                    plugin.logger.error("Cannot connect to server: {}", server.serverInfo.name)
                    player.sendMiniMessage(
                        plugin.messages.errorMessage,
                        Placeholder.unparsed(
                            "server",
                            server.serverInfo.name
                        )
                    )
                }
            }
    }
}