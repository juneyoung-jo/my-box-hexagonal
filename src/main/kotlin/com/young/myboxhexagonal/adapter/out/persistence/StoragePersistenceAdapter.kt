package com.young.myboxhexagonal.adapter.out.persistence

import com.young.myboxhexagonal.adapter.out.persistence.model.toDomain
import com.young.myboxhexagonal.adapter.out.persistence.model.toEntity
import com.young.myboxhexagonal.application.port.out.StoragePersistencePort
import com.young.myboxhexagonal.common.annotation.Adapter
import com.young.myboxhexagonal.domain.Storage

@Adapter
class StoragePersistenceAdapter(
    private val storageRepository: StorageRepository
) : StoragePersistencePort {

    override fun findById(id: Long): Storage? =
        storageRepository.findStorageById(id)?.toDomain()

    override fun save(storage: Storage) =
        storageRepository.save(storage.toEntity()).toDomain()

}
