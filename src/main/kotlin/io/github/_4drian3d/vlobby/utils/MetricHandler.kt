package io.github._4drian3d.vlobby.utils

import io.github._4drian3d.vlobby.VLobby
import org.bstats.charts.SimplePie
import org.bstats.velocity.Metrics

fun loadMetrics(plugin: VLobby, factory: Metrics.Factory) {
    with(factory.make(plugin, 17472)) {
        addCustomChart(SimplePie("command_handler") { plugin.config.commandHandler.toString() })
    }
}