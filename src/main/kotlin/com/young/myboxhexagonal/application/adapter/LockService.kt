package com.young.myboxhexagonal.application.adapter

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

@Component
class LockService(
    private val redissonClient: RedissonClient
) {

    fun <R> lock(
        key: String,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        waitTime: Long = 0L,
        leaseTime: Long = 20000L,
        func: () -> R
    ): R {
        val available = redissonClient.getLock(key)
            .tryLock(
                waitTime,
                leaseTime,
                timeUnit
            )

        if (!available) {
            throw RuntimeException("Can't lock")
        }

        return try {
            func()
        } finally {
            unlock(key)
        }
    }

    private fun unlock(key: String) {
        redissonClient.getLock(key).unlock()
    }

}
