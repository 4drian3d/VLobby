package me.adrianed.vlobby

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import me.adrianed.vlobby.commands.loadCommand
import me.adrianed.vlobby.configuration.Configuration
import me.adrianed.vlobby.configuration.loadConfig
import me.adrianed.vlobby.utils.Constants
import me.adrianed.vlobby.utils.loadDependencies
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = Constants.ID,
    name = Constants.NAME,
    version = Constants.VERSION,
    description = Constants.DESCRIPTION,
    url = Constants.URL,
    authors = ["4drian3d"],
    dependencies = [Dependency(id = "mckotlin-velocity")]
)
class VLobby @Inject constructor(
    val logger: Logger,
    @DataDirectory val pluginPath : Path,
    val proxy : ProxyServer
) {

    lateinit var config : Configuration
        private set

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        loadDependencies(this, logger, proxy.pluginManager, pluginPath)
        try {
            config = loadConfig(pluginPath)
            loadCommand(this)
            logger.info("Correctly loaded Configuration")
            logger.info("Lobby Servers: ${config.servers.lobbyServers}")
        } catch(ex: Exception) {
            logger.error("-- Cannot load Configuration --")
            logger.error("Disabling features")
            logger.error("Esception", ex)
        }
    }
}