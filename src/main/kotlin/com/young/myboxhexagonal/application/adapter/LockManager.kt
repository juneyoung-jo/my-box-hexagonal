package com.young.myboxhexagonal.application.adapter

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
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
        num: Int,
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
            println("${LocalDateTime.now()} [${Thread.currentThread().name}] lock 획득 실패 key: $key, num: $num")
            throw RuntimeException("Can't lock")
        }else{
            println("${LocalDateTime.now()} [${Thread.currentThread().name}] lock 획득 key: $key, num: $num")
        }

        try {
            // transaction의 이름과 transacitonal이 적용되었는지 확인
//            println("tx = " + getCurrentTransactionName() + " " + isActualTransactionActive())
//            println("get Lock = ${Thread.currentThread()}")
            return txAdvise.requireNew {
                println("${LocalDateTime.now()} [${Thread.currentThread().name}] tx open")
                // transaction의 이름과 new transacitonal 이 적용되었는지 확인
//                println("new tx = " + getCurrentTransactionName() + " " + isActualTransactionActive())
                func()
            }
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
//            println("final tx = " + getCurrentTransactionName() + " " + isActualTransactionActive())
            try {
                unlock(key)
                println("${LocalDateTime.now()} [${Thread.currentThread().name}] lock 해제 key: $key, num: $num")
            } catch (e: IllegalMonitorStateException) {
                println("${LocalDateTime.now()} [${Thread.currentThread().name}] IllegalMonitorStateException")
            }

        }
    }

    private fun unlock(key: String) {
//        println("unLock = ${Thread.currentThread()}")
        redissonClient.getLock(key).unlock()
    }

}

@Component
class TxAdvise {

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 2)
    fun <T> requireNew(func: () -> T): T = func()
}
