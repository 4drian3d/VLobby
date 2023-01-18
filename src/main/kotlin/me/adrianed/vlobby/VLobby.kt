package me.adrianed.vlobby

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import me.adrianed.vlobby.commands.CommandHandler
import me.adrianed.vlobby.configuration.Configuration
import me.adrianed.vlobby.configuration.Messages
import me.adrianed.vlobby.configuration.loadConfig
import me.adrianed.vlobby.enums.Handler
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
    lateinit var messages: Messages
        private set
    private lateinit var commandHandler: CommandHandler

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        loadDependencies(this, logger, proxy.pluginManager, pluginPath)
        try {
            config = loadConfig(pluginPath)
            messages = loadConfig(pluginPath)
            val handler = config.commandHandler
            commandHandler = handler.createInstance(this)
            commandHandler.register()
            logger.info("Correctly loaded Configuration")
            logger.info("Command Handler: $handler")

            when (handler!!) {
                Handler.REGULAR -> logger.info("Lobby Servers: ${commandHandler.servers}")
                Handler.COMMAND_TO_SERVER -> logger.info("Lobby Commands: ${commandHandler.servers}")
            }
        } catch(ex: Exception) {
            logger.error("Cannot load plugin configuration", ex)
            logger.error("Disabling features")
        }
    }

    fun reload() {
        commandHandler.unregister()
        config = loadConfig(pluginPath)
        messages = loadConfig(pluginPath)
        commandHandler = config.commandHandler.createInstance(this)
        commandHandler.register()
    }
}