package me.adrian3d.vlobby.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.proxy.Player
import me.adrian3d.vlobby.VLobby
import me.adrian3d.vlobby.enums.SendMode
import me.adrian3d.vlobby.extensions.sendMiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

fun loadCommand(plugin: VLobby) {
    val aliases = plugin.config.commands
    if (aliases.isEmpty()) {
        return
    }
    val lobbyCommand = LiteralArgumentBuilder.literal<CommandSource>(aliases[0])
        .requires { it.getPermissionValue("vlobby.command") != Tristate.FALSE }
        .executes { cmd ->
            val source = cmd.source
            if (source !is Player) {
                plugin.logger.info(plugin.config.messages.consoleMessage)
                return@executes Command.SINGLE_SUCCESS
            }
            val mode: SendMode = plugin.config.servers.sendMode
            var lobbyToSend = mode.getServer(plugin)
            if (lobbyToSend == null)
                lobbyToSend =
                    if (mode == SendMode.RANDOM)
                        SendMode.FIRST_AVAILABLE.getServer(plugin)
                    else
                        SendMode.RANDOM.getServer(plugin)

            if (lobbyToSend == null) {
                plugin.logger.error("Cannot found lobby server to send")
                source.sendMiniMessage(plugin.config.messages.notAvailableServerMessage)
                return@executes Command.SINGLE_SUCCESS
            }
            source.createConnectionRequest(lobbyToSend).connect()
                .thenAccept { result ->
                    val server = result.attemptedConnection
                    if (result.isSuccessful) {
                        source.sendMiniMessage(
                            plugin.config.messages.successfullyMessage,
                            Placeholder.unparsed(
                                "server",
                                server.serverInfo.name
                            )
                        )
                    } else {
                        plugin.logger.error("Cannot connect to server: {}", server.serverInfo.name)
                        source.sendMiniMessage(
                            plugin.config.messages.errorMessage,
                            Placeholder.unparsed(
                                "server",
                                server.serverInfo.name
                            )
                        )
                    }
                }
            Command.SINGLE_SUCCESS
        }
        .build()
    val bCommand = BrigadierCommand(lobbyCommand)
    val manager = plugin.proxy.commandManager
    val builder = manager.metaBuilder(bCommand).plugin(plugin)
    if (aliases.size > 1) {
        builder.aliases(*aliases.toTypedArray().copyOfRange(1, aliases.size))
    }
    manager.register(builder.build(), bCommand)
}
