package me.adrianed.vlobby.configuration;

import me.adrianed.vlobby.enums.Handler;
import me.adrianed.vlobby.enums.SendMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;
import java.util.Map;

// This specific class uses Java and not kotlin
// because of a kotlin bug in the text blocks inside annotations.
@SuppressWarnings("all")
@ConfigSerializable
public class Configuration implements Section {
    @Comment("""
            Set the Handler to use in the plugin
            Available options:
            - REGULAR
              | Default Handler
              | Configurable in regularHandler section
              | Multiple commands to try to move a user to multiple/single Lobby servers
            - COMMAND_TO_SERVER
              | Configurable in commandToServerHandler section
              | Allows you to set one command for each lobby you have""")
    private Handler commandHandler = Handler.REGULAR;
    @Comment("""
            Configures the default Handler
            Allows you to set a single command with multiple aliases
            to send the player to a server within the lobby group
            according to the configured SendMode""")
    private RegularHandler regularHandler = new RegularHandler();
    @Comment("Configure customized commands for each of your lobbies")
    private CommandToServerHandler commandToServerHandler = new CommandToServerHandler();

    public Handler getCommandHandler() {
        return commandHandler;
    }

    public RegularHandler getRegularHandler() {
        return regularHandler;
    }

    public CommandToServerHandler getCommandToServerHandler() {
        return commandToServerHandler;
    }
    @ConfigSerializable
    public static class RegularHandler {
        @Comment("List of your Lobby servers")
        private List<String> lobbyServers = List.of("lobby", "lobby1", "hub");
        @Comment("Alias for the command that will send the player to the Lobby")
        private List<String> commands = List.of("lobby", "hub");
        @Comment("""
            Send Mode Formula
            RANDOM: It will send the player to a random server among the configured ones
            FIRST_AVAILABLE: It will send the player to the first available server
            EMPTIEST: Send the player to the server with the least amount of players""")
        private SendMode sendMode = SendMode.RANDOM;

        public List<String> getLobbyServers() {
            return lobbyServers;
        }

        public SendMode getSendMode() {
            return sendMode;
        }

        public List<String> getCommands() {
            return commands;
        }

    }

    @ConfigSerializable
    public static class CommandToServerHandler {
        @Comment("Configuration format <command>=<server>")
        private Map<String, String> commandToServerAliases = Map.of(
                "lobby", "lobby1",
                "hub", "lobby2"
        );

        public Map<String, String> getCommandToServerAliases() {
            return commandToServerAliases;
        }
    }
}
