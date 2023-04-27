package io.github._4drian3d.vlobby.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import io.github._4drian3d.vlobby.VLobby
import io.github._4drian3d.vlobby.enums.SendMode
import io.github._4drian3d.vlobby.extensions.notNegatePermission
import io.github._4drian3d.vlobby.extensions.sendMiniMessage
import kotlin.jvm.optionals.getOrNull

class RegularHandler(plugin: VLobby): CommandHandler(plugin) {
    override lateinit var servers: List<String>

    override fun register() {
        servers = plugin.config.regularHandler.commands
        if (servers.isEmpty()) {
            return
        }
        val node = literal<CommandSource>(servers[0])
            .requires { it.notNegatePermission("vlobby.command") && it is Player }
            .executes { cmd ->
                val source = cmd.source as Player
                val mode = plugin.config.regularHandler.sendMode
                val lobbyToSend = mode.getServer(plugin) ?:
                    if (mode == SendMode.RANDOM) SendMode.FIRST_AVAILABLE.getServer(plugin)
                    else SendMode.RANDOM.getServer(plugin)

                if (lobbyToSend == null) {
                    plugin.logger.error("Cannot found lobby server to send")
                    source.sendMiniMessage(plugin.messages.notAvailableServerMessage)
                    return@executes Command.SINGLE_SUCCESS
                }
                if (lobbyToSend.serverInfo.name == source.currentServer.getOrNull()?.serverInfo?.name) {
                    source.sendMiniMessage(plugin.messages.alreadyInThisLobby)
                    return@executes Command.SINGLE_SUCCESS
                }
                connectionRequest(source, lobbyToSend)
                Command.SINGLE_SUCCESS
            }
            .build()

        with(plugin.proxy.commandManager) {
            val command = BrigadierCommand(node)
            val builder = metaBuilder(command).plugin(plugin).also {
                if (servers.size > 1) it.aliases(*servers.toTypedArray().copyOfRange(1, servers.size))
            }
            register(builder.build(), command)
        }
    }

    override fun unregister() {
        with(plugin.proxy.commandManager) {
            unregister(getCommandMeta(servers[0]))
        }
    }
}
