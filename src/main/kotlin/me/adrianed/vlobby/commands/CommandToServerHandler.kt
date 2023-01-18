package me.adrianed.vlobby.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.proxy.Player
import me.adrianed.vlobby.VLobby
import me.adrianed.vlobby.extensions.nil

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
        val command = LiteralArgumentBuilder.literal<CommandSource>(entry.key)
            .requires { it.getPermissionValue("vlobby.command.${entry.key}") != Tristate.FALSE && it is Player }
            .executes {
                val player = it.source as Player
                connectionRequest(player, plugin.proxy.getServer(entry.value).nil!!)
                Command.SINGLE_SUCCESS
            }.build()
        val brigadierCommand = BrigadierCommand(command)

        val meta = plugin.proxy.commandManager.metaBuilder(brigadierCommand).plugin(plugin).build()
        plugin.proxy.commandManager.register(meta, brigadierCommand)
    }

}
