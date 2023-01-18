package me.adrianed.vlobby.enums

import com.velocitypowered.api.proxy.server.RegisteredServer
import me.adrianed.vlobby.VLobby
import java.util.*

enum class SendMode {
    RANDOM {
        override fun getServer(plugin: VLobby): RegisteredServer? {
            val servers = plugin.config.servers.lobbyServers
            for (i in servers.indices) {
                val serverName = servers[rm.nextInt(servers.size - 1)]
                val server = plugin.proxy.getServer(serverName)
                if (server.isPresent) return server.get()
            }
            return null
        }
    },
    FIRST_AVAILABLE {
        override fun getServer(plugin: VLobby): RegisteredServer? {
            for (lobby in plugin.config.servers.lobbyServers) {
                val server = plugin.proxy.getServer(lobby)
                if (server.isPresent) return server.get()
            }
            return null
        }
    },
    EMPTIEST {
        override fun getServer(plugin: VLobby): RegisteredServer? {
            var emptiest: RegisteredServer? = null
            for (lobby in plugin.config.servers.lobbyServers) {
                val sv = plugin.proxy.getServer(lobby)
                if (sv.isPresent) {
                    val actual = sv.get()
                    if (emptiest == null) {
                        emptiest = actual
                    } else {
                        if (actual.playersConnected.isEmpty()) return actual
                        if (actual.playersConnected.size < emptiest.playersConnected.size) {
                            emptiest = actual
                        }
                    }
                }
            }
            return emptiest
        }
    };

    abstract fun getServer(plugin: VLobby) : RegisteredServer?

    val rm = Random()
}