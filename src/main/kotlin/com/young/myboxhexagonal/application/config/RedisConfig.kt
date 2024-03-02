package com.young.myboxhexagonal.application.config

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

}
