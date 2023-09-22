package io.github._4drian3d.vlobby.utils

import com.github.benmanes.caffeine.cache.Caffeine
import com.velocitypowered.api.proxy.Player
import io.github._4drian3d.vlobby.configuration.Configuration
import java.time.Duration
import java.time.Instant
import java.util.*

object CooldownManager {
    private lateinit var cooldownDuration: Duration
    private lateinit var configuration: Configuration.Cooldown

    private val cache = Caffeine.newBuilder().build<UUID, Instant>()!!

    fun cooldown(source: Player): Long {
        val now = Instant.now()
        val lastTimeExecuted = cache.get(source.uniqueId) { now }
        if (now == lastTimeExecuted) {
            return 0
        }
        val duration = Duration.between(lastTimeExecuted, now)
        return when {
            duration > cooldownDuration -> {
                cache.put(source.uniqueId, now)
                0
            }
            else -> configuration.unit.convert(duration)
        }
    }

    fun reload(config: Configuration) {
        this.configuration = config.cooldown
        this.cooldownDuration = with(configuration) { Duration.ofMillis(unit.toMillis(time)) }
    }
}


