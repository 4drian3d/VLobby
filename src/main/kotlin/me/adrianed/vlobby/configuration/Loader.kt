package me.adrianed.vlobby.configuration

import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.nio.file.Path
import kotlin.jvm.Throws

@Throws(ConfigurateException::class)
fun loadConfig(path: Path): Configuration {
    val loader = HoconConfigurationLoader.builder()
        .defaultOptions { opts ->
            opts
                .shouldCopyDefaults(true)
                .header("VLobby | by 4drian3d\n")
        }
        .path(path.resolve("config.conf"))
        .build()

    val node = loader.load()
    val config = node.get(Configuration::class.java)
    node.set(Configuration::class.java, config)
    loader.save(node)

    return config!!
}