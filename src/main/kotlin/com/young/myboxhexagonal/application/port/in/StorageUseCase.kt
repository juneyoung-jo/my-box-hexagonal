package com.young.myboxhexagonal.application.port.`in`

import com.young.myboxhexagonal.domain.Storage
import org.springframework.http.codec.multipart.FilePart


interface StorageUseCase {
    fun getStorageById(storageId: String): Storage

    fun getStorageByParentStorageId(parentStorageId: String): Storage

    fun makeFolder(
        parentStorageId: String,
        folderName: String
    ): Storage

    // TODO: 확인필요 
//    fun downloadStorage(
//        userId: String, storageId: String
//    ): Tuple<Storage, Resource>

    fun saveStorage(
        parentStorageId: String,
        filePart: FilePart
    ): Storage
}
