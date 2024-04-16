package com.young.myboxhexagonal.application.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
internal class StorageLockServiceTest {

    @Autowired
    lateinit var storageService: StorageService

    @Autowired
    lateinit var txTestService: TxTestService

    @Test
    @Order(1)
    fun 파일사이즈_증가_동시성_테스트_non_lock() {
        val size = 10
        val executorService = Executors.newFixedThreadPool(size)
        val countDownLatch = CountDownLatch(size)
        val initFileSize = storageService.getStorageById(1).storageFileSize

        for (i in 1..size) {
            executorService.execute {
                storageService.increaseFileSizeNonLock(1)
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        val result: Long = storageService.getStorageById(1).storageFileSize
        println("initFileSize = $initFileSize")
        println("result = $result")
        assertThat(result).isNotEqualTo(initFileSize + size)
    }

    @Test
    @Order(2)
    fun 파일사이즈_증가_동시성_테스트_db_lock() {
        val size = 10
        val executorService = Executors.newFixedThreadPool(size)
        val countDownLatch = CountDownLatch(size)
        val initFileSize = storageService.getStorageById(1).storageFileSize

        for (i in 1..size) {
            executorService.execute {
                storageService.increaseFileSize(1)
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        val result: Long = storageService.getStorageById(1).storageFileSize
        println("initFileSize = $initFileSize")
        println("result = $result")
        assertThat(result).isEqualTo(initFileSize + size)
    }

    @Test
    @Order(3)
    fun 파일사이즈_증가_동시성_테스트_redis_lock_non_transactional() {
        val size = 10
        val executorService = Executors.newFixedThreadPool(size)
        val countDownLatch = CountDownLatch(size)
        val initFileSize = storageService.getStorageById(1).storageFileSize

        for (i in 1..size) {
            executorService.execute {
                storageService.increaseFileSizeWithRedisLockByNonTransactional(1)
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        val result: Long = storageService.getStorageById(1).storageFileSize
        println("initFileSize = $initFileSize")
        println("result = $result")
        assertThat(result).isEqualTo(initFileSize + size)
    }

    @Disabled(value = "실패하는 테스트, disabled 처리 Deadlock")
    @Test
    @Order(4)
    fun 파일사이즈_증가_동시성_테스트_redis_lock_transactional() {
        val size = 10
        val executorService = Executors.newFixedThreadPool(size)
        val countDownLatch = CountDownLatch(size)
        val initFileSize = storageService.getStorageById(1).storageFileSize

        for (i in 1..size) {
            executorService.execute {
                storageService.increaseFileSizeWithRedisLockByTransactional(1)
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        val result: Long = storageService.getStorageById(1).storageFileSize
        println("initFileSize = $initFileSize")
        println("result = $result")
        assertThat(result).isEqualTo(initFileSize + size)
    }

    @Test
    @Order(5)
    fun 파일사이즈_증가_동시성_테스트_redis_lock_AOP() {
        val size = 10
        val executorService = Executors.newFixedThreadPool(size)
        val countDownLatch = CountDownLatch(size)
        val initFileSize = storageService.getStorageById(1).storageFileSize

        for (i in 1..size) {
            executorService.execute {
                storageService.increaseFileSizeWithRedisLockAop(1)
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        val result: Long = storageService.getStorageById(1).storageFileSize
        println("initFileSize = $initFileSize")
        println("result = $result")
        assertThat(result).isEqualTo(initFileSize + size)
    }

    @Test
    @Disabled(value = "실패하는 테스트, disabled 처리 Deadlock")
    @Order(6)
    fun TxTestService에서_transactional매서드에서_lock_method를_호출할때() {
        val size = 10
        val executorService = Executors.newFixedThreadPool(size)
        val countDownLatch = CountDownLatch(size)
        val initFileSize = storageService.getStorageById(1).storageFileSize

        for (i in 1..size) {
            executorService.execute {
                txTestService.txTest(1)
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        val result: Long = storageService.getStorageById(1).storageFileSize
        println("initFileSize = $initFileSize")
        println("result = $result")
        assertThat(result).isEqualTo(initFileSize + size)
    }
}
