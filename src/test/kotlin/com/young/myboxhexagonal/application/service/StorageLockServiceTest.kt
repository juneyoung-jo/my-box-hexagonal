package com.young.myboxhexagonal.application.service

import org.assertj.core.api.Assertions.assertThat
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
    lateinit var lockService: LockService

    @Test
    @Order(1)
    fun 파일사이즈_증가_동시성_테스트_non_lock() {
        val size = 100
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
        val size = 100
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
    fun 파일사이즈_증가_동시성_테스트_redis_lock() {
        val size = 100
        val executorService = Executors.newFixedThreadPool(size)
        val countDownLatch = CountDownLatch(size)
        val initFileSize = storageService.getStorageById(1).storageFileSize

        for (i in 1..size) {
            executorService.execute {
                lockService.increaseFileSizeWithRedisLock(1)
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
