package com.young.myboxhexagonal.application.service

import com.young.myboxhexagonal.application.adapter.LockManager
import com.young.myboxhexagonal.application.exception.ServiceException
import com.young.myboxhexagonal.application.exception.StorageErrorCode
import com.young.myboxhexagonal.application.port.`in`.StorageUseCase
import com.young.myboxhexagonal.application.port.out.StoragePersistencePort
import com.young.myboxhexagonal.common.type.StorageExtType
import com.young.myboxhexagonal.domain.Storage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class StorageService(
    private val storagePersistencePort: StoragePersistencePort,
    private val lockManager: LockManager
) : StorageUseCase {

    override fun getStorageById(storageId: Long): Storage =
        storagePersistencePort.findById(storageId) ?: throw ServiceException(StorageErrorCode.STORAGE_NOT_FOUND)


    override fun getStorageByParentStorageId(parentStorageId: Long): Storage {
        TODO("Not yet implemented")
    }

    override fun makeFolder(parentStorageId: Long, folderName: String): Storage {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun increaseFileSize(storageId: Long) =
        storagePersistencePort.findByIdForUpdate(storageId)!!
            .increaseFileSize()
            .run {
                storagePersistencePort.save(this)
            }

    @Transactional
    fun increaseFileSizeNonLock(storageId: Long): Storage =
        storagePersistencePort.findById(storageId)!!
            .increaseFileSize()
            .run {
                storagePersistencePort.save(this)
            }

    @Transactional
    override fun saveStorage(
        storageName: String,
        storageFileSize: Long,
        extType: StorageExtType
    ): Storage =
        storagePersistencePort.save(
            Storage(
                storageName = storageName,
                storageFileSize = storageFileSize,
                extType = extType
            )
        )

}

@Service
class LockService(
    private val lockManager: LockManager,
    private val storageService: StorageService
) {

    fun increaseFileSizeWithRedisLock(storageId: Long): Storage =
        lockManager.lock(key = storageId.toString()) {
            storageService.increaseFileSizeNonLock(storageId)
        }

}
