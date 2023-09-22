package io.github._4drian3d.vlobby.commands

import com.google.inject.Inject
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import io.github._4drian3d.vlobby.VLobby
import io.github._4drian3d.vlobby.extensions.sendMiniMessage

class MainCommand @Inject constructor(
    private val plugin: VLobby,
    private val commandManager: CommandManager
) {
    fun register() {
        val node = literal<CommandSource>("vlobby")
            .requires { it.hasPermission("vlobby.command") }
            .then(literal<CommandSource>("reload")
                .requires { it.hasPermission("vlobby.command.reload") }
                .executes {
                    plugin.reload().whenComplete { _, ex ->
                        it.source.sendMiniMessage(
                            if (ex == null) "<green>Correctly reloaded configuration"
                            else "<red>An error occurred while reloading the configuration, check the console"
                        )
                    }
                    Command.SINGLE_SUCCESS
                }
            )
            .build()

        with(commandManager) {
            val command = BrigadierCommand(node)
            register(metaBuilder(command).plugin(plugin).build(), command)
        }
    }
}
