package com.young.myboxhexagonal.application.exception

import org.springframework.http.HttpStatus

enum class StorageErrorCode(
    val status: HttpStatus,
    val message: String
) {
    STORAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found Storage")
}
