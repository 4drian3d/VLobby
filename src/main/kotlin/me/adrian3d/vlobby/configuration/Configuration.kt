package me.adrian3d.vlobby.configuration

import com.moandjiezana.toml.Toml
import me.adrian3d.vlobby.enums.SendMode

class Configuration(toml: Toml) {
    val servers: Servers
    val commands: List<String>
    val messages: Messages
    
    init {
        servers = Servers(toml.getTable("servers"))
        messages = Messages(toml.getTable("messages"))
        commands = toml.getList("commands", listOf("lobby", "hub"))
    }

    class Servers(toml: Toml?) {
        val lobbyServers: List<String>
        val sendMode: SendMode

        init {
            if (toml != null) {
                lobbyServers = toml.getList("servers", listOf("lobby"))
                sendMode = when (toml.getString("send-mode", "invalid").lowercase()) {
                    "random" -> SendMode.RANDOM
                    "first_available" -> SendMode.FIRST_AVAILABLE
                    "emptiest" -> SendMode.EMPTIEST
                    else -> SendMode.RANDOM
                }
            } else {
                lobbyServers = listOf("lobby")
                sendMode = SendMode.RANDOM
            }
        }
    }

    class Messages(toml: Toml?) {
        val errorMessage: String
        val notAvailableServerMessage: String
        val successfullyMessage: String
        val consoleMessage: String

        init {
            if (toml != null) {
                errorMessage = toml.getString("error", "<red>You could not be transported to the server <server>")
                notAvailableServerMessage =
                    toml.getString("not-available-server", "<red>No lobby servers available now, try again later")
                successfullyMessage =
                    toml.getString("successfully", "<red>You have been successfully sent to the lobby <server>")
                consoleMessage = toml.getString("console-message", "The console could not be teleported to the lobby")
            } else {
                errorMessage = "<red>You could not be transported to the server <server>"
                notAvailableServerMessage = "<red>No lobby servers available now, try again later"
                successfullyMessage = "<red>You have been successfully sent to the lobby <server>"
                consoleMessage = "The console could not be teleported to the lobby"
            }
        }
    }
}