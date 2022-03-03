package me.dreamerzero.vlobby;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import me.dreamerzero.vlobby.commands.LobbyCommand;
import me.dreamerzero.vlobby.configuration.Configuration;
import me.dreamerzero.vlobby.utils.Constants;

@Plugin(
    id = Constants.ID,
    name = Constants.NAME,
    version = Constants.VERSION,
    description = Constants.DESCRIPTION,
    url = Constants.URL,
    authors = {
        "4drian3d"
    }
)
public final class VLobby {
	private final Path pluginPath;
    private final Logger logger;
    private final ProxyServer proxy;
    private Configuration config;

    @Inject
    public VLobby(ProxyServer proxy, @DataDirectory Path pluginPath, Logger logger){
        this.proxy = proxy;
        this.pluginPath = pluginPath;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event){
        Toml toml = this.loadConfig();
        if(toml == null) {
            logger.error("-- Cannot load Configuration --");
            logger.error("Disabling features");
            return;
        }
        this.config = new Configuration(toml);
        LobbyCommand.loadCommands(this);
    }

    public ProxyServer getProxy(){
        return this.proxy;
    }

    public Logger getLogger(){
        return this.logger;
    }

    public Configuration getConfig(){
        return this.config;
    }

    private Toml loadConfig(){
        if(!Files.exists(pluginPath)){
            try {
                Files.createDirectory(pluginPath);
            } catch(IOException e){
                return null;
            }
        }
        Path configPath = pluginPath.resolve("config.toml");
        if(!Files.exists(configPath)){
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.toml")){
                Files.copy(in, configPath);
            } catch(IOException e){
                return null;
            }
        }
        try {
            return new Toml().read(Files.newInputStream(configPath));
        } catch (IOException e){
            return null;
        }
    }
}