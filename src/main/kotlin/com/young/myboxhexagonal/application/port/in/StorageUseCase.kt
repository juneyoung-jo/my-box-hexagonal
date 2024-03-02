package com.young.myboxhexagonal.application.port.`in`

import com.young.myboxhexagonal.common.type.StorageExtType
import com.young.myboxhexagonal.domain.Storage


interface StorageUseCase {
    fun getStorageById(storageId: Long): Storage

    fun getStorageByParentStorageId(parentStorageId: Long): Storage

    fun makeFolder(
        parentStorageId: Long,
        folderName: String
    ): Storage

    fun increaseFileSize(storageId: Long): Storage

    fun saveStorage(
        storageName: String,
        storageFileSize: Long,
        extType: StorageExtType
    ): Storage
}
