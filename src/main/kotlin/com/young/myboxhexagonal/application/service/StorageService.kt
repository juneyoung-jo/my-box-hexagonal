package com.young.myboxhexagonal.application.service

import com.young.myboxhexagonal.application.port.`in`.StorageUseCase
import com.young.myboxhexagonal.application.port.out.StoragePersistencePort
import com.young.myboxhexagonal.domain.Storage
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service


@Service
class StorageService(
    private val storagePersistencePort: StoragePersistencePort
) : StorageUseCase {

    override fun getStorageById(storageId: String): Storage {
        TODO("Not yet implemented")
    }

    override fun getStorageByParentStorageId(parentStorageId: String): Storage {
        TODO("Not yet implemented")
    }

    override fun makeFolder(parentStorageId: String, folderName: String): Storage {
        TODO("Not yet implemented")
    }

    override fun saveStorage(parentStorageId: String, filePart: FilePart): Storage {
        TODO("Not yet implemented")
    }

}
