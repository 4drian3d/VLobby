package me.adrianed.vlobby.configuration;

import me.adrianed.vlobby.enums.SendMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;

// This specific class uses Java and not kotlin
// because of a kotlin bug in the text blocks inside annotations.
@SuppressWarnings("all")
@ConfigSerializable
public class Configuration {
    public Servers getServers() {
        return servers;
    }

    public List<String> getCommands() {
        return commands;
    }

    public Messages getMessages() {
        return messages;
    }

    private Servers servers = new Servers();
    private List<String> commands = List.of("lobby", "hub");
    private Messages messages = new Messages();

    @ConfigSerializable
    public static class Servers {
        public List<String> getLobbyServers() {
            return lobbyServers;
        }

        public SendMode getSendMode() {
            return sendMode;
        }

        private List<String> lobbyServers = List.of("lobby");
        @Comment("""
            Send Mode Formula
            RANDOM: It will send the player to a random server among the configured ones
            FIRST_AVAILABLE: It will send the player to the first available server
            EMPTIEST: Send the player to the server with the least amount of players""")
        private SendMode sendMode = SendMode.RANDOM;
    }

    @ConfigSerializable
    public static class Messages {
        public String getErrorMessage() {
            return errorMessage;
        }

        public String getNotAvailableServerMessage() {
            return notAvailableServerMessage;
        }

        public String getSuccessfullyMessage() {
            return successfullyMessage;
        }

        public String getConsoleMessage() {
            return consoleMessage;
        }

        private String errorMessage = "<red>You could not be transported to the server <server>";
        private String notAvailableServerMessage = "<red>No lobby servers available now, try again later";
        private String successfullyMessage = "<red>You have been successfully sent to the lobby <server>";
        private String consoleMessage = "The console could not be teleported to the lobby";
    }
}
