package io.github._4drian3d.vlobby.enums

import com.velocitypowered.api.proxy.server.RegisteredServer
import io.github._4drian3d.vlobby.VLobby

enum class SendMode {
    RANDOM {
        override fun getServer(plugin: VLobby): RegisteredServer? {
            val servers = plugin.config.regularHandler.lobbyServers
            for (i in servers.indices) {
                val serverName = servers.random()
                val server = plugin.proxy.getServer(serverName)
                if (server.isPresent) return server.get()
            }
            return null
        }
    },
    FIRST_AVAILABLE {
        override fun getServer(plugin: VLobby): RegisteredServer? {
            for (lobby in plugin.config.regularHandler.lobbyServers) {
                val server = plugin.proxy.getServer(lobby)
                if (server.isPresent) return server.get()
            }
            return null
        }
    },
    EMPTIEST {
        override fun getServer(plugin: VLobby): RegisteredServer? {
            var emptiest: RegisteredServer? = null
            for (lobby in plugin.config.regularHandler.lobbyServers) {
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
}