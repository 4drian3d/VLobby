package me.dreamerzero.vlobby.configuration;

import java.util.List;
import java.util.Locale;

import com.moandjiezana.toml.Toml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.dreamerzero.vlobby.enums.SendMode;

public class Configuration {
    private final Servers servers;
    private final String[] commands;
    private final Messages messages;

    public Configuration(@NotNull Toml toml){
        this.servers = new Servers(toml.getTable("servers"));
        this.messages = new Messages(toml.getTable("messages"));
        this.commands = toml.getList("commands", List.of("lobby", "hub")).toArray(String[]::new);
    }

    public Servers getServerOptions(){
        return this.servers;
    }

    public Messages getMessages(){
        return this.messages;
    }

    public String[] getCommands(){
        return this.commands;
    }

    public static class Servers {
        private final String[] servers;
        private final SendMode sendMode;

        public Servers(@Nullable Toml toml){
            if(toml != null){
                this.servers = toml.getList("servers", List.of("lobby")).toArray(String[]::new);
                switch (toml.getString("send-mode", "invalid").toLowerCase(Locale.ROOT)) {
                    case "random": this.sendMode =  SendMode.RANDOM; break;
                    case "first_available": this.sendMode = SendMode.FIRST_AVAILABLE; break;
                    case "first_empty": this.sendMode = SendMode.FIRST_EMPTY; break;
                    default: this.sendMode = SendMode.RANDOM;
                }
            } else {
                this.servers = new String[]{"lobby"};
                this.sendMode = SendMode.RANDOM;
            }
        }

        public String[] getLobbyServers(){
            return this.servers;
        }

        public SendMode getSendMode(){
            return this.sendMode;
        }
    }

    public static class Messages {
        private final String error;
        private final String notAvailableServer;
        private final String succesfully;
        private final String consoleMessage;

        public Messages(@Nullable Toml toml){
            if(toml != null){
                this.error = toml.getString("error", "<red>You could not be transported to the server <server>");
                this.notAvailableServer = toml.getString("not-available-server", "<red>No lobby servers available now, try again later");
                this.succesfully = toml.getString("succesfully", "<red>You have been successfully sent to the lobby <server>");
                this.consoleMessage = toml.getString("console-message", "The console could not be teleported to the lobby");
            } else {
                this.error = "<red>You could not be transported to the server <server>";
                this.notAvailableServer = "<red>No lobby servers available now, try again later";
                this.succesfully = "<red>You have been successfully sent to the lobby <server>";
                this.consoleMessage = "The console could not be teleported to the lobby";
            }
        }

        public String getErrorMessage(){
            return this.error;
        }

        public String getNotAvailableServerMessage(){
            return this.notAvailableServer;
        }

        public String getSuccesfullyMessage(){
            return this.succesfully;
        }

        public String getConsoleMessage(){
            return this.consoleMessage;
        }

    }
}
