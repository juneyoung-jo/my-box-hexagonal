package com.young.myboxhexagonal.application.adapter

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

@Component
class LockManager(
    private val redissonClient: RedissonClient
) {

    /**
     * @param [key] 락의 이름
     * @param [timeUnit] 락의 시간 단위
     * @param [waitTime] 락을 기다리는 시간 (default - 5s)
     * @param [leaseTime] 락 임대 시간 (default - 3s)
     */
    fun <R> lock(
        key: String,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        waitTime: Long = 5000L,
        leaseTime: Long = 3000L,
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
