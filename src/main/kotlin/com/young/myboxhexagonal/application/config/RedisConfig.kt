package com.young.myboxhexagonal.application.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfig(
    private val redisProperties: RedisProperties
) {

    @Bean(name = ["redisTemplate"])
    fun redisTemplate(): RedisTemplate<String, Any> =
        RedisTemplate<String, Any>().apply {
            connectionFactory = redisConnectionFactory()
        }

    private fun redisConnectionFactory(): RedisConnectionFactory =
        LettuceConnectionFactory(
            RedisStandaloneConfiguration().apply {
                hostName = redisProperties.host
                port = redisProperties.port
            }
        )

    @Bean
    fun redissonClient(): RedissonClient = Redisson.create(
        redissonConfig()
    )

    private fun redissonConfig() =
        Config().apply {
            useSingleServer().apply {
                address = "$REDISSON_HOST_PREFIX${redisProperties.host}:${redisProperties.port}"
            }
        }

    companion object {
        private const val REDISSON_HOST_PREFIX = "redis://"
    }

}
