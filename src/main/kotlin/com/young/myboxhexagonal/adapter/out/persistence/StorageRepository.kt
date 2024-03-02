package com.young.myboxhexagonal.adapter.out.persistence

import com.young.myboxhexagonal.adapter.out.persistence.model.StorageEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface StorageRepository : JpaRepository<StorageEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findStorageById(id: Long): StorageEntity?
}
