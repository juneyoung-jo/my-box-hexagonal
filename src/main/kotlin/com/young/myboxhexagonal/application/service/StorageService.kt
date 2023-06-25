package com.young.myboxhexagonal.application.service

import com.young.myboxhexagonal.application.exception.ServiceException
import com.young.myboxhexagonal.application.exception.StorageErrorCode
import com.young.myboxhexagonal.application.port.`in`.StorageUseCase
import com.young.myboxhexagonal.application.port.out.StoragePersistencePort
import com.young.myboxhexagonal.domain.Storage
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service


@Service
class StorageService(
    private val storagePersistencePort: StoragePersistencePort
) : StorageUseCase {
    override fun getStorageById(storageId: Long): Storage =
        storagePersistencePort.findById(storageId) ?: throw ServiceException(StorageErrorCode.STORAGE_NOT_FOUND)


    override fun getStorageByParentStorageId(parentStorageId: Long): Storage {
        TODO("Not yet implemented")
    }

    override fun makeFolder(parentStorageId: Long, folderName: String): Storage {
        TODO("Not yet implemented")
    }

    override fun saveStorage(parentStorageId: Long, filePart: FilePart): Storage {
        TODO("Not yet implemented")
    }


}
