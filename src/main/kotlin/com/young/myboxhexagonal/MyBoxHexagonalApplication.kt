package com.young.myboxhexagonal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class MyBoxHexagonalApplication

fun main(args: Array<String>) {
    runApplication<MyBoxHexagonalApplication>(*args)
}
