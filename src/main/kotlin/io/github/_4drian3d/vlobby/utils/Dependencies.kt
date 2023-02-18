package io.github._4drian3d.vlobby.utils

import com.velocitypowered.api.plugin.PluginManager
import io.github._4drian3d.vlobby.VLobby
import net.byteflux.libby.Library
import net.byteflux.libby.VelocityLibraryManager
import net.byteflux.libby.relocation.Relocation

fun loadDependencies(plugin: VLobby, pluginManager: PluginManager) {
    val libraryManager = VelocityLibraryManager(
        plugin.logger, plugin.pluginPath, pluginManager, plugin, "libs")
    val configurateRelocation = Relocation(
            "org{}spongepowered",
            "io.github._4drian3d.vlobby.libs.org{}spongepowered"
        )
    val geantyrefRelocation = Relocation(
            "io{}leangen{}geantyref",
            "io.github._4drian3d.libs.io{}leangen{}geantyref"
        )
    val hocon = Library.builder()
        .groupId("org{}spongepowered")
        .artifactId("configurate-hocon")
        .version(Constants.CONFIGURATE)
        .id("configurate-hocon")
        .relocate(configurateRelocation)
        .relocate(geantyrefRelocation)
        .build()
    val confCore = Library.builder()
        .groupId("org{}spongepowered")
        .artifactId("configurate-core")
        .version(Constants.CONFIGURATE)
        .id("configurate-core")
        .relocate(configurateRelocation)
        .relocate(geantyrefRelocation)
        .build()
    val geantyref = Library.builder()
        .groupId("io{}leangen{}geantyref")
        .artifactId("geantyref")
        .version(Constants.GEANTYREF)
        .id("geantyref")
        .relocate(geantyrefRelocation)
        .build()
    libraryManager.addMavenCentral()
    libraryManager.loadLibrary(geantyref)
    libraryManager.loadLibrary(hocon)
    libraryManager.loadLibrary(confCore)
}