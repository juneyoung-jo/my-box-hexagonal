package com.young.myboxhexagonal.application.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
internal class StorageServiceTest {

    @Autowired
    lateinit var storageService: StorageService

    @Test
    fun 파일사이즈_증가_동시성_테스트() {
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

}
