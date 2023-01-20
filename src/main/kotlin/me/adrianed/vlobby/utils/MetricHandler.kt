package me.adrianed.vlobby.utils

import me.adrianed.vlobby.VLobby
import org.bstats.charts.SimplePie
import org.bstats.velocity.Metrics

fun loadMetrics(plugin: VLobby, factory: Metrics.Factory) {
    val metrics = factory.make(plugin, 17472)
    metrics.addCustomChart(SimplePie("command_handler") { plugin.config.commandHandler.toString() })
}