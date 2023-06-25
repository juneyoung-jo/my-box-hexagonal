package com.young.myboxhexagonal.adapter.`in`.api

import com.young.myboxhexagonal.application.port.`in`.StorageUseCase
import com.young.myboxhexagonal.domain.Storage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/storage")
class StorageController(
    private val storageUseCase: StorageUseCase
) {

    @GetMapping("/{storageId}")
    fun getStorage(@PathVariable storageId: Long): ResponseEntity<Storage> {
        return ofSuccess(storageUseCase.getStorageById(storageId = storageId))
    }

}
