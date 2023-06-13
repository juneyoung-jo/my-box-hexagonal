package com.young.myboxhexagonal.adapter.out.persistence

import com.young.myboxhexagonal.adapter.out.persistence.model.StorageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StorageRepository : JpaRepository<StorageEntity, Long> {
    fun findStorageById(id: Long): StorageEntity?
}
