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
    private Handler commandHandler = Handler.REGULAR;
    private RegularHandler regularHandler = new RegularHandler();
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
        private List<String> lobbyServers = List.of("lobby");
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
        private Map<String, String> commandToServerAliases = Map.of(
                "lobby", "lobby1",
                "hub", "lobby2"
        );

        public Map<String, String> getCommandToServerAliases() {
            return commandToServerAliases;
        }
    }
}
