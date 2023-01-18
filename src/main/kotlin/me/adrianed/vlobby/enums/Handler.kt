package me.adrianed.vlobby.enums

import me.adrianed.vlobby.VLobby
import me.adrianed.vlobby.commands.CommandHandler
import me.adrianed.vlobby.commands.RegularHandler
import me.adrianed.vlobby.commands.CommandToServerHandler

enum class Handler {
    REGULAR {
        override fun createInstance(plugin: VLobby): CommandHandler = RegularHandler(plugin)
    },
    COMMAND_TO_SERVER {
        override fun createInstance(plugin: VLobby): CommandHandler = CommandToServerHandler(plugin)
    };

    abstract fun createInstance(plugin: VLobby): CommandHandler
}