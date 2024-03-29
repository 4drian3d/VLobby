package io.github._4drian3d.vlobby

import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginManager
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import io.github._4drian3d.vlobby.commands.CommandHandler
import io.github._4drian3d.vlobby.commands.CommandToServerHandler
import io.github._4drian3d.vlobby.commands.MainCommand
import io.github._4drian3d.vlobby.configuration.Configuration
import io.github._4drian3d.vlobby.configuration.Messages
import io.github._4drian3d.vlobby.configuration.loadConfig
import io.github._4drian3d.vlobby.enums.Handler
import io.github._4drian3d.vlobby.utils.Constants
import io.github._4drian3d.vlobby.utils.CooldownManager
import io.github._4drian3d.vlobby.utils.loadDependencies
import io.github._4drian3d.vlobby.utils.loadMetrics
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@Plugin(
    id = "vlobby",
    name = "VLobby",
    version = Constants.VERSION,
    description = "Velocity Lobby plugin features",
    url = "https://modrinth.com/plugin/vlobby",
    authors = ["4drian3d"],
    dependencies = [
        Dependency(id = "mckotlin-velocity"),
        Dependency(id = "miniplaceholders", optional = true)
    ]
)
class VLobby @Inject constructor(
    val logger: Logger,
    @DataDirectory val pluginPath : Path,
    val proxy : ProxyServer,
    val commandManager: CommandManager,
    private val pluginManager: PluginManager,
    private val metrics: Metrics.Factory,
    private val injector: Injector,
) {

    lateinit var config : Configuration
        private set
    lateinit var messages: Messages
        private set
    private lateinit var commandHandler: CommandHandler

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        loadDependencies(this, pluginManager)
        try {
            config = loadConfig(pluginPath)
            messages = loadConfig(pluginPath)
            val handler = config.commandHandler
            commandHandler = handler.createInstance(this)
            commandHandler.register()
            logger.info("Correctly loaded Configuration")
            logger.info("Command Handler: $handler")
            injector.getInstance(MainCommand::class.java).register()
            CooldownManager.reload(config)

            when (handler) {
                Handler.REGULAR -> logger.info("Lobby Servers: ${commandHandler.servers}")
                Handler.COMMAND_TO_SERVER -> {
                    val commandsToServers = (commandHandler as CommandToServerHandler)
                        .serverMap
                        .map { "${it.key} -> ${it.value}" }
                        .reduce { v, v2 -> "$v\n$v2" }
                    logger.info("Lobby Commands (command->server):\n$commandsToServers")
                }
            }

            loadMetrics(this, metrics)
        } catch(ex: Exception) {
            logger.error("Cannot load plugin configuration", ex)
            logger.error("Disabling features")
        }
    }

    fun reload() = CompletableFuture.runAsync {
        commandHandler.unregister()
        config = loadConfig(pluginPath)
        messages = loadConfig(pluginPath)
        commandHandler = config.commandHandler.createInstance(this)
        commandHandler.register()
        CooldownManager.reload(config)
    }!!
}
