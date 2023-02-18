package me.adrianed.vlobby.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import me.adrianed.vlobby.VLobby
import me.adrianed.vlobby.extensions.nil
import me.adrianed.vlobby.extensions.notNegatePermission
import me.adrianed.vlobby.extensions.sendMiniMessage

class CommandToServerHandler(plugin: VLobby): CommandHandler(plugin) {
    private lateinit var serverMap: Map<String, String>
    override val servers: List<String>
        get() = serverMap.toList().map { it.first }

    override fun register() {
        serverMap = plugin.config
            .commandToServerHandler
            .commandToServerAliases
            .filter { (_, v) -> plugin.proxy.getServer(v).isPresent }

        serverMap.forEach { internalRegister(it) }
    }

    override fun unregister() {
        serverMap.forEach { plugin.proxy.commandManager.unregister(it.key) }
    }

    private fun internalRegister(entry: Map.Entry<String, String>) {
        val command = literal<CommandSource>(entry.key)
            .requires { it.notNegatePermission("vlobby.command.${entry.key}") && it is Player }
            .executes {
                val player = it.source as Player
                if (player.currentServer.nil?.serverInfo?.name == entry.value) {
                    player.sendMiniMessage(plugin.messages.alreadyInThisLobby)
                    return@executes Command.SINGLE_SUCCESS
                }
                plugin.proxy.getServer(entry.value)
                    .ifPresentOrElse(
                        { server -> connectionRequest(player, server) },
                        { player.sendMiniMessage(plugin.messages.notAvailableServerMessage) }
                    )
                Command.SINGLE_SUCCESS
            }.build()
        val brigadierCommand = BrigadierCommand(command)

        val meta = plugin.proxy.commandManager.metaBuilder(brigadierCommand).plugin(plugin).build()
        plugin.proxy.commandManager.register(meta, brigadierCommand)
    }

}
