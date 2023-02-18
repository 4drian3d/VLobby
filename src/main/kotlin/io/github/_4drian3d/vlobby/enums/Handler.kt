package io.github._4drian3d.vlobby.enums

import io.github._4drian3d.vlobby.VLobby
import io.github._4drian3d.vlobby.commands.CommandHandler
import io.github._4drian3d.vlobby.commands.RegularHandler
import io.github._4drian3d.vlobby.commands.CommandToServerHandler

enum class Handler {
    REGULAR {
        override fun createInstance(plugin: VLobby): CommandHandler = RegularHandler(plugin)
    },
    COMMAND_TO_SERVER {
        override fun createInstance(plugin: VLobby): CommandHandler = CommandToServerHandler(plugin)
    };

    abstract fun createInstance(plugin: VLobby): CommandHandler
}