package me.adrianed.vlobby.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import me.adrianed.vlobby.VLobby
import me.adrianed.vlobby.enums.SendMode
import me.adrianed.vlobby.extensions.nil
import me.adrianed.vlobby.extensions.notNegatePermission
import me.adrianed.vlobby.extensions.sendMiniMessage

class RegularHandler(plugin: VLobby): CommandHandler(plugin) {
    override lateinit var servers: List<String>

    override fun register() {
        servers = plugin.config.regularHandler.commands
        if (servers.isEmpty()) {
            return
        }
        val lobbyCommand = literal<CommandSource>(servers[0])
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
                if (lobbyToSend.serverInfo.name == source.currentServer.nil?.serverInfo?.name) {
                    source.sendMiniMessage(plugin.messages.alreadyInThisLobby)
                    return@executes Command.SINGLE_SUCCESS
                }
                connectionRequest(source, lobbyToSend)
                Command.SINGLE_SUCCESS
            }
            .build()
        val bCommand = BrigadierCommand(lobbyCommand)
        val manager = plugin.proxy.commandManager
        val builder = manager.metaBuilder(bCommand).plugin(plugin).also {
            if (servers.size > 1) it.aliases(*servers.toTypedArray().copyOfRange(1, servers.size))
        }

        manager.register(builder.build(), bCommand)
    }

    override fun unregister() {
        val manager = plugin.proxy.commandManager
        val meta = manager.getCommandMeta(servers[0])
        manager.unregister(meta)
    }

}