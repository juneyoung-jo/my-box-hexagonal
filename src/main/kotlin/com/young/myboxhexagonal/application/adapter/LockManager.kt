package com.young.myboxhexagonal.application.adapter

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

@Component
class LockManager(
    private val redissonClient: RedissonClient,
    private val txAdvise: TxAdvise
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
        try {
            val available = redissonClient.getLock(key)
                .tryLock(
                    waitTime,
                    leaseTime,
                    timeUnit
                )

            if (!available) {
                throw RuntimeException("Can't lock")
            }

            println("get Lock = ${Thread.currentThread()}")
            return txAdvise.requireNew {
                func()
            }
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            unlock(key)
        }
    }

    private fun unlock(key: String) {
        println("unLock = ${Thread.currentThread()}")
        redissonClient.getLock(key).unlock()
    }

}

@Component
class TxAdvise {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> requireNew(func: () -> T): T = func()
}
