package io.github._4drian3d.vlobby.configuration

import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.nio.file.Path
import kotlin.jvm.Throws

@Throws(Exception::class)
inline fun <reified S: Section> loadConfig(path: Path): S {
    val loader = HoconConfigurationLoader.builder()
        .defaultOptions { opts ->
            opts
                .shouldCopyDefaults(true)
                .header("VLobby | by 4drian3d\n")
        }
        .path(path.resolve("${S::class.simpleName!!.lowercase()}.conf"))
        .build()

    val node = loader.load()
    val config = node.get(S::class.java)
    node.set(S::class.java, config)
    loader.save(node)

    return config!!
}
