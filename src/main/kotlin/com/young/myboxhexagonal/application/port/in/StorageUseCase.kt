package com.young.myboxhexagonal.application.port.`in`

import com.young.myboxhexagonal.domain.Storage
import org.springframework.http.codec.multipart.FilePart


interface StorageUseCase {
    fun getStorageById(
        userId: String,
        storageId: String
    ): Storage

    fun getStorageByParentStorageId(
        userId: String,
        parentStorageId: String
    ): Storage

    fun getRootStorageByUserId(
        userId: String
    ): Storage

    fun makeFolder(
        userId: String,
        parentStorageId: String,
        folderName: String
    ): Storage

    // TODO: 확인필요 
//    fun downloadStorage(
//        userId: String, storageId: String
//    ): Tuple<Storage, Resource>

    fun saveStorage(
        userId: String,
        parentStorageId: String,
        filePart: FilePart
    ): Storage
}
