package io.github._4drian3d.vlobby.configuration

import io.github._4drian3d.vlobby.enums.Handler
import io.github._4drian3d.vlobby.enums.SendMode
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import java.util.concurrent.TimeUnit

@ConfigSerializable
class Configuration: Section {
    @Comment(
        "Set the Handler to use in the plugin\n"+
        "Available options:\n"+
        "  - REGULAR\n"+
        "    | Default Handler\n"+
        "    | Configurable in regularHandler section\n"+
        "    | Multiple commands to try to move a user to multiple/single Lobby servers\n"+
        "  - COMMAND_TO_SERVER\n"+
        "    | Configurable in commandToServerHandler section\n"+
        "    | Allows you to set one command for each lobby you have"
    )
    var commandHandler = Handler.REGULAR

    @Comment(
        "Configures the default Handler\n"+
        "Allows you to set a single command with multiple aliases\n"+
        "to send the player to a server within the lobby group\n"+
        "according to the configured SendMode"
    )
    var regularHandler = RegularHandler()

    @Comment("Configure customized commands for each of your lobbies")
    var commandToServerHandler = CommandToServerHandler()

    var cooldown = Cooldown()

    @ConfigSerializable
    class RegularHandler {
        @Comment("List of your Lobby servers")
        var lobbyServers = listOf("lobby", "lobby1", "hub")

        @Comment("Alias for the command that will send the player to the Lobby")
        var commands = listOf("lobby", "hub")

        @Comment(
            "Send Mode Formula\n"+
            "RANDOM: It will send the player to a random server among the configured ones\n"+
            "FIRST_AVAILABLE: It will send the player to the first available server\n"+
            "EMPTIEST: Send the player to the server with the least amount of players"
        )
        var sendMode = SendMode.RANDOM
    }

    @ConfigSerializable
    class CommandToServerHandler {
        @Comment("Configuration format <command>=<server>")
        var commandToServerAliases = mapOf(
            "lobby" to "lobby1",
            "hub" to "lobby2"
        )
    }

    @ConfigSerializable
    class Cooldown {
        var unit = TimeUnit.SECONDS
        var time: Long = 5

        var cooldownMessage = "You can't go to the lobby so soon, wait <time> seconds"
    }
}