package com.young.myboxhexagonal.adapter.`in`.api

import com.young.myboxhexagonal.application.port.`in`.StorageUseCase
import com.young.myboxhexagonal.common.type.StorageExtType
import com.young.myboxhexagonal.domain.Storage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @PostMapping
    fun saveStorage(@RequestBody request: SaveRequest): ResponseEntity<Storage> {
        return ofSuccess(
            storageUseCase.saveStorage(
                storageName = request.storageName,
                storageFileSize = request.storageFileSize,
                extType = request.extType
            )
        )
    }

    @PutMapping("/{storageId}")
    fun updateStorage(@PathVariable storageId: Long): ResponseEntity<Storage> {
        return ofSuccess(
            storageUseCase.increaseFileSize(storageId)
        )
    }

    data class SaveRequest(
        val storageName: String,
        val storageFileSize: Long,
        val extType: StorageExtType
    )

}
