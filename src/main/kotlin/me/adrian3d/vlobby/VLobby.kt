package me.adrian3d.vlobby

import com.google.inject.Inject
import com.moandjiezana.toml.Toml
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import me.adrian3d.vlobby.commands.loadCommand
import me.adrian3d.vlobby.configuration.Configuration
import me.adrian3d.vlobby.utils.Constants
import org.slf4j.Logger
import java.io.IOException
import java.nio.file.Files
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

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        loadConfig()?.let {
            config = Configuration(it)
            loadCommand(this)
        } ?: run {
            logger.error("-- Cannot load Configuration --")
            logger.error("Disabling features")
        }
    }

    private fun loadConfig(): Toml? {
        return try {
            if (Files.notExists(pluginPath)) {
                Files.createDirectory(pluginPath)
            }
            val configPath = pluginPath.resolve("config.toml")
            if (Files.notExists(configPath)) {
                javaClass.classLoader.getResourceAsStream("config.toml").use {
                    Files.copy(it!!, configPath)
                }
            }
            Toml().read(Files.newInputStream(configPath))
        } catch (e: IOException) {
            null
        }
    }
}