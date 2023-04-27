package io.github._4drian3d.vlobby.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import io.github._4drian3d.vlobby.VLobby
import io.github._4drian3d.vlobby.extensions.notNegatePermission
import io.github._4drian3d.vlobby.extensions.sendMiniMessage
import kotlin.jvm.optionals.getOrNull

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
        serverMap.forEach { plugin.commandManager.unregister(it.key) }
    }

    private fun internalRegister(entry: Map.Entry<String, String>) {
        val node = literal<CommandSource>(entry.key)
            .requires { it.notNegatePermission("vlobby.command.${entry.key}") && it is Player }
            .executes {
                val player = it.source as Player
                if (player.currentServer.getOrNull()?.serverInfo?.name == entry.value) {
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

        with(plugin.commandManager) {
            val command = BrigadierCommand(node)
            val meta = metaBuilder(command)
                .plugin(plugin)
                .build()
            register(meta, command)
        }
    }
}
